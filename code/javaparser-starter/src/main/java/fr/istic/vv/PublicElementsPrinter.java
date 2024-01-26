package fr.istic.vv;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    // Markdown file to write the output
    private static final String MARKDOWN_FILE = "output.md";

    public PublicElementsPrinter() {

        try {
            // Redirect System.out to the Markdown file
            System.setOut(new PrintStream(Files.newOutputStream(new File(MARKDOWN_FILE).toPath())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for (FieldDeclaration field : declaration.getFields()) {
            checkAndPrintField(field, declaration);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
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
        if(!declaration.isPublic()) return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

    /**
     * Checks if a private field has a corresponding public getter method,
     * and prints information if it doesn't.
     *
     * @param field           The field to check.
     * @param declaringClass  The class declaring the field.
     */
    private void checkAndPrintField(FieldDeclaration field, TypeDeclaration<?> declaringClass) {
        NodeList<Modifier> modifiers = field. getModifiers();
        boolean isPrivate = modifiers.contains(Modifier.privateModifier());

        if (isPrivate && !hasPublicGetter(field, declaringClass)) {
            System.out.println("  Private Field: " + field.getVariable(0).getNameAsString());
            System.out.println("    Declaring Class: " + declaringClass.getFullyQualifiedName().orElse("[Anonymous]"));
            System.out.println("    Package: " + declaringClass.findCompilationUnit().get().getPackageDeclaration().orElse(null));
        }
    }

    /**
     * Checks if a field has a corresponding public getter method in the declaring class.
     *
     * @param field           The field to check.
     * @param declaringClass  The class declaring the field.
     * @return True if a public getter method exists, false otherwise.
     */
    private boolean hasPublicGetter(FieldDeclaration field, TypeDeclaration<?> declaringClass) {
        String fieldName = field.getVariable(0).getNameAsString();
        String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        for (MethodDeclaration method : declaringClass.getMethods()) {
            if (method.isPublic() && method.getNameAsString().equals(getterMethodName)) {
                List<Parameter> parameters = method.getParameters();
                if (parameters.isEmpty() && method.getTypeAsString().equals(field.getElementType().asString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
