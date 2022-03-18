# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer
Le TCC et le LCC sont égaux s’ils sont tous les deux égaux à 1. C'est à dire si toutes les méthodes de la classe utilisent un même attributs.

````java
public class Cohesive {

    private int x;
    private int y;

    public int sum() {
        return x+y;
    }

    public int sub() {
        return x-y;
    }
}
````