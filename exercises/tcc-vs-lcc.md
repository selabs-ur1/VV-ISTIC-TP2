# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer
Dans certaines circonstances spécifiques, une classe peut avoir les mêmes valeurs de TCC et LCC. Cela se produit lorsque toutes les paires de méthodes dans une classe sont très lié ou aucune méthode sont lié.

Example :
```
public class ExampleClass {
    public void method1() {
        // Contenu de la méthode 1
    }

    public void method2() {
        // Contenu de la méthode 2
    }
}

```
Dans cet exemple, il n'y a que deux méthodes dans la classe ExampleClass. Supposons que ces deux méthodes ne partagent aucune donnée ou interaction directe entre elles. Dans ce cas, toutes les paires de méthodes seront considérées comme pas liées, car elles sont indépendantes les unes des autres. Ainsi, le nombre de paires de méthodes peu liées sera égal au nombre de paires de méthodes très liées, et donc TCC = LCC = 0.

Le TCC ne peux pas être plus petit que le LCC du fait que LCC inclut toute les méthode de TCC avec en plus les méthodes qui sont lié indirectment.
