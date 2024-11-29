package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class visits a compilation unit and collects information about fields that are missing getters
 */
public class PrivateFieldNoGetterPrinter extends VoidVisitorWithDefaults<Void> {

    public final List<FieldInfo> fields = new ArrayList<>();
    public final List<String> publicMethodNames = new ArrayList<>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        String packageName = unit.getPackageDeclaration()
                .map(pd -> pd.getName().toString())
                .orElse("[Default Package]");
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(new TypeVisitor(packageName), null);
        }
    }

    /**
     * Generates a CSV report with the fields that are missing getters
     *
     * @param outputPath - the path to the output file
     * @throws IOException
     */
    public void generateReport(String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("Field Name,Declaring Class,Package Name,Getter Missing\n");
            for (FieldInfo field : fields) {
                writer.write(field.toString() + "\n");
            }
        }
    }

    /**
     * Represents a field in a class
     */
    static class FieldInfo {
        String fieldName;
        String declaringClass;
        String packageName;
        boolean getterMissing;

        FieldInfo(String fieldName, String declaringClass, String packageName, boolean getterMissing) {
            this.fieldName = fieldName;
            this.declaringClass = declaringClass;
            this.packageName = packageName;
            this.getterMissing = getterMissing;
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s", fieldName, declaringClass, packageName, getterMissing ? "Yes" : "No");
        }
    }

    /**
     * Visits a type declaration and collects information about its fields
     */
    private class TypeVisitor extends VoidVisitorWithDefaults<Void> {
        private final String packageName;

        TypeVisitor(String packageName) {
            this.packageName = packageName;
        }

        /**
         * Visits a class or interface declaration and collects information about its fields
         *
         * @param declaration - the class or interface declaration
         * @param arg         - unused
         */
        @Override
        public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
            String className = declaration.getNameAsString();

            for (MethodDeclaration method : declaration.getMethods()) {
                if (method.isPublic()) {
                    publicMethodNames.add(method.getNameAsString());
                }
            }

            for (FieldDeclaration field : declaration.getFields()) {
                for (VariableDeclarator variable : field.getVariables()) {
                    String fieldName = variable.getNameAsString();
                    String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    boolean getterMissing = field.isPrivate() && !publicMethodNames.contains(getterName);
                    fields.add(new FieldInfo(fieldName, className, packageName, getterMissing));
                }
            }
        }
    }
}