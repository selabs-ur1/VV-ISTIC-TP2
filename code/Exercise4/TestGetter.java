public class TestGetter {
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC(int c) {
        return c;
    }

    public int getD() {
        return d;
    }

    public int getE() {
        return e;
    }

    public int getF(int f) {
        if (f == null) {
            return this.f;
        }
        return f;
    }
}