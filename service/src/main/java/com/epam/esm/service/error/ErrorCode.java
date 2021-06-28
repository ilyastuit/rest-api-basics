package com.epam.esm.service.error;

public enum ErrorCode {
    Tag("01"), GiftCertificate("02");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
