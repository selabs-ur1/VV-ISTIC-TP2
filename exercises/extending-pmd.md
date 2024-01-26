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

I use PMD Rule Designer.
My rule:
`//IfStatement//IfStatement//IfStatement`
My code test:
```java
public class NestedIfWithForExample {

    public static void main(String[] args) {
        int num1 = 10;
        int num2 = 20;
        int num3 = 30;

        if (num1 > 5) {
            System.out.println("Condition 1 is true");

            for (int i = 0; i < 3; i++) {
                System.out.println("Inside for loop iteration " + (i + 1));

                if (num2 > 15) {
                    System.out.println("Condition 2 is true");

                    if (num3 > 25) {
                        System.out.println("Condition 3 is true");
                    }
                }
            }
        }
    }
}
```

I test on commun apache math too.
