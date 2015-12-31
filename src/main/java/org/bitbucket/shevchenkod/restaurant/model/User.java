package org.bitbucket.shevchenkod.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by d_shevchenko on 11.12.2015.
 */
@Entity
@Table(name = "account")
@NamedQueries({
		@NamedQuery(name = "User.findByLogin", query = "SELECT user FROM User user WHERE user.login = ?1"),
		@NamedQuery(name = "User.findByName", query = "SELECT user FROM User user WHERE user.name = ?1")})
@Getter
@Setter
@ToString(exclude = {"password"})
public class User extends BasePersistable
		implements Serializable//, UserDetails, Principal
{

	private static final long serialVersionUID = -2949089332172042908L;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "login", nullable = false, unique = true)
	private String login;

	@Column(name = "password", nullable = false)
	@JsonIgnore
	private String password;

	@Column(name = "roles", nullable = false)
	private String roles;

	public User() {
	}

	public User(String name, String login, String roles) {
		this.name = name;
		this.login = login;
		this.roles = roles;
	}
}
