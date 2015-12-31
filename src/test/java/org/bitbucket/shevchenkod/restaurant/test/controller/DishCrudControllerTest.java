package org.bitbucket.shevchenkod.restaurant.test.controller;

import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.controller.DishController;
import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.service.DishService;
import org.bitbucket.shevchenkod.restaurant.view.dto.DishDto;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@EnableWebMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class DishCrudControllerTest extends GenericCrudControllerCrudTest<Long, Dish, DishDto, DishService, DishController> {

	@Autowired
	DishController dishController;

	@Autowired
	DishService dishService;

	@Override
	protected DishController getController() {
		return dishController;
	}


	protected String getEndPoint(){
		return "/api/dishes";
	}

	protected Dish createEntity(String name) {
		Dish entity = new Dish();
		entity.setName(name);
		return entity;
	}

	@Override
	protected void updateEntity(Dish entity, String name) {
		entity.setName(name);
	}

	@Override
	protected Optional<Dish> findByName(String name) {
		return getService().findByName(name);
	}

}
