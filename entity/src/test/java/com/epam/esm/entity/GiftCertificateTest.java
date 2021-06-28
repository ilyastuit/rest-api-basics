package com.epam.esm.entity;

import com.epam.esm.builder.GiftCertificateBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GiftCertificateTest {

    GiftCertificateBuilder builder = new GiftCertificateBuilder();

    @Test
    void testNullFieldsWithDefaultConstructor() {
        GiftCertificate giftCertificate = new GiftCertificate();

        assertNull(giftCertificate.getId());
        assertNull(giftCertificate.getName());
        assertNull(giftCertificate.getDescription());
        assertNull(giftCertificate.getPrice());
        assertNull(giftCertificate.getDuration());
        assertNull(giftCertificate.getTags());
    }
}
