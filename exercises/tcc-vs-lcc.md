# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Cela produit la même valeur quand les méthodes ont toute des connexions directes entre elles. Et qu'il n'y ait aucune connexion indirecte.


```
public class Example {

    private int attribut1;
    private int attribut2;
    private int attribut3;

    public Example(int attribut1, int attribut2, int attribut3) {
        this.attribut1 = attribut1;
        this.attribut2 = attribut2;
        this.attribut3 = attribut3;
    }

    public String getAttributs() {
        return "Attribut 1 : " + attribut1 + "\n"
             + "Attribut 2 : " + attribut2 + "\n"
             + "Attribut 3 : " + attribut3 + "\n";
    }

    public String sumAllAttributs() {
        int sum = attribut1 + attribut2 + attribut3;
        return "The sum is " + sum + "\n";
    }
}
```
