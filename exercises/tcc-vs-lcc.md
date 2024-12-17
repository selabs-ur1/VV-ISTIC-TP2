# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC and LCC metrics can both produce the same value if we have a class with no indirectly connected method pair at all
in this case since TCC only uses directly connected method pairs in the formula and LCC uses both indirect and direct connected pairs,
they would both give 1. This occurs when the graph of method connections forms a single fully connected component, for example :


public class Test {
    private int value;

    public void setValue(int value) {
        this.value = value; 
    }

    public int getValue() {
        return value; 
    }
}

LCC cannot be lower than TCC :

TCC = (Directly Connected Pairs) / Total Pairs

LCC = (Indirectly Connected Pairs + Directly Connected Pairs) / Total Pairs

Inherently LCC is more inclusive than TCC so it will never be lesser than TCC with the same exploited pair values. 