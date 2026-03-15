package ru.itmo.unit.trigonometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.function.MathFunction;
import ru.itmo.trigonometry.Cosec;
import ru.itmo.trigonometry.Sin;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ru.itmo.util.CustomAsserts.assertClose;

public class CosecTest implements BaseTrigTest {

    private final MathFunction sin = new Sin();
    private final MathFunction cosec = new Cosec(sin);

    @ParameterizedTest
    @CsvSource({
            "-1.0, -1.1883951057781212163",
            "-0.5, -2.0858296429334884686",
            "-0.2, -5.0334895476723440255",
            "0.2, 5.0334895476723440255",
            "0.5, 2.0858296429334884686",
            "1.0, 1.1883951057781212163"
    })
    void cosec_matches_reference(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = cosec.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @ParameterizedTest
    @CsvSource({
            "0.5235987755982989, 2", // pi/6
            "0.7853981633974483, 1.4142135623730950488", // pi/4, sqrt(2)
            "1.0471975511965977, 1.1547005383792515290", // pi/3, 2/sqrt(3)
            "1.5707963267948966, 1", // pi/2
            "2.356194490192345, 1.4142135623730950488", // 3*pi/4
            "4.7123889803846897, -1" // 3*pi/2
    })
    void cosec_matches_reference_key_points(String xs, String expectedS) {
        BigDecimal x = new BigDecimal(xs);
        BigDecimal actual = cosec.calc(x, eps);
        BigDecimal expected = new BigDecimal(expectedS);
        assertClose(expected, actual, tol);
    }

    @Test
    void cosec_throws_when_sin_is_zero() {
        BigDecimal x = BigDecimal.ZERO;

        assertThrows(ArithmeticException.class, () -> cosec.calc(x, eps));
    }

    @Test
    void cosec_changes_sign_around_zero() {
        BigDecimal left = cosec.calc(new BigDecimal("-0.01"), eps);
        BigDecimal right = cosec.calc(new BigDecimal("0.01"), eps);

        assertTrue(left.compareTo(BigDecimal.ZERO) < 0);
        assertTrue(right.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void cosec_uses_sin_dependency_with_mocks() {
        MathFunction sinMock = mock(MathFunction.class);
        MathFunction cosec = new Cosec(sinMock);

        BigDecimal x = new BigDecimal("1.5707963267948966");
        when(sinMock.calc(x, eps)).thenReturn(new BigDecimal("1"));

        BigDecimal actual = cosec.calc(x, eps);

        assertClose(new BigDecimal("1"), actual, new BigDecimal("1E-12"));
        verify(sinMock).calc(x, eps);
    }
}
