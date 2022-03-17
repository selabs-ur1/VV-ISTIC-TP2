# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer
Analyse du projet Apache commons math, avec le ruleset quickstart
````
pmd.bat -d commons-math-master -R rulesets/java/quickstart.xml
````

###Exemple de vrai positif
````
\commons-math-master\commons-math-legacy\src\test\maxima\special\RealFunctionValidation\RealFunctionValidation.java:330:        CloseResource:  Ensure that resources like this DataInputStream object are closed after use
````
````java
public static void run(final ApplicationProperties properties)
        throws IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, IOException {

        for (int i = properties.from; i < properties.to; i += properties.by) {
            final String inputFileName;
            inputFileName = String.format(properties.inputFileMask, i);
            final String outputFileName;
            outputFileName = String.format(properties.outputFileMask, i);

            final DataInputStream in;
            in = new DataInputStream(new FileInputStream(inputFileName));
            final DataOutputStream out;
            out = new DataOutputStream(new FileOutputStream(outputFileName));

            final SummaryStatistics stats;
            stats = assessAccuracy(properties.method, in, out);

            System.out.println("input file name = " + inputFileName);
            System.out.println("output file name = " + outputFileName);
            System.out.println(stats);

            //Il faut rajouter
            in.close();
            out.close();
        }
    }
````

###Exemple de faux négatif
````
\commons-math-master\src\userguide\java\org\apache\commons\math4\userguide\genetics\PolygonChromosome.java:106: UselessParentheses:     Useless parentheses.
````
Ligne concernée :
````java
return (1.0 - diff / (width * height * 3.0 * 256));
````
Il n'est pas nécessaire de supprimer les parenthèses inutiles. Au contraire cela améliore la compréhension du code.