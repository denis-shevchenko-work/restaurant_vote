package org.bitbucket.shevchenkod.restaurant.service.repository;

import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	/**
	 * Find all menus of restaurant between specified dates. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link Restaurant} class.
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	List<Menu> findAllBetweenDates(Date from, Date to);

	/**
	 * Find menu of restaurant between specified dates. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link Restaurant} class.
	 *
	 * @param restaurantName
	 * @param from
	 * @param to
	 * @return
	 */
	Optional<Menu> findByRestaurantNameBetweenDates(String restaurantName, Date from, Date to);

	/**
	 * Find menu of restaurant between specified dates. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link Restaurant} class.
	 *
	 * @param restaurant
	 * @param from
	 * @param to
	 * @return
	 */
	Optional<Menu> findByRestaurantBetweenDates(Restaurant restaurant, Date from, Date to);

}
