package CC;

public class TestClassCC {

    public void simpleMethod() {
        int a = 1;
        int b = 2;
        int c = a + b;
    }

    public void ifMethod() {
        int a = 1;
        if (a > 0) {
            a++;
        }
    }

    public void nestedIfMethod() {
        int a = 1;
        if (a > 0) {
            if (a < 10) {
                a++;
            }
        }
    }

    public void forMethod() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }

    public void complexMethod() {
        int a = 1;
        if (a > 0) {
            for (int i = 0; i < 10; i++) {
                if (i % 2 == 0) {
                    System.out.println(i);
                }
            }
        }
    }
}
