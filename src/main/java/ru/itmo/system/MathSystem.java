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
    private final MathFunction log;

    public MathSystem(MathFunction sin, MathFunction cos, MathFunction tan, MathFunction cotan, MathFunction sec, MathFunction cosec, MathFunction ln, MathFunction log) {
        this.sin = sin;
        this.cos = cos;
        this.tan = tan;
        this.cotan = cotan;
        this.sec = sec;
        this.cosec = cosec;
        this.ln = ln;
        this.log = log;
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        return BigDecimal.ONE;
    }
}
