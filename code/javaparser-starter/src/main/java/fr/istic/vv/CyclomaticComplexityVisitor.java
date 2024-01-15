package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.HashMap;
import java.util.Map;

public class CyclomaticComplexityVisitor extends VoidVisitorWithDefaults<Void> {
    private Map<String,Integer> ccMap = new HashMap<>();

    public Map<String,Integer> getCcMap() { return ccMap; }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        System.out.println("unit visit");
        super.visit(unit, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Void arg){
        //CC initiale
        System.out.println("method visit !");
        int cc = 1;

        for(Statement stmt : n.getBody().orElse(new BlockStmt()).getStatements()) {
            if(stmt.isIfStmt() || stmt.isSwitchStmt() || stmt.isForStmt()
                    || stmt.isWhileStmt() || stmt.isDoStmt()){
                cc++;
            }
        }

        String methodName = n.getNameAsString();
        String className = n.findCompilationUnit().flatMap(cu -> cu.findFirst(ClassOrInterfaceDeclaration.class)).map(c -> c.getNameAsString()).orElse("");
        String packageName = n.findCompilationUnit().flatMap(cu -> cu.getPackageDeclaration().map(p -> p.getNameAsString())).orElse("");

        String methodKey = packageName + "." + className + "." + methodName;

        ccMap.put(methodKey, cc);
        super.visit(n,arg);
    }

}
