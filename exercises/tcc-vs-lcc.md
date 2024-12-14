# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Il faut que toutes les méthodes utilisent les mêmes variables, il doit y avoir aucun noeud non connecté indirectement et directement.

class Point {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return 0;
    }

    public double dot(Point p) {
        return x*p.x + y*p.y;
    }

    public Point sub(Point p) {
        return new Point(x - p.x, y - p.y);
    }

}

LCC ne peut pas être inférieur à TCC étant donné que LCC comporte forcémment les lisaisons de TCC + les lisaisons indirectes.