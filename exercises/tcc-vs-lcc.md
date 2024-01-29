# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC ne prend en compte que les chemins directes dans un graph où les noeuds correspondent aux fonctions et les arcs aux variables en commun.
LCC, lui, prend en compte aux chemins directes ET aux chemins indirectes.

Les 2 valeurs peuvent donc être égales si il n'y a aucun chemins indirectes dans un graph.

LCC ne peut pas être plus faible que TCC car LCC est une extension de LCC. ( LCC = TCC + chemins indirectes.)
