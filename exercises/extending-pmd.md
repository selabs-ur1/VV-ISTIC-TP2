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
<ruleset name="my rule"
         xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">

	<rule name="if"
          language="java"
          since="1.4"
          deprecated="true"
          message="3 imbriquations de if n'est pas recommandé"
	  class="net.sourceforge.pmd.lang.rule.XPathRule">
		<priority>3</priority>
        <properties>
            <property name="version" value="2.0"/>
            <property name="xpath">
	    <value>
//IfStatement[@Else= false()]/Statement
 /IfStatement[@Else= false()]/Statement
 /IfStatement
 |
//IfStatement[@Else= false()]/Statement
 /Block[count(BlockStatement)=1]/BlockStatement
  /Statement/IfStatement[@Else= false()]/Statement
 /Block[count(BlockStatement)=1]/BlockStatement
  /Statement/IfStatement[@Else= false()]

	    </value>
            </property>
        </properties>
    </rule>
</ruleset>
```

Aucun projet n’a 3 if imbriqué avec la forme 

```
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
