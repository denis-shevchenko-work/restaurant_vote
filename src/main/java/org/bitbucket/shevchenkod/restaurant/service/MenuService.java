package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.view.dto.MenuDto;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuItemDto;
import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.MenuItem;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
public interface MenuService extends CrudService<Long, Menu> {

	List<Menu> getAllByDate(Date date);

	/**
	 * Find restaurant menu for specified day. Day starts from 11AM.
	 * @param restaurantName
	 * @param date
	 * @return
	 */
	Optional<Menu> findByRestaurantNameAndDate(String restaurantName, Date date);

	/**
	 * Find restaurant menu for specified day. Day starts from 11AM.
	 * @param restaurant
	 * @param date
	 * @return
	 */
	Optional<Menu> findByRestaurantAndDate(Restaurant restaurant, Date date);

	Optional<MenuItem> getMenuItemById(Long itemId);

	void overwriteMenuItems(MenuDto menuDto);

	MenuItem updateMenuItem(Long menuId, Long itemId, MenuItemDto menuItemDto);

	void addMenuItem(Long menu, MenuItemDto menuItemDto);

	void deleteMenuItem(Long menuId, Long itemId);
}
