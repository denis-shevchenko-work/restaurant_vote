package org.bitbucket.shevchenkod.restaurant.controller;

import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.model.Identifiable;
import org.bitbucket.shevchenkod.restaurant.service.DishService;
import org.bitbucket.shevchenkod.restaurant.service.transformer.TransformerService;
import org.bitbucket.shevchenkod.restaurant.view.dto.DishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * {@inheritDoc}
 */
@RestController
@RequestMapping("/api/dishes")
public class DishController extends GenericCrudController<Long, Dish, DishService, DishDto> {

	@Autowired
	TransformerService<DishDto, Dish> transformerService;

	@Override
	protected TransformerService<DishDto, Dish> getTransformerService() {
		return transformerService;
	}
}
