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
code tester:

public class NestedIfStatementsRule extends AbstractJavaRule {

   public main(){
    if(true){
        if(true){
            for(int i=0 ;i<10;i++){
            }
            if(true){
            }
            if(true){
            }
        }    
    }    
}
}


Utilisez votre règle avec différents projets et décrivez vos résultats ci-dessous

    Règle ajouter:

    XPATH : //IfStatement[descendant::IfStatement[descendant::IfStatement]]


    XML:

    <?xml version="1.0"?>
<ruleset name="CustomRuleSet">
    <description>Custom PMD Rule Set</description>
    <rule name="TreeImbrickedIf"
        language="java"
        message="!!! we have 3 imbricked IfStatement"
        class="net.sourceforge.pmd.lang.rule.XPathRule">
    <description>

    </description>
    <priority>3</priority>
    <properties>
        <property name="version" value="3.1"/>
        <property name="xpath">
            <value>
    <![CDATA[
    //IfStatement[descendant::IfStatement[descendant::IfStatement]]
    ]]>
            </value>
        </property>
    </properties>
    </rule>
</ruleset>





    Résultat obtenu:

    pmd check -f text -R /home/arthurlair/Documents/Universite/M2_ILA/ProjGit/VV/VV-ISTIC-TP2/custom_rule.xml -d /home/arthurlair/Documents/Universite/M2_ILA/ProjGit/VV/commons-collections/src/main/java

    pmd check -f text -R /home/arthurlair/Documents/Universite/M2_ILA/ProjGit/VV/VV-ISTIC-TP2/custom_rule.xml -d /home/arthurlair/Documents/Universite/M2_ILA/ProjGit/VV/VV-ISTIC-TP2/testCode/


    ```shell
    [main] INFO net.sourceforge.pmd.cli - Log level is at INFO
    [main] WARN net.sourceforge.pmd.cli - Progressbar rendering conflicts with reporting to STDOUT. No progressbar will be shown. Try running with argument -r <file> to output the report to a file instead.
    [main] WARN net.sourceforge.pmd.cli - This analysis could be faster, please consider using Incremental Analysis: https://docs.pmd-code.org/pmd-doc-7.0.0-rc4/pmd_userdocs_incremental_analysis.html
    /home/arthurlair/Documents/Universite/M2_ILA/ProjGit/VV/VV-ISTIC-TP2/testCode/main.java:6:      TreeImbrickedIf:        !!! we have 3 imbricked IfStatement
    ```
  
    

