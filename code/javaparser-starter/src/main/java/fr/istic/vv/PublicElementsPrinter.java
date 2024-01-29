package fr.istic.vv;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {


    Map<String,Integer> methodCC = new HashMap<>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        for (String methodName : methodCC.keySet()){
            System.out.println(methodName + " " +methodCC.get(methodName));
        }
    }
   
    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        System.out.println("Class: " + declaration.getFullyQualifiedName().get());

        for(FieldDeclaration f : declaration.getFields()) {
            f.accept(this, arg);
        }
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
        System.out.println("Method: " +declaration.getNameAsString());
        System.out.println("Parameters: " + declaration.getParameters());
        Optional<BlockStmt> body = declaration.getBody();
        final int[] ccValue = {1};
        if (body.isPresent()) {
            body.get().getStatements().forEach(statement -> {
                if (statement.isIfStmt() || statement.isWhileStmt() || statement.isSwitchStmt()) {
                    ccValue[0]++;
                }
            });
        }
        else{
            System.out.println("CC: 1"); //if no body, cc = 1
        }
        System.out.println("CC: " + ccValue[0]);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPublic()) return;
    }
}