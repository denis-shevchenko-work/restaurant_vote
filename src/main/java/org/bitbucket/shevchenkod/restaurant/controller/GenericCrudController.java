package org.bitbucket.shevchenkod.restaurant.controller;

import org.bitbucket.shevchenkod.restaurant.model.Identifiable;
import org.bitbucket.shevchenkod.restaurant.service.CrudService;
import org.bitbucket.shevchenkod.restaurant.service.transformer.TransformerService;
import org.bitbucket.shevchenkod.restaurant.view.dto.JsonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class GenericCrudController<I extends Serializable, T extends Identifiable<I>, S extends CrudService<I, T>, DTO extends Serializable> {

	private Logger logger = LoggerFactory.getLogger(GenericCrudController.class);

	S service;

	@Autowired
	private ApplicationContext appContext;

	public S getService() {
		if (service == null) {
			ParameterizedType ptype = (ParameterizedType) getClass().getGenericSuperclass();
			//idType = appContext.getBean((Class) ptype.getActualTypeArguments()[0]).getClass();
			service = (S) appContext.getBean((Class) ptype.getActualTypeArguments()[2]);
		}

		return service;
	}


	protected abstract TransformerService<DTO, T> getTransformerService();

	private T transform(DTO dto) {
		return getTransformerService().transform(dto);
	}

	private DTO transform(T t) {
		return getTransformerService().transform(t);
	}


	/**
	 * Get list of entities.
	 * URL: <b>/api/<entity>?page={page}&page_size={pageSize}</b><br/>
	 * Method: <b>GET</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 * <code>
	 *[{<entity json>},...]}
	 * </code>
	 *
	 * @param page  page from 0
	 * @param page_size  number of records per page
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
	public JsonModel read(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
						@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize) {
		JsonModel result = new JsonModel();
		result.putData(getService().getAll(page, pageSize).getContent().stream().map(entity -> transform(entity)).collect(Collectors.toList()));
		return result;
	}

	/**
	 * Get one entity.
	 * URL: <b>/api/<entity>/{id}</b><br/>
	 * Method: <b>GET</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * <li>404 Not Found - resource with id doesn't exists</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 * <code>
	 *{<entity json>}
	 * </code>
	 *
	 * @param id  entity identifier
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"application/json"})
	public JsonModel read(HttpServletResponse response, @PathVariable(value = "id") I id) throws IOException {
		JsonModel result = new JsonModel();
		Optional<T> entity = getService().getById(id);
		if (!entity.isPresent()) {
			result.fail();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}
		result.putData(transform(entity.get()));
		return result;
	}


	/**
	 * create new entity.
	 * URL: <b>/api/<entity></b><br/>
	 * Method: <b>POST</b><br/>
	 * Response format: <b>application/json</b><br/>
	 * Request format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * <li>404 Not Found - resource with id doesn't exists</li>
	 * </ul>
	 * <p/>
	 * Request sample:<br/>
	 * <code>
	 *{<entity json>}
	 * </code>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 *
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	public JsonModel create(@RequestBody DTO dto) {
		JsonModel result = new JsonModel();
		result.putData(transform(getService().create(transform(dto))));
		return result;
	}

	/**
	 * Update entity identified by id.
	 * URL: <b>/api/<entity>/{id}</b><br/>
	 * Method: <b>PUT</b><br/>
	 * Response format: <b>empty</b><br/>
	 * Request format: <b>application/json</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * <li>404 Not Found - resource with id doesn't exists</li>
	 * </ul>
	 * <p/>
	 * Request sample:<br/>
	 * <code>
	 *{<entity json>}
	 * </code>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 *
	 * @param id  entity identifier
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public JsonModel update(HttpServletResponse response, @PathVariable(value = "id") I id,
					   @RequestBody DTO dto) throws IOException {
		JsonModel result = new JsonModel();
		if (!getService().getById(id).isPresent()) {
			result.fail();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}
		T t = transform(dto);
		t.setId(id);
		getService().update(t);
		return result;
	}

	/**
	 * Delete entity identified by id.
	 * URL: <b>/api/<entity>/{id}</b><br/>
	 * Method: <b>DELETE</b><br/>
	 * Response format: <b>empty</b><br/>
	 * <p/>
	 * Error codes:
	 * <ul>
	 * <li>401 Unauthorized - authentication error</li>
	 * </ul>
	 * <p/>
	 * Response sample:<br/>
	 * 200 OK<br/>
	 *
	 * @param id  entity identifier
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/json"})
	public JsonModel delete(HttpServletResponse response, @PathVariable(value = "id") I id) throws IOException {
		JsonModel result = new JsonModel();
		if (!getService().getById(id).isPresent()) {
			result.fail();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return result;
		}

		getService().delete(id);
		return result;
	}

}
