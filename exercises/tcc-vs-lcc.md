# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC and LCC can only have the same values if they are both 1 or 0 : either all the methods are directly connected with each other, or none are.

Example of a class where both TCC and LCC are equal to 0 :

```java
class Palette {
    private String name;
    private Collection colors;

    public Group(String name, Collection colors) {
        this.name = name;
        this.colors = colors;
    }

    public int setName(String name) {
        this.name = name;
    }

    public void addColor(Color color) {
        colors.add(color);
    }
}
```

Example of a class where both TCC and LCC are equal to 1 :

```java
class Dog {
    private int foodLevel;
    private String status;

    public Group(int foodLevel, String status) {
        this.foodLevel = foodLevel;
        this.status = status;
    }

    public int walk() {
        this.status = "walking";
        this.foodLevel = this.foodLevel - 1;
    }

    public void sleep() {
        this.status = "sleeping";
        this.foodLevel = this.foodLevel - 2;
    }

    public void eat() {
        this.status = "eating";
        this.foodLevel = this.foodLevel + 5;
    }
}
```

LCC can never be lower than TCC. TCC is the ratio of directly connected pairs of methods, whereas LCC is the ratio of all connected pairs, directly or indirectly.
The number of directly connected pairs is always less than or equal to the number of all connected pairs, so TCC is always less than or equal to LCC.