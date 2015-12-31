package org.bitbucket.shevchenkod.restaurant.controller;

import org.bitbucket.shevchenkod.restaurant.model.Identifiable;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.service.RestaurantService;
import org.bitbucket.shevchenkod.restaurant.service.transformer.TransformerService;
import org.bitbucket.shevchenkod.restaurant.view.dto.RestaurantDto;
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
@RequestMapping("/api/restaurants")
public class RestaurantController extends GenericCrudController<Long, Restaurant, RestaurantService, RestaurantDto> {

	@Autowired
	TransformerService<RestaurantDto, Restaurant> transformerService;

	@Override
	protected TransformerService<RestaurantDto, Restaurant> getTransformerService() {
		return transformerService;
	}
}
