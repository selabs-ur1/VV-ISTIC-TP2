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

//IfStatement[count(ancestor::IfStatement) >= 2]

(added the PMD documentation xml snippet to have the list of rules format)
pmd check -f html -R C:\Users\Ethan\Documents\TPVV\VV-ISTIC-TP2\rulesets\complex-code-rule.xml -d C:\Users\Ethan\Documents\TPVV\projectExamples\commons-collections -r C:\Users\Ethan\Documents\TPVV\projectExamples\reports\report-common-collection.html

pmd check -f html -R C:\Users\Ethan\Documents\TPVV\VV-ISTIC-TP2\rulesets\complex-code-rule.xml -d C:\Users\Ethan\Documents\TPVV\projectExamples\commons-math -r C:\Users\Ethan\Documents\TPVV\projectExamples\reports\report-common-math.html

seems like it happens a lot in those projects.

