package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.Optional;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private int cyclomaticComplexity = 1;
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
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

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        System.out.println("  " + declaration.getDeclarationAsString(true, true));
        Optional<BlockStmt> blockStmtOptional = declaration.getBody();
        blockStmtOptional.ifPresent(blockStmt -> blockStmt.accept(this, arg));
        System.out.println("   Cyclomatic Complexity = " + cyclomaticComplexity);
        cyclomaticComplexity = 1;
    }

    @Override
    public void visit(BlockStmt blockStmt, Void arg) {
        for (Statement statement : blockStmt.getStatements()) {
            statement.accept(this, arg);
        }
    }

    @Override
    public void visit(IfStmt statement, Void arg) {
        cyclomaticComplexity++;
        super.visit(statement, arg);

        statement.getElseStmt().ifPresent(elseStmt -> elseStmt.accept(this, arg));
    }

    @Override
    public void visit(ForStmt statement, Void arg) {
        cyclomaticComplexity++;
        super.visit(statement, arg);
    }

    @Override
    public void visit(WhileStmt statement, Void arg) {
        cyclomaticComplexity++; // Each 'while' loop adds one path
        super.visit(statement, arg);
    }

    @Override
    public void visit(DoStmt statement, Void arg) {
        cyclomaticComplexity++; // Each 'do-while' loop adds one path
        super.visit(statement, arg);
    }

    @Override
    public void visit(SwitchStmt statement, Void arg) {
        cyclomaticComplexity += statement.getEntries().size(); // Each 'case' adds one path
        super.visit(statement, arg);
    }

    @Override
    public void visit(CatchClause clause, Void arg) {
        cyclomaticComplexity++; // Each 'catch' block adds one path
        super.visit(clause, arg);
    }

    @Override
    public void visit(AssertStmt statement, Void arg) {
        cyclomaticComplexity++; // Assert statements can throw exceptions, adding one path
        super.visit(statement, arg);
    }

    @Override
    public void visit(BinaryExpr expr, Void arg) {
        if (expr.getOperator() == BinaryExpr.Operator.AND || expr.getOperator() == BinaryExpr.Operator.OR) {
            cyclomaticComplexity++; // Logical operators in conditions add paths
        }
        super.visit(expr, arg);
    }

}
