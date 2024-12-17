package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.List;

// This class visits a compilation unit and
// generates a Markdown string listing classes or interfaces with private fields without getters
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private final StringBuilder markdownBuilder = new StringBuilder();
    private boolean hasGeneralTitle = false; // Ensure the general title is added only once

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        if (!hasGeneralTitle) {
            markdownBuilder.append("# ")
                    .append("Attributes with no getter")
                    .append("\n\n")
                    .append("---")
                    .append("\n\n");
            hasGeneralTitle = true;
        }
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;

        boolean hasPrivateFieldsWithoutGetters = false;

        // Check for private fields without getters
        for (FieldDeclaration fieldDeclaration : declaration.getFields()) {
            if (fieldDeclaration.isPrivate() && !containGetter(fieldDeclaration.getVariable(0).getNameAsString(), declaration.getMethods())) {
                if (!hasPrivateFieldsWithoutGetters) {
                    String typeLink = generateMarkdownLink(declaration);
                    markdownBuilder.append("## ")
                            .append(declaration.getName())
                            .append(" - ")
                            .append(typeLink).append("\n\n")
                            .append("### Fields without getters\n\n");
                    hasPrivateFieldsWithoutGetters = true;
                }
                markdownBuilder.append("- `")
                        .append(fieldDeclaration.getVariable(0).getNameAsString())
                        .append("`\n");
            }
        }

        // Process nested types
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration) {
                member.accept(this, arg);
            }
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

    /**
     * Checks whether a getter method corresponding to the specified field name exists in the given list of method declarations.
     *
     * @param fieldName the name of the field for which the getter is checked.
     *                  The expected getter name is constructed as "get" followed by the capitalized field name.
     * @param methods   the list of method declarations to search for the getter method.
     * @return {@code true} if a getter method with the expected name exists in the list; {@code false} otherwise.
     */
    private boolean containGetter(String fieldName, List<MethodDeclaration> methods) {
        String getterName = "get" + capitalized(fieldName);
        return methods.stream().anyMatch(methodDeclaration -> methodDeclaration.getName().toString().equals(getterName));
    }

    /**
     * Capitalizes the first letter of the given string.
     *
     * @param s the string to capitalize.
     *          If the string is empty, it will be returned as-is.
     * @return the string with its first letter converted to uppercase, or the same string if it is empty.
     */
    private String capitalized(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Generates a Markdown link for a type declaration.
     *
     * @param declaration the type declaration for which the link is generated.
     * @return a Markdown-formatted link to the type's source file, or its name if the path is unavailable.
     */
    private String generateMarkdownLink(TypeDeclaration<?> declaration) {
        // Determine the file path or relative URL for the source file
        String filePath = declaration.findCompilationUnit()
                .flatMap(CompilationUnit::getStorage)
                .map(storage -> storage.getPath().toString())
                .orElse("[Unknown Path]");
        return declaration.getFullyQualifiedName()
                .map(name -> "[" + name + "](" + filePath + ")")
                .orElse("[Anonymous]");
    }

    /**
     * Returns the generated markdown content as a string.
     *
     * @return the generated markdown content.
     */
    public String getMarkdown() {
        return markdownBuilder.toString();
    }
}
