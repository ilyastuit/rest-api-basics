package com.epam.esm.service.giftcertificate.validation;

import com.epam.esm.service.error.ErrorCode;
import com.epam.esm.service.error.HttpError;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonSerialize(using = GiftCertificateValidationErrorsSerializer.class)
public class GiftCertificateValidationErrors implements HttpError {

    private final HttpStatus status;
    private final String message;
    private final Map<String, String> errors;

    public GiftCertificateValidationErrors(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    @Override
    public String getCode() {
        return getStatus().value() + ErrorCode.GIFT_CERTIFICATE.getCode();
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
