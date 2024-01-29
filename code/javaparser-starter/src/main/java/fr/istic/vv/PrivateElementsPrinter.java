package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PrivateElementsPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(declaration.isPublic()) return;
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
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
        for(FieldDeclaration method : declaration.getFields()) {
            if (method.isPrivate()) {
                if(hasGetter(declaration, method.getVariable(0).getNameAsString()))
                {
                    writeToFile("code/Exercise4/Result.md", "This field is private and has no getter: " +method.getVariable(0).getTypeAsString() + " " +  method.getVariable(0).getNameAsString() +  "\n"
                    + " Package : " + declaration.getFullyQualifiedName().orElse("[Anonymous]")  + "\n" 
                    + " Class : "+ declaration.getNameAsString() + "\n" + "\n");
                }
                else{
                    writeToFile("code/Exercise4/Result.md","This field is private but has a getter : " +method.getVariable(0).getTypeAsString() + " " +  method.getVariable(0).getNameAsString() +  "\n"
                    + " Package : " + declaration.getFullyQualifiedName().orElse("[Anonymous]") + "\n" 
                    + " Class : "+ declaration.getNameAsString() + "\n" +"\n");
                }
                
            }
        }
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        //System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

    private static boolean hasGetter(ClassOrInterfaceDeclaration clazz, String fieldName) {

        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1); 
            
        return clazz.getMethodsByName(getterName).stream().anyMatch(methodDeclaration -> !methodDeclaration.getType().asString().equals("void"));
    }

    private static void writeToFile( String fileName, String content ) {
        try {
            // Création d'un objet File pour le fichier Markdown
            File file = new File(fileName);

            // Création du FileWriter pour écrire dans le fichier
            FileWriter fw = new FileWriter(file,true);

            // Création du BufferedWriter pour une écriture plus efficace
            BufferedWriter writer = new BufferedWriter(fw);

            // Écriture du contenu dans le fichier Markdown
            writer.write(content);

            // Fermeture du BufferedWriter
            writer.close();

            System.out.println("Les informations ont été écrites dans le fichier " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

