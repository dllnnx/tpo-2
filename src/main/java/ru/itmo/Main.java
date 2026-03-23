package ru.itmo;

import ru.itmo.function.MathFunction;
import ru.itmo.util.CsvExporter;
import ru.itmo.util.ModuleFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        List<String> modules = List.of("sin", "cos", "tan", "cotan", "sec", "cosec",
                "ln", "log2", "log3", "log5", "log10");

        for (String module : modules) {
            MathFunction fun = ModuleFactory.build(module, new BigDecimal("1E-10"));
            CsvExporter.export(
                    fun,
                    new BigDecimal("-10"),
                    new BigDecimal("10"),
                    new BigDecimal("0.08"),
                    new BigDecimal("1E-10"),
                    Path.of("out_" + module + ".csv"),
                    CsvExporter.ErrorMode.SKIP_X
            );
        }

        System.out.println("done");

    }
}