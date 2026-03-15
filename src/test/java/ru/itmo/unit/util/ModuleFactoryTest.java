package ru.itmo.unit.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.function.MathFunction;
import ru.itmo.util.ModuleFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ModuleFactoryTest {

    private static final BigDecimal EPS = new BigDecimal("1E-6");

    @ParameterizedTest
    @CsvSource({
            "sin, Sin",
            "cos, Cos",
            "tan, Tan",
            "cotan, Cotan",
            "sec, Sec",
            "cosec, Cosec",
            "ln, Ln",
            "log2, Log",
            "log3, Log",
            "log10, Log",
            "system, MathSystem"
    })
    void build_returns_expected_type(String moduleName, String expectedSimpleClassName) {
        MathFunction function = ModuleFactory.build(moduleName, EPS);
        assertNotNull(function);
        assertEquals(expectedSimpleClassName, function.getClass().getSimpleName());
    }

    @Test
    void build_is_case_insensitive() {
        assertEquals("Sin", ModuleFactory.build("SiN", EPS).getClass().getSimpleName());
        assertEquals("MathSystem", ModuleFactory.build("SyStEm", EPS).getClass().getSimpleName());
    }

    @Test
    void build_throws_for_unknown_module() {
        assertThrows(IllegalArgumentException.class, () -> ModuleFactory.build("unknown", EPS));
    }
}