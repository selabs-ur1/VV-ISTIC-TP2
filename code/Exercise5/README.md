# Code of your exercise

```java
package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        CyclomaticComplexityCalculator calculator = new CyclomaticComplexityCalculator();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(calculator, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // Generate CC report
        Map<String, Integer> ccMap = calculator.getCCMap();
        generateCCReport(ccMap);

        // Generate CC histogram
        generateCCHistogram(ccMap);
    }

    private static void generateCCReport(Map<String, Integer> ccMap) {
        // Generate and print CC report (you can customize the output format)
        System.out.println("CC Report:");
        System.out.printf("%-40s %-30s %-30s %-10s%n", "Package/Class", "Method", "Parameters", "CC");
        for (Map.Entry<String, Integer> entry : ccMap.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            String packageName = parts[0];
            String className = parts[1];
            String methodName = parts[2];
            String parameters = parts[3];
            int cc = entry.getValue();
            System.out.printf("%-40s %-30s %-30s %-10d%n", packageName + "." + className, methodName, parameters, cc);
        }
    }

    private static void generateCCHistogram(Map<String, Integer> ccMap) {
        // Generate and print CC histogram (you can customize the output format)
        System.out.println("\nCC Histogram:");
        System.out.println("CC \t Frequency");
        Map<Integer, Integer> histogram = new HashMap<>();
        for (int cc : ccMap.values()) {
            histogram.put(cc, histogram.getOrDefault(cc, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
            System.out.printf("%d \t %d%n", entry.getKey(), entry.getValue());
        }
    }
}

class CyclomaticComplexityCalculator extends VoidVisitorWithDefaults<Void> {

    private Map<String, Integer> ccMap;

    public CyclomaticComplexityCalculator() {
        this.ccMap = new HashMap<>();
    }

    public Map<String, Integer> getCCMap() {
        return ccMap;
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, Void arg) {
        if (methodDeclaration.isPublic()) {
            String packageName = methodDeclaration.findCompilationUnit().map(CompilationUnit::getPackageDeclaration)
                    .map(PackageDeclaration::getNameAsString).orElse("");
            String className = methodDeclaration.findCompilationUnit().map(CompilationUnit::getPrimaryTypeName)
                    .orElse("[Anonymous]");
            String methodName = methodDeclaration.getNameAsString();
            String parameters = methodDeclaration.getParameters().toString();

            int cc = calculateCyclomaticComplexity(methodDeclaration);
            String key = packageName + "|" + className + "|" + methodName + "|" + parameters;
            ccMap.put(key, cc);
        }
    }

    private int calculateCyclomaticComplexity(MethodDeclaration methodDeclaration) {
        CCVisitor ccVisitor = new CCVisitor();
        methodDeclaration.accept(ccVisitor, null);
        return ccVisitor.getCC();
    }

    private static class CCVisitor extends VoidVisitorWithDefaults<Void> {
        private int cc;

        public int getCC() {
            return cc + 1; // Adding 1 to represent the starting point of the method
        }

        @Override
        public void visit(IfStmt n, Void arg) {
            cc++;
            super.visit(n, arg);
        }

        @Override
        public void visit(WhileStmt n, Void arg) {
            cc++;
            super.visit(n, arg);
        }
    }
}
```