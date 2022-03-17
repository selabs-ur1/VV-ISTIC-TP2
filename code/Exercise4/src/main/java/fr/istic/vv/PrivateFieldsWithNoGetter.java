package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PrivateFieldsWithNoGetter extends VoidVisitorWithDefaults<Void> {

    private String nameClass;
    private String namePackage;
    private List<String> privateFields = new ArrayList<>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;

        nameClass = String.valueOf(declaration.getName());
        namePackage = declaration.getFullyQualifiedName().orElse("[Anonymous]")
                .replace("." + nameClass,"");
        privateFields.clear();

        for(FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }

        if(!privateFields.isEmpty()){
            try(FileWriter writer = new FileWriter("PrivateFieldsWithNoGetter.csv", true)){
                StringBuilder sb = new StringBuilder();
                for(String nameField : privateFields){
                    String nameGetter = "get" + nameField.substring(0,1).toUpperCase(Locale.ROOT)
                            + nameField.substring(1);
                    if(declaration.getMethodsByName(nameGetter).isEmpty()){
                        sb.append(namePackage).append(";").append(nameClass).append(";").append(nameField).append("\n");
                    }
                }
                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void visit(FieldDeclaration declaration, Void arg) {
        if(!declaration.isPrivate()) return;
        for(VariableDeclarator variable : declaration.getVariables()){
            String nameField = variable.getNameAsString();
            privateFields.add(nameField);
        }
    }
}