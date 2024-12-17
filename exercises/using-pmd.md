# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

Projet utilisé: Apache Commons Math

Problème qui devrait être résolu: Objet PrintStream non fermé après utilisation. Risque de créer une faille de sécurité ou une fuite de mémoire.
Changements à effectuer: Fermer le PrintStream dès qu'il ne sert plus.


Problème non utile à résoudre: Parenthèses inutiles.
Pourquoi: Si cela aide à la compréhension et à s'assurer d'éviter des erreurs ce n'est pas utile de les enlever, sachant que même si elle sont inutiles l'exécution ne change pas à cause de leur présence.



