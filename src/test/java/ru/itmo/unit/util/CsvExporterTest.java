package ru.itmo.unit.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.itmo.function.MathFunction;
import ru.itmo.util.CsvExporter;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvExporterTest {

    @TempDir
    Path tempDir;

    private static final BigDecimal EPS = new BigDecimal("1E-6");

    @Test
    void export_throws_when_step_is_non_positive() {
        Path out = tempDir.resolve("bad_step.csv");

        assertThrows(
                IllegalArgumentException.class,
                () -> CsvExporter.export(
                        (x, eps) -> x,
                        BigDecimal.ZERO,
                        BigDecimal.ONE,
                        BigDecimal.ZERO, // step
                        EPS,
                        out,
                        CsvExporter.ErrorMode.SKIP_X
                )
        );
    }

    @Test
    void export_throws_when_start_is_greater_than_end() {
        Path out = tempDir.resolve("bad_bounds.csv");

        assertThrows(
                IllegalArgumentException.class,
                () -> CsvExporter.export(
                        (x, eps) -> x,
                        BigDecimal.ONE, // startX
                        BigDecimal.ZERO, // endX
                        BigDecimal.ONE,
                        EPS,
                        out,
                        CsvExporter.ErrorMode.SKIP_X
                )
        );
    }

    @Test
    void export_skips_failed_points_in_skip_x_mode() throws Exception {
        Path out = tempDir.resolve("skip.csv");
        MathFunction function = (x, eps) -> {
            if (x.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("point is undefined");
            }
            return x.multiply(x);
        };

        CsvExporter.export(
                function,
                new BigDecimal("-1"),
                new BigDecimal("1"),
                BigDecimal.ONE,
                EPS,
                out,
                CsvExporter.ErrorMode.SKIP_X
        );

        List<String> lines = Files.readAllLines(out);
        assertEquals(List.of(
                "x,f(x)",
                "-1,1",
                "1,1"
        ), lines);
    }

    @Test
    void export_writes_error_marker_in_write_error_mode() throws Exception {
        Path out = tempDir.resolve("write_error.csv");
        MathFunction function = (x, eps) -> {
            if (x.compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("point is undefined");
            }
            return x.multiply(x);
        };

        CsvExporter.export(
                function,
                new BigDecimal("-1"),
                new BigDecimal("1"),
                BigDecimal.ONE,
                EPS,
                out,
                CsvExporter.ErrorMode.WRITE_ERROR
        );

        List<String> lines = Files.readAllLines(out);
        assertEquals(List.of(
                "x,f(x)",
                "-1,1",
                "0,ERROR",
                "1,1"
        ), lines);
    }
}