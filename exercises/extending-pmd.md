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

The project that we choose is `Apache Commons Math`.
After running the following command : `pmd check -f html --rulesets=commons-math/rulesets/java/quickstart.xml -d commons-math -r resultExtendedPMD.html`
We got the list of all the problems that PMD founded with our new rule. (cf rule in the "Code/Exercise3" folder)

Here an example of nested if in the file : `\commons-math\commons-math-core\src\main\java\org\apache\commons\math4\core\jdkmath\AccurateMath.java` line 540 :

``` java
if (x.equals(zero)) {
            if (Dfp.copySign(one, x).greaterThan(zero)) {
                // X == +0
                if (y.greaterThan(zero)) {
                    return x.newInstance(zero);
                } else {
                    return x.newInstance(x.newInstance((byte)1, Dfp.INFINITE));
                }
            } else {
                // X == -0
                if (y.classify() == Dfp.FINITE && y.rint().equals(y) && !y.remainder(two).equals(zero)) {
                    // If y is odd integer
                    if (y.greaterThan(zero)) {
                        return x.newInstance(zero.negate());
                    } else {
                        return x.newInstance(x.newInstance((byte)-1, Dfp.INFINITE));
                    }
                } else {
                    // Y is not odd integer
                    if (y.greaterThan(zero)) {
                        return x.newInstance(zero);
                    } else {
                        return x.newInstance(x.newInstance((byte)1, Dfp.INFINITE));
                    }
                }
            }
        }
```

We can see 3 nested if so, our rule works.
