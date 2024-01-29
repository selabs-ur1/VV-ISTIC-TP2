package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit 
public class CyclomaticComplexity extends VoidVisitorWithDefaults<Void> {
    public ArrayList<MethodElement> ccByMethod = new ArrayList<>();
    public int overallCC = 0;

    public class MethodElement {
        int cc; // cyclomatic complexity
        String className;
        String methodName;
        NodeList<Parameter> parameters;

        public MethodElement(int cc, String className, String methodName, NodeList<Parameter> parameters) {
            this.cc = cc;
            this.className = className;
            this.methodName = methodName;
            this.parameters = parameters;
        } 
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cc_results.txt"))) {
            writer.write("List of cyclomatic complexity by method:");
            writer.newLine();
            writer.newLine();

            for (MethodElement method : ccByMethod) {
                writer.write("MethodName: " + method.methodName);
                writer.newLine();
                
                writer.write("ClassName: " + method.className);
                writer.newLine();

                writer.write("Cyclomatic Complexity: " + method.cc);
                writer.newLine();

                // Write list of private fields to file
                writer.write("Private fields: ");
                for (Parameter parameter : method.parameters) {
                    writer.write(parameter.getTypeAsString() + " " + parameter.getNameAsString() + ", ");
                }

                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
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
        // if no body, complexity is still one
        int cc = 1; 

        Optional<BlockStmt> body = declaration.getBody();
        if (body.isPresent()) {
            for(Statement statement : body.get().getStatements()) {
                if(statement.isIfStmt() || statement.isSwitchStmt() || statement.isForStmt()
                        || statement.isDoStmt() || statement.isWhileStmt()){
                    cc++;
                }
            }
        }

        String className = declaration.findCompilationUnit().flatMap(cu -> cu.findFirst(ClassOrInterfaceDeclaration.class)).map(c -> c.getNameAsString()).orElse("");
        String methodName = declaration.getNameAsString();
        NodeList<Parameter> parameters = declaration.getParameters();
        ccByMethod.add(new MethodElement(cc, className, methodName, parameters));
        overallCC += cc;
    }
}
