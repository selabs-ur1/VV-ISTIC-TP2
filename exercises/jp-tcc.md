# Class cohesion with JavaParser

With the help of JavaParser implement a program that computes the Tight Class Cohesion (TCC) for each class in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each class: the package, name and TCC value. 
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.
Finally, your application should also produce the dependency graph of each class (cf. example [here](https://people.irisa.fr/Benoit.Combemale/pub/course/vv/vv-textbook-v0.1.pdf#cohesion-graph)). The graph should be written using the [GraphViz DOT format](https://www.graphviz.org/)

Ignore inherited members to compute TCC of a class.

Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.

## TCC

L'objectif de cette question était de déterminer le TCC de chaque classe dans un projet. On a donc créé une classe visitor qui permet de calculer chaque TCC pour chaque classe et des les ajouter dans un fichier csv appelé : TCC.csv. Ce dernier est créé dans le sous-dossier target.

Pour lancer le programme, il faut faire un maven install qui va générer une archive jar. Il suffit ensuite d'exécuter la commande suivante après avoir ouvert un terminal dans le sous-dossier target et en spécifiant le chemin vers le projet java que l'on souhaite analyser :
```
java -jar .\javaparser-starter-1.0-jar-with-dependencies.jar C:\Users\Erwann\IdeaProjects\commons-math\commons-math-core\src\main\java\org\apache\commons\math4\core\jdkmath
```

### Classe TightClassCohesion (visitor)
```java
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
```

### Classe Main
```java
package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
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

        try(FileWriter writerTcc = new FileWriter("TCC.csv")){
            StringBuilder sbTcc = new StringBuilder();
            sbTcc.append("Package;Class;TCC\n");
            writerTcc.write(sbTcc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        TightClassCohesion printer = new TightClassCohesion();

        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
    }
}
```

En lancant la commande suivante, voici ce que l'on obtient dans le fichier TCC.csv :
```
java -jar .\javaparser-starter-1.0-jar-with-dependencies.jar C:\Users\Erwann\IdeaProjects\commons-math\commons-math-core\src\main\java\org\apache\commons\math4\core\jdkmath
```

| Package | Class | TCC |
| -------- | -------- | -------- |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | 0.6700337 |
| org.apache.commons.math4.core.jdkmath     | JdkMath     | 0.24816327 |
