# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC et LCC sont des métriques utilisée pour évaluer la cohésion d'une class.
TCC est définie par le ration entre le nombre de couple de méthodes  qui sont liés entre elles de manière direct 
LCC est définie par le nombre de couple de méthodes qui sont liés entre elles de manière direct ou indirect.
Les liens direct et indirect font références au graphe de relation des variables et méthode d'une class.
Par définition donc LCC est un ratio qui est toujours égal ou supérieur à TCC.

Voici un exemple d'une class avec un ratio TCC et LCC égal.

```java
public class DumbClass {
    private boolean bar;

    public boolean isBar() {
        return bar;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }
}
```