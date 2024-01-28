# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC is defined as the ratio of directly connected pairs of node in the graph to the number or all pairs of nodes
LCC is the number of pairs of connected (directly or indirectly) nodes to all pairs of node

TCC et LCC produisent la même valeure lorsque 
le graph est complet ou composé seulement de sous-graph complet.

par exemple :
class A {
    methode1(int i);
    methode2(int i);
    methode3(double d);
    methode4(double d);
}
le graph associé : methode1   --i--   methode2
                
                   methode3   --d--   methode4

TCC = 2 edges directement reliés / 4 noeuds = 1/2
LCC = 2 edges directement ou indirectement reliés / 4 noeuds = 1/2