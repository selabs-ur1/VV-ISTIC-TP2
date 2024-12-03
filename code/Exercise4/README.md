# Code of your exercise

Put here all the code created for this exercise


```
package fr.istic.vv.fields;

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
```
```
package fr.istic.vv.fields;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MarkdownGenerator {

    public static void generateMarkdownReport(String filePath, Map<String, List<String>> fields) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("# Private fields without getters in public class\n\n");

            if (fields.isEmpty()) {
                writer.write("# No Private fields without getters in public class\n");
                return;
            }

            for (Map.Entry<String, List<String>> entry : fields.entrySet()) {
                String className = entry.getKey();
                List<String> fieldList = entry.getValue();

                writer.write("## Class: " + className + "\n");
                for (String field : fieldList) {
                    writer.write("- `" + field + "`\n");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing Markdown report: " + e.getMessage());
        }
    }
}
```
```
package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;
import fr.istic.vv.cc.CyclomaticComplexityCalculator;
import fr.istic.vv.cc.CyclomaticComplexityReport;
import fr.istic.vv.fields.MarkdownGenerator;
import fr.istic.vv.fields.PrivateElementWithoutGetterPrinter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        PrivateElementWithoutGetterPrinter printer = new PrivateElementWithoutGetterPrinter(new ArrayList<>(),new HashMap<>());
        CyclomaticComplexityCalculator calculator = new CyclomaticComplexityCalculator();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            result.ifSuccessful(compilationUnit -> compilationUnit.accept(calculator, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        MarkdownGenerator.generateMarkdownReport(args[1], printer.getFieldsWithoutGetters());
        CyclomaticComplexityReport.generateReport(args[2],calculator.getMethodComplexities());

    }


}
```
