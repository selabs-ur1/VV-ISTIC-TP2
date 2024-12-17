package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NoGetterPrinter extends PublicElementsPrinter {

    private final StringBuilder report = new StringBuilder();

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;
        super.visit(declaration, arg);

        // Traverse up to find the enclosing CompilationUnit
        CompilationUnit parentUnit = declaration.findAncestor(CompilationUnit.class).orElse(null);
        String packageName = parentUnit != null && parentUnit.getPackageDeclaration().isPresent()
                ? parentUnit.getPackageDeclaration().get().getNameAsString()
                : "[No Package]";

        List<FieldDeclaration> fields = declaration.getFields();
        List<MethodDeclaration> methods = declaration.getMethods();

        for (FieldDeclaration field : fields) {
            if (field.isPrivate()) {
                for (VariableDeclarator variable : field.getVariables()) {
                    String fieldName = variable.getNameAsString();
                    if (!hasPublicGetter(fieldName, methods)) {
                        String className = declaration.getFullyQualifiedName().orElse("[Anonymous]");
                        report.append(String.format("Field: %s, Class: %s, Package: %s%n", fieldName, className, packageName));
                    }
                }
            }
        }
    }

    // Check if a public getter exists for a field
    private boolean hasPublicGetter(String fieldName, List<MethodDeclaration> methods) {
        String getterName = "get" + capitalize(fieldName);
        for (MethodDeclaration method : methods) {
            if (method.isPublic() &&
                    method.getNameAsString().equals(getterName) &&
                    method.getParameters().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Capitalize the first letter of a string
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Save the report to a file
    public void saveReport(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(report.toString());
            System.out.println("Report saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save report: " + e.getMessage());
        }
    }

    // Main method for standalone execution
    public static void main(String[] args) {
        // Hardcoded source code path
        String sourcePath = "C:\\Users\\Ethan\\Documents\\TPVV\\projectExamples\\commons-math\\src";
        String reportPath = "C:\\Users\\Ethan\\Documents\\TPVV\\VV-ISTIC-TP2\\code\\Exercise4\\report.txt";

        File file = new File(sourcePath);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("The provided path does not point to an existing, readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        NoGetterPrinter printer = new NoGetterPrinter();
        try {
            root.parse("", (localPath, absolutePath, result) -> {
                result.ifSuccessful(unit -> unit.accept(printer, null));
                return SourceRoot.Callback.Result.DONT_SAVE;
            });
            printer.saveReport(reportPath);
        } catch (IOException e) {
            System.err.println("Failed to parse the source code: " + e.getMessage());
        }
    }
}
