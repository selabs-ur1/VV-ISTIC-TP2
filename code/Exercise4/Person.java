package fr.istic.vv;

public class Person {
    private int age;
    private String name;
    private String adress;

    public String getName() {
        return name;
    }

    public boolean isAdult() {
        return age > 17;
    }

    public void displayParam(int x){
        System.out.println(x);
    }

    public void test() {
        for (int i = 0; i < 2; i++) {
            System.out.println(adress);
        }
        if (age > 17) {
            for (int i = 0; i < 2; i++) {
                System.out.println(adress);
            }
            if(true){
                System.out.println("true");
            }
        } else {
            System.out.println("2");
        }

    }
}