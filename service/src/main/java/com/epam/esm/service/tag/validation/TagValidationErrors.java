package com.epam.esm.service.tag.validation;

import com.epam.esm.service.error.ErrorCode;
import com.epam.esm.service.error.HttpError;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonSerialize(using = TagValidationErrorsSerializer.class)
public class TagValidationErrors implements HttpError {

    private final HttpStatus status;
    private final String message;
    private final Map<String, String> errors;

    public TagValidationErrors(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    @Override
    public String getCode() {
        return getStatus().value() + ErrorCode.TAG.getCode();
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
