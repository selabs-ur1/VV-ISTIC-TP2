# TCC *vs* LCC
Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Authors: Dufeil Jaufret & Gentile Brian

Il y a trois cas où TCC et LCC produisent la même valeur pour une classe Java donnée 123:

TCC = LCC = 1 est la classe maximale cohésive où toutes les méthodes sont directement connectées les unes aux autres.
Lorsque TCC = 0 (et LCC = 0), la classe est totalement non cohésive et toutes les méthodes sont totalement déconnectées.
Lorsque TCC = 1 et LCC = 0, la classe est totalement cohésive et toutes les méthodes sont appelées l’une après l’autre.
Voici un exemple de classe Java qui illustre le cas où TCC et LCC produisent la même valeur:

```java
public class Example {
    public void method1() {
        method2();
    }

    public void method2() {
        method3();
    }

    public void method3() {
        method4();
    }

    public void method4() {
        method5();
    }

    public void method5() {
        method1();
    }
}
```
Dans cet exemple, toutes les méthodes sont appelées l’une après l’autre, ce qui signifie que TCC = LCC = 1.

LCC peut être inférieur à TCC pour une classe donnée. Cela se produit lorsque la classe a des méthodes qui ne sont pas appelées l’une après l’autre, mais qui sont appelées par une méthode commune. Dans ce cas, LCC sera inférieur à TCC car il y aura moins de paires de méthodes qui ne sont pas appelées l’une après l’autre