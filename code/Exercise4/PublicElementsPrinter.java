package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

	BufferedWriter bw;
	List<VariableDeclarator> fields = new ArrayList<VariableDeclarator>();
	TypeDeclaration<?> currentPackage;
	ClassOrInterfaceDeclaration currentClass;
	
	public PublicElementsPrinter(BufferedWriter bw) {
		this.bw = bw;
	}
	
    @Override
    public void visit(CompilationUnit unit, Void arg) {
		for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
		fields.forEach(v -> {
			try {
				bw.write("package : " + currentPackage.getFullyQualifiedName().orElse("[Anonymous]") + " -> ");
				bw.write("class : " + currentClass.getNameAsString() + " -> ");
				bw.write("variable withour getter : " + v.getNameAsString() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		fields.clear();
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) throws IOException {
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
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        try {
        	if(!declaration.isPublic()) return;
        	currentClass = declaration;
			visitTypeDeclaration(declaration, arg);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
    	if(!declaration.isPrivate()) return;
    	for(VariableDeclarator v: declaration.getVariables()) {
    		fields.add(v);
    	}
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return; //si la method est public
        if(!declaration.getParameters().isEmpty()) return; //n'a pas de param
        if(declaration.getBody().isEmpty()) return; //n'est pas vide
        if(declaration.getBody().get().getStatements().size()!=1) return; //ne contient que un statement
        if(!declaration.getBody().get().getStatements().get(0).isReturnStmt()) return; //et que c'est un return
        if(declaration.getBody().get().getStatements().get(0).asReturnStmt().getExpression().isEmpty()) return; //que ça retourne une expression
        if(!declaration.getBody().get().getStatements().get(0).asReturnStmt().getExpression().get().isNameExpr()) return; //et que c'est un nom de variable
        
        fields.removeIf(v -> declaration.getType().equals(v.getType()) && //que le type de retour est égal au type de la var
        declaration.getNameAsString().
        equals("get"+Character.toUpperCase(v.getNameAsString().charAt(0)) + v.getNameAsString().substring(1)) && //que le nom est correcte			
        declaration.getBody().get().getStatements().get(0).asReturnStmt().getExpression().get().asNameExpr().getNameAsString().
        equals(v.getNameAsString())); //que la variable retourner est la bonne
    }

}
