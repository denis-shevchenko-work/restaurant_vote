package org.bitbucket.shevchenkod.restaurant.model.security;

/**
 * Created by d_shevchenko on 16.12.2015.
 */
public enum UserRoles {
	ADMIN,
	USER,
	ANONYMOUS;

	UserRoles() {
	}

	public String getName(){
		return this.name();
	}
}
