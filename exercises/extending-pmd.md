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

Voici ci-dessous, le fichier ruleset.xml créé pour détecter l’utilisation de trois (ou plus) instructions if imbriquées dans des programmes Java.

```xml
<ruleset name="IfStatement rules"
    xmlns="http://pmd.sourceforge.net/ruleset/2.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd">

    <description>
        If Statements rules
    </description>

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
                    //IfStatement/Statement/Block//IfStatement/Statement/Block//IfStatement/Statement/Block
                    ]]>
                </value>
            </property>
        </properties>
    </rule>
</ruleset>
```

Commande Windows :
```
.\pmd.bat -d C:\Users\Erwann\IdeaProjects\commons-math\commons-math-core\src\main\java\ -f html -R 'C:\Users\Erwann\Desktop\M2 CCNa\VV\ruleset.xml' > 'C:\Users\Erwann\Desktop\M2 CCNa\VV\errorFile.html'
```

Commande Linux :
```
./run.sh pmd -d ~/Documents/Rennes1_CCNA/VV/TP2/commons-math/commons-math-core/src/main/java/ -f html -R ~/Documents/Rennes1_CCNA/VV/TP2/ruleset.xml > ~/Documents/Rennes1_CCNA/VV/TP2/errorFile.html
```

Par exemple, si on applique cette règle sur le répertoire commons-math-core du projet Apache Commons Math, on détecte 56 cas où il y a au moins trois instructions if imbriquées.
En effectuant la même opération sur le répertoire commons-math-neuralnet, 9 cas sont détectés.
Compte-tenu du nombre de détections sur le répertoire commons-math-core, il y a certainement des améliorations possibles à apporter au code.

Le fichier NeuronSquareMesh2D dans le répertoire commons-math-neuralnet contient 8 cas à lui seul. Après relecture du fichier, on s'aperçoit qu'il y a énormément de conditions if imbriquées. Ce code est peu compréhensible et très peu commenté. Il est assez facile de conclure que ce code mérite d'être relu, revu et simplifié.  
