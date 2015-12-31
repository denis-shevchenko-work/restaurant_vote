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
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"}), @UniqueConstraint(columnNames = {"name"})})
@NamedQueries({
		@NamedQuery(name = "Restauant.findByName", query = "SELECT r FROM Restaurant r WHERE r.name = ?1")})
@Getter
@Setter
@ToString
public class Restaurant extends BasePersistable implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -4060236186729194273L;

	@Column(nullable = false, unique = true)
	private String name;

	public Restaurant() {
	}

	public Restaurant(String restaurantName) {
		this.name = restaurantName;
	}

	public Restaurant(Long id, String restaurantName) {
		super(id);
		this.name = restaurantName;
	}

}
