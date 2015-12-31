package org.bitbucket.shevchenkod.restaurant.test.controller;

import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.controller.RestaurantController;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.service.*;
import org.bitbucket.shevchenkod.restaurant.view.dto.RestaurantDto;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@EnableWebMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class RestaurantCrudControllerTest extends GenericCrudControllerCrudTest<Long, Restaurant, RestaurantDto, RestaurantService, RestaurantController> {

	@Autowired
	RestaurantController restaurantController;

	@Override
	protected RestaurantController getController() {
		return restaurantController;
	}

	protected String getEndPoint(){
		return "/api/restaurants";
	}

	protected Restaurant createEntity(String name) {
		Restaurant entity = new Restaurant();
		entity.setName(name);
		return entity;
	}

	@Override
	protected void updateEntity(Restaurant entity, String name) {
		entity.setName(name);
	}

	@Override
	protected Optional<Restaurant> findByName(String name) {
		return getService().findByName(name);
	}
}
