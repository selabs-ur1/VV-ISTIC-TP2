package fr.istic.vv;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CyclomaticComplexityCalculator extends VoidVisitorAdapter<Void> {

    private Map<String, MethodData> methodDataMap = new HashMap<>();

    private static class MethodData {
        String packageName;
        String className;
        String methodName;
        String parameterTypes;
        int cyclomaticComplexity;

        MethodData(String packageName, String className, String methodName, String parameterTypes) {
            this.packageName = packageName;
            this.className = className;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
        }
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, Void arg) {
        String packageName = methodDeclaration.findCompilationUnit()
                .flatMap(CompilationUnit::getPackageDeclaration)
                .map(packageDeclaration -> packageDeclaration.getNameAsString())
                .orElse("[No Package]");
        String className = methodDeclaration.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
        String methodName = methodDeclaration.getNameAsString();
        String parameterTypes = methodDeclaration.getParameters().toString();

        // Calculate Cyclomatic Complexity
        int cc = calculateCyclomaticComplexity(methodDeclaration.getBody().orElse(null));

        // Save data for reporting
        MethodData methodData = new MethodData(packageName, className, methodName, parameterTypes);
        methodData.cyclomaticComplexity = cc;
        methodDataMap.put(methodName, methodData);

        super.visit(methodDeclaration, arg);
    }

    private int calculateCyclomaticComplexity(Statement statement) {
        if (statement == null) {
            return 1; // default complexity for empty method
        }

        CyclomaticComplexityVisitor ccVisitor = new CyclomaticComplexityVisitor();
        statement.accept(ccVisitor, null);

        return ccVisitor.getCyclomaticComplexity();
    }

    public void generateReport(String outputPath) throws IOException {
        // Write method-level details to a CSV file
        try (FileWriter writer = new FileWriter(outputPath + "/method_report.csv")) {
            writer.write("Package,Class,Method,Parameters,Cyclomatic Complexity\n");
            for (MethodData methodData : methodDataMap.values()) {
                writer.write(String.format("%s,%s,%s,%s,%d\n",
                        methodData.packageName,
                        methodData.className,
                        methodData.methodName,
                        methodData.parameterTypes,
                        methodData.cyclomaticComplexity));
            }
        }
    }

    public void generateHistogram(String outputPath) throws IOException {
        // Write histogram data to a CSV file
        try (FileWriter histogramWriter = new FileWriter(outputPath + "/histogram_data.csv")) {
            histogramWriter.write("Cyclomatic Complexity,Number of Methods\n");
            Map<Integer, Integer> histogram = new HashMap<>();
            for (MethodData methodData : methodDataMap.values()) {
                int cc = methodData.cyclomaticComplexity;
                histogram.put(cc, histogram.getOrDefault(cc, 0) + 1);
            }
            for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
                histogramWriter.write(entry.getKey() + ":");
                for (int i = 0; i < entry.getValue(); i++) {
                    histogramWriter.write("*");
                }
                histogramWriter.write("\n");
            }
        }
    }

    class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {

        private int cyclomaticComplexity = 1;

        public int getCyclomaticComplexity() {
            return cyclomaticComplexity;
        }

        @Override
        public void visit(IfStmt n, Void arg) {
            cyclomaticComplexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(WhileStmt n, Void arg) {
            cyclomaticComplexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(ForStmt n, Void arg) {
            cyclomaticComplexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(ForEachStmt n, Void arg) {
            cyclomaticComplexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(SwitchStmt n, Void arg) {
            cyclomaticComplexity += n.getEntries().size();
            super.visit(n, arg);
        }

        @Override
        public void visit(CatchClause n, Void arg) {
            cyclomaticComplexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(BinaryExpr n, Void arg) {
            switch (n.getOperator()) {
                case AND:
                case OR:
                    cyclomaticComplexity++;
                    break;
                default:
                    // Do nothing for other binary operators
            }
            super.visit(n, arg);
        }

        @Override
        public void visit(UnaryExpr n, Void arg) {
            switch (n.getOperator()) {
                case LOGICAL_COMPLEMENT:
                    cyclomaticComplexity++;
                    break;
                default:
                    // Do nothing for other unary operators
            }
            super.visit(n, arg);
        }

        @Override
        public void visit(ConditionalExpr n, Void arg) {
            cyclomaticComplexity += 2;
            super.visit(n, arg);
        }
    }
}