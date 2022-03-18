package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        List<String> attributes = new ArrayList<>();

        for(FieldDeclaration field : declaration.getFields()){
            //ajout de tous les attributs à la liste
            attributes.add(field.getVariable(0).getNameAsString().toLowerCase());
        }
        System.out.println(attributes);

        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
            //si attributes contient le nom de la methode sans "get" c'est que l'attribut a un getter
            //donc on l'enleve de la liste (case insensitive grace au toLowerCase)
            attributes.remove(method.getNameAsString().toLowerCase().replace("get",""));
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }

        attributes = attributes.stream().map(e -> e = declaration.getFullyQualifiedName().orElse("[Anonymous]") + "::" + e).collect(Collectors.toList());
        System.out.println("Attributes without getters : "+attributes);
        writeToFile(attributes);
    }

    private void writeToFile(List<String> items){
        try{
            FileWriter writer = new FileWriter("reportNoGetters.txt", true);
            for (String e : items) {
                writer.write(e+"\n");
            }
            writer.close();
        }
        catch (IOException e){
            System.err.println("Error writing report :"+e.getMessage());
            e.printStackTrace();
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
        //if(!declaration.isPublic()) return;
        //System.out.println("  " + declaration.getDeclarationAsString(true, true));
    }

}
