package org.bitbucket.shevchenkod.restaurant.test.services;

import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.model.User;
import org.bitbucket.shevchenkod.restaurant.model.security.UserRoles;
import org.bitbucket.shevchenkod.restaurant.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@EnableWebMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class UserCrudServiceTest extends GenericCrudServiceTest<Long, User, UserService> {

	@Override
	public User createEntity(String name){
		User entity = new User(name, name, UserRoles.USER.toString());
		return entity;
	}

	@Override
	public User updateEntity(User entity, String name){
		entity.setName(name);
		entity.setLogin(name);
		return  entity;
	}

	@Override
	public Class getType(){
		return User.class;
	}

	@Override
	protected Optional<User> getByName(String name) {
		return getService().findByName(name);
	}
}
