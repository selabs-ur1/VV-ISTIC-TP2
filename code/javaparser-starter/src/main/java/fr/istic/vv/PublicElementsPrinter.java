package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    List<FieldDeclaration> privateField = new ArrayList<>();
    private String currentClassName;
    private String currentPackageName;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        currentPackageName = unit.getPackageDeclaration()
                .map(pkg -> pkg.getNameAsString())
                .orElse("(default package)");
        for (TypeDeclaration<?> type : unit.getTypes()) {
            // type.accept(this, null);
            // recuperer le package du fichier ici
            visitTypeDeclaration(type, arg);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        // stocker la classe a cet endroit la pour l'utiliser
        if (!declaration.isPublic())
            return;
        currentClassName = declaration.getNameAsString();
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for (FieldDeclaration field : declaration.getFields()) {
            if (field.isPrivate()) {
                privateField.add(field);
            }
        }

        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        displayReportMissingGetter();
        privateField.clear();
        // Printing nested types in the top level
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
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
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
        checkIsGetterForPrivatField(declaration);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPrivate()) {

        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void checkIsGetterForPrivatField(MethodDeclaration method) {
        Iterator<FieldDeclaration> iterator = privateField.iterator();
        while (iterator.hasNext()) {
            FieldDeclaration field = iterator.next();
            if (isGetterForField(method, field.getVariable(0).getNameAsString())) {
                iterator.remove();
            }
        }
    }

    private boolean isGetterForField(MethodDeclaration method, String fieldName) {
        String getterName = "get" + capitalize(fieldName);

        return (method.getNameAsString().equals(getterName))
                && method.getParameters().isEmpty()
                && !method.getType().isVoidType();
    }

    private void displayReportMissingGetter() {
        StringBuilder reportBuilder = new StringBuilder("Missing getter : \n");
        for (FieldDeclaration field : privateField) {
            reportBuilder.append(field.getVariable(0).getNameAsString());
            reportBuilder.append("\t");
            reportBuilder.append(currentClassName);
            reportBuilder.append("\t");
            reportBuilder.append(currentPackageName);
            reportBuilder.append("\n");
        }
        Path reportFilePath = Paths.get("missing-getters-report.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(reportFilePath)) {
            writer.write(reportBuilder.toString());
        } catch (IOException e) {
            System.err.println("Failed to write the report to file: " + e.getMessage());
        }
        System.out.println(reportBuilder.toString());

    }

}
