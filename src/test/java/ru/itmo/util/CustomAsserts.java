package ru.itmo.util;

import java.math.BigDecimal;

public final class CustomAsserts {
    private CustomAsserts() {}

    public static void assertClose(BigDecimal expected, BigDecimal actual, BigDecimal tol) {
        BigDecimal diff = expected.subtract(actual).abs();
        if (diff.compareTo(tol) > 0) {
            throw new AssertionError("Expected " + expected + ", actual " + actual + ", diff " + diff);
        }
    }
}