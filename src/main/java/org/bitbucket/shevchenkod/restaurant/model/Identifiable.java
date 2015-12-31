package org.bitbucket.shevchenkod.restaurant.model;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;

/**
 * Created by d_shevchenko on 12.12.2015.
 */
public interface Identifiable<ID extends Serializable> extends Persistable<ID>{
	void setId(ID id);
}
