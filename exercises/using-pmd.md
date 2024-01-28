# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

A bug returned is the following : src\main\java\org\apache\commons\collections4\BagUtils.java:36:	ClassWithOnlyPrivateConstructorsShouldBeFinal:	This class has only private constructors and may be final

we should change the constructor of the class to final to check this bug.

