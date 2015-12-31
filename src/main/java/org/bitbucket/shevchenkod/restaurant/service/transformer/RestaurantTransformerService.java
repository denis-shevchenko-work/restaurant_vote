package org.bitbucket.shevchenkod.restaurant.service.transformer;

import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.view.dto.DishDto;
import org.bitbucket.shevchenkod.restaurant.view.dto.RestaurantDto;
import org.springframework.stereotype.Service;

@Service
public class RestaurantTransformerService implements TransformerService<RestaurantDto, Restaurant> {

	@Override
	public RestaurantDto transform(Restaurant restaurant) {
		return new RestaurantDto(restaurant.getId(), restaurant.getName());
	}

	@Override
	public Restaurant transform(RestaurantDto restaurantDto) {
		return new Restaurant(restaurantDto.getId(), restaurantDto.getName());
	}
}
