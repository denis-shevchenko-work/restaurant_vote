package org.bitbucket.shevchenkod.restaurant.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.bitbucket.shevchenkod.restaurant.controller.GenericCrudController;
import org.bitbucket.shevchenkod.restaurant.model.Identifiable;
import org.bitbucket.shevchenkod.restaurant.model.security.UserRoles;
import org.bitbucket.shevchenkod.restaurant.service.CrudService;
import org.bitbucket.shevchenkod.restaurant.util.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


public abstract class GenericCrudControllerCrudTest<ID extends Serializable, T extends Identifiable<ID>, DTO extends Serializable, S extends CrudService<ID, T>, C extends GenericCrudController> extends AbstractControllerTestCase<C> {

	@Autowired
	private UserDetailsService userDetailsService;

	private Authentication adminAuthentication = new TestingAuthenticationToken("admin", "123", Arrays.asList(new GrantedAuthority[]{new SimpleGrantedAuthority(UserRoles.ADMIN.getName())}));

	Authentication getAuthentication() {
		if (adminAuthentication == null) {
			UserDetails user = userDetailsService.loadUserByUsername("user");
			adminAuthentication = new TestingAuthenticationToken(user, user.getPassword(), (List<GrantedAuthority>) user.getAuthorities());
		}
		return adminAuthentication;
	}
	
	@Autowired
	S dishController;

	@Autowired
	S dishService;

	protected S getService(){
		return (S) getController().getService();
	}

	protected abstract T createEntity(String name);

	protected abstract void updateEntity(T entity, String name);

	protected abstract C getController();

	protected abstract String getEndPoint();

	protected abstract Optional<T> findByName(String name);

	@Test
	public void testCreateAndReadOne(){
		String name = "crud controller read one";
		assertFalse(findByName(name).isPresent());
		T entity = createEntity(name);
		getService().create(entity);

		assertNotNull(entity.getId());
		assertTrue(findByName(name).isPresent());

		JsonNode result = performAuthenticatedRequestAndReturnJsonNode(getAuthentication(), get(getEndPoint() + "/" + entity.getId()));
		assertEquals(entity.getId().toString(), result.findPath("id").asText());
		assertEquals(name, result.findPath("name").asText());
	}

	@Test
	public void testreadAll(){
		int before = getService().getAll(0, 10).getNumberOfElements();
		assertTrue(before < 10);
		String name = "crud controller read all 1";
		assertFalse(findByName(name).isPresent());
		T entity = createEntity(name);
		getService().create(entity);

		assertNotNull(entity.getId());
		assertTrue(findByName(name).isPresent());

		JsonNode result = performAuthenticatedRequestAndReturnJsonNode(getAuthentication(),
				get(getEndPoint())
						.param("page", "0")
						.param("page_size", "10"));

		result = result.findPath("data");
		assertTrue(result.isArray());
		assertEquals(before + 1, result.size());
		assertTrue(result.findValuesAsText("id").contains(entity.getId().toString()));
		assertTrue(result.findValuesAsText("name").contains(name));
	}



	@Test
	public void testcreate() throws IOException {
		String name = "crud controller create";
		assertFalse(findByName(name).isPresent());

		MvcResult result =
				performAuthenticatedRequestAndReturn(getAuthentication(),
						post(getEndPoint())
								.contentType(MediaType.APPLICATION_JSON)
								.content(JsonUtils.JSON_MAPPER.writeValueAsString(createEntity(name))))//createJson(name)
				;
		assertTrue(findByName(name).isPresent());
	}

	@Test
	public void testupdate() throws IOException {
		String name = "crud controller update";
		assertFalse(findByName(name).isPresent());
		T entity = createEntity(name);
		getService().create(entity);

		assertNotNull(entity.getId());
		assertTrue(getService().getById(entity.getId()).isPresent());

		name = name + "d";
		updateEntity(entity, name);
		MvcResult result =
				performAuthenticatedRequestAndReturn(getAuthentication(), put(getEndPoint() + "/" + entity.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonUtils.JSON_MAPPER.writeValueAsString(createEntity(name))));
		assertTrue(findByName(name).isPresent());
	}

	@Test
	public void testdelete(){
		String name = "crud controller delete";
		assertFalse(findByName(name).isPresent());

		T entity = createEntity(name);
		getService().create(entity);

		assertNotNull(entity.getId());
		assertTrue(getService().getById(entity.getId()).isPresent());

		getService().create(entity);
		MvcResult result = performAuthenticatedRequestAndReturn(getAuthentication(), delete(getEndPoint() + "/" + entity.getId()));

		assertFalse(getService().getById(entity.getId()).isPresent());
	}

}
