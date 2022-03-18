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

We generated this rule in PMD's rule descriptor 

```xml=
<rule name="TripleNestedIf"
      language="java"
      message="Triple nested if is deprecated, try call an external method"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="version" value="2.0"/>
      <property name="xpath">
         <value>
<![CDATA[
//IfStatement//IfStatement//IfStatement
]]>
         </value>
      </property>
   </properties>
</rule>
```


We tested it within the editor before running it on projects.

Then we created a ruleset file cotaining only this rule and we ran the analyse on the apache's collection project and got those results summary, as it's the only rule in our ruleset we can count how much triple nested ifs are present in the project

```
org.apache.commons.collections4.MapUtils : 8
org.apache.commons.collections4.trie.AbstractPatriciaTrie : 3
org.apache.commons.collections4.map.Flat3Map : 15
org.apache.commons.collections4.set.CompositeSet : 2
org.apache.commons.collections4.comparators.ComparatorChain : 1
org.apache.commons.collections4.list.AbstractListTest : 2
org.apache.commons.collections4.iterators.CollatingIterator : 1
org.apache.commons.collections4.map.LRUMap : 3
org.apache.commons.collections4.bidimap.TreeBidiMap : 14
org.apache.commons.collections4.list.CursorableLinkedList$Cursor : 1
org.apache.commons.collections4.CollectionUtils : 4
org.apache.commons.collections4.sequence.SequencesComparator : 1
org.apache.commons.collections4.map.AbstractReferenceMap$ReferenceEntry : 1
org.apache.commons.collections4.map.CompositeMap : 1
org.apache.commons.collections4.map.AbstractMapTest : 2
org.apache.commons.collections4.iterators.ObjectGraphIterator : 1
```
