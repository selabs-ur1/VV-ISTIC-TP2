package fr.istic.vv.exercise4;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReportPrinter {
    public void add(Object[] row) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("report.csv"), StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(row[0] + ";" + row[1] + ";" + row[2]);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
