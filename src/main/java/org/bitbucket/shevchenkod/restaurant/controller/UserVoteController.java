package org.bitbucket.shevchenkod.restaurant.controller;

import org.bitbucket.shevchenkod.restaurant.util.Pair;
import org.bitbucket.shevchenkod.restaurant.view.dto.JsonModel;
import org.bitbucket.shevchenkod.restaurant.view.serializer.MenuItemSerializer;
import org.bitbucket.shevchenkod.restaurant.view.serializer.MenuSerializer;
import org.bitbucket.shevchenkod.restaurant.view.serializer.UserVoteSerializer;
import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.model.UserVote;
import org.bitbucket.shevchenkod.restaurant.service.MenuService;
import org.bitbucket.shevchenkod.restaurant.service.UserService;
import org.bitbucket.shevchenkod.restaurant.service.UserVoteService;
import org.bitbucket.shevchenkod.restaurant.service.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/votes")
public class UserVoteController {
	private static final MenuItemSerializer MENU_ITEM_SERIALIZER = new MenuItemSerializer();
	private static final MenuSerializer MENU_SERIALIZER = new MenuSerializer(MENU_ITEM_SERIALIZER);
	private static final UserVoteSerializer USER_VOTE_SERIALIZER = new UserVoteSerializer(MENU_SERIALIZER);


	@Autowired
	UserVoteService userVoteService;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository simpleUserRepository;

	@Autowired
	MenuService menuService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "date", new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
	}

	/**
	 * Get today vote.
	 * URL: <b>/api/votes</b><br/>
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
	 * {"date":"12/12/2015", "vote":{"restaurant_name":"Rest 1", "menu":[{"dish_name":"Dish 1", "price": 16.5}...]}}
	 * </code>
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
	public JsonModel getUserVoteForToday(HttpServletResponse response) throws IOException {
		JsonModel result = new JsonModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByLogin(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername()).get();

		Optional<UserVote> userVoteOptional = userVoteService.findForUserForToday(user);

		if (!userVoteOptional.isPresent()) {
			//result.fail();
			response.sendError(HttpServletResponse.SC_NO_CONTENT);
			return result;
		} else {
			UserVote userVote = userVoteOptional.get();
			result.putData(USER_VOTE_SERIALIZER.serialize(userVote));
			return result;
		}
	}

	/**
	 * Get today vote result.
	 * URL: <b>/api/votes</b><br/>
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
	 * {"date":"12/12/2015", "vote":{"restaurant_name":"Rest 1", "menu":[{"dish_name":"Dish 1", "price": 16.5}...]}}
	 * </code>
	 *
	 */
	@RequestMapping(value = "results", method = RequestMethod.GET, produces = {"application/json"})
	public JsonModel getUserVoteResultForToday(HttpServletResponse response) throws IOException {
		JsonModel result = new JsonModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByLogin(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername()).get();

		List<Map<String,Object>> userVoteResults = userVoteService.collectResultsForToday().stream().map(item -> {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("restaurant_name", ((Menu) item[0]).getRestaurant().getName());
			map.put("votes", ((Long) item[1]));
			return map;
		}).collect(Collectors.toList());
		result.putData(userVoteResults);
		return result;
	}

	/**
	 * Vote for a restaurant's menu for today
	 * URL: <b>/api/votes</b><br/>
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
	 * <code>
	 * {"success": true}
	 * {"success": true, "errors":[{"restaurant_name":"menu_for_restaurant_is_absent"},...]}
	 * </code>
	 *
	 * @param restaurant_name  restaurant name
	 */
	@RequestMapping( method = RequestMethod.POST, produces = {"application/json"})
	public JsonModel voteByName(
			@RequestParam(value = "restaurant_name", required = true) String restaurantName,
			HttpServletResponse response
			) throws IOException {
		JsonModel result = new JsonModel();

		Date today = today();

		Optional<Menu> menuOptional = menuService.findByRestaurantNameAndDate(restaurantName, today);

		if (!menuOptional.isPresent()){
			result.addError("restaurant_name", "menu_for_restaurant_is_absent");
			return result;
		}

		Menu menu = menuOptional.get();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByLogin(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername()).get();

		userVoteService.createOrUpdateVote(user, menu, today);

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
