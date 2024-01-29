# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

There are two cases were TCC and LCC are equals.

The first case, LCC and TCC equal 1, this means that all the methods
from a class are connected to each other.

The second case is when LCC and TCC are equal to 0. In this case all the methods are totally unconected.

Let's create a class with a LCC=TCC=1 :
```
class tccLccEqual(){
    int data;
    
    int A(){
        return data++;
    }
    
    int B(){
        return data--;
    }
}
```
TROUVER UN PROJET GIT AVEC EXEMPLE

LCC can't be lower than TCC in any case. EXPLAIN