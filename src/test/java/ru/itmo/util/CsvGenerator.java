package ru.itmo.util;

import ru.itmo.function.MathFunction;
import ru.itmo.logarithm.Ln;
import ru.itmo.logarithm.Log;
import ru.itmo.system.MathSystem;
import ru.itmo.trigonometry.*;

import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvGenerator {

    private static final List<BigDecimal> DEFAULT_XS = List.of(
            new BigDecimal("-0.77777"),
            new BigDecimal("-2.54218"),
            new BigDecimal("-2.63702"),
            new BigDecimal("-2.72534"),
            new BigDecimal("-3.81645"),
            new BigDecimal("-3.80470"),
            new BigDecimal("0.1"),
            new BigDecimal("2.0"),
            new BigDecimal("3.0"),
            new BigDecimal("7.0")
    );

    public static void main(String[] args) throws Exception {
        generateDefaultTestResources();
        System.out.println("Все тестовые csv файлы сгенерированы");
    }

    public static void generateDefaultTestResources() throws Exception {
        generateForPoints(DEFAULT_XS, new BigDecimal("1E-10"), Path.of("src/test/resources"));
    }

    public static void generateForPoints(
            List<BigDecimal> xs,
            BigDecimal eps,
            Path outputDir
    ) throws Exception {
        Files.createDirectories(outputDir);

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
        MathFunction log10 = new Log(ln, new BigDecimal("10"));

        MathFunction system = new MathSystem(
                sin, cos, tan, cotan, sec, cosec,
                log2, log3, log5, log10
        );

        writeCsv(xs, sin, eps, outputDir.resolve("sin.csv"));
        writeCsv(xs, cos, eps, outputDir.resolve("cos.csv"));
        writeCsv(xs, tan, eps, outputDir.resolve("tan.csv"));
        writeCsv(xs, cotan, eps, outputDir.resolve("cotan.csv"));
        writeCsv(xs, sec, eps, outputDir.resolve("sec.csv"));
        writeCsv(xs, cosec, eps, outputDir.resolve("cosec.csv"));

        writeCsv(xs, ln, eps, outputDir.resolve("ln.csv"));
        writeCsv(xs, log2, eps, outputDir.resolve("log2.csv"));
        writeCsv(xs, log3, eps, outputDir.resolve("log3.csv"));
        writeCsv(xs, log5, eps, outputDir.resolve("log5.csv"));
        writeCsv(xs, log10, eps, outputDir.resolve("log10.csv"));

        writeCsv(xs, system, eps, outputDir.resolve("system.csv"));
    }

    private static void writeCsv(
            List<BigDecimal> xs,
            MathFunction fun,
            BigDecimal eps,
            Path path
    ) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("x,f(x)");
            writer.newLine();

            for (BigDecimal x : xs) {
                try {
                    BigDecimal fx = fun.calc(x, eps);

                    writer.write(x.toPlainString());
                    writer.write(",");
                    writer.write(fx.stripTrailingZeros().toPlainString());
                    writer.newLine();
                } catch (Exception ex) {
                    System.out.println("Ошибка при генерации тестового csv файла: " + ex.getMessage());
                }
            }
        }
    }
}
