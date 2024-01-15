# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Tight Class Cohesion (TCC) and Loose Class Cohesion (LCC) are metrics used to measure the degree of cohesion within a class in object-oriented programming.

**Tight Class Cohesion (TCC):**
TCC measures the ratio of the number of method pairs that access at least one common attribute to the total number of method pairs in a class. In simpler terms, it focuses on how closely the methods of a class are tied together by accessing common attributes.

**Loose Class Cohesion (LCC):**
LCC, on the other hand, measures the ratio of the number of method pairs that do not access any common attributes to the total number of method pairs in a class. It assesses the degree to which methods within a class are independent of each other, with little or no reliance on shared attributes.

Now, let's consider a scenario where TCC and LCC produce the same value for a given Java class. This could happen when all the methods in the class either access at least one common attribute or none of them access any attributes. In other words, either there is a high degree of dependency among methods or there is complete independence.

Here's an example of such a class:

```java
public class CohesionExample {
    private int commonAttribute = 0;

    public void method1() {
        // Access commonAttribute
        commonAttribute++;
    }

    public void method2() {
        // Access commonAttribute
        commonAttribute--;
    }

    public void method3() {
        // Access commonAttribute
        System.out.println(commonAttribute);
    }
}
```

In this example, all three methods (`method1`, `method2`, and `method3`) access the common attribute `commonAttribute`. As a result, TCC and LCC would produce the same value, indicating either high cohesion due to shared attributes.

It's also important to note that, theoretically, LCC could be lower than TCC for a given class. This would happen if the class has a significant number of method pairs that do not access any common attributes, leading to a higher LCC value. On the other hand, TCC would be influenced by method pairs that access common attributes, and if their count is relatively low compared to the total method pairs, TCC could be lower than LCC.

However, in practice, it's more common to observe classes with higher TCC and lower LCC because methods in a well-designed class often work together and share attributes to achieve a common goal.