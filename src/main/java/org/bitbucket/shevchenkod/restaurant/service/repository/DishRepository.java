package org.bitbucket.shevchenkod.restaurant.service.repository;

import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "dishes", path = "api/dishes")
public interface DishRepository extends JpaRepository<Dish, Long> {

	/**
	 * Find the user with the given login. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link Dish} class.
	 *
	 * @param name
	 * @return
	 */
	Optional<Dish> findByName(String name);
}
