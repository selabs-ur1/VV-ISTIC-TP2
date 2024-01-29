package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.*;
import java.util.List;

public class GetterPrinter extends VoidVisitorAdapter<Void> {

    FileWriter writer;

    public GetterPrinter(FileWriter writer) {
        this.writer = writer;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        if (!classDeclaration.isPublic()) {
            // Ignore non-public classes
            return;
        }

        String className = classDeclaration.getNameAsString();
        String packageName = classDeclaration.findCompilationUnit()
                .flatMap(CompilationUnit::getPackageDeclaration)
                .map(packageDeclaration -> packageDeclaration.getNameAsString())
                .orElse("[No Package]");

        // Visit fields
        for (FieldDeclaration fieldDeclaration : classDeclaration.getFields()) {
            if (fieldDeclaration.isPrivate()) {
                checkGetter(classDeclaration, fieldDeclaration, className, packageName);
            }
        }

        super.visit(classDeclaration, arg);
    }

    private void checkGetter(ClassOrInterfaceDeclaration classDeclaration, FieldDeclaration fieldDeclaration,
                             String className, String packageName) {
        String fieldName = fieldDeclaration.getVariable(0).getNameAsString();
        String getterMethodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

        // Check if there is a public method with the expected getter name
        List<MethodDeclaration> methods = classDeclaration.getMethodsByName(getterMethodName);
        for (MethodDeclaration method : methods) {
            if (method.isPublic() && method.getParameters().isEmpty()) {
                // Found a public getter, no need to report
                return;
            }
        }

        try {
            writer.write("Field without public getter:\n");
            writer.write("  Field: " + fieldName + "\n");
            writer.write("  Class: " + className + "\n");
            writer.write("  Package: " + packageName + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
