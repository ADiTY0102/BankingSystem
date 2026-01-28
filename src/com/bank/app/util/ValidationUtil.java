package com.bank.app.util;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + "This field must NOT BE BLANK");
        }
    }

    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + "This Field must be GREATER THAN 0");
        }
    }
}
