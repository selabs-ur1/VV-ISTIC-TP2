# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Authors: Dufeil Jaufret & Gentile Brian

```java
if (!result) {
            return false;
}
```

__C:\Users\jaufr\Documents\VV\maths\commons-math\commons-math-legacy\src\main\java\org\apache\commons\math4\legacy\stat\descriptive\ResizableDoubleArray.java__    822  
This if statement can be replaced by return !{condition} || {elseBranch};

False positive because this change nothing in terms of execution but when you read this kind of code you might lose time reading this line of code


```java
private void loadRealMatrix(RealMatrix m, String resourceName) {
        try {
            DataInputStream in = new DataInputStream(getClass().getResourceAsStream(resourceName));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int row = 0;
            while ((strLine = br.readLine()) != null) {
                if (!strLine.startsWith("#")) {
                    int col = 0;
                    for (String entry : strLine.split(",")) {
                        m.setEntry(row, col++, Double.parseDouble(entry));
                    }
                    row++;
                }
            }
            in.close();
        } catch (IOException e) {}
    }
```

__C:\Users\jaufr\Documents\VV\maths\commons-math\commons-math-legacy\src\test\java\org\apache\commons\math4\legacy\linear\SingularValueDecompositionTest.java__
True positive because the DataInputStream is created in a try catch so if there is an error then the DataInputStream will never be closed.