package com.epam.esm.domain.giftcertificate.validation;

import com.epam.esm.domain.ValidationErrors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonSerialize(using = GiftCertificateApiErrorsSerializer.class)
public class GiftCertificateValidationErrors implements ValidationErrors {

    private final HttpStatus status;
    private final String message;
    private final Map<String, String> errors;

    public GiftCertificateValidationErrors(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
