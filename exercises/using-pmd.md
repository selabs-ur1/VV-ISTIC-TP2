# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

The project that we choose is `Apache Commons Math`.
After running the command, we have the list of all the problems that PMD founded.

In the file : `\commons-math\commons-math-core\src\main\java\org\apache\commons\math4\core\jdkmath\AccurateMath.java`
We have line 1533 an example of `false positive` that is not worth to solve :

Here the code : 
``` java
final double ya = (y + tmp) - tmp;
```
We have useless parentheses in the code that is not necessary so that the code will works, but that's help the developper to understand better the code.

Now we can see an example of `true positive` that has to be solved:

In the file : `\commons-math\commons-math-legacy\src\test\java\org\apache\commons\math4\legacy\linear\SingularValueDecompositionTest.java`
We have line 272 in a try, an object "DataInputStream" that is closed at the end of the try but never closed in the catch. So if the code goes on the catch, it will never be closed.

Here the code : 
``` java
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
