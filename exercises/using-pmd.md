# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

input/commons-collections-master/src/test/java/org/apache/commons/collections4/trie/PatriciaTrieTest.java:359: LocalVariableNamingConventions: The final local variable name 'char_b' doesn't match '[a-z][a-zA-Z0-9]*'
La variable "char_b" ne respecte pas les conventions de nommage car elle contient un "_". Pour résoudre ce problème et respecter les conventions de nommage, il est recommandé de renommer la variable. Par exemple, elle peut être renommée en "charB".

input/commons-collections-master/src/test/java/org/apache/commons/collections4/trie/UnmodifiableTrieTest.java:24: UnnecessaryImport: Unused import 'org.apache.commons.collections4.Unmodifiable'
Nous pouvons remarquer la présence d'un UnnecessaryImport ce qui signifie qui un import est déclarer
mais n'est pas utilisé dans la classe dans laquelle elle est importé. 
Alors que si, elle est utilisée, c'est donc un faux positif.