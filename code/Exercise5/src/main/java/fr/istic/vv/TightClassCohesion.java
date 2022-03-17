package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class TightClassCohesion extends VoidVisitorWithDefaults<Void> {

    private String nameClass;
    private String namePackage;
    private String nameMethod;
    private Set<String> fieldsOfClass = new HashSet<>();
    private Map<String, Set<String>> mapFieldsOfMethods = new HashMap<>();
    private Map<String, Set<String>> mapFieldsOfMethods2 = new HashMap<>();
    private float tcc;
    private int nbLiens;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;

        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));

        nameClass = String.valueOf(declaration.getName());
        namePackage = declaration.getFullyQualifiedName().orElse("[Anonymous]")
                .replace("." + nameClass,"");
        mapFieldsOfMethods.clear();
        tcc = 0;
        nbLiens = 0;

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

        // Calcul nb de méthodes liées par au moins une variable
        Iterator<Map.Entry<String, Set<String>>> itr = mapFieldsOfMethods.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<String, Set<String>> entry = itr.next();
            mapFieldsOfMethods2.remove(entry.getKey());

            for(Map.Entry<String, Set<String>> entry2 : mapFieldsOfMethods2.entrySet()){
                for(String field : entry2.getValue()){
                    if (entry.getValue().contains(field)) {
                        nbLiens += 1;
                        break;
                    }
                }
            }
        }

        // Calcul TCC
        int numberOfMethods = mapFieldsOfMethods.size();
        float div = (float)(numberOfMethods*(numberOfMethods-1)/2);
        if(nbLiens == 0 || div == 0.0){
            tcc = 0;
        }else{
            tcc = nbLiens / div;
        }

        // Print results
        System.out.println("     Nb liens : " + nbLiens);
        System.out.println("     Nb methodes : " + numberOfMethods);
        System.out.println("     TCC : " + tcc);

        // Ecriture dans le csv
        try(FileWriter writer = new FileWriter("TCC.csv", true)){
            StringBuilder sb = new StringBuilder();
            sb.append(namePackage).append(";").append(nameClass).append(";").append(tcc).append("\n");
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        Set<String> fieldsOfMethod = new HashSet<>();

        for(Node node : declaration.getChildNodes()){
            for(String field : fieldsOfClass){
                if(node.toString().contains(field)){
                    fieldsOfMethod.add(field);
                }
            }
        }

        if(mapFieldsOfMethods.containsKey(declaration.getNameAsString())){
            mapFieldsOfMethods.get(declaration.getNameAsString()).addAll(fieldsOfMethod);
            mapFieldsOfMethods2.get(declaration.getNameAsString()).addAll(fieldsOfMethod);
        }else{
            mapFieldsOfMethods.put(declaration.getNameAsString(), fieldsOfMethod);
            mapFieldsOfMethods2.put(declaration.getNameAsString(), fieldsOfMethod);
        }
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        for(VariableDeclarator variable : declaration.getVariables()){
            fieldsOfClass.add(variable.getNameAsString());
        }
    }
}