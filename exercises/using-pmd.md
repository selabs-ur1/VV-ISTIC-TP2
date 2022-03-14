# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer

J’ai choisi le projet common-lang d’apache. J’ai appliqué PMD avec un ruleset bestpractices.xml.

Une recommandation qui pourrait être appliqué :

commons-lang\src\main\java\org\apache\commons\lang3\ArrayUtils.java:7142:    AvoidReassigningParameters:     Avoid reassigning parameters such as 'startIndexInclusive'

Ici, il y a une recommandation de ne pas réassigner une variable mise en paramètre pour régler le problème il faudra créer une variable qui est égale à la valeur du paramètre et de modifier celle-ci.


Une recommandation qui ne doit pas être appliqué : 

commons-lang\src\main\java\org\apache\commons\lang3\SystemUtils.java:1829:   LiteralsFirstInComparisons:     Position literals first in String comparisons

Ici, il y a une recommandation de changer l’ordre de comparaison de deux string (pour éviter que celle de droite soit null) cependant celle de droite ne peu pas être null car on a : Boolean.TRUE.toString()
