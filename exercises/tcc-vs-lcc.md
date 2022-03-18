# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

### Rappel Définition TCC et LCC 

Tight Class Cohesion (TCC) et Loose Class Cohesion (LCC) sont des métriques utilisées pour évaluer la cohésion d'une classe.

TCC correspond au rapport entre le nombre de paires de nœuds directement
connectés dans un graphe et le nombre total de paires de nœuds.
LCC quant à lui, correspond au nombre de paires de nœuds connectés (directement ou indirectement) divisé par toutes les paires de nœuds.

En rappel, on dit qu'une méthode est connectée à une variable d'instance si la méthode accède à cette variable d'instance.
Deux méthodes qui accèdent à une ou plusieurs variables d'instance communes sont dites directement connectées, tandis que deux méthodes qui sont connectées par l'intermédiaire d'autres méthodes directement ou indirectement connectées sont dites indirectement connectées.


### Expliquez dans quelles circonstances les métriques Tight Class Cohesion (TCC) et Loose Class Cohesion (LCC) produisent la même valeur pour une classe Java donnée.


Soit NDC, le nombre de paires de noeuds directement connectés; NIC, le nombre de paires de noeuds indirectement connectés; NP, le nombre maximum possible de connexions dans le graphe.
On aura donc : 
TCC = NDC/NP    ;     LCC = (NDC + NIC)/ NP

Si TCC = LCC => NDC = NDC + NIC => NIC = 0

En d'autres termes, *TCC et LCC produisent la même valeur pour une classe Java donnée s'il n'y a pas de méthodes indirectement connectées dans la dite classe*.
Cela pourrait être possible si par exemple les méthodes de la classe partagent toutes les mêmes variables d'instance ( `comme dans l'exemple ci-dessous`)

```java
class Rectangle {

private double length, width;

public Rectangle(double length, double width) {
this.length = length;
this.width = width;
}

public double perimeter() {
return (length + width)/2;
}

public double area() {
return (length * width);
}

}
````

Dans l'exemple ci-dessus, les 2 méthodes de la classe _perimeter()_ et _area()_  utilisent les mêmes variables en commun (_length_ et _width_).
Elles sont donc directement connectées.
On a :

TCC = 1 et 
LCC = 1

=> TCC = LCC

### LCC pourrait-il être inférieur à TCC pour une classe donnée ?

TCC = NDC/NP    
LCC = (NDC + NIC)/ NP

TCC et LCC ont le même diviseur qui est NP. Ils ne diffèrent donc que par leur dividende : NDC pour TCC et NDC+NIC pour LCC.

Si LCC < TCC => NDC + NIC < NDC => NIC < 0 ( ce qui n'est pas possible). NIC ne peut pas prendre une valeur négative.

*LCC ne peut donc pas être inférieur à TCC pour une classe donnée*.

#### En conclusion, LCC peut uniquement être supérieure ou égale à TCC.