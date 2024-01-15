# Code of your exercise

```java
package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        CCVisitor ccVisitor = new CCVisitor();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(ccVisitor, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        String csvFilePath = "CC_Report.csv";
        generateCSVReport(ccVisitor.getMethodCCMap(), csvFilePath);

        generateHistogram(ccVisitor.getCCValues());
    }

    private static void generateCSVReport(Map<String, Integer> methodCCMap, String csvFilePath) {
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            writer.append("Package,Class,Method,Parameters,CC\n");

            for (Map.Entry<String, Integer> entry : methodCCMap.entrySet()) {
                String[] parts = entry.getKey().split(",");
                writer.append(parts[0]).append(",").append(parts[1]).append(",")
                        .append(parts[2]).append(",").append(parts[3]).append(",")
                        .append(entry.getValue().toString()).append("\n");
            }

            System.out.println("CSV report generated: " + csvFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateHistogram(List<Integer> ccValues) {
        Map<Integer, Integer> histogram = new HashMap<>();
        for (int value : ccValues) {
            histogram.put(value, histogram.getOrDefault(value, 0) + 1);
        }

        System.out.println("Cyclomatic Complexity (CC) Histogram:");
        for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
            System.out.println("CC = " + entry.getKey() + ": " + entry.getValue() + " methods");
        }
    }
    
}

class CCVisitor extends VoidVisitorAdapter<Void> {
    private Map<String, Integer> methodCCMap = new HashMap<>();
    private List<Integer> ccValues = new ArrayList<>();

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;

        String packageName = declaration.findCompilationUnit().map(CompilationUnit::getPackageDeclaration)
                .map(pd -> pd.getName().asString()).orElse("");
        String className = declaration.findCompilationUnit().map(CompilationUnit::getPrimaryType)
                .map(TypeDeclaration::getNameAsString).orElse("");
        String methodName = declaration.getNameAsString();
        String parameters = declaration.getParameters().toString();
        int cc = calculateCC(declaration);
        String key = packageName + "," + className + "," + methodName + "," + parameters;

        methodCCMap.put(key, cc);
        ccValues.add(cc);
    }

    public Map<String, Integer> getMethodCCMap() {
        return methodCCMap;
    }

    public List<Integer> getCCValues() {
        return ccValues;
    }

    private int calculateCC(MethodDeclaration method) {
        CCCounterVisitor ccCounter = new CCCounterVisitor();
        method.accept(ccCounter, null);
        return ccCounter.getCC();
    }
}

class CCCounterVisitor extends VoidVisitorAdapter<Void> {
    private int cc = 1;

    @Override
    public void visit(IfStmt n, Void arg) {
        cc++;
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        cc++;
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        cc++;
        super.visit(n, arg);
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        cc++;
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchEntry n, Void arg) {
        cc++;
        super.visit(n, arg);
    }

    public int getCC() {
        return cc;
    }
}
```