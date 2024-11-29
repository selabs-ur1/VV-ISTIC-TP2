# Cyclomatic Complexity with JavaParser

With the help of JavaParser implement a program that computes the Cyclomatic Complexity (CC) of all methods in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each method: the package and name of the declaring class, the name of the method, the types of the parameters and the value of CC.
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.


Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.

# Answer

Le code que j'ai rédiger peut-être trouver dans le fichier code/exercise5 
comme pour l'exo 4, il suffit de faire maven install et ensuite de tester avec java -jar <nom du jar> <file a tester>

le rapport est de la forme suivante :
<Nom complet de la classe> <Nom de la méthode> : <Complexité>

Le calcul de cette compléxité à été fait en utilisant la mesure de Mc Cabe
le rapport présent dans le fichier code est le résultat obtenu en le lancant sur le fichier Person.java 
