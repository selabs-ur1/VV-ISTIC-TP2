# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC et LCC donnent le même résultat quand le nombre de connexions indirectes dans les méthodes publiques vaut 0.


LCC ne peut pas être plus petit que TCC pour le même code de classe :

NP = maximum number of possible connections
NDC = number of direct connections (number of edges in the connection graph)
NIC = number of indirect connections

Et TCC = NDC / NP, et LCC = (NDC + NIC) / NP, donc LCC >= TCC. 

Voici un exemple d'une classe où TCC et LCC sont égaux :

```java
public class Wejdene {

  private int n;

  public Wejdene(int i) { this.n = i; }

  public int getN() { return this.n; }

  public void incremente() { this.n = this.n + 1; }

}
```
