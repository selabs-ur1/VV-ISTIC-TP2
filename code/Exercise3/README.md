# Code of your exercise
Authors: Dufeil Jaufret & Gentile Brian


Put here all the code created for this exercise

```xml
<rule name="AvoidDeeplyNestedIfStatements"
      language="java"
      message="Avoid deeply nested if statements"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
        <description>
            Avoid creating deeply nested if statements since they are hard to read and error-prone.
        </description>
        <priority>3</priority>
        <properties>
            <property name="xpath">
                <value>
                    <![CDATA[
                    //IfStatement[count(ancestor::IfStatement) >= 2]
                    ]]>
                </value>
            </property>
        </properties>
    </rule>
```