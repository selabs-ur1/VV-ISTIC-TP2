# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC et LCC sont deux métriques permettant de mesurer la cohésion d'un classe en fonction de ses méthodes (en OO) et des ses attributs.
Pour faire simple, un TCC élevé indique que les méthodes de la classe utilisent beaucoup les attributs de la classe, tandis qu'un LCC élevé indique que les méthodes de la classe utilisent peu les attributs de cette même classe.

TCC et LCC produisent les mêmes valeurs pour une classe lorsqu'il n'y a qu'une seule méthode, il n'y a donc pas de paires de méthodes à comparer, et si chaque méthode de la classe fonctionne avec le même ensemble d'attributs. Dans ce cas, TCC et LCC sont égaux à 1.

Un exemple de classe Java pour laquelle TCC et LCC sont égaux est donné ci-dessous :

``` java
public class ExampleClass {
    private int attribute;

    public ExampleClass(int value) {
        this.attribute = value;
    }

    public void method1() {
        System.out.println(attribute);
    }

    public void method2() {
        System.out.println(attribute * 2);
    }

    public void method3() {
        System.out.println(attribute + 10);
    }
}
```

Dans cet exemple, les trois méthodes de la classe `ExampleClass` utilisent l'attribut `attribute`, donc TCC et LCC sont égaux à 1.

Est ce que LCC peut être inférieur à TCC ?
Non, car elles mesures deux aspects opposés de la cohésion d'une classe. Si TCC est élevé, cela signifie que les méthodes de la classe utilisent beaucoup les attributs de la classe, tandis que si LCC est élevé, cela signifie que les méthodes de la classe utilisent peu les attributs de la classe. Ils peuvent être égaux, mais LCC ne peut pas être inférieur à TCC.