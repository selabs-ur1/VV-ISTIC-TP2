package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileWriter;
import java.io.IOException;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    FileWriter fileWriter;

    PublicElementsPrinter(FileWriter fileWriter){
        this.fileWriter = fileWriter;
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        //System.out.println("Package : " + unit.getPackageDeclaration().get().getName().toString());
        try{
            fileWriter.write("Package : " + unit.getPackageDeclaration().get().getName().toString() + '\n');
        } catch (Exception e ){
            e.printStackTrace();
        }
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        Boolean hasGetter = false;
        try{
            fileWriter.write("  Class : " + declaration.getNameAsString() + '\n');
        } catch (Exception e ){
            e.printStackTrace();
        }
        for(FieldDeclaration field : declaration.getFields()){
            if (field.isPrivate()) {
                for (MethodDeclaration method : declaration.getMethods()) {
                    if (method.getNameAsString().toLowerCase().contains(field.getVariable(0).getNameAsString())){
                        hasGetter = true;
                    }
                }
                if (!hasGetter){
                    //System.out.println("    Field : " + field.getVariable(0).getNameAsString());
                    try{
                        fileWriter.write("    Field : " + field.getVariable(0).getNameAsString());
                    } catch (Exception e ){
                        e.printStackTrace();
                    }
                }
                hasGetter = false;
            }
        }
        /*for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }*/
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
        if(!declaration.isPublic()) return;
        //System.out.println("  " + declaration.getDeclarationAsString(true, true));
        try{
            fileWriter.write("  " + declaration.getDeclarationAsString(true, true) + '\n');
        } catch (Exception e ){
            e.printStackTrace();
        }

    }

}
