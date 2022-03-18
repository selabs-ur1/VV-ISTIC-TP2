package fr.istic.vv.codeToVerify;

public class Person {
    private int age;
    private String name;

    public String getName() { return name; }

    public boolean isAdult() {
        return age > 17;
    }
}