package com.epam.esm.domain;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

public interface ApiErrors extends Serializable {

    String DEFAULT_ERROR_MESSAGE = "Validation failed.";

    HttpStatus getStatus();

    String getMessage();

    Map<String, String> getErrors();

}
