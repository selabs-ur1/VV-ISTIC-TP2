# Code of your exercise

```java
package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
    }
}

package fr.istic.vv;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;

public class CohesionCalculator extends VoidVisitorAdapter<Void> {

    private Set<String> methodSet = new HashSet<>();

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        methodSet.add(md.getDeclarationAsString(true, true));
        super.visit(md, arg);
    }

    public double calculateTCC(ClassOrInterfaceDeclaration classDeclaration) {
        int totalMethods = classDeclaration.getMethods().size();
        int distinctMethods = methodSet.size();

        if (totalMethods == 0) {
            return 0.0; // Avoid division by zero
        }

        return 1.0 - (double) distinctMethods / totalMethods;
    }
}

package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;

        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));

        CohesionCalculator cohesionCalculator = new CohesionCalculator();
        declaration.accept(cohesionCalculator, null);

        double tcc = cohesionCalculator.calculateTCC((ClassOrInterfaceDeclaration) declaration);

        System.out.println("TCC: " + tcc);

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

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }
}
```