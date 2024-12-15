package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.utils.SourceRoot;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CyclomaticComplexityPrinter {

    private final StringBuilder report = new StringBuilder();
    private final List<Integer> cyclomaticComplexityList = new ArrayList<>();

    // Method to calculate Cyclomatic Complexity (CC)
    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        method.getBody().ifPresent(body -> body.accept(visitor, null));
        return visitor.getCyclomaticComplexity();
    }

    // Method to visit each class and its methods
    public void visit(CompilationUnit cu) {
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(ClassOrInterfaceDeclaration classDecl, Void arg) {
                if (!classDecl.isPublic()) return;

                // Traverse methods within the class
                List<MethodDeclaration> methods = classDecl.getMethods();
                for (MethodDeclaration method : methods) {
                    int cc = calculateCyclomaticComplexity(method); // Calculate CC for each method

                    // Store CC value for histogram
                    cyclomaticComplexityList.add(cc);

                    // Append the method information to the report
                    String methodName = method.getNameAsString();
                    String parameters = method.getParameters().toString();
                    String className = classDecl.getFullyQualifiedName().orElse("[Anonymous]");
                    String packageName = cu.getPackageDeclaration()
                            .map(pd -> pd.getNameAsString())
                            .orElse("[No Package]");

                    report.append(String.format("Package: %s, Class: %s, Method: %s, Parameters: %s, Cyclomatic Complexity: %d%n",
                            packageName, className, methodName, parameters, cc));
                }
            }
        }, null);
    }

    // Save the report to a file
    public void saveReport(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Package,Class,Method,Parameters,Cyclomatic Complexity\n");
            writer.write(report.toString());
            System.out.println("Report saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save report: " + e.getMessage());
        }
    }

    // Generate and save the histogram to a file
    public void saveHistogramAsImage(String histogramFilePath) {
        // Create a dataset for the histogram
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Create the histogram data by counting the CC values within specific ranges
        Map<String, Integer> histogram = new TreeMap<>();

        for (int cc : cyclomaticComplexityList) {
            String range = getRange(cc);
            histogram.put(range, histogram.getOrDefault(range, 0) + 1);
        }

        // Add data to the dataset
        for (Map.Entry<String, Integer> entry : histogram.entrySet()) {
            dataset.addValue(entry.getValue(), "Cyclomatic Complexity", entry.getKey());
        }

        // Create a bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Cyclomatic Complexity Histogram", // Chart title
                "Cyclomatic Complexity Range", // X-axis label
                "Count", // Y-axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot orientation
                true, // Include legend
                true, // Tooltips
                false // URLs
        );

        // Save the chart as a PNG image
        try {
            File chartFile = new File(histogramFilePath);
            org.jfree.chart.ChartUtils.saveChartAsPNG(chartFile, chart, 800, 600);
            System.out.println("Histogram saved as image to: " + histogramFilePath);
        } catch (IOException e) {
            System.err.println("Failed to save histogram image: " + e.getMessage());
        }
    }

    // Determine the range for a given CC value
    private String getRange(int cc) {
        if (cc <= 5) {
            return "1-5";
        } else if (cc <= 10) {
            return "6-10";
        } else if (cc <= 15) {
            return "11-15";
        } else {
            return "16+";
        }
    }

    // Main method for standalone execution
    public static void main(String[] args) {
        // Hardcoded source code path and report path
        String sourcePath = "C:\\Users\\Ethan\\Documents\\TPVV\\projectExamples\\commons-collections\\src";
        String reportPath = "C:\\Users\\Ethan\\Documents\\TPVV\\VV-ISTIC-TP2\\code\\Exercise5\\commons-collections_report.csv";
        String histogramPath = "C:\\Users\\Ethan\\Documents\\TPVV\\VV-ISTIC-TP2\\code\\Exercise5\\commons-collections_histogram.png";

        File file = new File(sourcePath);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("The provided path does not point to an existing, readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        CyclomaticComplexityPrinter printer = new CyclomaticComplexityPrinter();
        try {
            root.parse("", (localPath, absolutePath, result) -> {
                result.ifSuccessful(unit -> printer.visit(unit));  // Visit the CompilationUnit
                return SourceRoot.Callback.Result.DONT_SAVE;
            });
            printer.saveReport(reportPath);

            // Save the histogram as an image
            printer.saveHistogramAsImage(histogramPath);
        } catch (IOException e) {
            System.err.println("Failed to parse the source code: " + e.getMessage());
        }
    }

    // Visitor class to calculate Cyclomatic Complexity
    private static class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {
        private int complexity = 1; // Initialize with 1 for the method itself

        @Override
        public void visit(IfStmt stmt, Void arg) {
            complexity++; // Increment for each decision point (if statement)
            super.visit(stmt, arg);
        }

        @Override
        public void visit(ForStmt stmt, Void arg) {
            complexity++; // Increment for each loop (for statement)
            super.visit(stmt, arg);
        }

        @Override
        public void visit(WhileStmt stmt, Void arg) {
            complexity++; // Increment for each loop (while statement)
            super.visit(stmt, arg);
        }

        @Override
        public void visit(DoStmt stmt, Void arg) {
            complexity++; // Increment for each loop (do-while statement)
            super.visit(stmt, arg);
        }

        @Override
        public void visit(SwitchStmt stmt, Void arg) {
            complexity++; // Increment for each switch statement
            super.visit(stmt, arg);
        }

        @Override
        public void visit(CatchClause clause, Void arg) {
            complexity++; // Increment for each catch block
            super.visit(clause, arg);
        }

        public int getCyclomaticComplexity() {
            return complexity;
        }
    }
}
