 
Si chaque méthode utilise chaque attribut de la classe le LCC et le TCC seront égaux. 
Dans cet exemple toutes les méthodes sont directement liées.
`TCC= 3/3`
`LCC= 3/3`
````java
public class LCCequalsTCC{
    private int a, b;
    public void methodA() { a = 5; b = 10; }
    public void methodB() { b = a + 2; }
    public void methodC() { System.out.println(a + b); }
}
````
TCC est un indicateur de la densité des connexions dans la classe tandis que LCC indique si les méthodes sont en cohésion avec la classe. 

