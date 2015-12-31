package org.bitbucket.shevchenkod.restaurant.controller;

import org.bitbucket.shevchenkod.restaurant.view.dto.JsonModel;
import org.bitbucket.shevchenkod.restaurant.view.dto.UserDto;
import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.model.security.UserRoles;
import org.bitbucket.shevchenkod.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	UserService userService;

	/**
	 * test
	 * URL: <b>/api/admin</b><br/>
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
	 * </code>
	 *
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseBody
	public String root() {
		return "admin";
	}

	/**
	 * Get list of users.
	 * URL: <b>/api/admin/users</b><br/>
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
	 *[{"name":"User user", "login":"user", "roles":["USER"]},...]}
	 * </code>
	 *
	 * @param page  page from 0
	 * @param page_size  number of records per page
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseBody
	public JsonModel users(@RequestParam(value = "page", required = true, defaultValue = "0") int page,
										   @RequestParam(value = "page_size", required = true, defaultValue = "10") int pageSize) {
		JsonModel result = new JsonModel();
		result.putData( userService.getAll(page, pageSize).getContent()
				.stream().map(user -> new UserDto(user.getName(), user.getLogin(), user.getRoles())).collect(Collectors.toList()));
		return result;
	}

	/**
	 * Create new user.
	 * URL: <b>/api/admin/users</b><br/>
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
	 * @param login  user login
	 * @param password  user password
	 * @param roles  roles
	 * @param name  name
	 */
	//TODO encode password
	@RequestMapping(value = "/users", method = RequestMethod.POST, produces = {"application/json"})
	@ResponseBody
	public JsonModel add(HttpServletResponse response,
			@RequestParam(value = "login", required = true) String login,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "roles", required = true) List<UserRoles> roles,
			@RequestParam(value = "name", required = true) String name
	) throws IOException {
		JsonModel result = new JsonModel();

		if (roles.size() == 0) {
			result.addError("roles", "empty_roles");
			return result;
		}

		User user = new User(name, login, roles.stream().map(UserRoles::name).reduce("", (a, b) -> a + ";" + b));
		String passwordEncoded = password;
		//passwordEncoded = encoder.encodePassword(password, salt);
		user.setPassword(passwordEncoded);

		userService.create(user);
		return result;
	}

}
