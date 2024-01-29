# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

1- TCC et LCC produiront la même valeur pour une classe Java donnée si et seulement si toutes les méthodes de la classe sont directement connectées. Cela signifie que chaque paire de méthodes de la classe doit utiliser au moins une variable d'instance en commun.

Exemples :

```java
public class MaClasse {

    private int x;
    private int y;

    public void methode1() {
        x = 10;
    }

    public void methode2() {
        y = 20;
    }

    public void methode3() {
        int z = x + y;
        System.out.println(z);
    }
}
```

dans cet exemples tout les varibles utilisent au moins une varible d'instance en commun.

2- Oui le LCC peut etre inferrieur a TCC si il y'a des connexions indirect entre les methodes de classe.


