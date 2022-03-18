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
<rule name="NoMoreThreeIfs"
      language="java"
      message="More than 3 nested if !"
      class="net.sourceforge.pmd.lang.rule.XPathRule" >
    <description>
        Check if there is 3 if at a time
    </description>
    <priority>3</priority>
    <properties>
        <property name="xpath">
            <value>
            <![CDATA[
            //IfStatement/Statement
            /Block[count(BlockStatement)=1]
            /BlockStatement/Statement/IfStatement/Statement
            /Block[count(BlockStatement)=1]
            /BlockStatement/Statement/IfStatement
            ]]>
            </value>
        </property>
    </properties>
 </rule>
 ```


There is no result for commons-collections, but when we shrink it to two, there are some : 

/home/tanguy/Téléchargements/commons-collections/src/main/java/org/apache/commons/collections4/map/Flat3Map.java:522:	NoMoreThreeIfs:	More than 3 if in one another!
/home/tanguy/Téléchargements/commons-collections/src/main/java/org/apache/commons/collections4/sequence/SequencesComparator.java:267:	NoMoreThreeIfs:	More than 3 if in one another!
/home/tanguy/Téléchargements/commons-collections/src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:356:	NoMoreThreeIfs:	More than 3 if in one another!

