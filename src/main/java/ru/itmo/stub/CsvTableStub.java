package ru.itmo.stub;

import ru.itmo.function.MathFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class CsvTableStub implements MathFunction {

    private final Map<String, BigDecimal> table = new HashMap<>();

    public CsvTableStub(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("x") || line.isBlank()) {
                    continue;
                }

                String[] values = line.split(",");

                BigDecimal x = new BigDecimal(values[0].trim());
                BigDecimal fx = new BigDecimal(values[1].trim());

                table.put(key(x), fx);
            }
        }
    }

    @Override
    public BigDecimal calc(BigDecimal x, BigDecimal eps) {
        BigDecimal fx = table.get(key(x));

        if (fx == null) {
            throw new IllegalArgumentException(format("Не найдено значение функции для x = %s", x));
        }

        return fx;
    }

    private String key(BigDecimal x) {
        return x.stripTrailingZeros().toPlainString();
    }
}
