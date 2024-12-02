# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

True positive issue : `./commons-math/commons-math-core/src/main/java/org/apache/commons/math4/core/jdkmath/AccurateMath.java:4070: CloseResource: Ensure that resources like this PrintStream object are closed after use`

```diff
/**
    * Print out contents of arrays, and check the length.
    * <p>used to generate the preset arrays originally.</p>
    * @param a unused
    */
public static void main(String[] a) {
    PrintStream out = System.out;
    /* ...
    Prints out the contents of the arrays
    ... */
+   out.close();    
}
```

False positive issue : `./commons-math/commons-math-core/src/main/java/org/apache/commons/math4/core/jdkmath/AccurateMath.java:1677: UselessParentheses: Useless parentheses.`

```java
/** Compute the reciprocal of the instance.
    * @return reciprocal of the instance
    */
public Split reciprocal() {

    final double approximateInv = 1.0 / full;
    final Split  splitInv       = new Split(approximateInv);

    // if 1.0/d were computed perfectly, remultiplying it by d should give 1.0
    // we want to estimate the error so we can fix the low order bits of approximateInvLow
    // beware the following expressions must NOT be simplified, they rely on floating point arithmetic properties
    final Split product = multiply(splitInv);
    final double error  = (product.high - 1) + product.low; // THIS LINE IS IDENTIFIED AS AN ISSUE

    // better accuracy estimate of reciprocal
    return Double.isNaN(error) ? splitInv : new Split(splitInv.high, splitInv.low - error / full);
}
```

PMD identified the parenthesis around `(product.high - 1)` as useless. Even though PMD is right, the parenthesis are useful to the human reader. They serve a purpose not detected by the tool, and removing them would make the code harder to read. This is why I would not solve this issue.