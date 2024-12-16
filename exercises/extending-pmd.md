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

La règle se trouve [dans ce repertoire](/code/Exercise3), ainsi qu'une petite class de test. En utilisant cette règle sur la librairie apache 
common-collection nous pouvons trouver plusieurs class qui contiennent des nested if, comme la class 
`commons-collections/src/main/java/org/apache/commons/collections4/MapUtils.java` qui contient par exemple une méthode getBoolean possèdant trois 
niveau de if. À noter que la règle que nous utilisons détecte également les suites de else if, c'est une erreur qui vient probablement de la façon 
dont l'ast est construit par pmd.