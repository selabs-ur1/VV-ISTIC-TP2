# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC and LCC produce the same value for a given Java class when **all the methods of a class use all the attributes** of the class. Here is an example of such a class:

```java
public class Calcul {
    private int a;
    private int b;

    public int add() {
        return a + b;
    }

    public int sub() {
        return a - b;
    }

    public int mul() {
        return a * b;
    }

    public int div() {
        return a / b;
    }
}
```

LCC can't be lower than TCC because LCC measures the relationships between methods of a class **without considering** the common attributes of the class compare to TCC which measures the relationships between methods of a class **considering** the common attributes of the class.