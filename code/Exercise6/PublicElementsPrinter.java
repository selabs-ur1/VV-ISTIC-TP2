package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<MethodDeclaration> {
	
	BufferedWriter bw;
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private Map<String, List<MethodDeclaration>> fields = new HashMap<String, List<MethodDeclaration>>();
	TypeDeclaration<?> currentPackage;
	ClassOrInterfaceDeclaration currentClass;
	
	public PublicElementsPrinter(BufferedWriter bw) {
		this.bw = bw;
	}
	
	private String calculTCC(int nbMethod, int nbConnexion) {
		return "TCC = " + nbConnexion + " / " + (nbMethod * (nbMethod-1) / 2);
	}
	
	private int getNbConnexion() {
		for(MethodDeclaration method: methods) {
			if(method.getBody().isPresent()) {
				method.getBody().get().accept(this, method);
			}
		}
		int nbConnexion = 0;
		for(String s: fields.keySet()) {
			int nbMethod = fields.get(s).size();
			if(nbMethod > 1) nbConnexion += (nbMethod * (nbMethod-1) / 2);
		}
		return nbConnexion;
	}

    @Override
    public void visit(CompilationUnit unit, MethodDeclaration arg) {
    	methods.clear();
    	fields.clear();
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
        try {
        	bw.write("package : " + currentPackage.getFullyQualifiedName().orElse("[Anonymous]") + " -> ");
        	bw.write("class : " + currentClass.getFullyQualifiedName() + " -> ");
			bw.write( "TCC = " + calculTCC(methods.size(), getNbConnexion()) + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, MethodDeclaration arg) {
        if(!declaration.isPublic()) return;
        currentPackage = declaration;
        for(FieldDeclaration field : declaration.getFields()) {
        	field.accept(this, arg);
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
    public void visit(ClassOrInterfaceDeclaration declaration, MethodDeclaration arg) {
    	currentClass = declaration;
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, MethodDeclaration arg) {
        if(!declaration.isPublic()) return;
        methods.add(declaration);
    }
    
    @Override
    public void visit(FieldDeclaration declaration, MethodDeclaration arg) {
        for(VariableDeclarator v: declaration.getVariables()) {
        	if(!fields.containsKey(v.getNameAsString()))
        		fields.put(v.getNameAsString(), new ArrayList<MethodDeclaration>());
        }
    }
    
    @Override
    public void visit(BlockStmt n, MethodDeclaration arg) {
    	for(Statement s: n.getStatements()) {
			s.accept(this, arg);
		}
    }
    
    @Override
    public void visit(ForStmt n, MethodDeclaration arg) {
    	n.getBody().accept(this, arg);
    }
    
    @Override
    public void visit(ForEachStmt n, MethodDeclaration arg) {
    	n.getBody().accept(this, arg);
    }
    
    @Override
    public void visit(WhileStmt n, MethodDeclaration arg) {
    	n.getBody().accept(this, arg);
    }
    
    @Override
    public void visit(DoStmt n, MethodDeclaration arg) {
    	n.getBody().accept(this, arg);
    }
    
    @Override
    public void visit(IfStmt n, MethodDeclaration arg) {
    	n.getCondition().accept(this, arg);
    	n.getThenStmt().accept(this, arg);
    	if(n.getElseStmt().isPresent())
    		n.getElseStmt().get().accept(this, arg);
    }
    
    @Override
    public void visit(SwitchStmt n, MethodDeclaration arg) {
    	n.getSelector().accept(this, arg);
    	for(SwitchEntry e: n.getEntries()) {
    		for(Statement s: e.getStatements()) {
    			s.accept(this, arg);
    		}
    	}
    }
    
    @Override
    public void visit(SynchronizedStmt n, MethodDeclaration arg) {
    	n.getExpression().accept(this, arg);
    }
    
    @Override
    public void visit(TryStmt n, MethodDeclaration arg) {
    	n.getTryBlock().accept(this, arg);
    	for(CatchClause c: n.getCatchClauses()) {
    		c.getBody().accept(this, arg);
    	}
    	if(n.getFinallyBlock().isPresent())
    		n.getFinallyBlock().get().accept(this, arg);
    }
    
    @Override
    public void visit(AssertStmt n, MethodDeclaration arg) {
    	n.getCheck().accept(this, arg);
    	if(n.getMessage().isPresent())
    		n.getMessage().get().accept(this, arg);
    }
    
    @Override
    public void visit(ExpressionStmt n, MethodDeclaration arg) {
    	n.getExpression().accept(this, arg);
    }
    
    @Override
    public void visit(ReturnStmt n, MethodDeclaration arg) {
    	if(n.getExpression().isPresent())
    		n.getExpression().get().accept(this, arg);
    }
    
    @Override
    public void visit(AssignExpr n, MethodDeclaration arg) {
    	n.getTarget().accept(this, arg);
        n.getValue().accept(this, arg);
    }
    
    @Override
    public void visit(MethodCallExpr n, MethodDeclaration arg) {
    	if(n.getScope().isPresent()) {
    		n.getScope().get().accept(this, arg);
    	}
    }
    
    @Override
    public void visit(FieldAccessExpr n, MethodDeclaration arg) {
        if(fields.containsKey(n.getNameAsString())) {
        	fields.get(n.getNameAsString()).add(arg);
        }
    }
    
    @Override
    public void visit(NameExpr n, MethodDeclaration arg) {
        if(fields.containsKey(n.getNameAsString())) {
        	fields.get(n.getNameAsString()).add(arg);
        }
    }

}
