class MyClass {
    private int a, b, c;

    public void method1() {
        a = 5;  // Utilisation directe de 'a'
        method3();  // Appel à une autre méthode qui manipule 'b'
    }

    public void method2() {
        b = 10;  // Utilisation directe de 'b'
    }

    public void method3() {
        c = a + b;  // Utilisation de 'a' et 'b' indirectement
    }

    public void method4() {
        // Ne manipule pas directement 'a', 'b', ou 'c'
    }
}
