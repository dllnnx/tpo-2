package ru.itmo.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.itmo.function.MathFunction;
import ru.itmo.stub.CsvTableStub;
import ru.itmo.system.MathSystem;
import ru.itmo.util.CsvGenerator;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Map;

import static ru.itmo.util.CustomAsserts.assertClose;

public class MathSystemTopDownIntegrationTest {

    private static final BigDecimal EPS = new BigDecimal("1E-20");
    private static final BigDecimal TOL = new BigDecimal("1E-9");

    private static final String[] NEGATIVE_BRANCH_POINTS = {
            "-0.77777", "-2.54218", "-2.63702", "-2.72534", "-3.81645", "-3.80470"
    };

    private static final String[] POSITIVE_BRANCH_POINTS = {
            "0.1", "2.0", "3.0", "7.0"
    };

    private static final String[] INFINITE_VALUES = {
            "0.0", "-1.5708", "-4.71239", "-3.14159", "1.0"
    };

    private static final Map<String, BigDecimal> SYSTEM_EXPECTED = Map.ofEntries(
            Map.entry("-0.77777", new BigDecimal("1.78047")),
            Map.entry("-2.54218", new BigDecimal("9.80271")),
            Map.entry("-2.63702", new BigDecimal("10.66685")),
            Map.entry("-2.72534", new BigDecimal("-0.00057")),
            Map.entry("-3.81645", new BigDecimal("0.0")),
            Map.entry("-3.80470", new BigDecimal("-0.01215")),
            Map.entry("0.1", new BigDecimal("1.30336")),
            Map.entry("2.0", new BigDecimal("0.68261")),
            Map.entry("3.0", new BigDecimal("0.8587")),
            Map.entry("7.0", new BigDecimal("0.98977"))
    );

    @BeforeAll
    static void refreshStubTablesFromRealFunctions() throws Exception {
        CsvGenerator.generateDefaultTestResources();
    }

    @Test
    void trig_stage_0_all_stubs() throws Exception {
        Modules m = baseModules();
        assertNegativeBranchMatches(m.sinStub, m.cosStub, m.tanStub, m.cotanStub,
                m.secStub, m.cosecStub, m.log2Stub, m.log3Stub, m.log5Stub, m.log10Stub);
    }

    private static void assertNegativeBranchMatches(
            MathFunction sin,
            MathFunction cos,
            MathFunction tan,
            MathFunction cotan,
            MathFunction sec,
            MathFunction cosec,
            MathFunction log2,
            MathFunction log3,
            MathFunction log5,
            MathFunction log10
    ) {
        assertSystemMatches(
                new MathSystem(sin, cos, tan, cotan, sec, cosec, log2, log3, log5, log10),
                NEGATIVE_BRANCH_POINTS
        );
    }

    private static void assertSystemMatches(MathFunction system, String[] xValues) {
        for (String xText : xValues) {
            BigDecimal x = new BigDecimal(xText);
            BigDecimal expected = expectedValue(xText);
            BigDecimal actual = system.calc(x, EPS);
            assertClose(expected, actual, TOL);
        }
    }

    private static BigDecimal expectedValue(String x) {
        BigDecimal value = SYSTEM_EXPECTED.get(x);
        if (value == null) {
            throw new IllegalArgumentException("No expected value for x = " + x);
        }
        return value;
    }

    private static MathFunction stub(String fileName) throws Exception {
        return new CsvTableStub(Path.of("src/test/resources/" + fileName));
    }

    private static Modules baseModules() throws Exception {
        return new Modules(
                stub("sin.csv"),
                stub("cos.csv"),
                stub("tan.csv"),
                stub("cotan.csv"),
                stub("sec.csv"),
                stub("cosec.csv"),
                stub("log2.csv"),
                stub("log3.csv"),
                stub("log5.csv"),
                stub("log10.csv")
        );
    }

    private record Modules(
            MathFunction sinStub,
            MathFunction cosStub,
            MathFunction tanStub,
            MathFunction cotanStub,
            MathFunction secStub,
            MathFunction cosecStub,
            MathFunction log2Stub,
            MathFunction log3Stub,
            MathFunction log5Stub,
            MathFunction log10Stub
    ) {}
}
