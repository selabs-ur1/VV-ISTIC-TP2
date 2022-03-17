# No getter!

With the help of JavaParser implement a program that obtains the private fields of public classes that have no public getter in a Java project. 

A field has a public getter if, in the same class, there is a public method that simply returns the value of the field and whose name is `get<name-of-the-field>`.

For example, in the following class:

```Java

class Person {
    private int age;
    private String name;
    
    public String getName() { return name; }

    public boolean isAdult() {
        return age > 17;
    }
}
```

`name` has a public getter, while `age` doesn't.

The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) that lists for each detected field: its name, the name of the declaring class and the package of the declaring class.

Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. See the [instructions](../sujet.md) for suggestions on the projects to use.

*Disclaimer* In a real project not all fields need to be accessed with a public getter.

## No getter!

L'objectif de cette question était de déterminer les private fields n'ayant pas de getter. On a donc créé une classe visitor qui permet de récupérer ces private fields et des les ajouter dans un fichier csv appelé : PrivateFieldsWithNoGetter.csv. Ce dernier est créé dans le sous-dossier target.

Pour lancer le programme, il faut faire un maven install qui va générer une archive jar. Il suffit ensuite d'exécuter la commande suivante après avoir ouvert un terminal dans le sous-dossier target et en spécifiant le chemin vers le projet java que l'on souhaite analyser :
```
java -jar .\javaparser-starter-1.0-jar-with-dependencies.jar C:\Users\Erwann\IdeaProjects\commons-math\commons-math-core\src\main\java\org\apache\commons\math4\core\jdkmath
```

### Classe PrivateFieldsWithNoGetter (visitor)
```java
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

        try(FileWriter writer = new FileWriter("PrivateFieldsWithNoGetter.csv")){
            StringBuilder sb = new StringBuilder();
            sb.append("Package;Class;Field\n");
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrivateFieldsWithNoGetter printer = new PrivateFieldsWithNoGetter();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
    }


}
```


En lancant la commande suivante , voici ce que l'on obtient dans le fichier PrivateFieldsWithNoGetter.csv :
```
java -jar .\javaparser-starter-1.0-jar-with-dependencies.jar C:\Users\Erwann\IdeaProjects\commons-math\commons-math-core\src\main\java\org\apache\commons\math4\core\jdkmath
```

| Package | Class | Field |
| -------- | -------- | -------- |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | ZERO_DENOMINATOR_MSG     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | OVERFLOW_MSG     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | LOG_MAX_VALUE     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | RECOMPUTE_TABLES_AT_RUNTIME     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | LN_2_A     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | LN_2_B     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | LN_QUICK_COEF     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | LN_HI_PREC_COEF     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | SINE_TABLE_LEN     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | SINE_TABLE_A     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | SINE_TABLE_B     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | COSINE_TABLE_A     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | COSINE_TABLE_B     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | TANGENT_TABLE_A     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | TANGENT_TABLE_B     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | RECIP_2PI     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | PI_O_4_BITS     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | EIGHTHS     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | CBRTTWO     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | HEX_40000000     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | MASK_30BITS     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | MASK_NON_SIGN_INT     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | MASK_NON_SIGN_LONG     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | MASK_DOUBLE_EXPONENT     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | MASK_DOUBLE_MANTISSA     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | IMPLICIT_HIGH_BIT     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | TWO_POWER_52     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_3     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_5     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_7     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_9     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_11     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_13     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_15     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_17     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_3_4     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_15_16     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_13_14     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_11_12     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_9_10     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_7_8     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_5_6     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_2     |
| org.apache.commons.math4.core.jdkmath     | AccurateMath     | F_1_4     |
