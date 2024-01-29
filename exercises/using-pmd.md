# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

`pmd check -d commons-collections/src -R quickstart.xml -f text`

True positive:
`commons-collections/src/main/java/org/apache/commons/collections4/map/AbstractReferenceMap.java:782:	OneDeclarationPerLine:	Use one line for each declaration, it enhances code readability.`

For a better readability each variable should be declared on its own line.
Correction:
```java
// These Object fields provide hard references to the
// current and next entry; this assures that if hasNext()
// returns true, next() will actually return a valid element.
K currentKey;
K nextKey;
V currentValue;
V nextValue;
```

False positive:
`commons-collections/src/main/java/org/apache/commons/collections4/functors/FunctorUtils.java:50:	ReturnEmptyCollectionRatherThanNull:	Return an empty collection rather than null.`

According to PMD this method should be returning an empty collection instead of null, but the specification says that the method returns a copy of the parameters and in the current implementation if the parameter if null the method is returning null.
