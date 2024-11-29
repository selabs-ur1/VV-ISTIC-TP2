package fr.istic.vv.exercise5;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// report the cyclomatic complexity of all methods
public class MethodsFinder extends VoidVisitorWithDefaults<Void> {

    ReportPrinter reportPrinter;

    public MethodsFinder(ReportPrinter reportPrinter) {
        this.reportPrinter = reportPrinter;
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        for (MethodDeclaration method : declaration.getMethods()) {
            reportPrinter.add(declaration.getName(),
                    declaration.findCompilationUnit().get().getPackageDeclaration().get().getName(),
                    method.getName(), CyclomaticComplexityCalculator.calculate(method));
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
}
