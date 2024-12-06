# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

Pour cette exercice nous avons décidé d'utiliser le projet Collections Apache Commons qui est proposé. Une fois le projet choisi nous avons lancer une analyse à l'aide de pmd check et nous avons obtenu une multitude de résultats. Selon non, la majorité des problèmes remontés ne sont pas des problèmes majeurs mais il s'agit la d'améliorer la clarté et la lisibilité du code comme par exemple des variables inutilisés ou encore des morceaux de code qui peuvent être écrit de manière plus simple.

Pour le problème qui ne mérite pas d'être résolu nous avons détecté par exemple celui ci:

![img1.png](img1.png)

Même si l'utilisation d'interfaces est une bonne pratique il peux néanmoins être nécéssaire de conserver la classe d'implémentations dans plusieurs situations.

Bien que aucun des problèmes nous semble critique, ils nous semblent quand même nécéssaire de se pencher sur celui-ci:

![img2.png](img2.png)

Ce problème est remonter par la règle qui indique qu'il ne faut pas réaffecter les paramètres de la méthode directement.

Ce problème peut être éviter en créant une variable locale qui sera modifier par la suite au lieu de modifier directement le paramètre.