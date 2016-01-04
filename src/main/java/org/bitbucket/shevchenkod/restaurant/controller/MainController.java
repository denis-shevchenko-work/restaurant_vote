package org.bitbucket.shevchenkod.restaurant.controller;

import org.bitbucket.shevchenkod.restaurant.model.security.UserRoles;
import org.bitbucket.shevchenkod.restaurant.service.UserService;
import org.bitbucket.shevchenkod.restaurant.view.dto.JsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Controller
@RequestMapping("/")
public class MainController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseBody
	public JsonModel homePage(HttpServletResponse response){
		Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
		String userString  = authentication.isPresent() ? authentication.get().toString() : "anonymous";

		JsonModel result = new JsonModel();
		result.put("api_url", "/api");
		result.put("user", userString);
		return result;
	}

	@RequestMapping(value = "api", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseBody
	public JsonModel apiPage(HttpServletResponse response){
		Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
		String userString  = authentication.isPresent() ? authentication.get().toString() : "anonymous";

		JsonModel result = new JsonModel();
		result.put("doc_url", "/api/doc");
		result.put("user", userString);
		return result;
	}

	@RequestMapping(value = {"/api/doc"}, method = RequestMethod.GET, produces = "text/html")
	public void doc(HttpServletResponse response) throws IOException {
		Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
		Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
		if (authorities.contains(new SimpleGrantedAuthority(UserRoles.ADMIN.getName()))){
			response.sendRedirect("/admin-api.html");//api/doc/admin/
		} else if (authorities.contains(new SimpleGrantedAuthority(UserRoles.USER.getName()))) {
			response.sendRedirect("/user-api.html");//api/doc/user/
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN ,"Please authorize");
		}

	}


}
