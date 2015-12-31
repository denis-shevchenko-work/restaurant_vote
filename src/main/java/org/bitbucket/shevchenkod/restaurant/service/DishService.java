package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.view.dto.DishDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface DishService extends CrudService<Long, Dish> {
	Optional<Dish> findByName(String name);
}
