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

Voici notre rule XML :

    <rule name="AvoidNestedIf"
          language="java"
          maximumLanguageVersion="17"
          message="There are three or more nested ifs in your method"
          class="net.sourceforge.pmd.lang.rule.XPathRule">
        <description>
            <![CDATA[Detects three or more nested if statements in a method.]]>
        </description>
        <priority>1</priority>
        <properties>
            <property name="version" value="2.0"/>
            <property name="xpath">
                <value>
                    <![CDATA[
               //IfStatement[count(.//IfStatement) >= 2]                ]]>
                </value>
            </property>
        </properties>
    </rule>

Le soucis, c'est que ici les ifs qui sont suivis par des else ifs sont remontés car ils appartiennent au même if statement.

Exemple : 

if (radius > 0.019 && radius <= 0.076) {
            hashSize = "z";
        } else if (radius > 0.076 && radius <= 0.61){
            hashSize = "zz";
        } else if (radius > 0.61 && radius <= 2.4){
            hashSize = "zzz";
        }
est détecté par PMD.

Nous n'avons pas trouvé la rule exact permettant d'empecher cela :(


