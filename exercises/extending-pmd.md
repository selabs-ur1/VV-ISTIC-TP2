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

````xml
<?xml version="1.0"?>
<ruleset name="nestedIf"
         xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">
    <description>
        Detect three nested if
    </description>
    <rule ref="category/java/design.xml/AvoidDeeplyNestedIfStmts">
        <properties>
            <property name="problemDepth" value="3" />
        </properties>
    </rule>
</ruleset>
````

````
>>pmd.bat -d commons-math-master -R ruleIf.xml

commons-math-master\commons-math-legacy-core\src\test\java\org\apache\commons\math4\legacy\core\jdkmath\AccurateMathStrictComparisonTest.java:118:     AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
commons-math-master\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\optim\BaseMultivariateOptimizer.java:130:        AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
commons-math-master\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\optim\BaseMultivariateOptimizer.java:136:        AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
commons-math-master\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\optim\BaseMultivariateOptimizer.java:142:        AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
commons-math-master\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\optim\BaseMultivariateOptimizer.java:148:        AvoidDeeplyNestedIfStmts:       Deeply nested if..then statements are hard to read
commons-math-master\commons-math-legacy\src\test\maxima\special\RealFunctionValidation\RealFunctionValidation.java:110:     AvoidDeeplyNestedIfStmts:  Deeply nested if..then statements are hard to read
````
