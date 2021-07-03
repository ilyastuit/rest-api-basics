package com.epam.esm.service;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorUtilTest {

    @Test
    void testValidBoolean() {
        Optional<String> optionalS = Optional.of("true");

        assertTrue(ValidatorUtil.isValidBoolean(optionalS));
    }

    @Test
    void testNotValidBoolean() {
        Optional<String> optionalS = Optional.of("not valid boolean");

        assertFalse(ValidatorUtil.isValidBoolean(optionalS));
    }

    @Test
    void testNullBoolean() {
        Optional<String> optionalS = Optional.ofNullable(null);

        assertFalse(ValidatorUtil.isValidBoolean(optionalS));
    }

    @Test
    void testValidSortASC() {
        Optional<String> optionalS = Optional.of("ASC");

        assertTrue(ValidatorUtil.isValidSort(optionalS));
    }

    @Test
    void testValidSortDESC() {
        Optional<String> optionalS = Optional.of("DESC");

        assertTrue(ValidatorUtil.isValidSort(optionalS));
    }

    @Test
    void testNotValidSort() {
        Optional<String> optionalS = Optional.of("not valid sort");

        assertFalse(ValidatorUtil.isValidSort(optionalS));
    }

    @Test
    void testNullSort() {
        Optional<String> optionalS = Optional.ofNullable(null);

        assertFalse(ValidatorUtil.isValidSort(optionalS));
    }
}
