package org.bitbucket.shevchenkod.restaurant.model.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by d_shevchenko on 21.12.2015.
 */
public class UserPrincipal extends User {

	org.bitbucket.shevchenkod.restaurant.model.User user;

	public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities, org.bitbucket.shevchenkod.restaurant.model.User user) {
		super(username, password, authorities);
		this.user = user;
	}

	public UserPrincipal(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, org.bitbucket.shevchenkod.restaurant.model.User user) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.user = user;
	}

	public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), authorities);
	}

	public UserPrincipal(User user, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public org.bitbucket.shevchenkod.restaurant.model.User getUser() {
		return user;
	}
}
