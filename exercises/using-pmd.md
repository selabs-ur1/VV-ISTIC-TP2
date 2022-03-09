# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer
True positive error :  
**./commons-math-examples/examples-sofm/tsp/src/main/java/org/apache/commons/math4/examples/sofm/tsp/TravellingSalesmanSolver.java:127**	An instanceof check is being performed on the caught exception. Create a separate catch clause for this exception type.  

There is a check on the type of exception thrown but the code could be divided in two catch blocks :  
Code : 
```java
catch (InterruptedException | ExecutionException e) {
    if (e instanceof InterruptedException) {
        // Restore interrupted state...
        Thread.currentThread().interrupt();
    }
    throw new RuntimeException(e);
}
```
Fix : 
```java
catch (InterruptedException e) {
    // Restore interrupted state...
    Thread.currentThread().interrupt();
    throw new RuntimeException(e);
}
catch (ExecutionException e) {
    throw new RuntimeException(e);
}
```
False negative error:
**./commons-math-legacy/src/test/java/org/apache/commons/math4/legacy/stat/regression/MillerUpdatingRegressionTest.java:87**	Avoid unnecessary return statements
There is a `return` statement at the end of a void (test) method. It is not a potential source of error but it is not useful.
