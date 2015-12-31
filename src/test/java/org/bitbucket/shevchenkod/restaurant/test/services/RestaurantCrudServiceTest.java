package org.bitbucket.shevchenkod.restaurant.test.services;

import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.service.RestaurantService;
import org.bitbucket.shevchenkod.restaurant.view.dto.RestaurantDto;
import org.junit.runner.RunWith;
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
public class RestaurantCrudServiceTest extends GenericCrudServiceTest<Long, Restaurant, RestaurantService> {

	@Override
	public Restaurant createEntity(String name){
		Restaurant entity = new Restaurant();
		entity.setName(name);
		return entity;
	}

	@Override
	public Restaurant updateEntity(Restaurant entity, String name){
		entity.setName(name);
		return  entity;
	}

	@Override
	public Class getType(){
		return Restaurant.class;
	}

	@Override
	protected Optional<Restaurant> getByName(String name) {
		return getService().findByName(name);
	}
}
