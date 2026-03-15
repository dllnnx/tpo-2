package ru.itmo.unit.trigonometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.function.MathFunction;
import ru.itmo.trigonometry.Cos;
import ru.itmo.trigonometry.Sec;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ru.itmo.util.CustomAsserts.assertClose;

public class SecTest implements BaseTrigTest {

    private final MathFunction cos = new Cos(sin);
    private final MathFunction sec = new Sec(cos);

    @ParameterizedTest
    @CsvSource({
            "-1.0, 1.8508157176809256172",
            "-0.5, 1.1394939273245491467",
            "-0.2, 1.0203388449411926136",
            "0.2, 1.0203388449411926136",
            "0.5, 1.1394939273245491467",
            "1.0, 1.8508157176809256172"
    })
    void sec_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = sec.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "0.5235987755982989, 1.1547005383792515290", // pi/6, 2/sqrt(3)
            "0.7853981633974483, 1.4142135623730950488", // pi/4, sqrt(2)
            "1.0471975511965977, 2", // pi/3
            "2.0943951023931955, -2", // 2*pi/3
            "2.356194490192345, -1.4142135623730950488" // 3*pi/4
    })
    void sec_matches_reference_key_points(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = sec.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @Test
    void sec_throws_when_cos_is_zero() {
        BigDecimal x = HALF_PI;

        assertThrows(ArithmeticException.class, () -> sec.calc(x, eps));
    }

    @Test
    void sec_changes_sign_around_half_pi() {
        BigDecimal left = sec.calc(HALF_PI.subtract(new BigDecimal("0.01"), MathConfig.MATH_CONTEXT), eps);
        BigDecimal right = sec.calc(HALF_PI.add(new BigDecimal("0.01"), MathConfig.MATH_CONTEXT), eps);

        assertTrue(left.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(right.compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void sec_uses_cos_dependency_with_mocks() {
        MathFunction cosMock = mock(MathFunction.class);
        MathFunction sec = new Sec(cosMock);

        BigDecimal x = BigDecimal.ZERO;
        when(cosMock.calc(x, eps)).thenReturn(new BigDecimal("1"));

        BigDecimal actual = sec.calc(x, eps);

        assertClose(new BigDecimal("1"), actual, new BigDecimal("1E-12"));
        verify(cosMock).calc(x, eps);
    }
}
