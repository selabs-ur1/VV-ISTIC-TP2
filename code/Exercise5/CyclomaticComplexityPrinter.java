package fr.istic.vv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexityPrinter extends VoidVisitorWithDefaults<Void> {

    private List<MethodCC> results = new ArrayList<MethodCC>();

    @Override
    public void visit(CompilationUnit cu, Void arg) {
        cu.getTypes().forEach(type -> visitTypeDeclaration(type, cu));
        try {
            generateCsvReport(results, "output.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.visit(cu, arg);
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, CompilationUnit cu) {

        // Process methods
        declaration.getMethods().forEach(method -> visitMethod(method, cu));

        // Process nested types
        declaration.getMembers().stream()
                .filter(member -> member instanceof TypeDeclaration)
                .forEach(member -> visitTypeDeclaration((TypeDeclaration<?>) member, cu));
    }

    private void visitMethod(MethodDeclaration method, CompilationUnit cu) {
        int cc = calculateCyclomaticComplexity(method);
        String declaringClass = getDeclaringClassName(cu, method);
        results.add(new MethodCC(declaringClass, method.getNameAsString(),
                method.getParameters().toString(), cc));
    }

    private String getDeclaringClassName(CompilationUnit cu, MethodDeclaration method) {
        Optional<String> packageName = cu.getPackageDeclaration().map(pkg -> pkg.getNameAsString() + ".");
        String className = method.findAncestor(TypeDeclaration.class)
                .map(TypeDeclaration::getNameAsString)
                .orElse("Unknown");
        return packageName.orElse("") + className;
    }

    private static int calculateCyclomaticComplexity(MethodDeclaration method) {
        String body = method.getBody().map(Object::toString).orElse("");
        long decisionPoints = body.split("\\b(if|for|while|case|\\?\\:|catch)\\b").length - 1;
        return (int) decisionPoints + 1;
    }

    // Generate CSV Report
    private static void generateCsvReport(List<MethodCC> results, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("Class,Method,Parameters,Cyclomatic Complexity\n");
            for (MethodCC method : results) {
                writer.write(String.format("%s,%s,%s,%d\n",
                        method.getDeclaringClass(),
                        method.getMethodName(),
                        method.getParameters(),
                        method.getCc()));
            }
        }
    }

    // Data class for method analysis results
    private static class MethodCC {
        private final String declaringClass;
        private final String methodName;
        private final String parameters;
        private final int cc;

        public MethodCC(String declaringClass, String methodName, String parameters, int cc) {
            this.declaringClass = declaringClass;
            this.methodName = methodName;
            this.parameters = parameters;
            this.cc = cc;
        }

        public String getDeclaringClass() {
            return declaringClass;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getParameters() {
            return parameters;
        }

        public int getCc() {
            return cc;
        }
    }
}