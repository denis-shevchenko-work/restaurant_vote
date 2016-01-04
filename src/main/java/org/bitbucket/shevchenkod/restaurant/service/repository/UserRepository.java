package org.bitbucket.shevchenkod.restaurant.service.repository;

import org.bitbucket.shevchenkod.restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "users", path = "api/users")
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find the user with the given login. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link User} class.
	 *
	 * @param login
	 * @return
	 */
	User findByLogin(String login);

	/**
	 * Find the user with the given name. This method will be translated into a query using the
	 * {@link javax.persistence.NamedQuery} annotation at the {@link User} class.
	 *
	 * @param name
	 * @return
	 */
	Optional<User> findByName(String name);
}