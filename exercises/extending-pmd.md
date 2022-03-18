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

#### Définition XML de la règle
```xml
<rule name="au_moins_trois_if_imbriques"
      language="java"
      message="contenir au moins trois ifs imbriqués"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
    <description>

    </description>
    <priority>3</priority>
    <properties>
        <property name="version" value="2.0"/>
        <property name="xpath">
            <value>
                <![CDATA[//IfStatement//IfStatement//IfStatement]]>
            </value>
        </property>
    </properties>
</rule>
```
#### Exemple d'application 
```Java
class Foo {
  void baseCyclo() {                
    highCyclo();
  }

  void highCyclo() {              
    int x = 0, y = 2;
    boolean a = false, b = true;

    if (a && (y == 1 ? b : true)) { 
      if (y == x) {               
        while (true) {             
          if (x++ < 20) {           
            break;                  
          }
        }
      } else if (y == t && !d) {    
        x = a ? y : x;              
      } else {
        x = 2;
      }
    }
  }
}
````
L'expression XPATH (``//IfStatement//IfStatement//IfStatement``) retourne ici 2 noeuds :

``if (x++ < 20) {           
...              
}
``

``if (y == t && !d) {    
...        
}
``