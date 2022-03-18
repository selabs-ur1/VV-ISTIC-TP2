
package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class NoGetterVariablePrinter extends VoidVisitorWithDefaults<List<VariableDeclarator>> {


    BufferedWriter writer;

    NoGetterVariablePrinter(BufferedWriter writer) {
        super();
        this.writer = writer;
        
    }

    public void print(String string) {
        try {
            writer.append(string);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printRow(TypeDeclaration<?> declaration,String FieldName) {
    
        print("<tr><td>" + declaration.getFullyQualifiedName().get() + "</td><td>" + declaration.getNameAsString() + "</td><td>" + FieldName + "</td></tr>");

    }


    @Override
    public void visit(CompilationUnit unit, List<VariableDeclarator> arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, List<VariableDeclarator> arg) {
        if(!declaration.isPublic()) return;
        List<VariableDeclarator> privateFields;
        privateFields = new LinkedList<>();
        for (FieldDeclaration fieldDeclaration : declaration.getFields()) {
            fieldDeclaration.accept(this, privateFields);
        }
        System.out.println("Private variables are :");
        System.out.println(privateFields);
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, privateFields);
        }
        System.out.println("Private variables with no getters are :");
        System.out.println(privateFields);

        for (VariableDeclarator variable :privateFields) {
            printRow(declaration, variable.getNameAsString());
        }
    }

    @Override
    public void visit(FieldDeclaration declaration,List<VariableDeclarator> arg) {
        if (declaration.isPrivate()) arg.addAll(declaration.getVariables());
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, List<VariableDeclarator> arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, List<VariableDeclarator> arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, List<VariableDeclarator> arg) {
        if(!declaration.isPublic()) return;
        arg.removeIf((variable) -> (isGetterFor(declaration, variable)));
    }

    public boolean isGetterFor(MethodDeclaration method, VariableDeclarator variable) {
        if (!method.getNameAsString().equals("get"+capitalise(variable.getNameAsString()))) return false;
        Optional<BlockStmt> body = method.getBody();
        if (body.isPresent()) {
            NodeList<Statement> stmts = body.get().getStatements();
            if (stmts.size() != 1) return false;
            Optional<Statement> Ostmt = stmts.getFirst();
            if (Ostmt.isPresent()) {
                Statement stmt = Ostmt.get();
                if (!stmt.isReturnStmt()) return false;
                if (!stmt.toString().equals("return " + variable.getNameAsString() + ";")) return false;
            } else return false;
        }
        else return false;
        
        return true;
    }

    public static String capitalise(String word) {
        return word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase();
    }

}
