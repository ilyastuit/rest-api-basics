package com.epam.esm.service.error;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

/**
 * Interface for http errors bean.
 */
public interface HttpError extends Serializable {

    String DEFAULT_ERROR_MESSAGE = "Validation failed.";

    String getCode();

    HttpStatus getStatus();

    String getMessage();

    Map<String, String> getErrors();

}
