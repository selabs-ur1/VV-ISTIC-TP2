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

le résultat de mon travail peut-être trouver dans le dossier code/Exercice4.

après un maven install et une commande java -jar <nom de votre jar> <folder a tester> vous obtenez un report.txt.
le report.txt fera un résumé de toutes les variables qu'il à trouver dans le code analysé en mettant true ou false selon la présence de getter ou non pour chaque
variable.
Dans le cas de la classe Person donné en exemple, name à un getter mais pas age ce qui donne le résultat suivant :
test.Person age : false
test.Person name : true
