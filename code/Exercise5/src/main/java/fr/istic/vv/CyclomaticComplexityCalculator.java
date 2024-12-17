package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import fr.istic.vv.CCCUtils.MethodFlow;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexityCalculator extends VoidVisitorWithDefaults<Void> {

    /**
     * graphe de flot de la méthode en cours
     */
    private MethodFlow flot;

    /* --- Partie Visiteur --- */

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        // entrypoint; pseudoMain
        for (TypeDeclaration<?> type : unit.getTypes()) {
            // find classes
            type.accept(this, null);
        }

        System.out.println(CCCUtils.getResultPrint());
    }

    private void visitCommon(TypeDeclaration<?> declaration, Void arg) {
        // class

        // package+class name
        CCCUtils.currentClassName = declaration.getFullyQualifiedName().orElse("[Anonymous]");

        // find methods
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        // find nested classes
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof ClassOrInterfaceDeclaration)
                member.accept(this, arg);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        // class or interface
        visitCommon(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        // enum
        visitCommon(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        // methode

        // créer un graphe de flot pour la méthode
        flot = new MethodFlow(declaration.getDeclarationAsString(false, false, false));

        // lance l'exec séquentielle du parcours du code de la méthode
        declaration.getBody().ifPresent(block -> {
            block.accept(this, null);
            flot.plus(1, 1, "Code ; out");
        });

        // fin du parcours
        CCCUtils.includeToResult(flot);

    }

    @Override
    public void visit(BlockStmt statement, Void arg) {
        // block
        for (Statement stmt : statement.getStatements()) {
            stmt.accept(this, null);
        }
    }

    @Override
    public void visit(IfStmt statement, Void arg) {
        // if-then(-else)
        statement.getThenStmt().accept(this, null);
        flot.plus(2, 3, "Cond Then ; true outThen false");
        statement.getElseStmt().ifPresent(stmt -> {
            stmt.accept(this, null);
            flot.plus(1, 1, "Else ; outElse");
        });
    }

    @Override
    public void visit(ForStmt statement, Void arg) {
        // for
        statement.getBody().accept(this, null);
        flot.plus(2, 3, "Cond Do ; true loopback false");
    }

    @Override
    public void visit(ForEachStmt statement, Void arg) {
        // foreach
        statement.getBody().accept(this, null);
        flot.plus(2, 3, "Cond Do ; true loopback false");
    }

    @Override
    public void visit(WhileStmt statement, Void arg) {
        // while
        statement.getBody().accept(this, null);
        flot.plus(2, 3, "Cond Do ; true loopback false");
    }

    @Override
    public void visit(DoStmt statement, Void arg) {
        // do-while
        statement.getBody().accept(this, null);
        flot.plus(2, 3, "Do Cond ; check loopback out");
    }

    @Override
    public void visit(TryStmt statement, Void arg) {
        // try-catch-finally
        // should not be accounted for in CC calculations
        statement.getTryBlock().accept(this, null);
        // find clauses
        for (CatchClause clause : statement.getCatchClauses()) {
            clause.accept(this, null);
        }
        statement.getFinallyBlock().ifPresent(stmt -> {
            stmt.accept(this, null);
        });
    }

    @Override
    public void visit(CatchClause clause, Void arg) {
        // should not be accounted for in CC calculations
        clause.getBody().accept(this, null);
    }

    @Override
    public void visit(SwitchStmt statement, Void arg) {
        // switch
        // find clauses
        for (SwitchEntry entry : statement.getEntries()) {
            entry.accept(this, null);
        }
    }

    @Override
    public void visit(SwitchEntry entry, Void arg) {
        // switch entry
        // find statement
        for (Statement stmt : entry.getStatements()) {
            stmt.accept(this, null);
            flot.plus(2, 2, "Cond Code ; enter next");
        }
    }
}