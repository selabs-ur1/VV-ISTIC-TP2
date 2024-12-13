package fr.istic.vv;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NoGetterDetector {
    private final Path sourcePath;

    public NoGetterDetector(Path sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void detectAndReport(Path outputPath) throws IOException {
        List<PrivateFieldInfo> noGetterFields = findPrivateFieldsWithoutGetters();
        generateMarkdownReport(noGetterFields, outputPath);
    }

    private List<PrivateFieldInfo> findPrivateFieldsWithoutGetters() throws IOException {
        List<PrivateFieldInfo> noGetterFields = new ArrayList<>();
        SourceRoot sourceRoot = new SourceRoot(sourcePath);

        sourceRoot.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(compilationUnit -> {
                // Find public classes
                List<ClassOrInterfaceDeclaration> publicClasses = compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
                        .stream()
                        .filter(ClassOrInterfaceDeclaration::isPublic)
                        .collect(Collectors.toList());

                for (ClassOrInterfaceDeclaration publicClass : publicClasses) {
                    // Find private fields without getters
                    List<FieldDeclaration> privateFields = publicClass.getFields().stream()
                            .filter(field -> field.getModifiers().contains(Modifier.privateModifier()))
                            .collect(Collectors.toList());

                    for (FieldDeclaration privateField : privateFields) {
                        for (VariableDeclarator var : privateField.getVariables()) {
                            if (!hasPublicGetter(publicClass, var.getName().asString())) {
                                String packageName = compilationUnit.getPackageDeclaration()
                                        .map(p -> p.getName().asString())
                                        .orElse("default");
                                noGetterFields.add(new PrivateFieldInfo(
                                        var.getName().asString(),
                                        publicClass.getName().asString(),
                                        packageName
                                ));
                            }
                        }
                    }
                }
            });
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        return noGetterFields;
    }

    private boolean hasPublicGetter(ClassOrInterfaceDeclaration clazz, String fieldName) {
        String getterName = "get" + capitalize(fieldName);
        String booleanGetterName = "is" + capitalize(fieldName);
        return clazz.getMethods().stream()
                .filter(method -> method.getModifiers().contains(Modifier.publicModifier()))
                .anyMatch(method ->
                        method.getName().asString().equals(getterName) ||
                                method.getName().asString().equals(booleanGetterName)
                );
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void generateMarkdownReport(List<PrivateFieldInfo> noGetterFields, Path outputPath) throws IOException {
        StringBuilder report = new StringBuilder("# No Getter Fields Report\n\n");
        report.append("| Field Name | Class Name | Package |\n");
        report.append("|-----------|------------|----------|\n");

        for (PrivateFieldInfo field : noGetterFields) {
            report.append(String.format("| %s | %s | %s |\n",
                    field.fieldName, field.className, field.packageName));
        }

        Files.write(outputPath, report.toString().getBytes());
    }
}