package org.bitbucket.shevchenkod.restaurant.test.services;

import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.MenuItem;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.service.DishService;
import org.bitbucket.shevchenkod.restaurant.service.MenuService;
import org.bitbucket.shevchenkod.restaurant.service.RestaurantService;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuDto;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuItemDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@EnableWebMvc
@TestPropertySource(locations="classpath:application-test.properties")
//TODO
public class MenuServiceTest  extends GenericCrudServiceTest<Long, Menu, MenuService> {


	@Autowired
	MenuService menuService;

	@Autowired
	DishService dishService;

	@Autowired
	RestaurantService restaurantService;

	@Override
	public Menu updateEntity(Menu entity, String name){
		return updateEntity(entity);
	}

	public Menu updateEntity( Menu t){
		t.setDate(new Date());
		return t;
	};

	@Override
	public Class getType(){
		return Menu.class;
	}

	@Override
	protected Optional<Menu> getByName(String name) {
		return getService().findByRestaurantNameAndDate(name, new Date());
	}

	@Override
	public Menu createEntity(String restaurantName){
		Optional<Restaurant> restaurantOptional = restaurantService.findByName(restaurantName);
		Restaurant restaurant;
		if (restaurantOptional.isPresent()) {
			restaurant = restaurantOptional.get();
		}else {
			restaurant = new Restaurant(restaurantName);
			restaurantService.create(restaurant);
			assertNotNull(restaurant.getId());
			restaurantService.flush();
			assertTrue(restaurantService.findByName(restaurantName).isPresent());
		}
		Menu menu = new Menu();
		menu.setRestaurant(restaurant);
		return menu;
	}

	@Override
	@Test
	public void saveNonUnique() throws Exception {
		//No constraint in DB
		assertTrue(true);
	}

	@Test
	public void loadAllByDate() throws Exception {
		Menu t1 = createEntity(getType().getSimpleName() + " Crud loadAllByDate 1");
		menuService.create(t1);
		Menu t2 = createEntity(getType().getSimpleName() + " Crud loadAllByDate 2");
		menuService.create(t2);
		List<Menu> tList = menuService.getAllByDate(new Date());
		assertNotNull(tList);
		assertFalse(tList.isEmpty());
		assertTrue(tList.size() > 1);
		assertTrue(tList.contains(t1));
		assertTrue(tList.contains(t2));
	}

	@Test
	public void addMenuItem() throws Exception {
		String restaurantName = getType().getSimpleName() + " Crud addMenuItem";
		String dishName = "Dish 1";

		Menu t1 = createEntity(restaurantName);
		Menu tdb = menuService.create(t1);
		assertEquals(0, tdb.getItems().size());

		MenuItem menuItem = createMenuItem(tdb, dishName);
		menuService.addMenuItem(tdb.getId(), new MenuItemDto(dishName, menuItem.getDish().getId(), menuItem.getPrice()));
		tdb = menuService.getById(tdb.getId()).get();
		assertEquals(1, tdb.getItems().size());
		assertEquals(menuItem.getMenu().getId(), tdb.getItems().iterator().next().getMenu().getId());
	}

	@Test
	public void getMenuItemById() throws Exception {
		String restaurantName = getType().getSimpleName() + " Crud getMenuItemById";
		String dishName = "Dish 1" ;

		Menu t1 = createEntity(restaurantName);
		t1.getItems().add(createMenuItem(t1, dishName));
		Menu tdb = menuService.create(t1);

		MenuItem menuItem = tdb.getItems().iterator().next();

		Optional<MenuItem> menuItemOptional = menuService.getMenuItemById(menuItem.getId());

		assertTrue(menuItemOptional.isPresent());
		assertEquals(menuItem, menuItemOptional.get());
	}

