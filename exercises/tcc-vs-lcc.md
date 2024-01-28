# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

class Example {
    int n;

    int Method1(int p){
        n += p;
        return n;
    }

    int Method2(int p){
        n *= p;
        return n;
    }

    int Method3(int p){
        n -=p;
        return n;
    }

    int Method4(int p){
            n /= p;
    }
}

No he can't be lower because LCC is depedent of TCC.