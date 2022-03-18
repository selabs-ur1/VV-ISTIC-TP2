package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.utils.Pair;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class NoGetterPrinter extends VoidVisitorWithDefaults<Void> {

    Map<FieldDeclaration, Pair<String,String>> infoPrivate = new HashMap<>();
    String className;
    String packageName;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        if(unit.getPackageDeclaration().isPresent()){
            packageName = unit.getPackageDeclaration().get().toString().replace(";", "").replaceAll("\\r|\\n", "");
            System.out.println(packageName);
        }else{
            packageName = "no package";
        }
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        for(FieldDeclaration member : declaration.getFields()) {
            if (member instanceof FieldDeclaration)
                member.accept(this, arg);
        }
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        className = declaration.getNameAsString();
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        // On vérifie pour toutes les méthodes publiques si elles sont des getters des attributs
        if(!declaration.isPublic()) return;
        Iterator<Map.Entry<FieldDeclaration, Pair<String,String>>> iter = infoPrivate.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<FieldDeclaration, Pair<String,String>> entry = iter.next();
            String attribut = entry.getKey().getVariable(0).getNameAsString();
            String nameFunctionLow = ("get"+ attribut).toLowerCase();
            //Si on trouve un getter, alors on retire l'attribut de la collection

            if (declaration.getNameAsString().toLowerCase().startsWith(nameFunctionLow)) {
                System.out.println("Class : " + className + " -> attribut " + attribut + " a un getter");
                iter.remove();
                break;
            }
        }
    }

    @Override
    public void visit(FieldDeclaration attribut, Void arg) {
        //on créé une collection avec les attributs non publics et leurs infos
        if(!attribut.isPublic()){
            infoPrivate.put(attribut, new Pair<>(className, packageName));
        }
    }

    public void writeData() throws IOException {
        System.out.println("-------------------------------------------------------");
        List<String[]> data = new ArrayList<>();
        for (Map.Entry<FieldDeclaration, Pair<String,String>> entry : infoPrivate.entrySet()) {
            String[] noGetter = new String[]
                    { entry.getValue().b, entry.getValue().a, entry.getKey().getVariable(0).getNameAsString()};
            data.add(noGetter);
        }
        String currentPath = new java.io.File(".").getCanonicalPath();
        File csvOutputFile = new File(currentPath + "/code/Exercise4/exportedCSV.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            data.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .collect(Collectors.joining(";"));
    }

}