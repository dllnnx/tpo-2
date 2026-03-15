package ru.itmo.function;

import java.math.BigDecimal;

public interface MathFunction {

    BigDecimal calc(BigDecimal x, BigDecimal eps);
}
