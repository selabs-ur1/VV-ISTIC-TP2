# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer
J'ai exécuté la commande suivant sur le module common-math de java :
``
pmd.bat -d C:\Users\ethomas\IdeaProjects\commons-math -R rulesets/java/empty.xml
``

### Vrai positif

```
C:\Users\ethomas\IdeaProjects\commons-math\commons-math-legacy\src\test\java\org\apache\commons\math4\legacy\ode\nonstiff\AdamsBashforthIntegratorTest.java:282:        ReturnEmptyCollectionRatherThanNull:    Return an empty collection rather than null.
```

Il est préférable de retourner une collection vide plutôt qu'un null : 
````java
@Override
public double[] getInterpolatedSecondaryState(int index) {
    return null;
}
````
Corrigé : 
````java
@Override
public double[] getInterpolatedSecondaryState(int index) {
    double[]  empty = {};
    return empty;
}
````
### Faux positif
```
C:\Users\ethomas\IdeaProjects\commons-math\commons-math-legacy\src\test\java\org\apache\commons\math4\legacy\optim\nonlinear\scalar\MultivariateFunctionMappingAdapterTest.java:190:    UselessParentheses:     Useless parentheses.
```
Je ne pense pas que cette "erreur" devrait être corrigé car je trouve que cette parenthèse "en trop" améliore la lisibilité.
````java
public double getBoundedXOptimum() {
return (xOptimum < xMin) ? xMin : ((xOptimum > xMax) ? xMax : xOptimum);
}
````

