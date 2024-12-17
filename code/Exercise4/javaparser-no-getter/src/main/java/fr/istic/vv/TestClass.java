package fr.istic.vv;

public class TestClass {

    final private int getter = 2;
    final private int noGetter = 2;
    final public int noGetterPublic = 3;

    final int packageVisible = 4;

    public TestClass(int getter, int noGetter) {
    }

    public int getGetter() {
        return getter;
    }

}
