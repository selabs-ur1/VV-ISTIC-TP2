# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer
* _commons/collections4/SplitMapUtils.java:69:	SimplifyBooleanReturns:	This if statement can be replaced by `return {condition} || {elseBranch};`_ :
```java
  @Override
  public boolean equals(final Object arg0) {
      if (arg0 == this) {
          return true;
      }
      return arg0 instanceof WrappedGet && ((WrappedGet<?, ?>) arg0).get.equals(get);
  }
```

Ceci est un souci à corriger, on peut simplifier le code en :
```java
  @Override
  public boolean equals(final Object arg0) {
      return (arg0 == this) || (arg0 instanceof WrappedGet && ((WrappedGet<?, ?>) arg0).get.equals(get));
  }
```

* _commons/collections4/list/AbstractLinkedListJava21.java:597:	MissingOverride:	The method 'addFirst(E)' is missing an @Override annotation._ **Faux positif**
  Cette erreur ne touche pas directement au fonctionnement du code, mais plus au respect des conventions d'écriture. Cette annotation aide à simplement à prévenir des erreurs en signalant le rôle de la méthode comme surcharge d'une méthode de la superclasse ou interface.
