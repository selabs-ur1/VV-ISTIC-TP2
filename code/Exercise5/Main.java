package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        // Create our complexity report generator
        ComplexityReportGenerator reportGenerator = new ComplexityReportGenerator();

        // Parse the source code
        SourceRoot root = new SourceRoot(file.toPath());
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(reportGenerator, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // Generate Markdown report
        String reportPath = "cyclomatic_complexity_report.md";
        reportGenerator.generateMarkdownReport(reportPath);
        System.out.println("Report generated: " + reportPath);

        // Generate histogram
        List<Integer> complexities = reportGenerator.getComplexityInfos().stream()
                .map(info -> info.complexity)
                .collect(Collectors.toList());

        ComplexityHistogramGenerator histogramGenerator = new ComplexityHistogramGenerator();
        String histogramPath = "complexity_histogram.png";
        histogramGenerator.generateHistogram(complexities, histogramPath);
        System.out.println("Histogram generated: " + histogramPath);
    }
}