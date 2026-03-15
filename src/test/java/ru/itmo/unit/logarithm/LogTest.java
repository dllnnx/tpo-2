package ru.itmo.unit.logarithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.function.MathFunction;
import ru.itmo.logarithm.Log;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.itmo.util.CustomAsserts.assertClose;

public class LogTest implements BaseLogTest {

    private final MathFunction log2 = new Log(ln, new BigDecimal("2"));
    private final MathFunction log3 = new Log(ln, new BigDecimal("3"));
    private final MathFunction log5 = new Log(ln, new BigDecimal("5"));
    private final MathFunction log7 = new Log(ln, new BigDecimal("7"));
    private final MathFunction log10 = new Log(ln, new BigDecimal("10"));

    @ParameterizedTest
    @CsvSource({
            "0.5, -1.0",
            "1.0, 0",
            "2.0, 1.0",
            "4.0, 2.0",
            "8.0, 3.0",
            "16.0, 4.0"
    })
    void log2_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = log2.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0.333333, -1.0",
            "1.0, 0",
            "3.0, 1.0",
            "9.0, 2.0",
            "27.0, 3.0"
    })
    void log3_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = log3.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0.2, -1.0",
            "1.0, 0",
            "5.0, 1.0",
            "25.0, 2.0",
            "125.0, 3.0"
    })
    void log5_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = log5.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0.142857, -1.0",
            "1.0, 0",
            "7.0, 1.0",
            "49.0, 2.0",
            "343.0, 3.0"
    })
    void log7_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = log7.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0.1, -1.0",
            "1.0, 0",
            "10.0, 1.0",
            "100.0, 2.0",
            "1000.0, 3.0"
    })
    void log10_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = log10.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @Test
    void log_throws_for_non_positive() {
        assertThrows(ArithmeticException.class, () -> log2.calc(BigDecimal.ZERO, eps));
        assertThrows(ArithmeticException.class, () -> log2.calc(new BigDecimal("-1"), eps));
        assertThrows(ArithmeticException.class, () -> log10.calc(BigDecimal.ZERO, eps));
        assertThrows(ArithmeticException.class, () -> log10.calc(new BigDecimal("-1"), eps));
    }

    @Test
    void log_uses_ln_dependency_with_mock() {
        MathFunction lnMock = mock(MathFunction.class);
        MathFunction log2 = new Log(lnMock, new BigDecimal("2"));

        BigDecimal x = new BigDecimal("8");
        BigDecimal base = new BigDecimal("2");

        when(lnMock.calc(x, eps)).thenReturn(new BigDecimal("6"));
        when(lnMock.calc(base, eps)).thenReturn(new BigDecimal("2"));

        BigDecimal actual = log2.calc(x, eps);

        assertClose(new BigDecimal("3"), actual, new BigDecimal("1E-12"));
        verify(lnMock).calc(x, eps);
        verify(lnMock).calc(base, eps);
    }
}
