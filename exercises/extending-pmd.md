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

```xml
//IfStatement[descendant::Block/IfStatement[descendant::Block/IfStatement]]
```

```java
public class ComplexCodeExample {
    public void checkComplexity(int number) {
        if (number > 0) {
            if (number < 10) {
                if (number % 2 == 0) {
                    System.out.println("The number is positive, less than 10, and even.");
                }
            }
        }
    }
}
```

En testant sur common-collections j'obtiens bien des remontés de PMD sur la règle

```
src/main/java/org/apache/commons/collections4/CollectionUtils.java:1858:	3 ifs imbriqued:	Oui
src/main/java/org/apache/commons/collections4/CollectionUtils.java:1860:	3 ifs imbriqued:	Oui
src/main/java/org/apache/commons/collections4/CollectionUtils.java:1862:	3 ifs imbriqued:	Oui
src/main/java/org/apache/commons/collections4/CollectionUtils.java:1864:	3 ifs imbriqued:	Oui
...
```