package org.bitbucket.shevchenkod.restaurant.service.repository;

import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "restaurants", path = "api/restaurants")
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	/**
	 * Find the user with the given login. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link Restaurant} class.
	 *
	 * @param name
	 * @return
	 */
	Optional<Restaurant> findByName(String name);
}
