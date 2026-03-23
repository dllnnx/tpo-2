package ru.itmo.trigonometry;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

import static java.lang.String.format;

public class Cosec implements MathFunction {

    private final MathFunction sin;

    public Cosec(MathFunction sin) {
        this.sin = sin;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        BigDecimal s = sin.calc(x, eps);

        if (s.abs().compareTo(eps) < 0) {
            throw new IllegalArgumentException(format("У косеканса нет значения при x = %s", x));
        }

        return BigDecimal.ONE.divide(s, MathConfig.MATH_CONTEXT);
    }
}
