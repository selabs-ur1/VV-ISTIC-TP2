# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

Voici ce que la commande `pmd check -d src -R rulesets/java/quickstart.xml -f text` retourne pour le projet [Apache Commons Math](https://github.com/apache/commons-math)

```
src/userguide/java/org/apache/commons/math4/userguide/ClusterAlgorithmComparison.java:61:       UseUtilityClass:        This utility class has a non-private constructor
src/userguide/java/org/apache/commons/math4/userguide/ExampleUtils.java:38:     UseUtilityClass:        This utility class has a non-private constructor
src/userguide/java/org/apache/commons/math4/userguide/ExampleUtils.java:58:     MissingOverride:        The method 'run()' is missing an @Override annotation.
src/userguide/java/org/apache/commons/math4/userguide/ExampleUtils.java:62:     MissingOverride:        The method 'actionPerformed(ActionEvent)' is missing an @Override annotation.
src/userguide/java/org/apache/commons/math4/userguide/ExampleUtils.java:79:     MissingOverride:        The method 'actionPerformed(ActionEvent)' is missing an @Override annotation.
src/userguide/java/org/apache/commons/math4/userguide/FastMathTestPerformance.java:26:  UseUtilityClass:        This utility class has a non-private constructor
src/userguide/java/org/apache/commons/math4/userguide/FastMathTestPerformance.java:651: LocalVariableNamingConventions: The final local variable name 'SM' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/FastMathTestPerformance.java:652: LocalVariableNamingConventions: The final local variable name 'M' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/FastMathTestPerformance.java:653: LocalVariableNamingConventions: The final local variable name 'FM' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/LowDiscrepancyGeneratorComparison.java:51:        UseUtilityClass:        This utility class has a non-private constructor
src/userguide/java/org/apache/commons/math4/userguide/LowDiscrepancyGeneratorComparison.java:96:        OneDeclarationPerLine:  Use one line for each declaration, it enhances code readability.
src/userguide/java/org/apache/commons/math4/userguide/LowDiscrepancyGeneratorComparison.java:182:       NonExhaustiveSwitch:    Switch statements or expressions should be exhaustive, add a default case (or missing enum branches)
src/userguide/java/org/apache/commons/math4/userguide/filter/CannonballExample.java:28: UnnecessaryImport:      Unused import 'org.apache.commons.rng.UniformRandomProvider'
src/userguide/java/org/apache/commons/math4/userguide/filter/CannonballExample.java:53: UseUtilityClass:        This utility class has a non-private constructor
src/userguide/java/org/apache/commons/math4/userguide/filter/CannonballExample.java:147:        LocalVariableNamingConventions: The final local variable name 'A' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/CannonballExample.java:163:        LocalVariableNamingConventions: The final local variable name 'B' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/CannonballExample.java:180:        LocalVariableNamingConventions: The final local variable name 'H' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/CannonballExample.java:203:        LocalVariableNamingConventions: The final local variable name 'Q' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/CannonballExample.java:206:        LocalVariableNamingConventions: The final local variable name 'R' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/ConstantVoltageExample.java:28:    UnnecessaryImport:      Unused import 'org.apache.commons.rng.UniformRandomProvider'
src/userguide/java/org/apache/commons/math4/userguide/filter/ConstantVoltageExample.java:53:    UseUtilityClass:        This utility class has a non-private constructor
src/userguide/java/org/apache/commons/math4/userguide/filter/ConstantVoltageExample.java:101:   LocalVariableNamingConventions: The final local variable name 'A' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/ConstantVoltageExample.java:104:   LocalVariableNamingConventions: The final local variable name 'B' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/ConstantVoltageExample.java:107:   LocalVariableNamingConventions: The final local variable name 'H' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/ConstantVoltageExample.java:113:   LocalVariableNamingConventions: The final local variable name 'Q' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/ConstantVoltageExample.java:116:   LocalVariableNamingConventions: The final local variable name 'P0' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/filter/ConstantVoltageExample.java:119:   LocalVariableNamingConventions: The local variable name 'R' doesn't match '[a-z][a-zA-Z0-9]*'
src/userguide/java/org/apache/commons/math4/userguide/genetics/HelloWorldExample.java:40:       UseUtilityClass:        This utility class has a non-private constructor
src/userguide/java/org/apache/commons/math4/userguide/genetics/HelloWorldExample.java:76:       SimplifyBooleanReturns: This if statement can be replaced by `return {condition};`
src/userguide/java/org/apache/commons/math4/userguide/genetics/HelloWorldExample.java:168:      ClassWithOnlyPrivateConstructorsShouldBeFinal:  This class has only private constructors and may be final
src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:69:   UseUtilityClass:        This utility class has a non-private constructor
src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:86:   AvoidUsingVolatile:     Use of modifier volatile is not recommended.
src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:90:   SingularField:  Perhaps 'referenceImage' could be replaced by a local variable.
src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:91:   SingularField:  Perhaps 'testImage' could be replaced by a local variable.
src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:128:  MissingOverride:        The method 'actionPerformed(ActionEvent)' is missing an @Override annotation.
src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:160:  MissingOverride:        The method 'run()' is missing an @Override annotation.
src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:187:  MissingOverride:        The method 'getPreferredSize()' is missing an @Override annotation.
src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample.java:201:  MissingOverride:        The method 'paint(Graphics)' is missing an @Override annotation.
src/userguide/java/org/apache/commons/math4/userguide/genetics/Polygon.java:24: UnnecessaryImport:      Unused import 'org.apache.commons.rng.simple.RandomSource'
src/userguide/java/org/apache/commons/math4/userguide/genetics/PolygonChromosome.java:96:       AssignmentInOperand:    Avoid assignments in operands
src/userguide/java/org/apache/commons/math4/userguide/genetics/PolygonChromosome.java:106:      UselessParentheses:     Useless parentheses.
```

### True positive

Le fichier `src/userguide/java/org/apache/commons/math4/userguide/Polygon` contient une importation inutile `org.apache.commons.rng.simple.RandomSource`. Cette importation n'est pas utilisée dans le fichier et peut être supprimée.

```java
// import org.apache.commons.rng.simple.RandomSource;
```

### False positive

Le fichier `src/userguide/java/org/apache/commons/math4/userguide/genetics/ImageEvolutionExample` contient la méthode `getPreferredSize()` qui est manquante d'une annotation `@Override`. Cette méthode est une méthode de l'interface `JComponent` et n'est pas obligatoire d'être annotée avec `@Override`.
