package org.bitbucket.shevchenkod.restaurant.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.controller.MenuController;
import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.MenuItem;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.model.security.UserRoles;
import org.bitbucket.shevchenkod.restaurant.service.DishService;
import org.bitbucket.shevchenkod.restaurant.service.MenuService;
import org.bitbucket.shevchenkod.restaurant.service.RestaurantService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@EnableWebMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class MenuControllerTest extends AbstractControllerTestCase<MenuController> {

    @Autowired
    private MenuController controller;

	@Autowired
    private RestaurantService restaurantService;

	@Autowired
    private DishService dishService;

	@Autowired
    private MenuService menuService;

/*	@Autowired
    private MenuItemService menuItemService;
*/

	@Autowired
	private UserDetailsService userDetailsService;

	private Authentication adminAuthentication = new TestingAuthenticationToken("admin", "123", Arrays.asList(new GrantedAuthority[]{new SimpleGrantedAuthority(UserRoles.ADMIN.getName())}));

	Authentication getAuthentication() {
		if (adminAuthentication == null) {
			UserDetails user = userDetailsService.loadUserByUsername("user");
			adminAuthentication = new TestingAuthenticationToken(user, user.getPassword(), (List<GrantedAuthority>) user.getAuthorities());
		}
		return adminAuthentication;
	}

	@Override
    protected MenuController getController() {
        return controller;
    }

	@Before
	public void setup() {

	}

	@Test
	public void createAndGetTodayMenuSimple() throws Exception {
		String restaurantName = "Rest menuController.createAndGetTodayMenuSimple";
		String dishName = "Dish 1";
		Restaurant restaurant = new Restaurant(restaurantName);
		restaurantService.create(restaurant);
		restaurantService.flush();
		assertNotNull(restaurant.getId());
		assertTrue(restaurantService.findByName(restaurantName).isPresent());
		MvcResult result =
				performAuthenticatedRequestAndReturn(getAuthentication(), post("/api/menus")
						//mvc.perform(post("/api/menus").with(user("admin").password("123").authorities(new ArrayList<GrantedAuthority>() {{new SimpleGrantedAuthority(UserRoles.ADMIN.getName());}}))
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"restaurant_name\":\"" + restaurantName + "\", \"menu\":[{\"dish_name\":\"" + dishName + "\",\"price\":\"15.5\"}]}"))
				//.andExpect(status().isOk()).andReturn()
				;
		Optional<Menu> menuOptional = menuService.findByRestaurantNameAndDate(restaurantName, new Date());
		validateMenu(menuOptional, restaurantName, 1);
		validateItem(menuOptional.get().getItems().iterator().next(), menuOptional.get(), dishName, 15.5);

		JsonNode resultNode = performAuthenticatedRequestAndReturnJsonNode(getAuthentication(), get("/api/menus/today"));
	}


	@Test
	public void createAndGetTodayMenuComplex() throws Exception {
		String restaurantName = "Rest menuController.createAndGetTodayMenuComplex";
		String dishName = "Dish 1";
		Restaurant restaurant = new Restaurant(restaurantName);
		restaurantService.create(restaurant);
		restaurantService.flush();
		assertNotNull(restaurant.getId());
		assertTrue(restaurantService.findByName(restaurantName).isPresent());
		MvcResult result =
				performAuthenticatedRequestAndReturn(getAuthentication(), post("/api/menus")
						//mvc.perform(post("/api/menus").with(user("admin").password("123").authorities(new ArrayList<GrantedAuthority>() {{new SimpleGrantedAuthority(UserRoles.ADMIN.getName());}}))
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"restaurant_name\":\"" + restaurantName + "\", \"menu\":[{\"dish_name\":\"" + dishName + "\",\"price\":\"15.5\"}]}"))
				//.andExpect(status().isOk()).andReturn()
				;
		Optional<Menu> menuOptional = menuService.findByRestaurantNameAndDate(restaurantName, new Date());
		validateMenu(menuOptional, restaurantName, 1);
		validateItem(menuOptional.get().getItems().iterator().next(), menuOptional.get(), dishName, 15.5);

		JsonNode resultNode = performAuthenticatedRequestAndReturnJsonNode(getAuthentication(), get("/api/menus/today"));
	}

	@Test
	public void recreateAndGetTodayMenu() throws Exception {
		String restaurantName = "Rest menuController.recreateAndGetTodayMenu";
		String dish1Name = "Dish 1";
		String dish2Name = "Dish 2";
		Restaurant restaurant = new Restaurant(restaurantName);
		restaurantService.create(restaurant);
		restaurantService.flush();
		assertNotNull(restaurant.getId());
		assertTrue(restaurantService.findByName(restaurantName).isPresent());
		MvcResult result =
				performAuthenticatedRequestAndReturn(getAuthentication(), post("/api/menus")
						//mvc.perform(post("/api/menus").with(user("admin").password("123").authorities(new ArrayList<GrantedAuthority>() {{new SimpleGrantedAuthority(UserRoles.ADMIN.getName());}}))
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"restaurant_name\":\"" + restaurantName + "\", \"menu\":[{\"dish_name\":\"" + dish1Name + "\",\"price\":\"15.5\"}]}"))
				//.andExpect(status().isOk()).andReturn()
				;
		Optional<Menu> menuOptional = menuService.findByRestaurantNameAndDate(restaurantName, new Date());
		validateMenu(menuOptional, restaurantName, 1);
		validateItem(menuOptional.get().getItems().iterator().next(), menuOptional.get(), dish1Name, 15.5);

		result =
				performAuthenticatedRequestAndReturn(getAuthentication(), post("/api/menus")
						//mvc.perform(post("/api/menus").with(user("admin").password("123").authorities(new ArrayList<GrantedAuthority>() {{new SimpleGrantedAuthority(UserRoles.ADMIN.getName());}}))
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"restaurant_name\":\"" + restaurantName + "\", \"menu\":[{\"dish_name\":\"" + dish1Name + "\",\"price\":\"16.5\"}]}"))
				//.andExpect(status().isOk()).andReturn()
				;
		menuOptional = menuService.findByRestaurantNameAndDate(restaurantName, new Date());
		validateMenu(menuOptional, restaurantName, 1);
		validateItem(menuOptional.get().getItems().iterator().next(), menuOptional.get(), dish1Name, 16.5);

		//replace old menu
		result =
				performAuthenticatedRequestAndReturn(getAuthentication(), post("/api/menus")
						//mvc.perform(post("/api/menus").with(user("admin").password("123").authorities(new ArrayList<GrantedAuthority>() {{new SimpleGrantedAuthority(UserRoles.ADMIN.getName());}}))
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"restaurant_name\":\"" + restaurantName + "\", \"menu\":[" +
								"{\"dish_name\":\"" + dish1Name + "\",\"price\":\"16.5\"}" +
								",{\"dish_name\":\"" + dish2Name + "\",\"price\":\"6.5\"}]}"))
				//.andExpect(status().isOk()).andReturn()
		;
		menuOptional = menuService.findByRestaurantNameAndDate(restaurantName, new Date());
		validateMenu(menuOptional, restaurantName, 2);
		Iterator<MenuItem> itemsIterator = menuOptional.get().getItems().iterator();
		validateItem(itemsIterator.next(), menuOptional.get(), dish1Name, 16.5);
		validateItem(itemsIterator.next(), menuOptional.get(), dish2Name, 6.5);

		JsonNode resultNode = performAuthenticatedRequestAndReturnJsonNode(getAuthentication(), get("/api/menus/today"));
	}

	private void validateMenu(Optional<Menu> menuOptional, String restaurantName, int itemsCount) {
		assertTrue(menuOptional.isPresent());
		assertEquals(menuOptional.get().getRestaurant().getName(), restaurantName);
		assertNotNull(menuOptional.get().getItems());
		assertFalse(menuOptional.get().getItems().isEmpty());
		assertEquals(itemsCount, menuOptional.get().getItems().size());

	}

	private void validateItem(MenuItem item, Menu menu, String dishName, Double price){
		assertEquals(dishName, item.getDish().getName());
		assertEquals(new BigDecimal(price), item.getPrice());
	}

	@Test
	public void createAndGetTodayMenuForBadRestaurant() throws Exception {
		String restaurantName = "Rest CreateAndGetTodayMenuForBadRestaurant";

		Optional<Menu> menuOptional = menuService.findByRestaurantNameAndDate(restaurantName, new Date());
		assertFalse(menuOptional.isPresent());


		int before = menuService.getAllByDate(new Date()).size();

		String dishName = "Dish 1";
		MvcResult result =
				performAuthenticatedRequest(getAuthentication(), post("/api/menus")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"restaurant_name\":\"" + restaurantName + "\", \"menu\":[{\"dish_name\":\"" + dishName + "\",\"price\":\"15.5\"}]}"))
				.andExpect(status().isBadRequest()).andReturn()
				;

		int after = menuService.getAllByDate(new Date()).size();
		assertEquals(before, after);
	}


	@Test
	public void createAndGetMenuItemInTodayMenu() throws Exception {
		String restaurantName = "Rest 1";
		String dishName = "Dish 1";

		Date date = new Date();
		Optional<Restaurant> restaurant = restaurantService.findByName(restaurantName);
		assertTrue(restaurant.isPresent());

		Optional<Dish> dish1 = dishService.findByName(dishName);
		assertTrue(dish1.isPresent());

		Optional<Menu> menuOptional = menuService.findByRestaurantAndDate(restaurant.get(), date);
		Menu menu;
		if (menuOptional.isPresent()) {
			menu = menuOptional.get();
			menu.getItems().clear();
			menuService.update(menu);
		} else {
			menu = new Menu();
			menu.setRestaurant(restaurant.get());
			menu.setDate(new Date());
			List<MenuItem> items = new ArrayList<>();
			menu.setItems(items);
			menuService.create(menu);
		}
		menuService.flush();
		menu = menuService.findByRestaurantAndDate(restaurant.get(), date).get();
		assertEquals(0, menu.getItems().size());

		MvcResult result =
				performAuthenticatedRequestAndReturn(getAuthentication(), post("/api/menus/" + menu.getId() + "/items/")
						//mvc.perform(post("/api/menus").with(user("admin").password("123").authorities(new ArrayList<GrantedAuthority>() {{new SimpleGrantedAuthority(UserRoles.ADMIN.getName());}}))
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"dish_name\":\"" + dish1.get().getName() + "\",\"price\":\"16.5\"}]}"))
				//.andExpect(status().isOk()).andReturn()
				;
		menuOptional = menuService.getById(menu.getId());
		assertTrue(menuOptional.isPresent());
		assertEquals(menuOptional.get().getRestaurant().getName(), restaurantName);
		assertNotNull(menuOptional.get().getItems());
		assertFalse(menuOptional.get().getItems().isEmpty());
		assertEquals(1, menuOptional.get().getItems().size());
		MenuItem item = menuOptional.get().getItems().iterator().next();
		assertEquals(dishName, item.getDish().getName());
		assertEquals(new BigDecimal(16.5), item.getPrice());

		JsonNode resultNode = performAuthenticatedRequestAndReturnJsonNode(getAuthentication(), get("/api/menus/today"));
	}

	@Test
	public void updateMenuItemInTodayMenu() throws Exception {
		String restaurantName = "Rest 1";
		String dishName = "Dish 1";

		Date date = new Date();
		Optional<Restaurant> restaurant = restaurantService.findByName("Rest 1");
		assertTrue(restaurant.isPresent());

		Optional<Dish> dish1 = dishService.findByName("Dish 1");
		assertTrue(dish1.isPresent());

		Optional<Menu> menuOptional = menuService.findByRestaurantAndDate(restaurant.get(), date);
		Menu menu;
		if (menuOptional.isPresent()) {
			menu = menuOptional.get();
			menu.getItems().clear();
			menuService.update(menu);
			menuService.flush();
		} else {
			menu = new Menu();
			menu.setRestaurant(restaurant.get());
			menu.setDate(new Date());
			menuService.create(menu);
		}
		List<MenuItem> items = menu.getItems();
		MenuItem item = new MenuItem();
		item.setDish(dish1.get());
		item.setMenu(menu);
		items.add(item);
		menuService.update(menu);

		MenuItem menuItem = menu.getItems().iterator().next();

		MvcResult result =
				performAuthenticatedRequestAndReturn(getAuthentication(), put("/api/menus/" + menu.getId() + "/items/" + menuItem.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"dish_name\":\"" + dishName + "\",\"price\":\"" + BigDecimal.ONE.add(menuItem.getPrice()) + "\"}"))
				;
		JsonNode resultNode = performAuthenticatedRequestAndReturnJsonNode(getAuthentication(), get("/api/menus/today"));

		final Optional<MenuItem> menuItemOptional = menuService.getMenuItemById(menuItem.getId());
		assertTrue(menuItemOptional.isPresent());
		assertEquals(menuItem.getId(), menuItemOptional.get().getId());
		assertEquals(menuItem.getDish().getName(), menuItemOptional.get().getDish().getName());
		//assertEquals(0, menuItem.getPrice().subtract(menuItemOptional.get().getPrice()).compareTo(BigDecimal.ONE));

		assertTrue(resultNode.findPath("list").isArray());
		resultNode.findPath("list").forEach(node -> {
					if (menu.getId().equals(node.get("id").asLong())) {
						assertTrue(node.get("menu").isArray());
						node.get("menu").forEach(menuNode -> {
									if (menuItem.getId().equals(menuNode.get("id").asLong())) {
										assertEquals(0, menuNode.get("price").decimalValue().subtract(menuItemOptional.get().getPrice()).compareTo(BigDecimal.ZERO));
									}
								}
						);
					}
				}
		);
	}




}
