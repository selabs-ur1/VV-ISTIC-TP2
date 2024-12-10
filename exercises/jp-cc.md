# Cyclomatic Complexity with JavaParser

With the help of JavaParser implement a program that computes the Cyclomatic Complexity (CC) of all methods in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each method: the package and name of the declaring class, the name of the method, the types of the parameters and the value of CC.
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.

Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.

## Answer 


Le code est disponible [ici](../code/javaparser-starter/src/main/java/fr/istic/vv/CyclomaticComplexity.java).
Le script python créant un histogramme [ici](cc_to_histo.py).
le fichier csv  [ici](../cc.csv).

J'ai réalisé l'histogramme pour common collections et common math.
Voici les résultats :

|Common Collections |common math |
|----|----|
|![text](../images/CC_common%20collections%20.png)|![text](../images/CC_common%20math.png)|

On voit que la complexité moyenne ne dépasse jamais 4. C'est un bon signe.
Les repartissions sembles grossierement équivalente malgré la différence en combre d'item à afficher.
