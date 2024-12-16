public class ComplexClass {
    public void complexMethod(int x, int y, int z) {
        if (x > 0) {
            if (y > 0) {
                System.out.println("x and y are positive");
            } else if (z > 0) {
                System.out.println("x and z are positive");
            }
        } else if (y < 0) {
            System.out.println("y is negative");
        } else {
            for (int i = 0; i < z; i++) {
                System.out.println("z loop iteration: " + i);
            }
        }

        switch (x) {
            case 1:
                System.out.println("x is one");
                break;
            case 2:
                System.out.println("x is two");
                break;
            default:
                System.out.println("x is something else");
                break;
        }
    }
}
