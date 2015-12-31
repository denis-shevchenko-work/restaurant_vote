package org.bitbucket.shevchenkod.restaurant.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by d_shevchenko on 11.12.2015.
 */
@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id","dish_id"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem extends BasePersistable implements Serializable, Identifiable<Long> {

	private static final long serialVersionUID = -5441966417890393996L;

	@ManyToOne
	@JoinColumn(name="menu_id")
	private Menu menu;

	@ManyToOne
	@JoinColumn(name = "dish_id")
	private Dish dish;

	@Column(name = "price")
	private BigDecimal price = BigDecimal.ZERO;

	public String toString() {
		return "org.bitbucket.shevchenkod.restaurant.model.MenuItem(menu=" + this.menu.getId() + ", dish=" + this.dish + ", price=" + this.price + ")";
	}
}
