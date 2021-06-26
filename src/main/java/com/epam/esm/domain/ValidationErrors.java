package com.epam.esm.domain;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

/**
 * Interface for validation errors bean.
 */
public interface ValidationErrors extends Serializable {

    String DEFAULT_ERROR_MESSAGE = "Validation failed.";

    HttpStatus getStatus();

    String getMessage();

    Map<String, String> getErrors();

}
