# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

---
	TCC (Tight Class Cohesion) : Cette mesure est basée sur la relation entre les méthodes d'une classe, en particulier sur la façon dont elles accèdent aux mêmes variables d'instance. Une classe avec une cohésion serrée aura des méthodes qui accèdent toutes ou principalement aux mêmes attributs de la classe.

	LCC (Loose Class Cohesion) : La LCC mesure la dispersion des méthodes au sein d'une classe. Une cohésion lâche peut indiquer que les méthodes de la classe n'ont que peu ou pas d'interactions entre elles, voire qu'elles agissent sur des données très différentes, ce qui peut rendre la classe moins cohésive.
---
	
	Expliquez dans quelles circonstances les mesures *Tight Class Cohesion* (TCC) et *Loose Class Cohesion* (LCC) produisent la même valeur pour une classe Java donnée.
		Cela peut se produire par exemple lorsqu'une classe contient des méthodes qui manipulent de manière égale ou similaire tous ses attributs, sans distinction marquée de regroupement de ces attributs entre les méthodes.
		
	Créez un exemple d'une telle classe et incluez le code ci-dessous ou trouvez un exemple dans un projet open source de Github et incluez le lien vers la classe ci-dessous: 

Exemple ??
--
public class ExempleCohesion {

    private int a;
    private int b;

    public ExempleCohesion(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int additionner() {
        return a + b;
    }

    public int soustraire() {
        return a - b;
    }
}
--

	La LCC pourrait-elle être inférieure à la TCC pour une classe donnée ? Expliquez.
		La LCC pourrait être inférieure dans certains cas où les méthodes de la classe ne partagent que très peu d'attributs communs, ce qui entraînerait une faible cohésion "lâche".
			

