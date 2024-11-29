package fr.istic.vv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        // Validate input
        if(args.length == 0) {
            System.err.println("Please provide the path to the source code directory");
            System.exit(1);
        }

        File sourceDir = new File(args[0]);
        if(!sourceDir.exists() || !sourceDir.isDirectory() || !sourceDir.canRead()) {
            System.err.println("Provide a path to an existing, readable directory");
            System.exit(2);
        }

        // Create output path for the report
        Path outputPath = Paths.get("no_getter_report.md");

        // Create and run the detector
        NoGetterDetector detector = new NoGetterDetector(sourceDir.toPath());
        detector.detectAndReport(outputPath);

        System.out.println("No getter report generated at: " + outputPath);
    }
}