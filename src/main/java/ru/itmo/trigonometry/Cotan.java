package ru.itmo.trigonometry;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

import static java.lang.String.format;

public class Cotan implements MathFunction {

    private final MathFunction sin;
    private final MathFunction cos;

    public Cotan(MathFunction sin, MathFunction cos) {
        this.sin = sin;
        this.cos = cos;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        BigDecimal s = sin.calc(x, eps);

        if (s.abs().compareTo(eps) < 0) {
            throw new ArithmeticException(format("У котангенса нет значения при x = %s", x));
        }

        return cos.calc(x, eps).divide(s, MathConfig.MATH_CONTEXT);
    }
}
