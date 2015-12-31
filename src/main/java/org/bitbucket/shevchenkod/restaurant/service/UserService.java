package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.view.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
public interface UserService extends CrudService<Long, User> {

	Optional<User> findByLogin(String login);

	Optional<User> findByName(String name);
}
