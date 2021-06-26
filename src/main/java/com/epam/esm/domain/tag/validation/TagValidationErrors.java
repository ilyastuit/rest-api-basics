package com.epam.esm.domain.tag.validation;

import com.epam.esm.domain.ValidationErrors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonSerialize(using = TagApiErrorsSerializer.class)
public class TagValidationErrors implements ValidationErrors {

    private final HttpStatus status;
    private final String message;
    private final Map<String, String> errors;

    public TagValidationErrors(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Map<String, String> getErrors() {
        return errors;
    }
}
