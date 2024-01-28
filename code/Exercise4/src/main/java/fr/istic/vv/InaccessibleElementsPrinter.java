package fr.istic.vv;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all private fields from classes without a corresponding private getter
public class InaccessibleElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private String test = "";

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));

        NodeList<VariableDeclarator> variableDeclarators = new NodeList<VariableDeclarator>();
        declaration.getFields().stream()
            .filter((f) -> { return f.isPrivate(); })
            .forEach(
                (f) -> {
                    f.getVariables().forEach((vd) -> { variableDeclarators.add(vd); });
                });

        List<MethodDeclaration> methodDeclarations = declaration.getMethods();

        for (VariableDeclarator vDeclarator : variableDeclarators) {
            String getterName = "get" + vDeclarator.getNameAsString().substring(0, 1).toUpperCase() + vDeclarator.getNameAsString().substring(1);
            boolean res = methodDeclarations.stream()
                .noneMatch((m) -> {
                    System.err.println(vDeclarator.getNameAsString());
                    return m.isPublic() && m.getName().getIdentifier().equals(getterName);
                });
            if (res) {
                System.out.println(vDeclarator.toString());
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
        if(!declaration.isPublic()) return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

}
