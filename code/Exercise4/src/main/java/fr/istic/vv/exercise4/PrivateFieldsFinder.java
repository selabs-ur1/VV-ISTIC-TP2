package fr.istic.vv.exercise4;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// reports all private fields of public classes that have no public getter
public class PrivateFieldsFinder extends VoidVisitorWithDefaults<Void> {

    ReportPrinter reportPrinter = new ReportPrinter();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic())
            return;

        for (FieldDeclaration field : declaration.getFields()) {
            if (field.isPrivate() && !hasPublicGetter(declaration, field)) {
                reportPrinter.add(new Object[] { field.getVariable(0).getName(), declaration.getName(),
                        declaration.findCompilationUnit().get().getPackageDeclaration().get().getName() });
            }
        }

        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
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

    private boolean hasPublicGetter(TypeDeclaration<?> declaration, FieldDeclaration field) {
        String fieldName = field.getVariable(0).getNameAsString();
        String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

        List<MethodDeclaration> methods = declaration.getMethods();
        for (MethodDeclaration method : methods) {
            if (method.isPublic() && method.getNameAsString().equals(getterName) && method.getParameters().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
