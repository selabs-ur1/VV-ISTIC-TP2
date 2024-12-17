# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

1. 
TCC vs LCC
 
On a TCC = LCC s'il n'est pas possible de relier avec un chemin deux noeuds "méthodes" qui ne sont pas reliés par un arc "données manipulées".
Note: si tous les arcs sont validés (TCC = 1), alors il existe un chemin pour toutes les paires de noeuds (LCC = 1), donc TCC = 1 = LCC.
 
On peut avoir TCC <= LCC car TCC "valide" des arcs entre 2 noeuds et LCC "valide" des chemins entre 2 noeuds, or un arc est un chemin de longueur 1, donc tout ce qui valide TCC valide aussi LCC.

Voici un exemple de classe dont les valeurs de TCC et TLC sont égales:

```java

public class TestGetter {

   public int nombreDeBulles;
   private String nomDuPoisson;
   private int ageDuPoisson;
   private int tailleDuPoisson;
   
   public TestGetter(int nombreDeBulles,String nomDuPoisson,int ageDuPoisson,int tailleDuPoisson){
      this.nombreDeBulles = nombreDeBulles;
      this.nomDuPoisson = nomDuPoisson;
      this.ageDuPoisson = ageDuPoisson;
      this.tailleDuPoisson = tailleDuPoisson;
   }
   
   public static int getAgeDuPoisson(){ return ageDuPoisson;}
   
}

```

Comme aucune connexion n'est faite entre les différentes méthodes, les deux valeurs sont égales à 0.
