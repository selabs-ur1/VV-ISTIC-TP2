package fr.istic.vv;

import java.util.ArrayList;
import java.util.List;

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
public class NoGettersPrinter extends VoidVisitorWithDefaults<Void> {

    private List<String> methods = new ArrayList<String>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            visitTypeDeclaration(type, arg);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }

        // Variables
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof FieldDeclaration)
                visit((FieldDeclaration) member, arg);
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
        methods.add(declaration.getDeclarationAsString(true, true));
    }

    @Override
    // Ajout de cette méthode pour afficher les champs
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPublic())
            return;

        VariableDeclarator variable = declaration.getVariables().get(0);
        String fieldName = variable.getNameAsString();

        if (!isGetterForField(fieldName)) {

            ClassOrInterfaceDeclaration classDeclaration = variable.findAncestor(ClassOrInterfaceDeclaration.class)
                    .orElse(null);
            String className = (classDeclaration != null) ? classDeclaration.getNameAsString() : "UnknownClass";

            CompilationUnit unit = variable.findAncestor(CompilationUnit.class).orElse(null);
            String packageName = "UnknownPackage";
            if (unit != null && unit.getPackageDeclaration().isPresent()) {
                packageName = unit.getPackageDeclaration().get().getNameAsString();
            }

            System.out.println(String.format("%s %s (Declared in: %s, Package: %s)",
                    variable.getTypeAsString(), variable.getNameAsString(), className, packageName));
        }
    }

    private boolean isGetterForField(String fieldName) {
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (String method : methods) {
            if (method.contains(getterName)) {
                return true;
            }
        }
        return false;
    }
}