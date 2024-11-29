package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class NoGetterAnalyzer extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        unit.getTypes().forEach(type -> type.accept(this, null));
    }

    private void analyzeType(TypeDeclaration<?> declaration) {
        // Gestion des fichiers avec try-with-resources
        try (FileWriter fileWriter = new FileWriter("report.txt", true);
             PrintWriter writer = new PrintWriter(fileWriter)) {

            declaration.getFields().forEach(field -> analyzeField(field, declaration, writer));

        } catch (IOException e) {
            throw new RuntimeException("Error while writing to the report file", e);
        }
    }

    private void analyzeField(FieldDeclaration field, TypeDeclaration<?> declaration, PrintWriter writer) {
        field.getVariables().forEach(variable -> {
            String varName = variable.getNameAsString();
            String expectedGetterName = constructGetterName(varName);

            boolean hasGetter = declaration.getMethods().stream()
                    .anyMatch(method -> method.getNameAsString().equals(expectedGetterName));

            writer.println(declaration.getFullyQualifiedName().orElse("[Unknown]") +
                    " " + varName + " : " + hasGetter);
        });
    }

    private String constructGetterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        analyzeType(declaration);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        analyzeType(declaration);
    }
}
