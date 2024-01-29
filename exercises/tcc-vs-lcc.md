# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

In order to have the TCC and LCC metrics produce the same value in a java class, all the methods in the class must have at least one common instance variable with every other method.

Here an example : 


```java
public class Example {
  public int a;
  public int b;
  public int c;

  public Example(int a, int b, int c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  public int add() {
    return a + b;
  }

  public int sub() {
    return b - c;
  }

  public int multiply () {
    return c * a;
  }  
}
```
We can create the graph associated :
![GraphTCCLCC](https://github.com/lise-rg/VV-ISTIC-TP2/tree/main/exercises/imagetcclcc.png)

With this graph, we can calculate TCC and LCC.
We have TCC = 3/3 = 1 LCC = 3/3 = 1
Whith this example, both metrics produce equal values.

We never can have LCC < TCC because direct connections counted for TCC will necessarily be counted in LCC as well.
Then, we can say that LCC >= TCC.
