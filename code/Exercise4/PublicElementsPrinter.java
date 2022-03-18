package fr.istic.vv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private BufferedWriter logs;
    private List<VariableDeclarator> listVariables = new ArrayList<VariableDeclarator>();
    public PublicElementsPrinter(File logs) throws IOException{
        this.logs = new BufferedWriter(new FileWriter(logs));
    }
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
        try {
            listVariables.forEach(var ->
            {
                try {
                    logs.write(var.toString() + '\n');
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            listVariables.clear();
            logs.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) throws IOException {
        if(!declaration.isPublic()) return;
        for(FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        for(BodyDeclaration<?> member : declaration.getMembers()){
            if(member instanceof TypeDeclaration){
                member.accept(this, arg);
            }
        }
    }

    public void visit(ClassOrInterfaceDeclaration declaration, Void arg){
                if(!declaration.isPublic()) return;
                try {
                    visitTypeDeclaration(declaration, arg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg){
        if(!declaration.isPrivate()) return;
            for(VariableDeclarator var : declaration.getVariables()){
                listVariables.add(var);
            }
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        if(declaration.getBody().isEmpty()) return;
        if(declaration.getBody().get().getStatements().size() != 1) return;
        if(!declaration.getBody().get().getStatements().get(0).isReturnStmt()) return;
        if(declaration.getBody().get().getStatements().get(0).asReturnStmt().getExpression().isEmpty()) return;
         Expression varName = declaration.getBody().get().getStatements().get(0).asReturnStmt().getExpression().get();
        if(!varName.isNameExpr()) return;
        if(!declaration.getNameAsString().contains("get")) return;
        System.out.println(declaration.getNameAsString());
            listVariables.removeIf(var -> declaration.getNameAsString().equals("get"+Character.toUpperCase(var.getNameAsString().charAt(0))+var.getNameAsString().substring(1)));
    }

}
