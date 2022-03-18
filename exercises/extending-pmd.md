# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer
Nous pouvons introduire la règle suivante pour détecter les if imbriqués :
````xml
<rule ref="category/java/design.xml/AvoidDeeplyNestedIfStmts">
    <properties>
        <property name="problemDepth" value="3" />
    </properties>
</rule>
````
```
pmd.bat -d C:\Users\ethomas\IdeaProjects\commons-math -R C:\Users\ethomas\IdeaProjects\VV-ISTIC-TP2\code\Exercise3\custom-rule.xml -f text
```
Pmd a détecté 5 cas de if imbriqués dans le module common-math :
```C:\Users\ethomas\IdeaProjects\commons-math\commons-math-legacy-core\src\test\java\org\apache\commons\math4\legacy\core\jdkmath\AccurateMathStrictComparisonTest.java:118:    AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
C:\Users\ethomas\IdeaProjects\commons-math\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\optim\BaseMultivariateOptimizer.java:130:       AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
C:\Users\ethomas\IdeaProjects\commons-math\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\optim\BaseMultivariateOptimizer.java:136:       AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
C:\Users\ethomas\IdeaProjects\commons-math\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\optim\BaseMultivariateOptimizer.java:142:       AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
C:\Users\ethomas\IdeaProjects\commons-math\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\optim\BaseMultivariateOptimizer.java:148:       AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
C:\Users\ethomas\IdeaProjects\commons-math\commons-math-legacy\src\test\maxima\special\RealFunctionValidation\RealFunctionValidation.java:110:       AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
```
Dont voici un exemple :
````java
/**
     * Check parameters consistency.
     */
    private void checkParameters() {
        if (start != null) {
            final int dim = start.length;
            if (lowerBound != null) {
                if (lowerBound.length != dim) {
                    throw new DimensionMismatchException(lowerBound.length, dim);
                }
                for (int i = 0; i < dim; i++) {
                    final double v = start[i];
                    final double lo = lowerBound[i];
                    if (v < lo) {
                        throw new NumberIsTooSmallException(v, lo, true);
                    }
                }
            }
            if (upperBound != null) {
                if (upperBound.length != dim) {
                    throw new DimensionMismatchException(upperBound.length, dim);
                }
                for (int i = 0; i < dim; i++) {
                    final double v = start[i];
                    final double hi = upperBound[i];
                    if (v > hi) {
                        throw new NumberIsTooLargeException(v, hi, true);
                    }
                }
            }
        }
    }

````