	@Test
	public void updateMenuItem() throws Exception {
		String restaurantName = getType().getSimpleName() + " Crud updateMenuItem";
		String dishName = "Dish 1" ;

		Menu t1 = createEntity(restaurantName);
		t1.getItems().add(createMenuItem(t1, dishName));
		Menu tdb = menuService.create(t1);

		MenuItem menuItem = tdb.getItems().iterator().next();
		BigDecimal oldPrice = menuItem.getPrice();
		Optional<MenuItem> menuItemOptional = menuService.getMenuItemById(menuItem.getId());

		assertTrue(menuItemOptional.isPresent());
		assertEquals(menuItem, menuItemOptional.get());

		MenuItem menuItemSaved = menuService.updateMenuItem(t1.getId(), menuItem.getId(), new MenuItemDto(dishName, menuItem.getPrice().add(BigDecimal.ONE)));

		menuItemOptional = menuService.getMenuItemById(menuItem.getId());
		assertTrue(menuItemOptional.isPresent());

 		assertEquals(BigDecimal.ONE, menuItemOptional.get().getPrice().subtract(oldPrice).setScale(0, BigDecimal.ROUND_HALF_EVEN));

	}

	@Test
	public void deleteMenuItem() throws Exception {
		String restaurantName = getType().getSimpleName() + " Crud deleteMenuItem";
		String dishName = "Dish 1" ;

		Menu t1 = createEntity(restaurantName);
		t1.getItems().add(createMenuItem(t1, dishName));
		Menu tdb = menuService.create(t1);

		MenuItem menuItem = tdb.getItems().iterator().next();

		Optional<MenuItem> menuItemOptional = menuService.getMenuItemById(menuItem.getId());
		assertTrue(menuItemOptional.isPresent());
		assertEquals(menuItem, menuItemOptional.get());

		menuService.deleteMenuItem(tdb.getId(), menuItem.getId());
		menuService.flush();
		menuItemOptional = menuService.getMenuItemById(menuItem.getId());
		assertFalse(menuItemOptional.isPresent());
	}

	@Test
	public void overwriteEmptyMenuItems() throws Exception {
		String restaurantName = getType().getSimpleName() + " Crud overwriteEmptyMenuItems";
		String dish1Name = "Dish 1" ;

		Menu t1 = createEntity(restaurantName);
		Menu tdb = menuService.create(t1);
		assertEquals(0, tdb.getItems().size());

		MenuDto menuDto = new MenuDto();
		menuDto.setRestaurantId(tdb.getRestaurant().getId());
		menuDto.getMenu().add(new MenuItemDto(dish1Name, BigDecimal.TEN));

		menuService.overwriteMenuItems(menuDto);

		tdb = menuService.getById(tdb.getId()).get();
		assertEquals(1, tdb.getItems().size());

		MenuItem replacedItem = tdb.getItems().iterator().next();
		assertNotNull(replacedItem.getId());

	}

	@Test
	public void overwriteExistingMenuItems() throws Exception {
		String restaurantName = getType().getSimpleName() + " Crud overwriteMenuItems";
		String dishName = "Dish 1" ;

		Menu t1 = createEntity(restaurantName);
		t1.getItems().add(createMenuItem(t1, dishName));
		Menu tdb = menuService.create(t1);

		MenuItem menuItem = tdb.getItems().iterator().next();

		Optional<MenuItem> menuItemOptional = menuService.getMenuItemById(menuItem.getId());
		assertTrue(menuItemOptional.isPresent());
		assertEquals(menuItem, menuItemOptional.get());

		menuService.deleteMenuItem(tdb.getId(), menuItem.getId());
		menuService.flush();

		menuItemOptional = menuService.getMenuItemById(menuItem.getId());
		assertFalse(menuItemOptional.isPresent());

	}

	private MenuItem createMenuItem(Menu menu, String dishName) {
		MenuItem menuItem = new MenuItem();
		menuItem.setDish(dishService.findByName(dishName).get());
		menuItem.setPrice(new BigDecimal(10.2));
		menuItem.setMenu(menu);
		return menuItem;
	}

}
