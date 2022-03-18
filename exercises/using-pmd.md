# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer

PMD found lot ShortVariableName issue, wich should be solved for more clarity of what are their purpose for easier learning by new developpers :

/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/splitmap/TransformedSplitMapTest.java:53:	ShortVariable:	Avoid variables with short names like k
/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/splitmap/TransformedSplitMapTest.java:54:	ShortVariable:	Avoid variables with short names like v
/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/splitmap/TransformedSplitMapTest.java:68:	ShortVariable:	Avoid variables with short names like sz
/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/splitmap/TransformedSplitMapTest.java:103:	ShortVariable:	Avoid variables with short names like k
/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/splitmap/TransformedSplitMapTest.java:115:	ShortVariable:	Avoid variables with short names like in
/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/splitmap/TransformedSplitMapTest.java:135:	ShortVariable:	Avoid variables with short names like in

I would resolve them by changing their name to a more explicit one  

However, there is also some OnlyOneReturn :

/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/set/AbstractSortedSetTest.java:277:	OnlyOneReturn:	A method should have only one exit point, and that should be the last statement in the method
/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/set/AbstractSortedSetTest.java:279:	OnlyOneReturn:	A method should have only one exit point, and that should be the last statement in the method
/home/tanguy/Téléchargements/commons-collections/src/test/java/org/apache/commons/collections4/set/AbstractSortedSetTest.java:281:	OnlyOneReturn:	A method should have only one exit point, and that should be the last statement in the method

i would ignore them because they are easy to read and fix if there is an issue, plus sometimes it can be harder to carry a variable until the end of a function, and for example returning something if a data is null makes the function work properly, and permit to handle crashes 
