package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FieldGetterChecker {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        Path projectPath = Path.of(args[0]);
        SourceRoot sourceRoot = new SourceRoot(projectPath);
        List<String[]> reportData = new ArrayList<>();

        sourceRoot.parse("", (localPath, absolutePath, result) -> {
            if (result.isSuccessful()) {
                CompilationUnit cu = result.getResult().get();
                cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDeclaration -> {
                    if (classDeclaration.isPublic()) {
                        String packageName = cu.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");
                        classDeclaration.getFields().forEach(field -> {
                            if (!field.isPublic()) {
                                String fieldName = field.getVariable(0).getNameAsString();
                                boolean hasPublicGetter = classDeclaration.getMethods().stream()
                                        .anyMatch(method -> isGetterForField(method, fieldName));
                                if (!hasPublicGetter) {
                                    String[] fieldInfo = {fieldName, classDeclaration.getNameAsString(), packageName};
                                    reportData.add(fieldInfo);
                                }
                            }
                        });
                    }
                });
            } else {
                System.out.println("Failed to parse: " + absolutePath);
            }
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        System.out.println("Field Name, Declaring Class, Package");
        reportData.forEach(fieldInfo -> System.out.println(String.join(", ", fieldInfo)));
    }

    private static boolean isGetterForField(MethodDeclaration method, String fieldName) {
        String methodName = method.getNameAsString();
        String fieldGetterMethodName = "get" + fieldName;
        String booleanGetterMethodName = "is" + fieldName;

        boolean isFieldGetter = methodName.equalsIgnoreCase(fieldGetterMethodName);
        boolean isBooleanGetter = method.getType().isPrimitiveType() &&
                ((PrimitiveType) method.getType()).getType().asString().equals("boolean") &&
                methodName.equalsIgnoreCase(booleanGetterMethodName);

        return isFieldGetter || isBooleanGetter;
    }
}