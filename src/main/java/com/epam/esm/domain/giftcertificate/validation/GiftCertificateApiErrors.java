package com.epam.esm.domain.giftcertificate.validation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

@JsonSerialize(using = GiftCertificateSerializer.class)
public class GiftCertificateApiErrors implements Serializable {

    public final static String DEFAULT_ERROR_MESSAGE = "Validation failed.";

    private final HttpStatus status;
    private final String message;
    private final Map<String, String> errors;

    public GiftCertificateApiErrors(HttpStatus status, String message, Map<String, String> errors) {
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
