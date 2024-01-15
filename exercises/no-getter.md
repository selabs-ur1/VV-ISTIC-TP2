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

## Answer

Le programme parcours les classes publiques et va, pour chaque champ privé, chercher si celui-ci possède un getter public.

Il indique, dans le terminal, le nom ainsi que le package des classes analysée et indique les champs ne disposant pas de getter public.

Code de `NoGetterPrinter`

``` java
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;

public class NoGetterPrinter extends VoidVisitorWithDefaults<Void> {

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        List<String> noGetterFields = new ArrayList<>();

        for(FieldDeclaration field : declaration.getFields()){
            if(field.isPrivate()){
                //Si le champ est privé, vérifier présence d'un getter public
                String nameOfField = field.getVariable(0).getNameAsString();

                String nof = nameOfField.substring(0,1).toUpperCase()
                        + nameOfField.substring(1);

                //Si un getter existe, il est du format getNameOfField ou isNameOfField si c'est un booléen
                String getterName1 = "get" + nof;
                String getterName2 = "is" + nof;

                boolean getterExist = declaration.getMethodsByName(getterName1).stream().anyMatch(
                        method -> method.isPublic())
                        || declaration.getMethodsByName(getterName2).stream().anyMatch(
                        method -> method.isPublic()) ;

                if(!getterExist){
                    noGetterFields.add(nameOfField);
                }
            }
        }

        System.out.println(
                "Class : " + declaration.getNameAsString() + "\n"
                + "Package : " + declaration.findCompilationUnit().flatMap(CompilationUnit::getPackageDeclaration).get().getNameAsString() + "\n"
                + "No getter field : " + noGetterFields.toString() + "\n"
        );
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }
}
```