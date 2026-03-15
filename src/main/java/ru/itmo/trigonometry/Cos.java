package ru.itmo.trigonometry;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

public class Cos implements MathFunction {

    private final MathFunction sin;

    private static final BigDecimal HALF_PI = new BigDecimal("1.57079632679489661923");

    public Cos(MathFunction sin) {
        this.sin = sin;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        return sin.calc(HALF_PI.subtract(x, MathConfig.MATH_CONTEXT), eps);
    }
}
