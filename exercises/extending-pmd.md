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

Pour rajouter cette nouvelle règle j'ai creer le fichier "newrule.xml" et j'ai ajouter ceci dedans : 

<?xml version="1.0" encoding="UTF-8"?>
<ruleset name="Custom Rules" xmlns="http://pmd.sf.net/ruleset/2.0.0"
xsi:schemaLocation="http://pmd.sf.net/ruleset/2.0.0 http://pmd.sf.net/ruleset_2_0_0.xsd"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <description>
        This is a custom ruleset to detect excessive nested if-statements or high cyclomatic complexity.
    </description>

    <rule name="TooManyNestedIfs"
          language="java"
          message="Avoid nesting too many if statements or having high cyclomatic complexity."
          class="net.sourceforge.pmd.lang.java.rule.design.CyclomaticComplexityRule"
          since="6.0.0">

        <description>
            This rule detects methods with high cyclomatic complexity.
            High complexity often correlates with deeply nested code, like excessive 'if' statements.
        </description>

    </rule>

</ruleset>

je l'ai ensuite tester sur le code que j'ai mis dans le dossier "code/exercice3" et voici le résultat :

./src/main/java/pmdtest.java:9:	TooManyNestedIfs:	Avoid nesting too many if statements or having high cyclomatic complexity.
