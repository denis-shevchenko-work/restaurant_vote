package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.service.repository.UserRepository;
import org.bitbucket.shevchenkod.restaurant.view.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl extends GenericCrudService<Long, User, UserRepository> implements UserService {

	@Override
	public Optional<User> findByLogin(String login) {
		return Optional.ofNullable(getRepository().findByLogin(login));
	}

	@Override
	public Optional<User> findByName(String name) {
		return getRepository().findByName(name);
	}

}
