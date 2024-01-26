# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

### true positive
Sorry but every bad smell reviewing was justify. I lost a lot of time on it so I move forward to another exercice. 

### false positive
`commons-math\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\ode\nonstiff\AdamsNordsieckTransformer.java:194: SingletonClassReturningNewInstance:     getInstance method always creates a new object and hence does not comply to Singleton Design Pattern behaviour. Please review`

```java
public static AdamsNordsieckTransformer getInstance(final int nSteps) {
  synchronized(CACHE) {
    AdamsNordsieckTransformer t = CACHE.get(nSteps);
    if (t == null) {
      t = new AdamsNordsieckTransformer(nSteps);
      CACHE.put(nSteps, t);
    }
    return t;
  }
}
```
This false positive explain that the Singleton Design Pattern isn't respected. However, there is a check on t. So it's a false positive.
