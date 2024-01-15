# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Ils produisent la même valeur quand une classe à toutes ses méthodes de connectées, c'est à dire que toutes les méthodes sont connectées de manière directe, ou indirecte, avec toutes les autres.
C'est par exemple le cas de la classe java.lang.Math.

Pour calculer le TCC et le LCC, on part d'un graphe dans lequel chaque méthdoe d'une classes est un noeud.
Si deux méthodes d'une classes utilisent un même attribut de classe, alors un lien est créé entre les deux noeuds.
TCC est égal au nombre de liens directs sur le nombre de liens possibles.
LCC est égal au nombre de liens directs ou indirects sur le nombre de liens possibles.
LCC ne peut donc pas être inférieur au TCC car, ce qui est comptabilisé dans le TCC, l'est aussi dans LCC.
La valeur de LCC sera toujours, au minimum égale au TCC.

