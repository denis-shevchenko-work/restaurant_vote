package org.bitbucket.shevchenkod.restaurant.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Allows to add several vallidators which will be called basing on class
 */

public final class CompoundValidator implements Validator {

	private final List<Validator> validators;

	public CompoundValidator(final Validator... validators) {
		super();
		this.validators = Stream.of(validators).filter(v -> Optional.ofNullable(v).isPresent()).collect(Collectors.toList());
	}

	/**
	 * Will return true if this class is in the specified map.
	 */
	public boolean supports(final Class clazz) {
		return validators.stream().filter(v -> v.supports(clazz)).findFirst().isPresent();
	}

	/**
	 * Validate the specified object using the validator registered for the object's class.
	 */
	public void validate(final Object obj, final Errors errors) {
		validators.stream().filter(v -> v.supports(obj.getClass())).forEach(v -> validate(obj, errors));
	}

}
