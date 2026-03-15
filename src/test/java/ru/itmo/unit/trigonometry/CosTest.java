package ru.itmo.unit.trigonometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.function.MathFunction;
import ru.itmo.trigonometry.Cos;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.itmo.util.CustomAsserts.assertClose;

public class CosTest implements BaseTrigTest {

    private final MathFunction cos = new Cos(sin);

    @ParameterizedTest
    @CsvSource({
            "-2.0, -0.41614683654714233135",
            "-1.0, 0.54030230586813976890",
            "-0.5, 0.87758256189037274547",
            "-0.2, 0.98006657784124164329",
            "0.0, 1",
            "0.2, 0.98006657784124161897",
            "0.5, 0.87758256189037268676",
            "2.0, -0.41614683654714244268"
    })
    void cos_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = cos.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "0.5235987755982989, 0.86602540378443864676", // pi/6, sqrt(3)/2
            "0.7853981633974483, 0.70710678118654752440", // pi/4, 1/sqrt(2)
            "1.0471975511965977, 0.5", // pi/3
            "3.141592653589793, -1" // pi
    })
    void cos_matches_reference_key_points(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = cos.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0.1",
            "0.35",
            "0.7"
    })
    void cos_is_periodic_with_2pi(String xString) {
        BigDecimal x = new BigDecimal(xString);
        BigDecimal base = cos.calc(x, eps);
        BigDecimal shifted = cos.calc(x.add(TWO_PI), eps);
        assertClose(base, shifted, new BigDecimal("1E-4"));
    }

    @Test
    void cos_uses_sin_dependency_with_mock() {
        MathFunction sinMock = mock(MathFunction.class);
        MathFunction cos = new Cos(sinMock);

        BigDecimal x = new BigDecimal("0.5235987755982989"); // pi/6
        BigDecimal expectedCosValue = new BigDecimal("0.86602540378443864676"); // sqrt(3)/2
        BigDecimal sinArg = HALF_PI.subtract(x, MathConfig.MATH_CONTEXT);

        when(sinMock.calc(sinArg, eps)).thenReturn(expectedCosValue);

        BigDecimal actual = cos.calc(x, eps);

        assertEquals(expectedCosValue, actual);
        verify(sinMock).calc(sinArg, eps);
    }
}
