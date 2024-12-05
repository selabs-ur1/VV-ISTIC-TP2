package fr.istic.vv.cc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CyclomaticComplexityReport {

    public static void generateReport(String filePath, Map<String, Integer> complexities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("# Cyclomatic Complexity Report\n\n");
            writer.write("| Method Signature | Cyclomatic Complexity |\n");
            writer.write("|------------------|------------------------|\n");

            for (Map.Entry<String, Integer> entry : complexities.entrySet()) {
                writer.write("| " + entry.getKey() + " | " + entry.getValue() + " |\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
