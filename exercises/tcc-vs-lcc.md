# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

En quelles circonstances les métriques *Tight Class Cohesion* (TCC) et *Loose Class Cohesion* (LCC) produisent la même valeur pour une classe Java donnée ?

TCC et LCC sont deux métriques employées pour évaluer la cohésion d'une classe. Ces deux métriques commencent par créer un graphique à partir de la classe. Le graphe est construit comme suit :
Etant donné une classe, chaque méthode m déclarée dans la classe devient un nœud. Étant donné deux méthodes m et n déclarées en C, nous ajoutons une arête entre m et n si et seulement si, m et n utilisent au moins une variable d'instance en commun.

Ainsi, le TCC est défini comme le rapport de paires directement connectées de nœuds dans le graphe au nombre de toutes les paires de nœuds.
De son côté, LCC est le nombre de paires de nœuds connectés (directement ou indirectement) à toutes les paires de nœuds.

Ainsi, à la lecture de la définition de ces deux métriques, il apparaît que TCC ne peut jamais être supérieur à LCC, ce dernier référençant tous les nœuds, même indirect. TCC sera donc toujours inférieur ou égal au LCC.

***Exemple de code :***
```java
public class QuestionA{

    private String a;
    private String b;
    private String c;
    
    public QuestionA(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public QuestionA(){}

    public void methodA(){
        System.out.print(a);
        methodB();
        methodC();
    }

    public void methodB(){
        System.out.print(b);
        methodA();
        methodC();
    }

    public void methodC(){
        System.out.print(c);
        methodA();
        methodB();
    }
}
```
