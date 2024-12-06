# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same
value for a given Java class. Build an example of such as class and include the code below or find one example in an
open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given
class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Les métriques TCC et LCC sont égales dans le cas ou toutes les méthodes d'une classe qui sont connectés entre elles le
sont directement.

Par exemple, pour la classe suivante :

```java
class Test {
    int x;
    int y;

    private getX() {
        return x;
    }

    private setX(int x) {
        this.x = x;
    }

    private getY() {
        return y;
    }

    private getY(int y) {
        this.y = y;
    }
}
```

On obtient un graphe avec 4 nœuds (6 paires de méthode possibles)

Dans ce graphe, il y a 2 connexions directes :

- Une entre getX et setX
- Une entre getY et setY

Il n'y a aucune connexion indirecte.

On a donc TCC = LCC = 2/6

--- 

Si on avait rajouté une méthode setPos()

```java
private setPos(int x,int y){this.x=x;this.y=y;}
```

Cela aurait créé un nœud (10 paires de méthodes), 4 connexions directes entre la nouvelle méthode et les 4 autres, car
elles partagent toutes soit la référence à x, soit à y. Cependant, ce nœud crée aussi des connexions indirectes entre les
méthodes n'utilisant que x avec les méthodes n'utilisant que y.

On obtient donc :

TCC = 6/10
LCC = 10/10