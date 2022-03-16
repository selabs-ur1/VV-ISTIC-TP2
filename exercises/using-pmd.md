# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer

Sur le projet common-lang :

true positive :

ArrayUtils.java:7018:    UseVarargs:     Consider using varargs for methods or constructors which take an array the last parameter.

Cette recommandation peut être facilement appliqué en modifiant final short[] array par final short… array 

false negative : 

FluentBitSet.java:348:      ShortMethodName:        Avoid using short method names

Ici le nom de la fonction est or(), elle est en effet très courte mais ne doit pas être remplacé car on comprends parfaitement son utilité avec ce nom.
