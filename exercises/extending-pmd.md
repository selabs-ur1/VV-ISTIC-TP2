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
* Le nouveau ruleset réalisé à partir de PMD Designer :
```xml
<?xml version="1.0"?>

<ruleset name="Custom Rules"
    xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">

    <description>
        My custom rules
    </description>
        <rule name="NestedIfStatements"
            language="java"
            message="3 or more nested if statements"
            class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
        <description>

        </description>
        <priority>3</priority>
        <properties>
            <property name="xpath">
                <value>
        <![CDATA[
        //IfStatement/Block/IfStatement/Block/IfStatement/Block
        ]]>
                </value>
            </property>
        </properties>
        </rule>
</ruleset>
```

* La commande :
```bash
pmd check -d ./src/ -R ~/dev/ifrulset.xml -f text -r ../pmd-report
```

* Le résultat de l'exécution du check : 
```
./src/main/java/org/apache/commons/collections4/MapUtils.java:230:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/MapUtils.java:233:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/MapUtils.java:236:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/MapUtils.java:930:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/MapUtils.java:933:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1245:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1249:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1250:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1252:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1294:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1326:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1372:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1381:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1398:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:1407:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:2121:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:2123:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:2146:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:2148:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/comparators/ComparatorChain.java:208:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/comparators/ComparatorChain.java:210:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/iterators/CollatingIterator.java:278:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/iterators/ObjectGraphIterator.java:246:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/iterators/ObjectGraphIterator.java:248:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/map/LRUMap.java:252:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/map/LRUMap.java:263:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:1643:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:1991:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:2054:	NestedIfStatements:	3 or more nested if statements
./src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:2056:	NestedIfStatements:	3 or more nested if statements
```

