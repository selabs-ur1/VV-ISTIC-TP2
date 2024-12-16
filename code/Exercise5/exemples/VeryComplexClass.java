public class VeryComplexClass {
    public void veryComplexMethod(int a, int b, int c) {
        if (a > b) {
            if (c > a) {
                while (b < c) {
                    b++;
                    if (b % 2 == 0) {
                        System.out.println("b is even");
                    } else {
                        System.out.println("b is odd");
                    }
                }
            } else {
                for (int i = 0; i < a; i++) {
                    if (i % 3 == 0) {
                        System.out.println("i is divisible by 3");
                    }
                }
            }
        } else if (b > c) {
            do {
                b--;
            } while (b > a);
        }

        switch (c) {
            case 1: 
                System.out.println("c is one");
                break;
            case 2: 
                System.out.println("c is two");
                break;
            case 3: 
                System.out.println("c is three");
                break;
            default: 
                System.out.println("c is something else");
                break;
        }
    }
}
