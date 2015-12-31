package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.view.dto.RestaurantDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface RestaurantService extends CrudService<Long, Restaurant> {

	Optional<Restaurant> findByName(String name);
}
