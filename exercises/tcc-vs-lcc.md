# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Les métriques Tight Class Cohesion (TCC) et Loose Class Cohesion (LCC) produisent la même valeur pour une classe Java donnée lorsque le nombre de paires de méthodes liées directement est égal au nombre de paires de méthodes liées directement et indirectement.

Il y a donc deux cas possibles pour que TCC et LCC soient égaux pour une classe donnée:
1. Aucune paire de méthodes n'est liée indirectement, c'est-à-dire que toutes les paires de méthodes liées sont liées directement.
2. Le nombre de paires de méthodes liées indirectement est égal au nombre de paires de méthodes liées directement.

Exemple de code pour un TCC et LCC différents: :
```java
public class Example {
    private int a, b, c;

    public void method1() {
        a = 1;
        b = 2;
    }

    public void method2() {
        b = 3;
        c = 4;
    }

    public void method3() {
        c = 5;
    }

    public void method4() {
        a = 6;
    }
}
```

Calcul de TCC et LCC:

3 paires de méthodes liées directement : (method1, method2), (method1, method4), (method2, method3)
2 paires de méthodes liées indirectement : (method1, method3 via method2), (method2, method4 via method1)
6 paires de méthodes possibles : (method1, method2), (method1, method3), (method1, method4), (method2, method3), (method2, method4), (method3, method4)

TCC : Méthodes connectées par des attributs communs (direct) / Total de paires de méthodes possibles
TCC = 3 / 6 = 0.5

LCC : Méthodes connectées par des attributs communs (direct et indirect) / Total de paires de méthodes possibles
LCC = (3 + 2) / 6 = 0.833

Exemple de code pour un TCC et LCC egaux: :
```java
public class Example {
    private int a, b, c;

    public void method1() {
        a = 1;
        b = 2;
    }

    public void method2() {
        b = 3;
        c = 4;
    }

    public void method3() {
        a = 5;
        c = 5;
    }
}
```

Calcul de TCC et LCC:
3 paires de méthodes liées directement : (method1, method2 via b), (method1, method3 via a), (method2, method3 via c)
0 paires de méthodes liées indirectement : car elles sont toutes liées directement
3 paires de méthodes possibles : (method1, method2), (method1, method3), (method2, method3)

TCC : Méthodes connectées par des attributs communs (direct) / Total de paires de méthodes possibles
TCC = 3 / 3 = 1

LCC : Méthodes connectées par des attributs communs (direct et indirect) / Total de paires de méthodes possibles
LCC = 3 / 3 = 1

Le LCC ne peut pas être inférieur au TCC pour une classe donnée, car le LCC inclut les paires de méthodes liées directement et indirectement, tandis que le TCC inclut uniquement les paires de méthodes liées directement. Ainsi, le LCC est toujours supérieur ou égal au TCC pour une classe donnée.