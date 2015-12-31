package org.bitbucket.shevchenkod.restaurant.controller;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.shevchenkod.restaurant.view.dto.JsonModel;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuDto;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuItemDto;
import org.bitbucket.shevchenkod.restaurant.view.serializer.MenuItemSerializer;
import org.bitbucket.shevchenkod.restaurant.view.serializer.MenuSerializer;
import org.bitbucket.shevchenkod.restaurant.validator.CompoundValidator;
import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.MenuItem;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.service.DishService;
import org.bitbucket.shevchenkod.restaurant.service.MenuService;
import org.bitbucket.shevchenkod.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * {@inheritDoc}
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

	private static final MenuItemSerializer MENU_ITEM_SERIALIZER = new MenuItemSerializer();
	private static final MenuSerializer MENU_SERIALIZER = new MenuSerializer(MENU_ITEM_SERIALIZER);

	@Autowired
	RestaurantService restaurantService;

	@Autowired
	MenuService menuService;

	@Autowired
	DishService dishService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "date", new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
		binder.addValidators(new CompoundValidator(new MenuItemValidator(), new MenuValidator()));
	}

	public class MenuValidator implements Validator {

		@Override
		public boolean supports(Class clazz) {
			return MenuDto.class.equals(clazz);
		}

		@Override
		public void validate(Object target, Errors errors) {
			MenuDto menu = (MenuDto) target;

			if(StringUtils.isEmpty(menu.getRestaurantName()) && menu.getRestaurantId() == null) {
				errors.rejectValue("restaurant_name", "one_of_restaurant_name_or_restaurant_id_is_necessary");
				errors.rejectValue("restaurant_id", "one_of_restaurant_name_or_restaurant_id_is_necessary");
			}

		}

	}

	public class MenuItemValidator implements Validator {

		@Override
		public boolean supports(Class clazz) {
			return MenuItemDto.class.equals(clazz);
		}

		@Override
		public void validate(Object target, Errors errors) {
			MenuItemDto menu = (MenuItemDto) target;

			if(StringUtils.isEmpty(menu.getDishName()) && menu.getDishId() == null) {
				errors.rejectValue("dish_name", "one_of_dish_name_or_dish_id_is_necessary");
				errors.rejectValue("dish_id", "one_of_dish_name_or_dish_id_is_necessary");
			}

			if(menu.getPrice().signum() >= 0) {
				errors.rejectValue("price", "price_should_be_positive_or_zero");
			}

		}

	}

	/**
	 * Get the list of all restaurants menus for today. Day start end finish at 11AM.
	 * URL: <b>/api/menus/today</b><br/>
	 * Method: <b>GET</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 * <code>
	 * {"date":"20/12/2015", [{"id":1, "restaurant_name":"Restaurant 1", "menu":[{"id":1, "dish_name":"Dish 1", "price":15.5}...]}, ...]}
	 * </code>
	 *
	 */
	@RequestMapping(value = "/today", method = RequestMethod.GET, produces = {"application/json"})
	public JsonModel todayMenu() {
		JsonModel result = new JsonModel();
		Date today = today();
		Map<String, Object> dto = new HashMap<>();
		dto.put("date", today);//new SimpleDateFormat("dd/MM/yyyy").format(today));

		List voteListRestaurantsMenus = new ArrayList();
		for (Menu menu : menuService.getAllByDate(today) ){
			voteListRestaurantsMenus.add(MENU_SERIALIZER.serialize(menu));
		}
		dto.put("list", voteListRestaurantsMenus);
		result.putData(dto);

		return result;
	}

	/**
	 * Create restaurant menu for today. Day start end finish at 11AM.
	 * URL: <b>/api/menus</b><br/>
	 * Method: <b>POST</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 *
	 */
	@RequestMapping( method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	public JsonModel create(HttpServletResponse response, @RequestBody MenuDto menuDto, BindingResult bindingResult) throws IOException {
		JsonModel result = new JsonModel();
		result.processBindingResult(bindingResult);
		if (!result.isSuccess()){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return result;
		}

		Optional<Restaurant> restaurantOptional = restaurantService.findByName(menuDto.getRestaurantName());
		if (!restaurantOptional.isPresent()) {
			restaurantOptional = restaurantService.getById(menuDto.getRestaurantId());
			if (!restaurantOptional.isPresent()) {
				result.addError("restaurant_name", "Restaurant is not found");
				result.addError("restaurant_id", "Restaurant is not found");
				//response.getOutputStream().write(result.toJson().getBytes());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return result;
			}
		}

		menuService.overwriteMenuItems(menuDto);
		return result;
	}

	/**
	 * Delete menu for restaurant fo today. Day start end finish at 11AM.
	 * URL: <b>/api/menus/{menu_id}/items</b><br/>
	 * Method: <b>DELETE</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 *
	 */
	@RequestMapping(value = "/{menu_id}", method = RequestMethod.DELETE, produces = {"application/json"})
	public JsonModel removeDailyMenu(
			HttpServletResponse response,
			@PathVariable("menu_id") Long menuId) throws IOException {
		JsonModel result = new JsonModel();
		Optional<Menu> menuOptional = menuService.getById(menuId);
		if(!menuOptional.isPresent()){
			result.fail();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}

		menuService.delete(menuId);

		return result;
	}

	/**
	 * Add new menu item in restaurant menu. Day start end finish at 11AM.
	 * URL: <b>/api/menus/{menu_id}/items</b><br/>
	 * Method: <b>POST</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 *
	 */
	@RequestMapping(value = "/{menu_id}/items", method = RequestMethod.POST, produces = {"application/json"})
	public JsonModel addDailyMenuItem(
			HttpServletResponse response,
			@RequestBody MenuItemDto menuItemDto,
			BindingResult bindingResult,
			@PathVariable("menu_id") Long menuId) throws IOException {
		JsonModel result = new JsonModel();
		Optional<Menu> menuOptional = menuService.getById(menuId);
		if(!menuOptional.isPresent()){
			result.fail();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}

		menuService.addMenuItem(menuId, menuItemDto);

		return result;
	}

	/**
	 * Update menu item in restaurant menu.
	 * URL: <b>/api/menus/{menu_id}/items/{item_id}</b><br/>
	 * Method: <b>PUT</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 *
	 *
	 */
	@RequestMapping(value = "/{menu_id}/items/{item_id}", method = RequestMethod.PUT, produces = {"application/json"})
	public JsonModel updateDailyMenuItem(
			HttpServletResponse response,
			@RequestBody MenuItemDto menuItemDto,
			BindingResult bindingResult,
			@PathVariable("menu_id") Long menuId,
			@PathVariable("item_id") Long itemId) throws IOException {
		JsonModel result = new JsonModel();
		Optional<Menu> menuOptional = menuService.getById(menuId);
		if(!menuOptional.isPresent()){
			result.fail();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}

		Optional<MenuItem> menuItemOptional = menuService.getMenuItemById(itemId);
		if(!menuItemOptional.isPresent()){
			result.fail();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}

		menuService.updateMenuItem(menuId, itemId, menuItemDto);

		return result;
	}

	/**
	 * Delete menu item in restaurant menu.
	 * URL: <b>/api/menus/{menu_id}/items/{item_id}</b><br/>
	 * Method: <b>DELETE</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 *
	 *
	 */
	@RequestMapping(value = "/{menu_id}/items/{item_id}", method = RequestMethod.DELETE, produces = {"application/json"})
	public JsonModel deleteDailyMenuItem(
			HttpServletResponse response,
			@PathVariable("menu_id") Long menuId,
			@PathVariable("item_id") Long itemId) throws IOException {

		Optional<Menu> menuOptional = menuService.getById(menuId);
		JsonModel result = new JsonModel();
		if(!menuOptional.isPresent()){
			result.fail();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}

		Optional<MenuItem> menuItemOptional = menuService.getMenuItemById(itemId);
		if(!menuItemOptional.isPresent()){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}

		menuService.deleteMenuItem(menuId, itemId);

		return result;
	}

	private Date today() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		if (calendar.get(Calendar.HOUR_OF_DAY) > 11) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}




}
