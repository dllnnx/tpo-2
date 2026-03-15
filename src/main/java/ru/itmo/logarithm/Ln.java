package ru.itmo.logarithm;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

import static java.lang.String.format;

public class Ln implements MathFunction {
    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        if (x.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ArithmeticException(format("Натуральный логарифм не имеет значения при x = %s", x));
        }

        if (x.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal y = x.subtract(BigDecimal.ONE).divide(x.add(BigDecimal.ONE), MathConfig.MATH_CONTEXT);
        BigDecimal y2 = y.multiply(y, MathConfig.MATH_CONTEXT);

        BigDecimal term = y;
        BigDecimal sum = BigDecimal.ZERO;
        int n = 0;

        while (term.abs().compareTo(eps) > 0) {
            n++;

            BigDecimal divisor = new BigDecimal(2 * n - 1);
            BigDecimal termDivided = term.divide(divisor, MathConfig.MATH_CONTEXT);
            sum = sum.add(termDivided, MathConfig.MATH_CONTEXT);

            term = term.multiply(y2, MathConfig.MATH_CONTEXT);
        }

        return sum.multiply(BigDecimal.TWO, MathConfig.MATH_CONTEXT);
    }
}
