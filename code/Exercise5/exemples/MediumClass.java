public class MediumClass {
    public void mediumMethod(int a, int b) {
        if (a > 0) {
            System.out.println("a is positive");
        } else if (a < 0) {
            System.out.println("a is negative");
        } else {
            System.out.println("a is zero");
        }

        if (b % 2 == 0) {
            System.out.println("b is even");
        }
    }
}
