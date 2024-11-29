# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

J'ai utiilisé PMD sur mon projet personnel suivant :
https://github.com/Kazoie/GameOfLife

et voici le résultat de la commande suivante :
pmd check -d . -R rulesets/java/quickstart.xml -f text -r /home/kazoie/IdeaProjects/GameOfLife/result.txt

./src/main/java/GameOfLife.java:9:	NoPackage:	All classes, interfaces, enums and annotations must belong to a named package
./src/main/java/GameOfLife.java:13:	SingularField:	Perhaps 'timer' could be replaced by a local variable.
./src/main/java/GameOfLife.java:49:	ForLoopCanBeForeach:	This for loop can be replaced by a foreach loop
./src/main/java/GameOfLife.java:57:	NonExhaustiveSwitch:	Switch statements or expressions should be exhaustive, add a default case (or missing enum branches)
./src/main/java/GameOfLife.java:105:	OneDeclarationPerLine:	Use one line for each declaration, it enhances code readability.
./src/main/java/GameOfLife.java:107:	NonExhaustiveSwitch:	Switch statements or expressions should be exhaustive, add a default case (or missing enum branches)
./src/main/java/GameOfLife.java:222:	LocalVariableNamingConventions:	The local variable name 'LWSSPattern' doesn't match '[a-z][a-zA-Z0-9]*'

Une issue importante qui pourrait être résolu est l'ajout d'un case default dans le switch de la ligne 57, ne pas avoir un switch exhaustif pourrait mener
à la non-gestion de cas imprévus et donc à des comportements non prévus

Une issue non importante est le nom de la variable LWSSPattern qui ne match pas un banc de caractères précis, il semblerait que ca soit juste une erreur
syntaxique pas importante dans notre contexte.


