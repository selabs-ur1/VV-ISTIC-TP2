package fr.istic.vv;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CCCalculator extends VoidVisitorAdapter<Void> {

    private int cc;
    private String currentClassName;
    private String currentPackageName;

    @Override
    public void visit(MethodDeclaration methodDeclaration, Void arg) {
        cc = 1;

        currentClassName = methodDeclaration.findAncestor(ClassOrInterfaceDeclaration.class)
                .map(ClassOrInterfaceDeclaration::getNameAsString)
                .orElse("");
        currentPackageName = methodDeclaration.findCompilationUnit()
                .flatMap(cu -> cu.getPackageDeclaration().map(pkg -> pkg.getNameAsString()))
                .orElse("");

        methodDeclaration.accept(this, null);

        System.out.println("Method: " + methodDeclaration.getNameAsString() +
                ", Class: " + currentClassName +
                ", Package: " + currentPackageName +
                ", CC: " + cc);
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        cc++;
        super.visit(n, arg);
    }
    @Override
    public void visit(ForStmt n, Void arg) {
        cc++;
        super.visit(n, arg);
    }
    @Override
    public void visit(ForEachStmt n, Void arg) {
        cc++;
        super.visit(n, arg);
    }
    @Override
    public void visit(WhileStmt n, Void arg) {
        cc++;
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchStmt n, Void arg) {
        cc += n.getEntries().size();
        super.visit(n, arg);
    }

    public static void main(String[] args) {
        CCCalculator ccCalculator = new CCCalculator();
        // ccCalculator.visit(yourMethodDeclaration, null);
    }
}
