package ru.itmo.util;

import java.math.MathContext;
import java.math.RoundingMode;

public class MathConfig {

    public static final MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_EVEN);
}
