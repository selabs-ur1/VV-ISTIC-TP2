package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class CyclicComplexityCalculator extends VoidVisitorAdapter<Void> {

    private final List<CyclicComplexityAnalysis> analysisList = new ArrayList<>();
    private int complexity = 1;

    public CyclicComplexityCalculator() {
        // empty
    }

    public List<CyclicComplexityAnalysis> getAnalysis() {
        return analysisList;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classDecl, Void arg) {
        for (MethodDeclaration method : classDecl.getMethods()) {
            complexity = 1;
            method.accept(this, null);

            String packageName = classDecl.findCompilationUnit()
                    .flatMap(CompilationUnit::getPackageDeclaration)
                    .map(NodeWithName::getNameAsString)
                    .orElseThrow(RuntimeException::new);

            String className = classDecl.getNameAsString();
            String methodName = method.getNameAsString();
            List<TypeParameter> typeParameters = method.getTypeParameters();

            CyclicComplexityAnalysis analysis = new CyclicComplexityAnalysis(
                    packageName,
                    className,
                    methodName,
                    typeParameters,
                    complexity
            );

            analysisList.add(analysis);
        }
        super.visit(classDecl, arg);

    }

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
    public void visit(ForEachStmt stmt, Void arg) {
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
    public void visit(SwitchEntry switchEntry, Void arg) {
        complexity++;
        super.visit(switchEntry, arg);
    }

    @Override
    public void visit(CatchClause stmt, Void arg) {
        complexity++;
        super.visit(stmt, arg);
    }

    @Override
    public void visit(ConditionalExpr expr, Void arg) {
        complexity++;
        super.visit(expr, arg);
    }
}
