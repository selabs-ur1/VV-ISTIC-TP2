public class main {
    
    public static void main(String[] args) {
        int x = 5;

        if (x > 0) {
            System.out.println("x is positive.");

            if (x % 2 == 0) {
                System.out.println("x is even.");

                if (x > 10) {
                    System.out.println("x is greater than 10.");
                } else {
                    System.out.println("x is not greater than 10.");
                }
            } else {
                System.out.println("x is odd.");
            }
        } else {
            System.out.println("x is non-positive.");
        }
    }
}
