package org.bitbucket.shevchenkod.restaurant.service.transformer;

import org.bitbucket.shevchenkod.restaurant.model.Identifiable;

import java.io.Serializable;

/**
 * Created by d_shevchenko on 30.12.2015.
 */
public interface TransformerService<DTO extends Serializable, T extends Identifiable> {

	 T transform(DTO dto);

	 DTO transform(T t);
}
