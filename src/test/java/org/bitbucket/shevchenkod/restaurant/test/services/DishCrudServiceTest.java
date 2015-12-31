package org.bitbucket.shevchenkod.restaurant.test.services;

import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.service.DishService;
import org.bitbucket.shevchenkod.restaurant.view.dto.DishDto;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@EnableWebMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class DishCrudServiceTest extends GenericCrudServiceTest<Long, Dish, DishService> {

	@Override
	public Dish createEntity(String name){
		Dish entity = new Dish();
		entity.setName(name);
		return entity;
	}

	@Override
	public Dish updateEntity(Dish entity, String name){
		entity.setName(name);
		return entity;
	}

	@Override
	public Class getType(){
		return Dish.class;
	}

	@Override
	protected Optional<Dish> getByName(String name) {
		return getService().findByName(name);
	}

}
