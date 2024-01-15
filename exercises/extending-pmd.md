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

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<ruleset name="CustomRuleset" xmlns="http://pmd.sourceforge.net/ruleset/2.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 http://pmd.sourceforge.net/ruleset_2_0_0.xsd" xsi:noNamespaceSchemaLocation="http://pmd.sourceforge.net/ruleset_2_0_0.xsd">

    <description>Custom PMD Ruleset</description>

    <rule class="net.sourceforge.pmd.lang.rule.XPathRule" name="AvoidDeeplyNestedIfStatements" language="java" since="1.0" message="Avoid deeply nested if statements">
        <description>
            Detects three or more nested if statements.
        </description>
        <priority>3</priority>
        <properties>
            <property name="xpath">
                <value>
                    <![CDATA[
                        //IfStatement[count(.//IfStatement) >= 3]
                    ]]>
                </value>
            </property>
        </properties>
    </rule>
</ruleset>
```

On remarque que ce problème de complexité revient le plus souvent sur des gros projets (ex: commons-lang, commons-math) et plus rarement sur des petits projets (tp java).