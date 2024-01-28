# Cyclomatic Complexity with JavaParser

With the help of JavaParser implement a program that computes the Cyclomatic Complexity (CC) of all methods in a given Java project. The program should take as input the path to the source code of the project. It should produce a report in the format of your choice (TXT, CSV, Markdown, HTML, etc.) containing a table showing for each method: the package and name of the declaring class, the name of the method, the types of the parameters and the value of CC.
Your application should also produce a histogram showing the distribution of CC values in the project. Compare the histogram of two or more projects.


Include in this repository the code of your application. Remove all unnecessary files like compiled binaries. Do include the reports and plots you obtained from different projects. See the [instructions](../sujet.md) for suggestions on the projects to use.

You may use [javaparser-starter](../code/javaparser-starter) as a starting point.

## Answer
Lien vers l'implémentation : [CyclomaticComplexityAnalyzer](../code/javaparser-starter/src/main/java/fr/istic/vv/CyclomaticComplexityAnalyzer.java)

### Apache Commons Collections
| Cyclomatic Complexity | Occurrences |
|-----------------------|-------------|
| 1                     | 5613        |
| 2                     | 926         |
| 3                     | 350         |
| 4                     | 128         |
| 5                     | 45          |
| 6                     | 38          |
| 7                     | 17          |
| 8                     | 11          |
| 9                     | 8           |
| 10                    | 5           |
| 11                    | 6           |
| 12                    | 3           |
| 13                    | 2           |
| 14                    | 1           |
| 15                    | 2           |
| 19                    | 1           |

### commons-math/commons-math-core
| Cyclomatic Complexity | Occurrences |
|-----------------------|-------------|
| 1                     | 124         |
| 2                     | 25          |
| 3                     | 11          |
| 4                     | 5           |
| 5                     | 11          |
| 6                     | 6           |
| 7                     | 4           |
| 8                     | 2           |
| 9                     | 2           |
| 10                    | 2           |
| 13                    | 3           |
| 15                    | 2           |
| 17                    | 1           |
