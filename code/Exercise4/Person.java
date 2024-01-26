public class Person {
    private int age;
    private String name;
    private boolean bald;

    public String getName() { return name; }

    public boolean isAdult() {
        return age > 17;
    }
}