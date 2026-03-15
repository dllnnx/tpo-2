package ru.itmo.unit.logarithm;

import ru.itmo.function.MathFunction;
import ru.itmo.logarithm.Ln;

import java.math.BigDecimal;

public interface BaseLogTest {

    MathFunction ln = new Ln();
    BigDecimal eps = new BigDecimal("1E-6");
    BigDecimal tol = new BigDecimal("1E-3");
}
