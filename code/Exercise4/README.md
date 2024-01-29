# Code of your exercise

Authors: Dufeil Jaufret & Gentile Brian

Main:
```java
package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        args = new String[1];
        args[0] = "C:/Cours/VV/test"; // change the path to the desired folder

        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.SAVE;
        });
    }


}
```

PublicElementsPrinter:
```java
package fr.istic.vv;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    //map to stock and know wether a var is to be printed or not
    private Map<String,Boolean> varsPrivate = new HashMap<>();
    private Optional<String> className;
    private boolean varPrivateExist = false;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        for (String varName : varsPrivate.keySet()){
            if (varsPrivate.get(varName) == true){
                varPrivateExist = true;
                break;
            }
        }
        print(varPrivateExist);
    }

    //print the result
    public void print(boolean varPrivateExist){
        if (varPrivateExist){
            System.out.println("Fields: ");
            for (String varName : varsPrivate.keySet()){
                if (varsPrivate.get(varName) == true){
                    System.out.println(varName);
                }
            }
            System.out.println("Class: " + className);
        }
        else{
            System.out.println("No private fields without public getter!");
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        className = declaration.getFullyQualifiedName().get();

        for(FieldDeclaration f : declaration.getFields()) {
            f.accept(this, arg);
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
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        for (String varName : varsPrivate.keySet()){
            String getName = "get" + varName;
            if (declaration.getNameAsString().equalsIgnoreCase(getName)){
                for (Object s2 : declaration.getChildNodes().toArray()){
                    if (s2.toString().contains("return " + varName)){
                        varsPrivate.replace(varName, false);
                    }
                }
            }
        }
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPublic()) return;
        varsPrivate.put(declaration.getVariable(0).getNameAsString(), true);
    }
}
```