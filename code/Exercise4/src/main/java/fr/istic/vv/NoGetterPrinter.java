package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class NoGetterPrinter extends VoidVisitorWithDefaults<Void> {

    private StringBuilder html = new StringBuilder();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;

        // Collect all fields and methods
        List<FieldDeclaration> privateFields = declaration.getFields()
                .stream()
                .filter(FieldDeclaration::isPrivate)
                .collect(Collectors.toList());

        List<MethodDeclaration> publicMethods = declaration.getMethods()
                .stream()
                .filter(MethodDeclaration::isPublic)
                .collect(Collectors.toList());

        List<String> tableLines = new ArrayList<>();

        // Check which private fields lack a getter
        for (FieldDeclaration field : privateFields) {
            for (VariableDeclarator variable : field.getVariables()) {
                String fieldName = variable.getNameAsString();
                String getterName = "get" + capitalize(fieldName);

                boolean hasGetter = publicMethods.stream()
                        .anyMatch(method -> method.getNameAsString().equals(getterName) &&
                                method.getParameters().isEmpty() &&
                                method.getType().equals(variable.getType()));

                if (!hasGetter) {
                    tableLines.add("<tr><td>"+fieldName + "</td><td>"+variable.getType()+ "</td></tr>");
                }
            }
        }

        if (!tableLines.isEmpty()) {
            html.append("<h2>").append(declaration.getFullyQualifiedName().orElse("")).append("</h2>\n");
            html.append("<table border='1'>");
            html.append("<tr><th>Field</th><th>Type</th></tr>");
            html.append(tableLines.stream().collect(Collectors.joining("\n")));
            html.append("</table>");
        }

        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration) {
                member.accept(this, arg);
            }
        }
    }

    public String generateHTMLTable() {
        return html.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }
}
