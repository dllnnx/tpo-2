package ru.itmo.unit.trigonometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.function.MathFunction;
import ru.itmo.trigonometry.Cotan;
import ru.itmo.trigonometry.Cos;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ru.itmo.util.CustomAsserts.assertClose;

public class CotanTest implements BaseTrigTest {

    private final MathFunction cos = new Cos(sin);
    private final MathFunction cotan = new Cotan(sin, cos);

    @ParameterizedTest
    @CsvSource({
            "-1.0, -0.64209261593433070300",
            "-0.5, -1.8304877217124519192",
            "-0.2, -4.9331548755868935093",
            "0.2, 4.9331548755868935093",
            "0.5, 1.8304877217124519192",
            "1.0, 0.64209261593433070300"
    })
    void cotan_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = cotan.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5235987755982989, 1.7320508075688772935", // pi/6, sqrt(3)
            "0.7853981633974483, 1", // pi/4
            "1.0471975511965977, 0.57735026918962576451", // pi/3, sqrt(3)/3
            "1.5707963267948966, 0", // pi/2
            "2.356194490192345, -1" // 3*pi/4
    })
    void cotan_matches_reference_key_points(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = cotan.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @Test
    void cotan_throws_when_sin_is_zero() {
        BigDecimal x = BigDecimal.ZERO;

        assertThrows(ArithmeticException.class, () -> cotan.calc(x, eps));
    }

    @Test
    void cotan_changes_sign_around_zero() {
        BigDecimal left = cotan.calc(new BigDecimal("-0.01"), eps);
        BigDecimal right = cotan.calc(new BigDecimal("0.01"), eps);

        assertTrue(left.compareTo(BigDecimal.ZERO) < 0);
        assertTrue(right.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void cotan_uses_sin_and_cos_dependencies_with_mocks() {
        MathFunction sinMock = mock(MathFunction.class);
        MathFunction cosMock = mock(MathFunction.class);
        MathFunction cotan = new Cotan(sinMock, cosMock);

        BigDecimal x = new BigDecimal("1.5707963267948966");
        when(sinMock.calc(x, eps)).thenReturn(new BigDecimal("1"));
        when(cosMock.calc(x, eps)).thenReturn(new BigDecimal("0"));

        BigDecimal actual = cotan.calc(x, eps);

        assertClose(new BigDecimal("0"), actual, new BigDecimal("1E-12"));
        verify(sinMock).calc(x, eps);
        verify(cosMock).calc(x, eps);
    }
}
