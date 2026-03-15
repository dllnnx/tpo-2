package ru.itmo.unit.trigonometry;

import ru.itmo.function.MathFunction;
import ru.itmo.trigonometry.Sin;

import java.math.BigDecimal;

public interface BaseTrigTest {

    BigDecimal TWO_PI = new BigDecimal("6.2831853071795864769");
    BigDecimal HALF_PI = new BigDecimal("1.57079632679489661923");
    BigDecimal eps = new BigDecimal("1E-6");
    BigDecimal tol = new BigDecimal("1E-4");

    MathFunction sin = new Sin();
}
