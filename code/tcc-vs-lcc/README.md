 
Si chaque méthode utilise par exemple toutes le chaque attribut de la classe le LCC et le TCC seront égaux
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