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

Authors: Dufeil Jaufret & Gentile Brian

This is my rule in xml : 
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



I tested this rule with the command : 
```shell
pmd check -f html -R myruleset.xml -d C:/Users/jaufr/Documents/VV/maths -r rendu html
```

The report detected the rule on the file AccurateMath.javacat the line 869.

```java
 if (x < 0.0) {

            // We don't check against intVal here as conversion of large negative double values
            // may be affected by a JIT bug. Subsequent comparisons can safely use intVal
            if (x < -746d) {
                if (hiPrec != null) {
                    hiPrec[0] = 0.0;
                    hiPrec[1] = 0.0;
                }
                return 0.0;
            }

            if (intVal < -709) {
                /* This will produce a subnormal output */
                final double result = exp(x + 40.19140625, extra, hiPrec) / 285040095144011776.0;
                if (hiPrec != null) {
```

the line 869 is : 
```java
if (hiPrec != null) {
```


So we can see that there is 3 nested if that could be reduced. We could make only one if by assembling the 2nd and the 3rd in this example.
