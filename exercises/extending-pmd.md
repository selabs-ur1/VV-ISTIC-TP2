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

Voici la règle créée pour détecter les if statements :

```xml
<rule name=""
      language="java"
      message=""
      class="net.sourceforge.pmd.lang.rule.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="version" value="2.0"/>
      <property name="xpath">
         <value>
<![CDATA[
//IfStatement[.//IfStatement[.//IfStatement]]
]]>
         </value>
      </property>
   </properties>
</rule>
```

La règle est pour les if inclus les uns dans les autres est donc : ``//IfStatement[.//IfStatement[.//IfStatement]]``.


Et voici un code d'exemple utilisé pour la vérification de la nouvelle règle :

```java
public class WejdeneClass {
    public String horsDeMaVue(String capter, String anissa, String calecons){
        if (capter == "T'as cru qu'j'allais pas capter?") {
            if (anissa == "Tu parles avec une Anissa"){
                if (calecons == "Tu prends tes caleçons sales") {
                    return new String("Hors de ma vue");
                }
            }
        }
    }
}
```
