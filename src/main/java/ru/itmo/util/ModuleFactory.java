package ru.itmo.util;

import ru.itmo.function.MathFunction;
import ru.itmo.logarithm.*;
import ru.itmo.system.MathSystem;
import ru.itmo.trigonometry.*;

import java.math.BigDecimal;

public class ModuleFactory {

    public static MathFunction build(String name, BigDecimal epsForBase) {
        MathFunction sin = new Sin();
        MathFunction cos = new Cos(sin);
        MathFunction tan = new Tan(sin, cos);
        MathFunction cotan = new Cotan(sin, cos);
        MathFunction sec = new Sec(cos);
        MathFunction cosec = new Cosec(sin);

        MathFunction ln = new Ln();
        MathFunction log2 = new Log(ln, new BigDecimal("2"));
        MathFunction log3 = new Log(ln, new BigDecimal("3"));
        MathFunction log5 = new Log(ln, new BigDecimal("5"));
        MathFunction log7 = new Log(ln, new BigDecimal("7"));
        MathFunction log10 = new Log(ln, new BigDecimal("10"));

        MathFunction system = new MathSystem(sin, cos, tan, cotan, sec, cosec, ln, log2, log3, log5, log7, log10);

        return switch (name.toLowerCase()) {
            case "sin" -> sin;
            case "cos" -> cos;
            case "tan" -> tan;
            case "cotan" -> cotan;
            case "sec" -> sec;
            case "cosec" -> cosec;

            case "ln" -> ln;
            case "log2" -> log2;
            case "log3" -> log3;
            case "log5" -> log5;
            case "log7" -> log7;
            case "log10" -> log10;

            case "system" -> system;
            default -> throw new IllegalArgumentException("Unknown module: " + name);
        };
    }
}