package org.bitbucket.shevchenkod.restaurant.service.security;

import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.model.security.UserRoles;
import org.bitbucket.shevchenkod.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> userOptional = userService.findByLogin(userName);
		if(!userOptional.isPresent()) {
			throw new UsernameNotFoundException(MessageFormat.format("User [{0}] is not found", userName));
		}
		User user = userOptional.get();
		Set<GrantedAuthority> roles = new HashSet();
		for(String role : user.getRoles().split(";")){
			roles.add(new SimpleGrantedAuthority(UserRoles.valueOf(role).getName()));
		}

		UserDetails userDetails =
				new org.springframework.security.core.userdetails.User(user.getLogin(),
						user.getPassword(),
						roles);

		return userDetails;
	}
}