package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CyclomaticComplexityCalculator {

    private final List<MethodCCInfo> methodCCInfos = new ArrayList<>();

    public void analyze(CompilationUnit unit) {
        String packageName = unit.getPackageDeclaration()
                .map(pd -> pd.getName().asString())
                .orElse("[Default Package]");

        unit.findAll(MethodDeclaration.class).forEach(method -> {
            int complexity = calculateCyclomaticComplexity(method);
            String className = method.findAncestor(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration.class)
                    .map(NodeWithSimpleName::getNameAsString)
                    .orElse("[Anonymous Class]");
            String methodName = method.getNameAsString();
            String parameters = method.getParameters().stream()
                    .map(param -> param.getType().toString())
                    .reduce((param1, param2) -> param1 + " " + param2)
                    .orElse("No Parameters");

            methodCCInfos.add(new MethodCCInfo(packageName, className, methodName, parameters, complexity));
        });
    }

    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        if (method.getBody().isEmpty()) {
            return 1;
        }

        CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        visitor.visit(method.getBody().get(), null);
        return visitor.getCyclomaticComplexity();
    }

    public void generateReport(String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("Package,Class,Method,Parameters,Cyclomatic Complexity\n");
            for (MethodCCInfo info : methodCCInfos) {
                writer.write(info.toString() + "\n");
            }
        }
    }

    static class MethodCCInfo {
        String packageName;
        String className;
        String methodName;
        String parameters;
        int cyclomaticComplexity;

        MethodCCInfo(String packageName, String className, String methodName, String parameters, int cyclomaticComplexity) {
            this.packageName = packageName;
            this.className = className;
            this.methodName = methodName;
            this.parameters = parameters;
            this.cyclomaticComplexity = cyclomaticComplexity;
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%d",
                    packageName, className, methodName, parameters, cyclomaticComplexity);
        }
    }

    private static class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {
        private int complexity = 1;

        @Override
        public void visit(IfStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(ForStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(ForEachStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(WhileStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(DoStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(SwitchStmt n, Void arg) {
            complexity += n.getEntries().size();
            super.visit(n, arg);
        }

        @Override
        public void visit(CatchClause n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        public int getCyclomaticComplexity() {
            return complexity;
        }
    }
}