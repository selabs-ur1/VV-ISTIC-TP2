package test;

class Person {
    private int age;
    private String name;

    public String getName() { return name; }

    public boolean isAdult() {
        return age > 17;
    }

    public void exampleMethod(int x) {
        if (x > 0) {
            for (int i = 0; i < x; i++) {
                if (i % 2 == 0) {
                    System.out.println(i);
                }
            }
        } else {
            System.out.println("x is negative or zero");
        }
    }

}