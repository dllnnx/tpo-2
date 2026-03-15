package ru.itmo.trigonometry;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

public class Sin implements MathFunction {

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal term = x;
        int n = 1;

        while (term.compareTo(eps) > 0) {
            result = result.add(term, MathConfig.MATH_CONTEXT);

            BigDecimal numerator = term.multiply(x.pow(2), MathConfig.MATH_CONTEXT).negate();
            BigDecimal denominator = new BigDecimal((2 * n) * (2 * n + 1));

            term = numerator.divide(denominator, MathConfig.MATH_CONTEXT);

            n++;
        }

        return result;
    }
}
