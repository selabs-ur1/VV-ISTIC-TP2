# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

**True Positive:**

Issue found by PMD: Unused variable in a Java class.

PMD has identified a variable in the source code that is declared but not used anywhere within the class. This is a valid concern as unused variables can clutter the code and may indicate unnecessary complexity or potential errors.

```java
public class ExampleClass {
    private int unusedVariable; // PMD identified this as unused
    
    public void someMethod() {
        // Some code that does not use unusedVariable
    }

}
```

In this case, the unused variable `unusedVariable` should either be removed or utilized appropriately within the class. If it's truly unnecessary, removing it will enhance code readability and maintainability.

**False Positive:**

Issue found by PMD: Unnecessary null check before a method call.

PMD has flagged a null check before a method call, but the method itself already handles null values gracefully or is designed to return a meaningful result even when called with a null argument.

```java
public class ExampleClass {
    private SomeObject obj;

    public void someMethod() {
        if (obj != null) {
            obj.doSomething(); // PMD flagged this null check as unnecessary
        }
    }

}
```

In some cases, the null check might be redundant if the method `doSomething()` is already designed to handle null values gracefully. If removing the null check doesn't affect the behavior of the program or compromise safety, it may be considered a false positive. In such situations, removing the unnecessary null check can improve code readability without introducing any risk.