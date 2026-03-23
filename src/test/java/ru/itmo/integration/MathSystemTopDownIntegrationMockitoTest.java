package ru.itmo.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.itmo.function.MathFunction;
import ru.itmo.logarithm.Ln;
import ru.itmo.logarithm.Log;
import ru.itmo.system.MathSystem;
import ru.itmo.trigonometry.*;
import ru.itmo.util.CsvGenerator;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.itmo.util.CustomAsserts.assertClose;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MathSystemTopDownIntegrationMockitoTest {

    private static final BigDecimal EPS = new BigDecimal("1E-20");
    private static final BigDecimal TOL = new BigDecimal("1E-5");

    private static final String[] NEGATIVE_BRANCH_POINTS = {
            "-0.77777", "-2.54218", "-2.63702", "-2.72534", "-3.81645", "-3.80470"
    };

    private static final String[] POSITIVE_BRANCH_POINTS = {
            "0.1", "2.0", "3.0", "7.0"
    };

    private static final String[] INFINITE_VALUES = {
            "0.0", "-1.57079", "-4.71239", "-3.14159", "1"
    };

    private static final Map<String, BigDecimal> SYSTEM_EXPECTED = Map.ofEntries(
            Map.entry("-0.77777", new BigDecimal("1.78047")),
            Map.entry("-2.54218", new BigDecimal("9.80271")),
            Map.entry("-2.63702", new BigDecimal("10.66685")),
            Map.entry("-2.72534", new BigDecimal("-0.00057")),
            Map.entry("-3.81645", new BigDecimal("2.00818")),
            Map.entry("-3.80470", new BigDecimal("-0.01215")),
            Map.entry("0.1", new BigDecimal("1.30336")),
            Map.entry("2.0", new BigDecimal("0.68261")),
            Map.entry("3.0", new BigDecimal("0.8587")),
            Map.entry("7.0", new BigDecimal("0.98977"))
    );

    @Mock
    private MathFunction sinStub;

    @Mock
    private MathFunction cosStub;

    @Mock
    private MathFunction tanStub;

    @Mock
    private MathFunction cotanStub;

    @Mock
    private MathFunction secStub;

    @Mock
    private MathFunction cosecStub;

    @Mock
    private MathFunction log2Stub;

    @Mock
    private MathFunction log3Stub;

    @Mock
    private MathFunction log5Stub;

    @Mock
    private MathFunction log10Stub;

    @BeforeAll
    static void refreshStubTablesFromRealFunctions() throws Exception {
        CsvGenerator.generateDefaultTestResources();
    }

    @Test
    void trig_stage_0_all_stubs() throws Exception {
        setupMockFromCsv(sinStub, "sin.csv");
        setupMockFromCsv(cosStub, "cos.csv");
        setupMockFromCsv(tanStub, "tan.csv");
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log2Stub, "log2.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        assertNegativeBranchMatches(sinStub, cosStub, tanStub, cotanStub, secStub, cosecStub,
                log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void trig_stage_1_real_sin_rest_stubs() throws Exception {
        setupMockFromCsv(cosStub, "cos.csv");
        setupMockFromCsv(tanStub, "tan.csv");
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log2Stub, "log2.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction sinReal = new Sin();
        assertNegativeBranchMatches(sinReal, cosStub, tanStub, cotanStub, secStub, cosecStub,
                log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void trig_stage_2_real_sin_cos_rest_stubs() throws Exception {
        setupMockFromCsv(tanStub, "tan.csv");
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log2Stub, "log2.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction sinReal = new Sin();
        MathFunction cosReal = new Cos(sinReal);
        assertNegativeBranchMatches(sinReal, cosReal, tanStub, cotanStub, secStub, cosecStub,
                log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void trig_stage_3_real_sin_cos_tan_rest_stubs() throws Exception {
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log2Stub, "log2.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction sinReal = new Sin();
        MathFunction cosReal = new Cos(sinReal);
        MathFunction tanReal = new Tan(sinReal, cosReal);
        assertNegativeBranchMatches(sinReal, cosReal, tanReal, cotanStub, secStub, cosecStub,
                log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void trig_stage_4_real_sin_cos_tan_cotan_rest_stubs() throws Exception {
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log2Stub, "log2.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction sinReal = new Sin();
        MathFunction cosReal = new Cos(sinReal);
        MathFunction tanReal = new Tan(sinReal, cosReal);
        MathFunction cotanReal = new Cotan(sinReal, cosReal);
        assertNegativeBranchMatches(sinReal, cosReal, tanReal, cotanReal, secStub, cosecStub,
                log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void trig_stage_5_real_sin_cos_tan_cotan_sec_rest_stubs() throws Exception {
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log2Stub, "log2.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction sinReal = new Sin();
        MathFunction cosReal = new Cos(sinReal);
        MathFunction tanReal = new Tan(sinReal, cosReal);
        MathFunction cotanReal = new Cotan(sinReal, cosReal);
        MathFunction secReal = new Sec(cosReal);
        assertNegativeBranchMatches(sinReal, cosReal, tanReal, cotanReal, secReal, cosecStub,
                log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void trig_stage_6_real_trig_rest_stubs() throws Exception {
        setupMockFromCsv(log2Stub, "log2.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction sinReal = new Sin();
        MathFunction cosReal = new Cos(sinReal);
        MathFunction tanReal = new Tan(sinReal, cosReal);
        MathFunction cotanReal = new Cotan(sinReal, cosReal);
        MathFunction secReal = new Sec(cosReal);
        MathFunction cosecReal = new Cosec(sinReal);
        assertNegativeBranchMatches(sinReal, cosReal, tanReal, cotanReal, secReal, cosecReal,
                log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void log_stage_0_all_stubs() throws Exception {
        setupMockFromCsv(sinStub, "sin.csv");
        setupMockFromCsv(cosStub, "cos.csv");
        setupMockFromCsv(tanStub, "tan.csv");
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log2Stub, "log2.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        assertPositiveBranchMatches(sinStub, cosStub, tanStub, cotanStub, secStub, cosecStub,
                log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void log_stage_1_real_log2_rest_stubs() throws Exception {
        setupMockFromCsv(sinStub, "sin.csv");
        setupMockFromCsv(cosStub, "cos.csv");
        setupMockFromCsv(tanStub, "tan.csv");
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log3Stub, "log3.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction lnReal = new Ln();
        MathFunction log2Real = new Log(lnReal, BigDecimal.TWO);
        assertPositiveBranchMatches(sinStub, cosStub, tanStub, cotanStub, secStub, cosecStub,
                log2Real, log3Stub, log5Stub, log10Stub);
    }

    @Test
    void log_stage_2_real_log2_log3_rest_stubs() throws Exception {
        setupMockFromCsv(sinStub, "sin.csv");
        setupMockFromCsv(cosStub, "cos.csv");
        setupMockFromCsv(tanStub, "tan.csv");
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log5Stub, "log5.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction lnReal = new Ln();
        MathFunction log2Real = new Log(lnReal, BigDecimal.TWO);
        MathFunction log3Real = new Log(lnReal, new BigDecimal("3"));
        assertPositiveBranchMatches(sinStub, cosStub, tanStub, cotanStub, secStub, cosecStub,
                log2Real, log3Real, log5Stub, log10Stub);
    }

    @Test
    void log_stage_3_real_log2_log3_log5_rest_stubs() throws Exception {
        setupMockFromCsv(sinStub, "sin.csv");
        setupMockFromCsv(cosStub, "cos.csv");
        setupMockFromCsv(tanStub, "tan.csv");
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        setupMockFromCsv(log10Stub, "log10.csv");
        MathFunction lnReal = new Ln();
        MathFunction log2Real = new Log(lnReal, BigDecimal.TWO);
        MathFunction log3Real = new Log(lnReal, new BigDecimal("3"));
        MathFunction log5Real = new Log(lnReal, new BigDecimal("5"));
        assertPositiveBranchMatches(sinStub, cosStub, tanStub, cotanStub, secStub, cosecStub,
                log2Real, log3Real, log5Real, log10Stub);
    }

    @Test
    void log_stage_3_real_logs_trig_stubs() throws Exception {
        setupMockFromCsv(sinStub, "sin.csv");
        setupMockFromCsv(cosStub, "cos.csv");
        setupMockFromCsv(tanStub, "tan.csv");
        setupMockFromCsv(cotanStub, "cotan.csv");
        setupMockFromCsv(secStub, "sec.csv");
        setupMockFromCsv(cosecStub, "cosec.csv");
        MathFunction lnReal = new Ln();
        MathFunction log2Real = new Log(lnReal, BigDecimal.TWO);
        MathFunction log3Real = new Log(lnReal, new BigDecimal("3"));
        MathFunction log5Real = new Log(lnReal, new BigDecimal("5"));
        MathFunction log10Real = new Log(lnReal, new BigDecimal("10"));
        assertPositiveBranchMatches(sinStub, cosStub, tanStub, cotanStub, secStub, cosecStub,
                log2Real, log3Real, log5Real, log10Real);
    }

    @Test
    void func_stage_1_all_real() {
        MathFunction sinReal = new Sin();
        MathFunction cosReal = new Cos(sinReal);
        MathFunction tanReal = new Tan(sinReal, cosReal);
        MathFunction cotanReal = new Cotan(sinReal, cosReal);
        MathFunction secReal = new Sec(cosReal);
        MathFunction cosecReal = new Cosec(sinReal);
        MathFunction lnReal = new Ln();
        MathFunction log2Real = new Log(lnReal, BigDecimal.TWO);
        MathFunction log3Real = new Log(lnReal, new BigDecimal("3"));
        MathFunction log5Real = new Log(lnReal, new BigDecimal("5"));
        MathFunction log10Real = new Log(lnReal, new BigDecimal("10"));
        assertNegativeBranchMatches(sinReal, cosReal, tanReal, cotanReal, secReal, cosecReal,
                log2Real, log3Real, log5Real, log10Real);
        assertPositiveBranchMatches(sinReal, cosReal, tanReal, cotanReal, secReal, cosecReal,
                log2Real, log3Real, log5Real, log10Real);
    }

    private static void setupMockFromCsv(MathFunction mock, String fileName) throws Exception {
        Path path = Path.of("src/test/resources/" + fileName);
        try (Stream<String> lines = Files.lines(path)) {
            lines.skip(1).forEach(line -> {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    BigDecimal x = new BigDecimal(parts[0].trim());
                    BigDecimal y = new BigDecimal(parts[1].trim());
                    when(mock.calc(Mockito.eq(x), any(BigDecimal.class))).thenReturn(y);
                }
            });
        }
        for (String xText : INFINITE_VALUES) {
            BigDecimal x = new BigDecimal(xText);
            when(mock.calc(Mockito.eq(x), any(BigDecimal.class)))
                    .thenThrow(new IllegalArgumentException("Infinite value for x = " + xText));
        }
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
        MathSystem system = new MathSystem(sin, cos, tan, cotan, sec, cosec, log2, log3, log5, log10);
        assertSystemMatches(system, NEGATIVE_BRANCH_POINTS);
        assertInfiniteValues(system);
    }

    private static void assertPositiveBranchMatches(
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
        MathSystem system = new MathSystem(sin, cos, tan, cotan, sec, cosec, log2, log3, log5, log10);
        assertSystemMatches(system, POSITIVE_BRANCH_POINTS);
        assertInfiniteValues(system);
    }

    private static void assertInfiniteValues(MathSystem system) {
        for (String xText : INFINITE_VALUES) {
            BigDecimal x = new BigDecimal(xText);
            assertThrows(
                    java.lang.IllegalArgumentException.class,
                    () -> system.calc(x, new BigDecimal("1E-5")),
                    "Expected IllegalArgumentException for x = " + xText
            );
        }
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
}
