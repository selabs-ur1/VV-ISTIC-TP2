# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Le TCC et le LCC produisent la même valeur lorsque chaque paire de méthodes est directement connectée.
C'est le cas si
- Toutes les méthodes de la classe accèdent directement au même ensemble d'attributs.
- La classe est petite et les méthodes sont étroitement liées par des attributs partagés.

Voici un classe exemple pour un cas où le TCC et le LCC sont égaux:

      public class Example {
        private int sharedAttribute;

        public int getSharedAttribute() {
            return sharedAttribute;
        }

        public void setSharedAttribute(int value) {
            this.sharedAttribute = value;
        }

        public int incrementAttribute() {
            return ++sharedAttribute;
        }

        public int decrementAttribute() {
            return --sharedAttribute;
        }
      }

Le LCC peut-il être inférieur au TCC ?

Non, la LCC ne peut jamais être inférieure à la TCC pour les raisons suivantes :

LCC inclut à la fois les connexions directes et transitives, ce qui est un sur-ensemble des connexions comptabilisées par TCC. Par conséquent, LCC ≥ TCC pour toute classe donnée.
