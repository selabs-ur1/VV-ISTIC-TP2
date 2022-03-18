package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class TCCCalculator extends VoidVisitorWithDefaults<Void> {


    BufferedWriter writer;

    TCCCalculator(BufferedWriter writer) {
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

    public void printRow(TypeDeclaration<?> declaration,double TCC) {
    
        print("<tr><td>" + declaration.getFullyQualifiedName().get() + "</td><td>" + declaration.getNameAsString() + "</td><td>" + TCC + "</td></tr>");

    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
      
       //n * (n-1)/2;
        double mPP = declaration.getMethods().size() * (declaration.getMethods().size() - 1)/2;
        //map variable -> all methods that use this variable
        Map<String,List<String>> map = new HashMap<String,List<String>>(); 
        for (MethodDeclaration method : declaration.getMethods()) {
            for (String variable : getUsedVariables(method)) {
                if (map.containsKey(variable))
                    map.get(variable).add(method.getNameAsString());
                else {
                    map.put(variable, new LinkedList<>());
                    map.get(variable).add(method.getNameAsString());
                }
            }
        }

        LinkList list = new LinkList();
        for (String key : map.keySet()) {
            for (String method1 : map.get(key)) {
                for (String method2 : map.get(key)) {
                    if (!method1.equals(method2)) {
                        BiLink link = new BiLink(method1, method2);
                        list.add(link);
                    }
                }
            }
        }
        double TCC;
        if (mPP != 0) {
            TCC = list.size()/mPP;
        } else {
            TCC = 1;
        }
        printRow(declaration,TCC);
        

    }

    public List<String> getUsedVariables(MethodDeclaration method) {
        List<String> list = new LinkedList<>();
        for (NameExpr exp : method.findAll(NameExpr.class)) {
            list.add(exp.getNameAsString());
        }
        
        return list;
    }

    

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }


}
