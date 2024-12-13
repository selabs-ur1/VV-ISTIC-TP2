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

Voici la règle que nous avons écrite pour détecter si il y a 3 if ou plus qui sont imbriqués : 

    <?xml version="1.0" encoding="UTF-8"?>
            <ruleset xmlns="http://pmd.sourceforge.net/ruleset/2.0.0" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
             xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 
                             http://pmd.sourceforge.net/ruleset_2_0_0.xsd"
             name="Custom Ruleset"
             description="Custom ruleset to detect deeply nested if statements in Java code.">

        <rule name="AvoidDeeplyNestedIfs"
              language="java"
              message="Avoid using three or more nested if statements to keep code simple and maintainable."
              class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
        
            <description>
                This rule detects deeply nested if statements (three or more levels). 
                It is designed to promote code simplicity and readability.
            </description>

            <priority>2</priority>

            <properties>
                <property name="xpath">
                    <value><![CDATA[
                        //IfStatement[
                        IfStatement[
                            parent::IfStatement/IfStatement[
                                parent::IfStatement
                            ]
                        ]
                    ]
                    ]]></value>
                </property>
            </properties>   

        </rule>
    </ruleset>
