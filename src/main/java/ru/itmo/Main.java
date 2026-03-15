package ru.itmo;

import ru.itmo.function.MathFunction;
import ru.itmo.util.CsvExporter;
import ru.itmo.util.ModuleFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {

        String module = "sin";
        MathFunction fun = ModuleFactory.build(module, new BigDecimal("1E-10"));

        CsvExporter.export(
                fun,
                new BigDecimal("-3"),
                new BigDecimal("3"),
                new BigDecimal("0.2"),
                new BigDecimal("1E-10"),
                Path.of("out_" + module + ".csv"),
                CsvExporter.ErrorMode.SKIP_X
        );

        System.out.println("done");

    }
}