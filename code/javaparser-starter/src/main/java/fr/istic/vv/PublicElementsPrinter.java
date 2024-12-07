package org.example;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private Map<String, Boolean> attributHaveGetter;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        attributHaveGetter = new HashMap<>();
        System.out.print("[");
        int lenght = unit.getTypes().size();
        int i = 0;
        for(TypeDeclaration<?> type : unit.getTypes()) {
            System.out.print("{");
            type.accept(this, null);
            System.out.print("}");
            if(i<lenght-1){
                System.out.print(",");
            }
            i++;
        }
        System.out.print("]");
    }

    @Override
    public void visit(PackageDeclaration packageDeclaration, Void arg) {
        super.visit(packageDeclaration, arg);
        System.out.print("package : '" + packageDeclaration.getName()+ "'");
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        System.out.print("classname : '" + String.valueOf(declaration.getName())+ "',");
        for (FieldDeclaration field : declaration.getFields()) {
            field.getVariables().forEach(var -> {
                field.accept(this, null);
            });


            for(MethodDeclaration method : declaration.getMethods()) {
                method.accept(this, arg);
            }
        }
        super.visit(declaration, arg);

        printChamp();
    }

    @Override
    public void visit(FieldDeclaration fieldDeclaration, Void arg) {
        super.visit(fieldDeclaration, arg);

        for (VariableDeclarator variable : fieldDeclaration.getVariables()) {
            attributHaveGetter.put(String.valueOf(variable.getName()), Boolean.FALSE);
        }
    }


    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        String methodName = String.valueOf(declaration.getName());
        Optional<String> opt_attributInMethodName = getNameAttributeInMethodIfItsGetMethod(methodName);
        if(opt_attributInMethodName.isPresent()){
            String attributInMethodName = opt_attributInMethodName.get();
            if(attributHaveGetter.containsKey(attributInMethodName)) {
                attributHaveGetter.put(attributInMethodName, Boolean.TRUE);
            }
        }
    }

    private Optional<String> getNameAttributeInMethodIfItsGetMethod(String methodName){
        Optional<String> result;
        if(methodName.startsWith("get")){
            String[] split = methodName.split("get");
            result = Optional.of(split[1].toLowerCase());
        }
        else {
            result = Optional.empty();
        }
        return result;
    }

    public void printChamp(){
        System.out.print("'attributs' : [");

        int lenght = 0;
        for (Map.Entry<String, Boolean> entry : attributHaveGetter.entrySet()){
            if(!entry.getValue()) {
                lenght++;
            }
        }


        int i = 0;
        for(Map.Entry<String, Boolean> entry : attributHaveGetter.entrySet()){
            if(!entry.getValue()){
                System.out.print("'" + entry.getKey() + "'");

                if (i < lenght -1)
                    System.out.print(",");
                i++;
            }
        }
        System.out.print("]");
    }

}
