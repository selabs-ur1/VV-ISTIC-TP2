# Détecter les if imbriqués
````xml
<?xml version="1.0"?>

<ruleset name="Imbriquation de if"
         xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">

    <description>
        My custom rules
    </description>

    <rule name="EviterIfImbriqués"
          language="java"
          message="Trop de if imbriqués"
          class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
        <description>
            Detects if statements nested beyond a threshold.
        </description>
        <priority>3</priority>
        <properties>
            <property name="xpath">
                <value>
                    //IfStatement[
                    count(ancestor::IfStatement) >= 2
                    ]
                </value>
            </property>
        </properties>
    </rule>
</ruleset>
````
On compte 2 ancêtres ou plus car le premier if n'a pas d'ancêtre de type `IfStatement`.

### Voici le résultat
![img.png](images%2Fimg.png)