package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComplexityReportGenerator extends VoidVisitorAdapter<Void> {
    private List<MethodComplexityInfo> complexityInfos = new ArrayList<>();
    private CyclomaticComplexityCalculator calculator = new CyclomaticComplexityCalculator();
    private String currentPackageName = "";
    private String currentClassName = "";

    public void visit(CompilationUnit cu, Void arg) {
        // Extract package name
        cu.getPackageDeclaration().ifPresent(pkg ->
                currentPackageName = pkg.getName().asString());

        // Visit all types in the compilation unit
        super.visit(cu, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        currentClassName = n.getName().asString();
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        // Skip non-public methods or abstract methods
        if (!n.isPublic() || n.isAbstract()) {
            return;
        }

        int complexity = calculator.calculateComplexity(n);

        MethodComplexityInfo info = new MethodComplexityInfo(
                currentPackageName,
                currentClassName,
                n.getName().asString(),
                getParameterTypes(n),
                complexity
        );

        complexityInfos.add(info);
    }

    private String getParameterTypes(MethodDeclaration method) {
        return method.getParameters().stream()
                .map(p -> p.getType().asString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("void");
    }

    public void generateMarkdownReport(String outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("# Cyclomatic Complexity Report\n\n");
            writer.write("| Package | Class | Method | Parameters | Complexity |\n");
            writer.write("|---------|-------|--------|------------|------------|\n");

            for (MethodComplexityInfo info : complexityInfos) {
                writer.write(String.format("| %s | %s | %s | %s | %d |\n",
                        info.packageName,
                        info.className,
                        info.methodName,
                        info.parameters,
                        info.complexity));
            }
        }
    }

    public List<MethodComplexityInfo> getComplexityInfos() {
        return complexityInfos;
    }

    public static class MethodComplexityInfo {
        public final String packageName;
        public final String className;
        public final String methodName;
        public final String parameters;
        public final int complexity;

        public MethodComplexityInfo(String packageName, String className,
                                    String methodName, String parameters, int complexity) {
            this.packageName = packageName;
            this.className = className;
            this.methodName = methodName;
            this.parameters = parameters;
            this.complexity = complexity;
        }
    }
}