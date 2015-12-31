package org.bitbucket.shevchenkod.restaurant.service.transformer;

import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.view.dto.DishDto;
import org.springframework.stereotype.Service;

@Service
public class DishTransformerService implements TransformerService<DishDto, Dish> {


	@Override
	public Dish transform(DishDto dto) {
		return new Dish(dto.getId(), dto.getName());
	}

	@Override
	public DishDto transform(Dish dish) {
		return new DishDto(dish.getId(), dish.getName());
	}
}
