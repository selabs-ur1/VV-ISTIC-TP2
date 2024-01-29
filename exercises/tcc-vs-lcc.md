# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC will be equal to LCC if every method shares a variable with the others methods.

```java
public class TCCequalsLCC {

  public String one;
  public String two;
  public String three;

  public String first() {
    return one + " " + three;
  }

  public String second() {
    return one + " " + two;
  }

  public String third() {
    return two + " " + three;
  }
}
```

LCC can't be lower than TCC since TCC calcul is based on direct relations between nodes and in LCC we also count the transitive relation, so LCC is at least  equal to TCC or greater.
