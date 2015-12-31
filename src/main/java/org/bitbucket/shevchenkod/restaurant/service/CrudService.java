package org.bitbucket.shevchenkod.restaurant.service;

import org.bitbucket.shevchenkod.restaurant.model.Identifiable;
import org.bitbucket.shevchenkod.restaurant.service.transformer.TransformerService;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

@Transactional
public interface CrudService<ID extends Serializable, T extends Identifiable<ID>>  {

	Page<T> getAll(int page, int pageSize);

	Optional<T> getById(ID id);

	T create(T item);

	void update(T item);

	boolean delete(ID id);

	void flush();

}
