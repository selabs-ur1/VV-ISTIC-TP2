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

Here's a new ruleset including the asked rule.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ruleset name="extendedPMD"
         xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">
    <description>Extended PMD.</description>

    <rule name="NestedIfs"
        language="java"
        message="Your code shouldn't have more than 2 nested if statement."
        class="net.sourceforge.pmd.lang.rule.XPathRule">
        <description>
            Detect if your code have 3 or more nested if statement.
        </description>
        <priority>3</priority>
        <properties>
            <property name="version" value="3.1"/>
            <property name="xpath">
                <value>
                    <![CDATA[
                    //IfStatement//IfStatement//IfStatement
                    ]]>
                </value>
            </property>
        </properties>
    </rule>

</ruleset>
```

When we check with this command:
`pmd check -d commons-collections/src -R extendedPMD.xml -f text`

We find few examples, such as:
`commons-collections/src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:890:  NestedIfs:  Your code shouldnt have more than 2 nested if statement.`


And this the code sample that trigger this warning.
```java
if (lengthInBits == 0) {
    if (!root.isEmpty()) {
        // If data in root, and more after -- return it.
        if (size() > 1) {
            return nextEntry(root);
        }
        // If no more after, no higher entry.
        return null;
    }
    // Root is empty & we want something after empty, return first.
    return firstEntry();
}
```
