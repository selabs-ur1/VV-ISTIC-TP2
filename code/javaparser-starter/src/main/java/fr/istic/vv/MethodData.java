package fr.istic.vv;

import java.util.List;

public class MethodData {
    private String packageName;
    private String className;
    private String methodName;
    private List<String> parametersType;
    private int cyclomaticComplexity;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParametersType() {
        return parametersType;
    }

    public void setParametersType(List<String> parametersType) {
        this.parametersType = parametersType;
    }

    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }
}
