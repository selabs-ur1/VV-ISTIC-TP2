# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Dans notre code nous avons : byte[] image.

La ligne this.image = image est noté comme incorrect par PMD avec le ruleset bestpractices.xml.

Le message d'avertissement dit : ArrayIsStoredDirectly 	The user-supplied array 'image' is stored directly.
Ce que PMD nous demande de faire est d'uiliser la méthode clone() (ou autres méthodes) pour cloner l'adresse du tableau et eviter de l'overwrite. 

Un faux positif pourrait être des méthodes avancées d'ingénierie logicielle, non adaptées à certaines situations. Par exemple sur un projet perso, nous avons testé un ruleset orienté API REST, PMD nous proposait des méthodes de développement que nous ne connaissions pas mais après recherche, ces dernières faisaient exactement la même chose que notre code actuel. 
PMD s'avère pratique pour nettoyer le code, enlever les variables/ imports non utilisés, et en fonction des rulesets, definir des bonnes pratiques pour un projet.
