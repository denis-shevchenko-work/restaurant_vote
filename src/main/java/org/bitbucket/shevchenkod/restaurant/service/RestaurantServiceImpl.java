package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.service.repository.RestaurantRepository;
import org.bitbucket.shevchenkod.restaurant.view.dto.RestaurantDto;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RestaurantServiceImpl extends GenericCrudService<Long, Restaurant, RestaurantRepository> implements RestaurantService {

	@Override
	public Optional<Restaurant> findByName(String name) {
		return getRepository().findByName(name);
	}

}
