package org.bitbucket.shevchenkod.restaurant.service;

import javassist.convert.Transformer;
import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.service.repository.DishRepository;
import org.bitbucket.shevchenkod.restaurant.service.transformer.*;
import org.bitbucket.shevchenkod.restaurant.view.dto.DishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

@Service
@Transactional
public class DishServiceImpl extends GenericCrudService<Long, Dish, DishRepository> implements DishService {

	@Override
	public Optional<Dish> findByName(String name) {
		return getRepository().findByName(name);
	}

}
