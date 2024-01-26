# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC et LCC rendre la même valeur pour le cas ou une seule méthode est présente dans la classe.

public class SingleMethodClass {
    private int member1;
    private String member2;

    public SingleMethodClass(int member1, String member2) {
        this.member1 = member1;
        this.member2 = member2;
    }

    public void print() {
        System.out.println(member1);
        System.out.println(member2);
    }
}

Could LCC be lower than TCC for any given class? Explain.

Si LCC est faible, cela signifie que les éléments de la classe sont relativement dependants les uns des autres.
Si TCC à une valeur élevée, ça veut dire que les éléments sont fortement interconnectés.
Ainsi dans une classe si les éléments s'appellent souvent entre eux, on peut imaginer que LCC aura une valeur plus faible que TCC.
