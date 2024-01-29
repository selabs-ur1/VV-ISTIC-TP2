# Code of your exercise

We can achieve this by using the javaparser library as a direct dependency in our maven project.

It helps us parsing and analyzing the source code in order to get the attributes without actuel getters.

```java
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FieldGetterAnalyzer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java FieldGetterAnalyzer <path-to-source>");
            System.exit(1);
        }

        String sourcePath = args[0];
        analyzeSourceCode(sourcePath);
    }

    private static void analyzeSourceCode(String sourcePath) {
        File sourceFolder = new File(sourcePath);
        List<FieldInfo> fieldsWithoutGetter = new ArrayList<>();

        if (sourceFolder.isDirectory()) {
            for (File file : sourceFolder.listFiles((dir, name) -> name.endsWith(".java"))) {
                try {
                    CompilationUnit compilationUnit = StaticJavaParser.parse(file);

                    compilationUnit.accept(new VoidVisitorAdapter<Void>() {
                        @Override
                        public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
                            String className = classDeclaration.getNameAsString();
                            String packageName = compilationUnit.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("");

                            classDeclaration.getFields().forEach(field -> {
                                if (isPrivate(field) && !hasPublicGetter(classDeclaration, field)) {
                                    fieldsWithoutGetter.add(new FieldInfo(field.getVariable(0).getNameAsString(), className, packageName));
                                }
                            });
                        }
                    }, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Print or save the report
            for (FieldInfo fieldInfo : fieldsWithoutGetter) {
                System.out.println("Field: " + fieldInfo.getFieldName() +
                        ", Class: " + fieldInfo.getClassName() +
                        ", Package: " + fieldInfo.getPackageName());
            }
        } else {
            System.out.println("Invalid source code directory");
        }
    }

    private static boolean isPrivate(FieldDeclaration field) {
        return field.isPrivate();
    }

    private static boolean hasPublicGetter(ClassOrInterfaceDeclaration classDeclaration, FieldDeclaration field) {
        String fieldName = field.getVariable(0).getNameAsString();
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        return classDeclaration.getMethods().stream()
                .filter(method -> method.getNameAsString().equals(getterName))
                .anyMatch(method -> method.isPublic() && method.getParameters().isEmpty() &&
                        method.getType().equals(field.getCommonType()));
    }

    private static class FieldInfo {
        private final String fieldName;
        private final String className;
        private final String packageName;

        public FieldInfo(String fieldName, String className, String packageName) {
            this.fieldName = fieldName;
            this.className = className;
            this.packageName = packageName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getClassName() {
            return className;
        }

        public String getPackageName() {
            return packageName;
        }
    }
}

```