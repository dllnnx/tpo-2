package ru.itmo.trigonometry;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

import static java.lang.String.format;

public class Tan implements MathFunction {

    private final MathFunction sin;
    private final MathFunction cos;

    public Tan(MathFunction sin, MathFunction cos) {
        this.sin = sin;
        this.cos = cos;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        BigDecimal c = cos.calc(x, eps);

        if (c.abs().compareTo(eps) < 0) {
            throw new ArithmeticException(format("У тангенса нет значения при x = %s", x));
        }

        return sin.calc(x, eps).divide(c, MathConfig.MATH_CONTEXT);
    }
}
