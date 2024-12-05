# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer
```
<ruleset name="Custom Ruleset"
         xmlns="https://pmd.github.io/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://pmd.github.io/ruleset/2.0.0 https://pmd.github.io/ruleset_2_0_0.xsd"
         xmlns:java="https://pmd.github.io/ruleset/java/2.0.0">

    <description>
        This ruleset contains custom rules to detect and prevent complex code patterns.
    </description>
    <rule name="NoTripleNestedIf"
          language="java"
          message="Simplify your condition"
          class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
        <description>
            Avoid using deeply nested if statements. They increase code complexity and reduce readability. Consider refactoring.
        </description>
        <priority>3</priority>
        <properties>
            <property name="xpath">
                <value>
                    <![CDATA[
//IfStatement[.//Block[.//IfStatement[.//Block[.//IfStatement]]]]
    ]]>
                </value>
            </property>
        </properties>
    </rule>
</ruleset>
```
In commons-collections:
```
./src/main/java/org/apache/commons/collections4/MapUtils.java:227:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/MapUtils.java:927:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1153:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1217:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1238:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1277:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1361:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:2107:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:2132:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/comparators/ComparatorChain.java:205:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/iterators/CollatingIterator.java:271:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/iterators/ObjectGraphIterator.java:242:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/map/CompositeMap.java:194:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/map/ConcurrentReferenceHashMap.java:837:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/map/ConcurrentReferenceHashMap.java:960:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/map/ConcurrentReferenceHashMap.java:1024:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/map/LRUMap.java:241:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/set/CompositeSet.java:193:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:1640:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:1989:	NoTripleNestedIf:	Simplify your condition
./src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:2041:	NoTripleNestedIf:	Simplify your condition
./src/test/java/org/apache/commons/collections4/map/AbstractMapTest.java:1400:	NoTripleNestedIf:	Simplify your condition
./src/test/java/org/apache/commons/collections4/map/AbstractMapTest.java:1536:	NoTripleNestedIf:	Simplify your condition
./src/test/java/org/apache/commons/collections4/map/AbstractMapTest.java:1625:	NoTripleNestedIf:	Simplify your condition
./src/test/java/org/apache/commons/collections4/map/AbstractMapTest.java:1844:	NoTripleNestedIf:	Simplify your condition
./src/test/java/org/apache/commons/collections4/map/AbstractMapTest.java:1979:	NoTripleNestedIf:	Simplify your condition
```
I checked half of these, there seems to be no false positive

In commons-math: 98 violations
