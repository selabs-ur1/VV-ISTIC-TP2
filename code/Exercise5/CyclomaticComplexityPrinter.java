package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.ast.stmt.SwitchEntry;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.javaparser.ast.expr.BinaryExpr.Operator.AND;
import static com.github.javaparser.ast.expr.BinaryExpr.Operator.OR;

public class CyclomaticComplexityPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        unit.getTypes().forEach(type -> type.accept(this, arg));
    }

    private void analyzeTypeDeclaration(TypeDeclaration<?> declaration) {
        // Si la classe ou l'interface n'est pas publique, ignorer
        if (!declaration.isPublic()) return;

        // Gestion du fichier avec try-with-resources
        try (FileWriter fileWriter = new FileWriter("report.txt", true);
             PrintWriter writer = new PrintWriter(fileWriter)) {

            // Analyse de chaque méthode
            declaration.getMethods().forEach(method -> {
                int complexity = calculateCyclomaticComplexity(method);
                String reportLine = declaration.getFullyQualifiedName().orElse("[Unknown]") +
                        " " + method.getName() + " : " + complexity;
                writer.println(reportLine);
            });

            // Analyse récursive des classes ou interfaces imbriquées
            declaration.getMembers().stream()
                    .filter(member -> member instanceof TypeDeclaration)
                    .map(member -> (TypeDeclaration<?>) member)
                    .forEach(this::analyzeTypeDeclaration);

        } catch (IOException e) {
            throw new RuntimeException("Error while writing to the report file", e);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        analyzeTypeDeclaration(declaration);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        analyzeTypeDeclaration(declaration);
    }

    /**
     * Calcule la complexité cyclomatique d'une méthode donnée.
     *
     * @param method La méthode à analyser
     * @return La complexité cyclomatique
     */
    public int calculateCyclomaticComplexity(MethodDeclaration method) {
        // Collecter toutes les structures influençant la complexité cyclomatique
        int count = 0;

        count += method.getNodesByType(IfStmt.class).size();
        count += method.getNodesByType(ForStmt.class).size();
        count += method.getNodesByType(WhileStmt.class).size();
        count += method.getNodesByType(DoStmt.class).size();
        count += method.findAll(SwitchEntry.class).stream()
                .filter(entry -> entry.getLabels().isNonEmpty())
                .count();
        count += method.getNodesByType(ConditionalExpr.class).size();
        count += method.getNodesByType(BinaryExpr.class).stream()
                .filter(expr -> expr.getOperator() == AND || expr.getOperator() == OR)
                .count();

        // Ajouter 1 pour le chemin par défaut
        return count + 1;
    }
}
