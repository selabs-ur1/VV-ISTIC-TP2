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

Voici la règle :

```
<?xml version="1.0"?>

<ruleset name="my rule"
         xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">

    <description>
        The rules prevent complex code in Java programs
    </description>

	<rule name="nestedIf"
          language="java"
          since="1.4"
          deprecated="true"
          message="three or more nested if statements is deprecated"
	  class="net.sourceforge.pmd.lang.rule.XPathRule">
		<description>
			The rule should detect the use of three or more nested if statements in Java programs
        	</description>
		<priority>1</priority>
        <properties>
            <property name="version" value="2.0"/>
            <property name="xpath">
	    <value>
//IfStatement/Statement
 /Block[count(BlockStatement)=1]/BlockStatement
  /Statement/IfStatement/Statement
 /Block[count(BlockStatement)=1]/BlockStatement
  /Statement/IfStatement

	    </value>
            </property>
        </properties>
        <example>
<![CDATA[
if (cond1) {
    ...
    if (cond2) {
        ...
        if (cond3) {
            ....
        }
    }

}

//can be 

if (cond1 && cond2 && cond3) {
    ...
}
]]>
        </example>
    </rule>
</ruleset>
```

En lançant pmd sur le projet common-lang on obtient :

commons-lang\src\main\java\org\apache\commons\lang3\CharRange.java:290:   nestedIf:       three or more nested if statements is deprecated
commons-lang\src\main\java\org\apache\commons\lang3\time\DateUtils.java:993:      nestedIf:       three or more nested if statements is deprecated

Alors qu'avec le projet common-math il y a de nombreux cas d'imbriquation de if, par exemple :

commons-math\commons-math-core\src\main\java\org\apache\commons\math4\core\jdkmath\AccurateMath.java:1721:        nestedIf:       three or more nested if statements is deprecated
