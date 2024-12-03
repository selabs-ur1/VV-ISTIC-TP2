package fr.istic.vv.fields;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MarkdownGenerator {

    public static void generateMarkdownReport(String filePath, Map<String, List<String>> fields) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("# Private fields without getters in public class\n\n");

            if (fields.isEmpty()) {
                writer.write("# No Private fields without getters in public class\n");
                return;
            }

            for (Map.Entry<String, List<String>> entry : fields.entrySet()) {
                String className = entry.getKey();
                List<String> fieldList = entry.getValue();

                writer.write("## Class: " + className + "\n");
                for (String field : fieldList) {
                    writer.write("- `" + field + "`\n");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing Markdown report: " + e.getMessage());
        }
    }
}
