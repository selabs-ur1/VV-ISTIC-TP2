package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexity extends VoidVisitorWithDefaults<Void> {

    private String currentClassName;
    private String currentPackageName;
    StringBuilder reportBuilder = new StringBuilder();
    private int complexity = 1;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        currentPackageName = unit.getPackageDeclaration()
                .map(pkg -> pkg.getNameAsString())
                .orElse("(default package)");
        for (TypeDeclaration<?> type : unit.getTypes()) {
            visitTypeDeclaration(type, arg);
        }
        displayCyclomaticComplexity();
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        currentClassName = declaration.getNameAsString();

        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
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
    public void visit(MethodDeclaration method, Void arg) {
        reportBuilder.append(currentPackageName);
        reportBuilder.append(";");
        reportBuilder.append(currentClassName);
        reportBuilder.append(";");
        reportBuilder.append(method.getName()
        );
        reportBuilder.append(";");
        for (Parameter parameter : method.getParameters()) {
            reportBuilder.append(parameter.getType());
        }
        reportBuilder.append(";");
        method.getBody().get().accept(this, arg);
        complexity = 1; // Reset for the method
        method.getBody().ifPresent(body -> body.accept(this, arg));
        reportBuilder.append(complexity);
        reportBuilder.append("\n");
    }

    @Override
    public void visit(BlockStmt block, Void arg) {
        // Parcourir chaque instruction du bloc
        for (Statement statement : block.getStatements()) {
            statement.accept(this, arg);
        }
    }

    @Override
    public void visit(IfStmt stmt, Void arg) {
        complexity++;
        stmt.getThenStmt().accept(this, arg);

        if (stmt.hasElseBranch()) {
            complexity++;
            stmt.getElseStmt().get().accept(this, arg);
        }
    }

    @Override
    public void visit(ForStmt stmt, Void arg) {
        complexity++; // Chaque 'for' ajoute un chemin
        stmt.getBody().accept(this, arg);

    }

    @Override
    public void visit(ForEachStmt stmt, Void arg) {
        complexity++;
        stmt.getBody().accept(this, arg);
    }

    @Override
    public void visit(WhileStmt stmt, Void arg) {
        complexity++; // Chaque 'while' ajoute un chemin
        stmt.getBody().accept(this, arg);
    }

    @Override
    public void visit(DoStmt stmt, Void arg) {
        complexity++; // Chaque 'do-while' ajoute un chemin
        stmt.getBody().accept(this, arg);
    }

    @Override
    public void visit(SwitchEntry entry, Void arg) {
        complexity++; // Chaque 'case' dans un switch ajoute un chemin
        for (Statement stmt : entry.getStatements()) {
            stmt.accept(this, arg);
        }

    }

    @Override
    public void visit(CatchClause clause, Void arg) {
        complexity++; // Chaque 'catch' ajoute un chemin
        clause.getBody().accept(this, arg);
    }

    private void displayCyclomaticComplexity() {
        Path reportFilePath = Paths.get("cyclomatic-complexity-report.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(reportFilePath)) {
            writer.write(reportBuilder.toString());
        } catch (IOException e) {
            System.err.println("Failed to write the report to file (txt): " + e.getMessage());
        }
        //System.out.println(reportBuilder.toString());
        try (FileWriter writer = new FileWriter("cc.csv")) {
            writer.write("Package;Class;Method;Parameters;CC\n");
                writer.write(reportBuilder.toString());
                
        }catch(Exception e){
            System.err.println("Failed to write the report to file (csv): " + e.getMessage());
        }
    }

}
