# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

1/-
Tight Class Cohesion (TCC) et Loose Class Cohesion (LCC) sont des métriques utilisées pour mesurer les relations entre les méthodes à l'intérieur d'une classe Java. Explorons les conditions dans lesquelles TCC et LCC produisent la même valeur pour une classe Java donnée :

TCC et LCC Produisent la Même Valeur :
S'il n'y a aucun lien indirect entre une paire de méthodes dans la classe, alors TCC et LCC produiront la même valeur.
Les liens indirects font référence aux dépendances entre des méthodes qui ne sont pas directement appelées ou invoquées les unes par les autres, mais qui partagent des données communes (attributs).
Exemple de Classe :

public class GenericObject {
private int x;
private int y;
public int getX() {
return x;
}

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }
}
Dans l'exemple, il n'y a aucun lien indirect entre les 	méthodes. Chaque méthode fonctionne indépendamment, et il n'y a aucun appel de méthode ou dépendance entre elles.

LCC 	Plus Bas que TCC :
Il n'est pas possible que LCC soit inférieur à TCC pour une classe donnée. Cela est dû au fait que les nœuds (méthodes) utilisés par LCC sont un sous-ensemble des nœuds utilisés par TCC.

TCC prend en compte à la fois les liens directs et indirects entre les méthodes, tandis que LCC ne considère que les liens directs. Par conséquent, l'ensemble de méthodes utilisé par LCC est toujours un sous-ensemble de celui utilisé par TCC.
Donc LCC est une métrique plus stricte, et toute relation considérée par LCC est également considérée par TCC.
Exemple d'un Projet Open Source :
public class ExampleClass {
private int a;
private int b;
public int getA() {
return a;
}

    public int getB() {
        return b;
    }

    public void setA(int newA) {
        a = newA;
    }

    public void setB(int newB) {
        b = newB;
    }

    public void performOperation() {
        // Quelque opération
    }
}
Dans cet exemple, la méthode ajoutée performOperation()n'a aucun lien indirect avec les méthodes existantes, et donc TCC et LCC produisent la même valeur pour cette classe
