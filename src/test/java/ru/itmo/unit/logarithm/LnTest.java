package ru.itmo.unit.logarithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.itmo.util.CustomAsserts.assertClose;

public class LnTest implements BaseLogTest {

    @ParameterizedTest
    @CsvSource({
            "0.1, -2.3025850929940456836",
            "0.2, -1.6094379124341003746",
            "0.5, -0.69314718055994530940",
            "1.0, 0",
            "2.0, 0.69314718055994530940",
            "3.0, 1.0986122886681096914",
            "10.0, 2.3025850929940456836"
    })
    void ln_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = ln.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @Test
    void ln_throws_for_non_positive() {
        assertThrows(ArithmeticException.class, () -> ln.calc(BigDecimal.ZERO, eps));
        assertThrows(ArithmeticException.class, () -> ln.calc(new BigDecimal("-1"), eps));
    }
}
