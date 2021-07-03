package com.epam.esm.service.giftcertificate.validation;

import com.epam.esm.service.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GiftCertificateValidationErrorsTest {

    @Test
    void testAssertionEmptyMap() {
        String message = "message";
        HttpStatus httpStatus = HttpStatus.OK;
        Map<String, String> errors = new HashMap<>();

        GiftCertificateValidationErrors httpError = new GiftCertificateValidationErrors(httpStatus, message, errors);

        assertTrue(httpError.getErrors().isEmpty());
        assertEquals(httpError.getMessage(), message);
        assertEquals(httpError.getStatus(), httpStatus);
        assertEquals(httpError.getCode(), httpStatus.value() + ErrorCode.GIFT_CERTIFICATE.getCode());
    }

    @Test
    void testAssertionWithMap() {
        String message = "message";
        HttpStatus httpStatus = HttpStatus.OK;
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "error");

        GiftCertificateValidationErrors httpError = new GiftCertificateValidationErrors(httpStatus, message, errors);

        assertFalse(httpError.getErrors().isEmpty());
        assertEquals(httpError.getErrors().get("error"), errors.get("error"));
        assertEquals(httpError.getMessage(), message);
        assertEquals(httpError.getStatus(), httpStatus);
        assertEquals(httpError.getCode(), httpStatus.value() + ErrorCode.GIFT_CERTIFICATE.getCode());
    }

}
