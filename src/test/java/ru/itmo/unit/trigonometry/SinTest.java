package ru.itmo.unit.trigonometry;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static ru.itmo.util.CustomAsserts.assertClose;

public class SinTest implements BaseTrigTest {

    @ParameterizedTest
    @CsvSource({
            "-2.0, -0.90929742682568169542",
            "-1.0, -0.84147098480789650666",
            "-0.5, -0.47942553860420300026",
            "-0.2, -0.19866933079506121546",
            "0.0, 0",
            "0.2, 0.19866933079506121546",
            "0.5, 0.47942553860420300026",
            "2.0, 0.90929742682568169542"
    })
    void sin_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = sin.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "0.5235987755982989, 0.5", // pi/6
            "0.7853981633974483, 0.70710678118654752440", // pi/4, 1/sqrt(2)
            "1.0471975511965977, 0.86602540378443864676", // pi/3, sqrt(3)/2
            "3.141592653589793, 0" // pi
    })
    void sin_matches_reference_key_points(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = sin.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0.1",
            "0.35",
            "0.7"
    })
    void sin_is_periodic_with_2pi(String xString) {
        BigDecimal x = new BigDecimal(xString);
        BigDecimal base = sin.calc(x, eps);
        BigDecimal shifted = sin.calc(x.add(TWO_PI), eps);
        assertClose(base, shifted, new BigDecimal("1E-4"));
    }
}
