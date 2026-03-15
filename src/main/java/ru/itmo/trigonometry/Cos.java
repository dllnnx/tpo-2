package ru.itmo.trigonometry;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

public class Cos implements MathFunction {

    private final MathFunction sin;

    private static final BigDecimal HALF_PI = new BigDecimal(Math.PI / 2, MathConfig.MATH_CONTEXT);

    public Cos(MathFunction sin) {
        this.sin = sin;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        return sin.calc(HALF_PI.subtract(x), eps);
    }
}
