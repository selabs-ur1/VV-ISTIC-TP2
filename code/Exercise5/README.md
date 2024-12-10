# Code of your exercise

Put here all the code created for this exercise

Classe de test : CyclomaticComplexityCalculatorTest.java
```java	
package CC;

public class TestClassCC {

    public void simpleMethod() {
        int a = 1;
        int b = 2;
        int c = a + b;
    }

    public void ifMethod() {
        int a = 1;
        if (a > 0) {
            a++;
        }
    }

    public void nestedIfMethod() {
        int a = 1;
        if (a > 0) {
            if (a < 10) {
                a++;
            }
        }
    }

    public void forMethod() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }

    public void complexMethod() {
        int a = 1;
        if (a > 0) {
            for (int i = 0; i < 10; i++) {
                if (i % 2 == 0) {
                    System.out.println(i);
                }
            }
        }
    }
}
```

```java	
package fr.istic.vv;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexityCalculator extends VoidVisitorWithDefaults<Void> {

    private final List<MethodInfo> methodInfos = new ArrayList<>();
    private String currentPackageName;
    private String currentClassName;

    private static class MethodInfo {
        String packageName;
        String className;
        String methodName;
        String parameterTypes;
        int cyclomaticComplexity;

        public MethodInfo(String packageName, String className, String methodName, String parameterTypes,
                int cyclomaticComplexity) {
            this.packageName = packageName;
            this.className = className;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
            this.cyclomaticComplexity = cyclomaticComplexity;
        }
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        currentPackageName = unit.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("");
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        printMethodInfos();

        methodInfos.clear();
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        currentClassName = declaration.getNameAsString();
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

    // la fonction calcule la complexité est simplifiée
    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        int complexity = 1; // La complexité est au moins 1
        complexity += method.findAll(com.github.javaparser.ast.stmt.IfStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.ForStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.ForEachStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.WhileStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.DoStmt.class).size();
        complexity += method.findAll(com.github.javaparser.ast.stmt.SwitchStmt.class).stream()
                .mapToInt(s -> s.getEntries().size()).sum();
        complexity += method.findAll(com.github.javaparser.ast.stmt.CatchClause.class).size();
        return complexity;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        for (VariableDeclarator variable : declaration.getVariables()) {
            System.out.println("  " + variable.getNameAsString() + " : " + variable.getType());
        }
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        int cyclomaticComplexity = calculateCyclomaticComplexity(declaration);
        methodInfos.add(new MethodInfo(currentPackageName, currentClassName, declaration.getNameAsString(),
                declaration.getParameters().toString(), cyclomaticComplexity));
    }

    public List<MethodInfo> getMethodInfos() {
        return methodInfos;
    }

    public void printMethodInfos() {
        for (MethodInfo methodInfo : methodInfos) {
            System.out.println("Method: " + methodInfo.packageName + "." + methodInfo.className + "."
                    + methodInfo.methodName + methodInfo.parameterTypes + " : " + methodInfo.cyclomaticComplexity);
        }
    }
}
```