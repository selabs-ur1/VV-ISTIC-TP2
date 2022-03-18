# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

Le TCC est défini comme le rapport de paires de noeuds connectées directement dans le graphe au nombre de paires de noeuds.

Le LCC est le nombre de paires de noeuds connectés (directement ou indirectement) à toutes les paires de noeuds.

En résumé :  
- TCC = paires directes / toutes les paires
- LCC = (paires directes + paires indirectes) / toutes les paires

Pour avoir le cas où LCC=TCC, il faut que le nombre de paires indirectes soit égal à 0. 

```java
class Rectangle{
    private int length;
    private int width;
    
    public Rectangle(int length, int width){
        this.length = length;
        this.width = width;
    }
    
    public int area(Rectangle r){
        return r.width * r.length;
    }

    public int perimeter(Rectangle r){
        return (r.length + r.width) * 2;
    }
    
}
```

TCC<=LCC
Pour que LCC soit inférieur à TCC, il faudrait que la somme (paires directes + paires indirectes) soit inférieur au nombre de paires directes, hors c'est impossible. LCC sera toujours au minimum égal à TCC.
