package org.bitbucket.shevchenkod.restaurant.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by d_shevchenko on 11.12.2015.
 */
@Entity
@Table(name = "menu")
@NamedQueries({
		@NamedQuery(name = "Menu.findAllBetweenDates", query = "SELECT menu FROM Menu menu WHERE menu.date > ?1 AND menu.date < ?2"),
		@NamedQuery(name = "Menu.findByRestaurantNameBetweenDates", query = "SELECT menu FROM Menu menu JOIN menu.restaurant restaurant WHERE menu.date > ?2 AND menu.date < ?3 AND restaurant.name = ?1"),
		@NamedQuery(name = "Menu.findByRestaurantBetweenDates", query = "SELECT menu FROM Menu menu WHERE menu.date > ?2 AND menu.date < ?3 AND menu.restaurant = ?1")
		})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BasePersistable implements Serializable {

	private static final long serialVersionUID = 2432335271353867415L;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@OneToMany(mappedBy = "menu", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	private List<MenuItem> items = new ArrayList<>();

	@Column(name = "date")
	private Date date = new Date();

	public Menu(Restaurant restaurant, Date date) {
		this.restaurant = restaurant;
		this.date = date;
	}

	public void clearItems(){
		items.clear();
		//items.stream().forEach(item -> item.setMenu(null));
	}

	public Menu(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public String toString() {
		return "org.bitbucket.shevchenkod.restaurant.model.Menu(restaurant=" + this.restaurant + ", items=" + this.items + ", date=" + this.date + ")";
	}

}
