package testCode;

public class testclass {
    private int age;
    private String name;
    
    public String getName() { return name; }

    public boolean isAdult() {
        if (name == "yoyo") {
            return false;
        }
        return age > 17;
    }

    public boolean isYoung() {
        System.out.println(name);
        return age < 17;
    }
}