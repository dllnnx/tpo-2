package ru.itmo.unit.trigonometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.function.MathFunction;
import ru.itmo.trigonometry.Cos;
import ru.itmo.trigonometry.Tan;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ru.itmo.util.CustomAsserts.assertClose;

public class TanTest implements BaseTrigTest {

    private final MathFunction cos = new Cos(sin);
    private final MathFunction tan = new Tan(sin, cos);

    @ParameterizedTest
    @CsvSource({
            "-1.0, -1.5574077246549020821",
            "-0.5, -0.54630248984379049497",
            "-0.2, -0.20271003550867248081",
            "0.2, 0.20271003550867248584",
            "0.5, 0.54630248984379053151",
            "1.0, 1.5574077246549022305"
    })
    void tan_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = tan.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "0.5235987755982989, 0.57735026918962576451", // pi/6, sqrt(3)/3
            "0.7853981633974483, 1", // pi/4
            "1.0471975511965977, 1.7320508075688772935", // pi/3, sqrt(3)
            "3.141592653589793, 0" // pi
    })
    void tan_matches_reference_key_points(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = tan.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @Test
    void tan_throws_when_cos_is_zero() {
        BigDecimal x = BigDecimal.valueOf(Math.PI / 2);

        assertThrows(ArithmeticException.class, () -> tan.calc(x, eps));
    }

    @Test
    void tan_changes_sign_around_half_pi() {
        BigDecimal left = tan.calc(HALF_PI.subtract(new BigDecimal("0.01"), MathConfig.MATH_CONTEXT), eps);
        BigDecimal right = tan.calc(HALF_PI.add(new BigDecimal("0.01"), MathConfig.MATH_CONTEXT), eps);

        assertTrue(left.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(right.compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void tan_uses_sin_and_cos_dependencies_with_mocks() {
        MathFunction sinMock = mock(MathFunction.class);
        MathFunction cosMock = mock(MathFunction.class);
        MathFunction tan = new Tan(sinMock, cosMock);

        BigDecimal x = new BigDecimal("3.141592653589793");
        when(sinMock.calc(x, eps)).thenReturn(new BigDecimal("0"));
        when(cosMock.calc(x, eps)).thenReturn(new BigDecimal("-1"));

        BigDecimal actual = tan.calc(x, eps);

        assertClose(new BigDecimal("0"), actual, new BigDecimal("1E-12"));
        verify(sinMock).calc(x, eps);
        verify(cosMock).calc(x, eps);
    }
}
