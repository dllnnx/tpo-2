package ru.itmo.system;

import ru.itmo.function.MathFunction;

import java.math.BigDecimal;

public class MathSystem implements MathFunction {

    private final MathFunction sin;
    private final MathFunction cos;
    private final MathFunction tan;
    private final MathFunction cotan;
    private final MathFunction sec;
    private final MathFunction cosec;
    private final MathFunction ln;
    private final MathFunction log2;
    private final MathFunction log3;
    private final MathFunction log5;
    private final MathFunction log7;
    private final MathFunction log10;

    public MathSystem(MathFunction sin, MathFunction cos, MathFunction tan, MathFunction cotan, MathFunction sec, MathFunction cosec, MathFunction ln, MathFunction log2, MathFunction log3, MathFunction log5, MathFunction log7, MathFunction log10) {
        this.sin = sin;
        this.cos = cos;
        this.tan = tan;
        this.cotan = cotan;
        this.sec = sec;
        this.cosec = cosec;
        this.ln = ln;
        this.log2 = log2;
        this.log3 = log3;
        this.log5 = log5;
        this.log7 = log7;
        this.log10 = log10;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        return BigDecimal.ONE;
    }
}
