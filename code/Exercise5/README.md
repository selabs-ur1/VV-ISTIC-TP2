# Code of your exercise

```java
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetterChecker {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java GetterChecker <path-to-source-code>");
            return;
        }

        String sourceCodePath = args[0];
        processSourceCode(sourceCodePath);
    }

    private static void processSourceCode(String sourceCodePath) {
        File projectDir = new File(sourceCodePath);

        if (!projectDir.isDirectory()) {
            System.out.println("Invalid source code directory path.");
            return;
        }

        List<FieldInfo> fieldsWithoutPublicGetter = new ArrayList<>();

        for (File file : projectDir.listFiles((dir, name) -> name.endsWith(".java"))) {
            try {
                CompilationUnit cu = StaticJavaParser.parse(file);

                cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDeclaration -> {
                    String packageName = cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");
                    classDeclaration.findAll(FieldDeclaration.class).forEach(fieldDeclaration -> {
                        fieldDeclaration.getVariables().forEach(variable -> {
                            String fieldName = variable.getNameAsString();
                            boolean hasPublicGetter = classDeclaration.getMethods().stream()
                                    .anyMatch(methodDeclaration -> isPublicGetter(methodDeclaration, fieldName));

                            if (!hasPublicGetter) {
                                fieldsWithoutPublicGetter.add(new FieldInfo(fieldName, classDeclaration.getNameAsString(), packageName));
                            }
                        });
                    });
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Output the report in CSV format
        System.out.println("Field Name,Declaring Class,Package");
        fieldsWithoutPublicGetter.forEach(fieldInfo ->
                System.out.println(fieldInfo.getFieldName() + "," + fieldInfo.getDeclaringClass() + "," + fieldInfo.getPackageName()));
    }

    private static boolean isPublicGetter(MethodDeclaration methodDeclaration, String fieldName) {
        return methodDeclaration.getModifiers().contains(com.github.javaparser.ast.Modifier.PUBLIC)
                && methodDeclaration.getParameters().isEmpty()
                && (methodDeclaration.getNameAsString().equals("get" + capitalize(fieldName))
                || methodDeclaration.getNameAsString().equals("is" + capitalize(fieldName)));
    }

    private static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private static class FieldInfo {
        private String fieldName;
        private String declaringClass;
        private String packageName;

        public FieldInfo(String fieldName, String declaringClass, String packageName) {
            this.fieldName = fieldName;
            this.declaringClass = declaringClass;
            this.packageName = packageName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getDeclaringClass() {
            return declaringClass;
        }

        public String getPackageName() {
            return packageName;
        }
    }
}
```