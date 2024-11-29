package fr.istic.vv;

public class PrivateFieldInfo {
    public final String fieldName;
    public final String className;
    public final String packageName;

    public PrivateFieldInfo(String fieldName, String className, String packageName) {
        this.fieldName = fieldName;
        this.className = className;
        this.packageName = packageName;
    }
}