package org.bitbucket.shevchenkod.restaurant.view.dto;

import org.bitbucket.shevchenkod.restaurant.util.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class JsonModel extends HashMap<String, Object> {

    public JsonModel() {
        this(false);
    }

    public JsonModel(boolean clean) {
        if (!clean) {
			put("success", true);
		}
    }

	public JsonModel setSuccess(boolean success) {
        put("success", success);
        return this;
    }

    public JsonModel pass() {
        return setSuccess(true);
    }

    public JsonModel fail() {
        return setSuccess(false);
    }

    public void addError(String field, String message) {
        Map<String, String> errors = getErrors();
        errors.put(field, message);
        setSuccess(false);
    }

	public void putData(Object data) {
		put("data", data);
	}

    public void processBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError fieldError : result.getFieldErrors()) {
                addError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        } else {
            put("success", true);
        }
    }

    public boolean isSuccess() {
        return (Boolean) get("success");
    }

    public Map<String, String> getErrors() {
        Map<String, String> errors = (Map<String, String>) get("errors");
        if (errors == null) {
            errors = new HashMap<String, String>();
            put("errors", errors);
        }
        return errors;
    }

    @JsonIgnore
    public String toJson() {
        try {
            return JsonUtils.JSON_MAPPER.writeValueAsString(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void failure(Exception e) {
        setSuccess(false);
    }
}