package org.bitbucket.shevchenkod.restaurant.test.services;


import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.model.*;
import org.bitbucket.shevchenkod.restaurant.service.*;
import org.bitbucket.shevchenkod.restaurant.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
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
public class UserVoteServiceTest {
	
	@Autowired
	UserVoteService userVoteService;

	@Autowired
	RestaurantService restaurantService;

	@Autowired
	MenuService menuService;

	@Autowired
	DishService dishService;

	@Autowired
	UserService userService;

	@Test
	public void createOrUpdateVote(){
		Date date = new Date();
		String restaurantName = " createOrUpdateVote ";
		String dishName = " createOrUpdateVote ";
		Restaurant restaurant = restaurantService.create(new Restaurant(restaurantName));
		Dish dish = dishService.create(new Dish(dishName));

		Menu menu = menuService.create(createMenu(restaurant, dish));
		User user = userService.findByLogin("user").get();

		Optional<UserVote> previousVoteOptional = userVoteService.findForUserForToday(user);
		UserVote vote = userVoteService.createOrUpdateVote(user, menu, date);
		Optional<UserVote> newVoteOptional = userVoteService.findForUserForToday(user);

		assertTrue(newVoteOptional.isPresent());

		//saved
		assertEquals(menu.getId(), newVoteOptional.get().getMenu().getId());
		//replaced
		if (previousVoteOptional.isPresent()){
			assertNotEquals(previousVoteOptional.get().getMenu().getId(), newVoteOptional.get().getMenu().getId());
		}

	}


	@Test
	public void collectResultsForToday(){
		Date date = new Date();

		String restaurant1Name = " collectResultsForToday 1";
		String restaurant2Name = " collectResultsForToday 2";
		String restaurant3Name = " collectResultsForToday 3";
		String dishName = "Dish 1";

		Restaurant restaurant1 = restaurantService.create(new Restaurant(restaurant1Name));
		Restaurant restaurant2 = restaurantService.create(new Restaurant(restaurant2Name));
		Restaurant restaurant3 = restaurantService.create(new Restaurant(restaurant3Name));

		Dish dish = dishService.findByName(dishName).get();

		Menu menu1 = menuService.create(createMenu(restaurant1, dish));
		Menu menu2 = menuService.create(createMenu(restaurant2, dish));
		Menu menu3 = menuService.create(createMenu(restaurant3, dish));

		User user1 = userService.findByLogin("user").get();
		User user2 = userService.findByLogin("user2").get();
		User user3 = userService.findByLogin("user3").get();


		UserVote vote1 = userVoteService.createOrUpdateVote(user1, menu1, date);
		UserVote vote2 = userVoteService.createOrUpdateVote(user2, menu2, date);
		UserVote vote3 = userVoteService.createOrUpdateVote(user3, menu1, date);

		List<Object[]> results = userVoteService.collectResultsForToday();
		assertTrue(results.size() > 1);

		assertFalse(results.contains(menu3));
		assertFalse(results.contains(menu1));
		assertFalse(results.contains(menu2));

		results.stream().forEach(result -> {
					if (((Menu) result[0]).getId().equals(menu1.getId())) {
						assertEquals(2, ((Long) result[1]).intValue());
					}
					if (((Menu) result[0]).getId().equals(menu2.getId())) {
						assertEquals(1, ((Long) result[1]).intValue());
					}

				}
		);

	}

	@Test
	public void findForUserByDate(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR, 11);
		Date date = c.getTime();

		String restaurantName = " findForUserByDate ";
		String dishName = " findForUserByDate ";
		Restaurant restaurant = restaurantService.create(new Restaurant(restaurantName));
		Dish dish = dishService.create(new Dish(dishName));

		Menu menu = menuService.create(createMenu(restaurant, dish));
		User user = userService.findByLogin("user").get();

		Optional<UserVote> previousVoteOptional = userVoteService.findForUserByDate(user, date);
		UserVote vote = userVoteService.createOrUpdateVote(user, menu, date);
		Optional<UserVote> newVoteOptional = userVoteService.findForUserByDate(user, date);
		assertTrue(newVoteOptional.isPresent());
		assertEquals(menu.getId(), newVoteOptional.get().getMenu().getId());


	}

	private Menu createMenu(Restaurant restaurant, Dish dish) {
		Menu menu = new Menu();
		menu.setRestaurant(restaurant);
		menu.getItems().add(new MenuItem(menu, dish, BigDecimal.TEN));
		return menu;
	}


}
