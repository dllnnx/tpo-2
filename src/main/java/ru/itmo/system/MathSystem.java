package ru.itmo.system;

import ru.itmo.function.MathFunction;
import ru.itmo.util.MathConfig;

import java.math.BigDecimal;

public class MathSystem implements MathFunction {

    private static final BigDecimal HALF_PI = new BigDecimal("1.57079632679489661923");

    private final MathFunction sin;
    private final MathFunction cos;
    private final MathFunction tan;
    private final MathFunction cotan;
    private final MathFunction sec;
    private final MathFunction cosec;
    private final MathFunction log2;
    private final MathFunction log3;
    private final MathFunction log5;
    private final MathFunction log10;

    public MathSystem(MathFunction sin, MathFunction cos, MathFunction tan, MathFunction cotan, MathFunction sec, MathFunction cosec, MathFunction log2, MathFunction log3, MathFunction log5, MathFunction log10) {
        this.sin = sin;
        this.cos = cos;
        this.tan = tan;
        this.cotan = cotan;
        this.sec = sec;
        this.cosec = cosec;
        this.log2 = log2;
        this.log3 = log3;
        this.log5 = log5;
        this.log10 = log10;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        if (x.compareTo(BigDecimal.ZERO) <= 0) {
            return calcTrigBranch(x, eps);
        } else {
            return calcLogBranch(x, eps);
        }
    }

    private BigDecimal calcTrigBranch(BigDecimal x, BigDecimal eps) {
        if (x.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Система не определена в точке x = 0");
        }

        BigDecimal cosecX = cosec.calc(x, eps);
        BigDecimal secX = sec.calc(x, eps);
        BigDecimal cotX = cotan.calc(x, eps);
        BigDecimal tanX = tan.calc(x, eps);
        BigDecimal cosX = cos.calc(x, eps);
        BigDecimal sinX = sin.calc(x, eps);

        BigDecimal part1 = cosecX
                .divide(secX, MathConfig.MATH_CONTEXT)
                .pow(3, MathConfig.MATH_CONTEXT)
                .divide(cosecX.add(cosecX.subtract(cosecX, MathConfig.MATH_CONTEXT)), MathConfig.MATH_CONTEXT)
                .multiply(cosecX, MathConfig.MATH_CONTEXT)
                .pow(2, MathConfig.MATH_CONTEXT)
                .subtract(cosX, MathConfig.MATH_CONTEXT)
                .add(cosecX.divide(cotX, MathConfig.MATH_CONTEXT), MathConfig.MATH_CONTEXT)
                .divide(cotX.multiply(tanX, MathConfig.MATH_CONTEXT), MathConfig.MATH_CONTEXT);

        BigDecimal part2 = cotX
                .add(secX, MathConfig.MATH_CONTEXT)
                .add(cotX, MathConfig.MATH_CONTEXT)
                .add(secX, MathConfig.MATH_CONTEXT)
                .add(cosX, MathConfig.MATH_CONTEXT)
                .divide(cosecX, MathConfig.MATH_CONTEXT);

        BigDecimal numerator = part1.multiply(part2, MathConfig.MATH_CONTEXT);

        BigDecimal denominator = sinX
                .pow(2, MathConfig.MATH_CONTEXT)
                .multiply(cosecX, MathConfig.MATH_CONTEXT)
                .multiply(cosecX, MathConfig.MATH_CONTEXT)
                .add(
                        sinX.multiply(secX, MathConfig.MATH_CONTEXT).add(sinX, MathConfig.MATH_CONTEXT), MathConfig.MATH_CONTEXT
                )
                .add((
                        tanX.pow(2, MathConfig.MATH_CONTEXT).pow(3, MathConfig.MATH_CONTEXT).pow(3, MathConfig.MATH_CONTEXT)
                ));

        return numerator
                .divide(denominator, MathConfig.MATH_CONTEXT)
                .subtract(
                        secX.pow(3, MathConfig.MATH_CONTEXT).pow(3, MathConfig.MATH_CONTEXT)
                );
    }

    private BigDecimal calcLogBranch(BigDecimal x, BigDecimal eps) {
        if (x.equals(BigDecimal.ONE)) {
            throw new IllegalArgumentException("Система не определена в точке x = 1");
        }

        BigDecimal log2X = log2.calc(x, eps);
        BigDecimal log5X = log5.calc(x, eps);
        BigDecimal log3X = log3.calc(x, eps);
        BigDecimal log10X = log10.calc(x, eps);

        return log5X
                .add(log10X, MathConfig.MATH_CONTEXT)
                .multiply(log2X, MathConfig.MATH_CONTEXT)
                .subtract(log10X, MathConfig.MATH_CONTEXT)
                .divide(log2X, MathConfig.MATH_CONTEXT)
                .divide(log3X, MathConfig.MATH_CONTEXT);
    }
}
