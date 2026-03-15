package ru.itmo.util;

import ru.itmo.function.MathFunction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvExporter {

    public enum ErrorMode {
        SKIP_X,
        WRITE_ERROR
    }

    public static void export(
            MathFunction fun,
            BigDecimal startX,
            BigDecimal endX,
            BigDecimal step,
            BigDecimal eps,
            Path outputFile,
            ErrorMode errorMode
    ) throws IOException {

        if (step.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("step должен быть > 0");
        }

        if (startX.compareTo(endX) > 0) {
            throw new IllegalArgumentException("startX должен быть больше либо равен endX");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            writer.write("x,f(x)");
            writer.newLine();

            for (BigDecimal x = startX; x.compareTo(endX) <= 0; x = x.add(step)) {
                try {
                    BigDecimal fx = fun.calc(x, eps);
                    writer.write(x.toPlainString());
                    writer.write(",");
                    writer.write(fx.toPlainString());
                    writer.newLine();
                } catch (Exception ex) {
                    if (errorMode == ErrorMode.WRITE_ERROR) {
                        writer.write(x.toPlainString());
                        writer.write(",ERROR");
                        writer.newLine();
                    }
                }
            }
        }
    }
}
