package com.epam.esm.service;

import java.util.Optional;

public class CheckerUtil {
    public final static String ASC = "ASC";
    public final static String DESC = "DESC";

    public static boolean isValidSort(Optional<String> value) {
        return value.isPresent() && (CheckerUtil.ASC.equalsIgnoreCase(value.get()) || CheckerUtil.DESC.equalsIgnoreCase(value.get()));
    }

    public static boolean isValidBoolean(Optional<String> value) {
        return value.isPresent() && Boolean.parseBoolean(value.get());
    }
}
