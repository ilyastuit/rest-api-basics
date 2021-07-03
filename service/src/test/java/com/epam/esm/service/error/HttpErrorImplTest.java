package com.epam.esm.service.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpErrorImplTest {

    @Test
    void successAssertion() {
        String message = "message";
        HttpStatus httpStatus = HttpStatus.OK;
        ErrorCode errorCode = ErrorCode.TAG;

        HttpError httpError = new HttpErrorImpl(message, httpStatus, errorCode);

        assertNull(httpError.getErrors());
        assertEquals(httpError.getMessage(), message);
        assertEquals(httpError.getStatus(), httpStatus);
        assertEquals(httpError.getCode(), httpStatus.value() + errorCode.getCode());
    }
}
