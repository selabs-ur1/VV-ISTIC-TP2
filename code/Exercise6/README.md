# Code of your exercise

Put here all the code created for this exercise

```java
package fr.istic.vv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class TCCCalculator extends VoidVisitorWithDefaults<Void> {

    private Set<String> fields = new HashSet<>();
    private Map<String, Set<String>> methodFieldMap = new HashMap<>();
    private String currentClassName;
    private String currentPackageName;
    private List<ClassInfo> classInfos = new ArrayList<>();

    private static class ClassInfo {
        String packageName;
        String className;
        double tcc;

        public ClassInfo(String packageName, String className, double tcc) {
            this.packageName = packageName;
            this.className = className;
            this.tcc = tcc;
        }
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        currentPackageName = unit.getPackageDeclaration().map(pd -> pd.getName().asString()).orElse("");
        for(TypeDeclaration<?> type : unit.getTypes()) {
            visitTypeDeclaration(type, arg);
        }

        printResults();

    }

    // Classe Type
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        currentClassName = declaration.getName().asString();
        fields.clear();
        methodFieldMap.clear();

        for (FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }

        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }

        calculateTCC();
        generateDependencyGraphs(methodFieldMap, classInfos);
    }

    private void calculateTCC() {
        int methodPairs = 0;
        int connectedPairs = 0;

        List<String> methods = new ArrayList<>(methodFieldMap.keySet());
        for (int i = 0; i < methods.size(); i++) {
            for (int j = i + 1; j < methods.size(); j++) {
                methodPairs++;
                Set<String> fields1 = methodFieldMap.get(methods.get(i));
                Set<String> fields2 = methodFieldMap.get(methods.get(j));
                if (!Collections.disjoint(fields1, fields2)) {
                    connectedPairs++;
                }
            }
        }
        double tcc = 1;
        if(methodPairs != 0) {
            tcc = (double) connectedPairs / methodPairs;
        }
        classInfos.add(new ClassInfo(currentPackageName, currentClassName, tcc));
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        for(VariableDeclarator variable : declaration.getVariables()) {
            fields.add(variable.getName().asString());
        }
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        Set<String> usedFields = new HashSet<>();
        for (String field : fields) {
            if (declaration.getBody().isPresent() && declaration.getBody().get().toString().contains(field)) {
                usedFields.add(field);
            }
        }
        methodFieldMap.put(declaration.getNameAsString(), usedFields);
    }

    private void printResults() {
        System.out.println("TCC Results:");
        for (ClassInfo classInfo : classInfos) {
            System.out.println(classInfo.packageName + "." + classInfo.className + ": " + classInfo.tcc);
        }
    }

    private static void generateDependencyGraphs(Map<String, Set<String>> methodFieldMap, List<ClassInfo> classInfos) {
        for (ClassInfo info : classInfos) {
            String className = info.className;
            try (FileWriter writer = new FileWriter(className + "_dependency_graph.dot")) {
                writer.write("digraph " + className + " {\n");
                for (Map.Entry<String, Set<String>> entry : methodFieldMap.entrySet()) {
                    String method = entry.getKey();
                    for (String field : entry.getValue()) {
                        writer.write("    \"" + method + "\" -> \"" + field + "\";\n");
                    }
                }
                writer.write("}\n");
                System.out.println("Dependency graph generated: " + className + "_dependency_graph.dot");

                ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", className + "_dependency_graph.dot", "-o", className + "_dependency_graph.png");
                pb.inheritIO();
                Process process = pb.start();
                process.waitFor();
                System.out.println("Dependency graph image generated: " + className + "_dependency_graph.png");

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```