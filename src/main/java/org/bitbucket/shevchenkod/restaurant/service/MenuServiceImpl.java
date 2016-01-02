package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.*;
import org.bitbucket.shevchenkod.restaurant.service.transformer.TransformerService;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuDto;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuItemDto;
import org.bitbucket.shevchenkod.restaurant.service.repository.DishRepository;
import org.bitbucket.shevchenkod.restaurant.service.repository.MenuItemRepository;
import org.bitbucket.shevchenkod.restaurant.service.repository.MenuRepository;
import org.bitbucket.shevchenkod.restaurant.service.repository.RestaurantRepository;
import org.bitbucket.shevchenkod.restaurant.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class MenuServiceImpl extends GenericCrudService<Long, Menu, MenuRepository> implements MenuService {

	@Autowired
	MenuItemRepository menuItemRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	//@Autowired
	//DishRepository dishRepository;

	@Autowired
	DishService dishService;

	/**
	 * Get all menus for specified date. Day start end finish at 11AM.
	 * @param date
	 * @return
	 */
	@Override
	public List<Menu> getAllByDate(Date date) {
		Pair<Date, Date> range = getRange(date);
		return  getRepository().findAllBetweenDates(range.getLeft(), range.getRight());
	}

	/**
	 * Find menu by restaurant and day. Day start end finish at 11AM.
	 * @param restaurantName
	 * @param date
	 * @return
	 */
	@Override
	public Optional<Menu> findByRestaurantNameAndDate(String restaurantName, Date date) {
		Pair<Date, Date> range = getRange(date);
		return  getRepository().findByRestaurantNameBetweenDates(restaurantName, range.getLeft(), range.getRight());
	}

	@Override
	public Optional<MenuItem> getMenuItemById(Long itemId) {
		return  Optional.ofNullable(menuItemRepository.findOne(itemId));
	}

	/**
	 * Find menu by restaurant and day. Day start end finish at 11AM.
	 * @param restaurant
	 * @param date
	 * @return
	 */
	@Override
	public Optional<Menu> findByRestaurantAndDate(Restaurant restaurant, Date date) {
		Pair<Date, Date> range = getRange(date);
		Optional<Menu> result =  getRepository().findByRestaurantBetweenDates(restaurant, range.getLeft(), range.getRight());
		return result;
	}

	@Override
	public void overwriteMenuItems(MenuDto menuDto) {
		Optional<Restaurant> restaurantOptional = restaurantRepository.findByName(menuDto.getRestaurantName());
		if (!restaurantOptional.isPresent()) {
			restaurantOptional = Optional.ofNullable(restaurantRepository.findOne(menuDto.getRestaurantId()));
			if (!restaurantOptional.isPresent()) {
				//TODO log&throw
			}
		}
		Optional<Menu> menuOptional = findByRestaurantAndDate(restaurantOptional.get(), menuDto.getDate());

		Menu menu;
		if (menuOptional.isPresent()){
			menu = menuOptional.get();
		} else {
			menu = create(new Menu(restaurantOptional.get(), menuDto.getDate()));
		}

		final Menu finalMenu = menu;
		menu.clearItems();

		List<MenuItem> items = menuDto.getMenu().stream().map(menuItemDto -> createMenuItem(finalMenu, menuItemDto)).collect(Collectors.toList());
		menu.getItems().addAll(items);

		getRepository().save(menu);
	}

	@Override
	public MenuItem updateMenuItem(Long menuId, Long menuItemId, MenuItemDto menuItemDto) {
		MenuItem menuItem = menuItemRepository.findOne(menuItemId);
		updateMenuItem(menuItem, menuItemDto);
		return menuItemRepository.save(menuItem);
	}

	@Override
	public void addMenuItem(Long menuId, MenuItemDto menuItemDto) {
		Menu menu = getRepository().findOne(menuId);
		menu.getItems().add(createMenuItem(menu, menuItemDto));
		getRepository().save(menu);
	}

	@Override
	public void deleteMenuItem(Long menuId, Long itemId) {
		MenuItem menuItem = menuItemRepository.findOne(itemId);
		Menu menu = getRepository().findOne(menuId);
		menu.getItems().remove(menuItem);
		getRepository().saveAndFlush(menu);
		menuItemRepository.delete(itemId);

	}

	private MenuItem createMenuItem(Menu menu, MenuItemDto menuItemDto) {
		MenuItem menuItem = new MenuItem();
		menuItem.setMenu(menu);
		updateMenuItem(menuItem, menuItemDto);
		return menuItem;
	}

	private void updateMenuItem(MenuItem menuItem, MenuItemDto menuItemDto) {
		Optional<Dish> dishOptional = Optional.empty();
		if (Optional.ofNullable(menuItemDto.getDishName()).isPresent()) {
			dishOptional = dishService.findByName(menuItemDto.getDishName());
		} else if (Optional.ofNullable(menuItemDto.getDishId()).isPresent()) {
			dishOptional = dishService.getById(menuItemDto.getDishId());
		}

		Dish dish;
		if (dishOptional.isPresent()) {
			dish = dishOptional.get();
		} else {
			dish = new Dish();
			dish.setName(menuItemDto.getDishName());
			dishService.create(dish);
		}
		menuItem.setDish(dish);
		menuItem.setPrice(menuItemDto.getPrice());
	}


	private Pair<Date,Date> getRange(Date date){
		Date from;
		Date to;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 11);

		if (date.compareTo(calendar.getTime()) < 0) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}

		from = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		to = calendar.getTime();
		return new Pair<>(from,to);
	}

}
