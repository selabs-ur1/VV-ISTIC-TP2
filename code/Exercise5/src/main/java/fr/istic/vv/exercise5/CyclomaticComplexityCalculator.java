package fr.istic.vv.exercise5;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CyclomaticComplexityCalculator {
    private CyclomaticComplexityCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static int calculate(MethodDeclaration method) {
        CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        visitor.visit(method, null);
        return visitor.getComplexity();
    }

    private static class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {
        private int complexity = 1; // Start with 1 for the method itself

        @Override
        public void visit(IfStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(ForStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(ForEachStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(WhileStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(DoStmt n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(SwitchEntry n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(CatchClause n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(ConditionalExpr n, Void arg) {
            complexity++;
            super.visit(n, arg);
        }

        @Override
        public void visit(BinaryExpr n, Void arg) {
            switch (n.getOperator()) {
                case OR:
                case AND:
                    complexity++;
                    break;
                default:
                    break;
            }
            super.visit(n, arg);
        }

        public int getComplexity() {
            return complexity;
        }
    }
}
