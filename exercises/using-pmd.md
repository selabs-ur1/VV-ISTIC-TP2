# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

J'ai choisi le projet appache common collections.

J'ai utilisé le ruleset quickstarts.xml.

Issue remontée par pmd :
    src/main/java/org/apache/commons/collections4/iterators/BoundedIterator.java:88:        SimplifyBooleanReturns: This if statement can be replaced by `return !{condition};`

Le code est le suivant :

```java
/**
    * Checks whether the iterator is still within its bounded range.
    * @return {@code true} if the iterator is within its bounds, {@code false} otherwise
    */
private boolean checkBounds() {
    if (pos - offset + 1 > max) {
        return false;
    }
    return true;
}
```

on peut en effet faire une simplification de code qui permet de simplifier la lecture.

```java
/**
 * Checks whether the iterator is still within its bounded range.
 * @return {@code true} if the iterator is within its bounds, {@code false} otherwise
 */
private boolean checkBounds() {
    return !(pos - offset + 1 > max);
}
```

Faux positif remontée par pmd :
src/main/java/org/apache/commons/collections4/set/CompositeSet.java:122:        UncommentedEmptyConstructor:    Document empty constructor

```java
Le code est le suivant : 
    /**
     * Creates an empty CompositeSet.
     */
    public CompositeSet() {
    }
```

Il s'agit d'un faux positif car on veut pouvoir créer un CompositeSet meme si cela passe par un constructeur vide.
