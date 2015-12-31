package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

@Transactional
public abstract class GenericCrudService<ID extends Serializable, T extends Identifiable<ID>, R extends JpaRepository<T,ID>> implements CrudService<ID,T> {


	@Autowired
	private ApplicationContext appContext;

	@Autowired
	R repository;

	public R getRepository(){
		if (repository == null) {
			ParameterizedType ptype = (ParameterizedType) getClass().getGenericSuperclass();
			repository = (R) appContext.getBean((Class) ptype.getActualTypeArguments()[3]);
		}
		return repository;
	}

	/**
	 * Retrieve page of entities
	 * @param page number of page
	 * @param pageSize records per page
	 * @return
	 */
	@Override
	public Page<T> getAll(int page, int pageSize) {
		return repository.findAll(new PageRequest(page, pageSize));
	}

	/**
	 * Retrieve single entity
	 * @param id of entity to retrieve
	 * @return
	 */
	@Override
	public Optional<T> getById(ID id) {
		return Optional.ofNullable(id).isPresent() ?
				Optional.ofNullable((T) getRepository().findOne(id)):
				Optional.empty();
	}

	/**
	 * Persist entity
	 * @param t
	 * @return
	 */
	@Override
	public T create(T t) {
		return  getRepository().save(t);
	}

	/**
	 * Save changes
	 * @param t
	 */
	@Override
	public void update(T t) {
		getRepository().saveAndFlush(t);
	}

	/**
	 * Delete specified entity
	 * @param id
	 * @return
	 */
	@Override
	public boolean delete(ID id) {
		boolean exists = getRepository().exists(id);

		if(exists) {
			getRepository().delete(id);
		}
		return exists;
	}

	@Override
	public void flush(){
		getRepository().flush();
	}


}
