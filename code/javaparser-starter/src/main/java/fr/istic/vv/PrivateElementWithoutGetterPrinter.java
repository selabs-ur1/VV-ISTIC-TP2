package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrivateElementWithoutGetterPrinter extends VoidVisitorWithDefaults<Void> {

    private final List<String> getters;
    private final Map<String, List<String>> fields;

    public PrivateElementWithoutGetterPrinter(List<String> getters, Map<String, List<String>> fields) {
        this.getters = getters;
        this.fields = fields;
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        unit.getTypes().forEach(type -> type.accept(this, arg));
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;

        String className = declaration.getFullyQualifiedName().orElse("[Anonymous]");

        declaration.getMethods().stream()
                .filter(method -> method.isPublic() && method.getNameAsString().startsWith("get"))
                .forEach(method -> getters.add(method.getNameAsString().toLowerCase()));

        declaration.getFields().stream()
                .filter(FieldDeclaration::isPrivate)
                .forEach(field -> field.getVariables().forEach(variable -> {
                    String fieldName = variable.getNameAsString();
                    String getterName = "get" + capitalize(fieldName);

                    if (!getters.contains(getterName.toLowerCase())) {
                        fields.computeIfAbsent(className, k -> new ArrayList<>()).add(fieldName);
                    }
                }));
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Map<String, List<String>> getFieldsWithoutGetters() {
        return fields;
    }
}
