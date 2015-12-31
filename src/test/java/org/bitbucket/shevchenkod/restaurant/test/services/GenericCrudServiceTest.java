package org.bitbucket.shevchenkod.restaurant.test.services;

import org.bitbucket.shevchenkod.restaurant.model.Identifiable;
import org.bitbucket.shevchenkod.restaurant.service.CrudService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public abstract class GenericCrudServiceTest<ID extends Serializable, T extends Identifiable<ID>, S extends CrudService<ID, T>> {

	public abstract T createEntity(String name);

	public abstract T updateEntity(T t, String name);

	public abstract Class getType();
	
	@Autowired
	S service;

	public S getService() {
		return service;
	}

	protected abstract Optional<T> getByName(String name);

	@Test
	public void save() throws Exception {
		String name = getType().getSimpleName() + " Crud Save";
		T t = createEntity(name);
		assertFalse(getByName(name).isPresent());
		service.create(t);
		assertTrue(t.getId() != null);
		Optional<T> tOptional = service.getById(t.getId());
		assertTrue(tOptional.isPresent());
		assertTrue(getByName(name).isPresent());
	}


	@Test(expected = DataIntegrityViolationException.class)
	public void saveNonUnique() throws Exception {
		String name = getType().getSimpleName() + " Crud Save Non Unique";

		T t1 = createEntity(name);
		service.create(t1);
		assertTrue(t1.getId() != null);
		Optional<T> t1Optional = service.getById(t1.getId());
		assertTrue(t1Optional.isPresent());

		T t2 = createEntity(name);
		service.create(t2);
	}

	@Test
	public void loadOneSaved() throws Exception {
		T t = createEntity(getType().getSimpleName() + " Crud Load One");
		service.create(t);
		Optional<T> tOptional = service.getById(t.getId());
		assertTrue(tOptional.isPresent());
		assertEquals(tOptional.get(), t);
	}


	@Test
	public void loadAllSaved() throws Exception {
		T t1 = createEntity(getType().getSimpleName() + " Crud Load All 1");
		service.create(t1);

		T t2 = createEntity(getType().getSimpleName() + " Crud Load All 2");
		service.create(t2);
		service.flush();
		List<T> tList = service.getAll(0, 50).getContent();
		assertNotNull(tList);
		assertFalse(tList.isEmpty());
		assertTrue(tList.size() > 1);
		assertTrue(tList.contains(t1));
		assertTrue(tList.contains(t2));
	}

	@Test
	public void updateSaved() throws Exception {
		T t = createEntity(getType().getSimpleName() + " Crud Update");
		service.create(t);
		Optional<T> tOptional = service.getById(t.getId());
		t = tOptional.get();
		String updated = getType().getSimpleName() + " Crud Updated";
		updateEntity(t, updated);
		Serializable id = t.getId();
		service.update(t);
		tOptional = service.getById(t.getId());
		assertTrue(tOptional.isPresent());
		assertEquals(id, tOptional.get().getId());
		assertEquals(t, tOptional.get());
	}

	@Test
	public void deleteSaved() throws Exception {
		T t = createEntity(getType().getSimpleName() + " Crud delete");
		service.create(t);
		Optional<T> tOptional = service.getById(t.getId());
		assertTrue(tOptional.isPresent());
		service.delete(t.getId());
		tOptional = service.getById(t.getId());
		assertFalse(tOptional.isPresent());

	}

}
