# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Tests sur le projet commons-lang (https://github.com/apache/commons-lang)

### Vrai positif

PMD a détecté des parenthèses inutiles dans une expression booléenne de la classe UnicodeEscaper.
Ces parenthèses n'aident pas non plus à la lisibilité de l'expression.

``` java
    out.write(HEX_DIGITS[(codePoint) & 15]);
```

### Faux positif

PMD a détecté que le nom d'une méthode ne suit pas la convention de nommage.

``` bash
    MethodNamingConventions:        The static method name 'init_Aarch_64Bit' doesn't match '[a-z][a-zA-Z0-9]*'
```

Changer le nom de la méthode réduirait la clarté dans la lecture du code. Il n'est pas nécessaire de la modifier. 