package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class NoGetterPrinter extends VoidVisitorWithDefaults<Void> {
    private ArrayList<VariableDeclarator> privateFields = new ArrayList<>();
    private ArrayList<MethodDeclaration> methods = new ArrayList<>();
    private Map<String, ArrayList<String>> privateFieldsReported = new HashMap<>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        // txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"))) {
            writer.write("List of private fields without public getter:");
            writer.newLine();
            writer.newLine();

            for (String key : privateFieldsReported.keySet()) {
                ArrayList<String> values = privateFieldsReported.get(key);

                // Write class name to file
                writer.write("ClassName: " + key);
                writer.newLine();

                // Write list of private fields to file
                writer.write("Private fields: ");
                for (String value : values) {
                    writer.write(value + " | ");
                }

                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic())
            return;

        // add all private fields to privateFields
        for (FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }

        // add all methods to methods
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        // add private fields without getters to privateFieldsReported
        ArrayList<String> fieldsWithoutPublicGetter = fieldsWithoutPublicGetter(declaration.getNameAsString());

        if(!fieldsWithoutPublicGetter.isEmpty())
            privateFieldsReported.put(declaration.getNameAsString(), fieldsWithoutPublicGetter);

        privateFields.clear();
        methods.clear();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPublic())
            return;

        privateFields.addAll(declaration.getVariables());
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic())
            return;

        methods.add(declaration);
    }

    private ArrayList<String> fieldsWithoutPublicGetter(String className) {
        ArrayList<String> privateFieldsReported = new ArrayList<>();

        for (VariableDeclarator privateField : privateFields) {
            boolean hasGetter = false;
            String fieldName = privateField.getNameAsString();

            for (MethodDeclaration methodDeclaration : methods) {
                if (isGetter(methodDeclaration, fieldName))
                    hasGetter = true;
            }

            if (!hasGetter)
                privateFieldsReported.add(fieldName);
        }

        return privateFieldsReported;
    }

    private boolean isGetter(MethodDeclaration declaration, String fieldName) {
        // A field has a public getter if, in the same class, there is a public method
        // that simply returns the value of the field and whose name is
        // get<name-of-the-field>.

        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (!declaration.getNameAsString().equals(getterName))
            return false;

        if (!declaration.getBody().isPresent())
            return false;

        BlockStmt body = declaration.getBody().get();
        if (body.getStatements().size() != 1)
            return false;

        Statement statement = body.getStatement(0);
        if (!(statement.isReturnStmt()))
            return false;

        return true;
    }

}
