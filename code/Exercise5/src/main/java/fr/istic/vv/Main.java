package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.lang.System.out;


public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        final String pathName1 = args[0];
        File file = new File(pathName1);
        final String outputFileName1 = "output.csv";
        computeComplexityAnalysisAndWrite(file, outputFileName1);


        if (args.length > 1) {
            final String pathName2 = args[1];
            File file2 = new File(pathName2);
            final String outputFileName2 = "output2.csv";
            computeComplexityAnalysisAndWrite(file2, outputFileName2);
        }

    }

    private static void computeComplexityAnalysisAndWrite(File file, String fileName) throws IOException {
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        out.println("Computing now complexity for file : " + file.getPath());

        SourceRoot root = new SourceRoot(file.toPath());
        CyclicComplexityCalculator calculator = new CyclicComplexityCalculator();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(calculator, null));
            return SourceRoot.Callback.Result.SAVE;
        });


        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            String[] header = CyclicComplexityAnalysis.getCsvHeader();
            writer.writeNext(header);
            List<String[]> data = calculator.getAnalysis().stream()
                    .map(CyclicComplexityAnalysis::getCsvString).toList();
            writer.writeAll(data);
        }

        out.println(" '+' means 100 values, '-' means 10 values, '*' means 1 value. So for instance '+---**' means 112 values");
        TreeMap<Integer, Long> histogram = calculator.getAnalysis().stream()
                .map(CyclicComplexityAnalysis::cyclicComplexity)
                .collect(Collectors.groupingBy(v -> v, TreeMap::new, Collectors.counting())); // tree map for printing
        histogram.forEach((k, v) -> {
            final int hundreds = v.intValue() / 100;
            out.println(k + ": " +
                        "+".repeat(hundreds) +
                        "-".repeat((v.intValue() - hundreds * 100) / 10)
                        + "*".repeat(v.intValue() % 10)
                        + " (" + v.intValue() + ") ");
        });
    }


}
