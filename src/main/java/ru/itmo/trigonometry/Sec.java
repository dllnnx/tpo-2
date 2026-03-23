package ru.itmo.trigonometry;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

import static java.lang.String.format;

public class Sec implements MathFunction {

    private final MathFunction cos;

    public Sec(MathFunction cos) {
        this.cos = cos;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        BigDecimal c = cos.calc(x, eps);

        if (c.abs().compareTo(eps) < 0) {
            throw new IllegalArgumentException(format("У секанса нет значения при x = %s", x));
        }

        return BigDecimal.ONE.divide(c, MathConfig.MATH_CONTEXT);
    }
}
