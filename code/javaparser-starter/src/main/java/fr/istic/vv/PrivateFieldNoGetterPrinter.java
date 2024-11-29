package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.HashMap;
import java.util.Map;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PrivateFieldNoGetterPrinter extends VoidVisitorWithDefaults<Void> {

    private final Map<String, MethodDeclaration> methods = new HashMap<>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));

        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        for (FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }

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
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;
        methods.put(declaration.getNameAsString(), declaration);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPublic()) return;

        String fieldName = declaration.getVariable(0).getName().asString();
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (!methods.containsKey(getterName)) {
            System.out.println("private field " + fieldName + " has no public getter");
        }
    }
}
