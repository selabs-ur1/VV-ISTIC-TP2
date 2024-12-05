package fr.istic.vv.cc;

import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {

    private int complexity = 1;

    @Override
    public void visit(IfStmt stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(ForStmt stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(WhileStmt stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(DoStmt stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(SwitchEntry stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(CatchClause stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    public int getComplexity() {
        return complexity;
    }
}
