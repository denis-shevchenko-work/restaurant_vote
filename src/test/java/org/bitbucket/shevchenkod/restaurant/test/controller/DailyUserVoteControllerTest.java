package org.bitbucket.shevchenkod.restaurant.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.controller.UserVoteController;
import org.bitbucket.shevchenkod.restaurant.model.*;
import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.service.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@EnableWebMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class DailyUserVoteControllerTest extends AbstractControllerTestCase<UserVoteController> {

    @Autowired
    private UserVoteController controller;

    @Autowired
    private UserVoteService userVoteService;

	@Autowired
    private RestaurantService restaurantService;

	@Autowired
    private DishService dishService;

	@Autowired
    private MenuService menuService;

	@Autowired
    private UserDetailsService userDetailsService;

	@Autowired
    private UserService userService;

	@Autowired
	private WebApplicationContext context;

	@Override
	protected UserVoteController getController() {
		return controller;
	}

	private Authentication userAuthentication;

	Authentication getUserAuthentication() {
		if (userAuthentication == null) {
			UserDetails user = userDetailsService.loadUserByUsername("user");
			userAuthentication = new TestingAuthenticationToken(user, user.getPassword(), new ArrayList<GrantedAuthority>(user.getAuthorities()));
		}
		return userAuthentication;
	}

	/*
	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		//mvc.perform(get("/api/votes").with(user("user").password("123").roles("USER"))).andExpect(status().isOk()).andReturn();
	}*/

	@Test
	public void CreateAndGetTodayVoteTest() throws Exception {
		Date date = new Date();
		Optional<Restaurant> restaurant = restaurantService.findByName("Rest 1");
		assertTrue(restaurant.isPresent());

		Optional<Dish> dish1 = dishService.findByName("Dish 1");
		assertTrue(dish1.isPresent());

		Optional<Menu> menuOptional = menuService.findByRestaurantAndDate(restaurant.get(), date);
		Menu menu;
		if (menuOptional.isPresent()) {
			menu = menuOptional.get();
		} else {
			menu = new Menu();
			menu.setRestaurant(restaurant.get());
			menu.setDate(new Date());
			List<MenuItem> items = new ArrayList<>();
			menu.setItems(items);
			MenuItem item = new MenuItem();
			item.setDish(dish1.get());
			item.setMenu(menu);
			items.add(item);

			menuService.create(menu);
		}

		Optional<User> user = userService.findByLogin("user");
		assertTrue(user.isPresent());

		UserVote userVote = userVoteService.createOrUpdateVote(user.get(), menu, date);
		assertNotNull(userVote.getId());

		JsonNode resultNode = performAuthenticatedRequestAndReturnJsonNode(getUserAuthentication(), get("/api/votes"));
		assertTrue(performAuthenticatedRequestAndReturnJsonNode(getUserAuthentication(), get("/api/votes")).findValuesAsText("restaurant_name").contains(restaurant.get().getName()));
		//TODO

	}


	@Test
	public void UpdateAndGetTodayVoteTest() throws Exception {
		Date date = new Date();
		Optional<Restaurant> restaurant = restaurantService.findByName("Rest 1");
		assertTrue(restaurant.isPresent());

		Optional<Dish> dish1 = dishService.findByName("Dish 1");
		assertTrue(dish1.isPresent());

		Optional<Menu> menuOptional = menuService.findByRestaurantAndDate(restaurant.get(), date);
		Menu menu;
		if (menuOptional.isPresent()) {
			menu = menuOptional.get();
		} else {
			menu = new Menu();
			menu.setRestaurant(restaurant.get());
			menu.setDate(new Date());
			List<MenuItem> items = new ArrayList<>();
			menu.setItems(items);
			MenuItem item = new MenuItem();
			item.setDish(dish1.get());
			item.setMenu(menu);
			items.add(item);
			menuService.create(menu);
			assertNotNull(menu.getId());
		}

		Optional<User> user = userService.findByLogin("user");
		assertTrue(user.isPresent());

		UserVote userVote = userVoteService.createOrUpdateVote(user.get(), menu, date);
		JsonNode resultNode = performAuthenticatedRequestAndReturnJsonNode(getUserAuthentication(),
				post("/api/votes").param("restaurant_name", restaurant.get().getName()));

		resultNode = performAuthenticatedRequestAndReturnJsonNode(getUserAuthentication(), get("/api/votes"));
		assertTrue(performAuthenticatedRequestAndReturnJsonNode(getUserAuthentication(), get("/api/votes")).findValuesAsText("restaurant_name").contains(restaurant.get().getName()));

	}

}
