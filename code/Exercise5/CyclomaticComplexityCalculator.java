package fr.istic.vv;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CyclomaticComplexityCalculator extends VoidVisitorAdapter<Void> {
    private int complexity;

    public int calculateComplexity(MethodDeclaration method) {
        complexity = 1; // Base complexity is always 1
        method.accept(this, null);
        return complexity;
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        complexity++; // Each if statement adds 1 to complexity
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchStmt n, Void arg) {
        complexity += n.getEntries().size(); // Each switch case adds complexity
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        complexity++; // While loop adds 1 to complexity
        super.visit(n, arg);
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        complexity++; // Do-while loop adds 1 to complexity
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        complexity++; // For loop adds 1 to complexity
        super.visit(n, arg);
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        complexity++; // ForEach loop adds 1 to complexity
        super.visit(n, arg);
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        complexity++; // Ternary operator adds 1 to complexity
        super.visit(n, arg);
    }

    @Override
    public void visit(CatchClause n, Void arg) {
        complexity++; // Each catch clause adds 1 to complexity
        super.visit(n, arg);
    }
}