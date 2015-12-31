package org.bitbucket.shevchenkod.restaurant.model;

import javax.persistence.*;
import java.io.Serializable;

import lombok.*;

/**
 * Created by d_shevchenko on 11.12.2015.
 */
@Entity
@Table(name = "dish")
@NamedQueries({
		@NamedQuery(name = "Disn.findByName", query = "SELECT d FROM Dish d WHERE d.name = ?1")})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Dish extends BasePersistable implements Serializable {

	private static final long serialVersionUID = -4008165918066979273L;

	@Column(nullable = false, updatable = true, unique = true)
	private String name;


	public Dish(Long id, String name) {
		super(id);
		this.name = name;
	}
}
