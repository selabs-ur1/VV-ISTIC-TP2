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

Nous avons détecté le code suivant : 

![image](https://github.com/TDerudder/VV-ISTIC-TP2/assets/113186590/7a143325-62a2-47ff-8429-bc9236301d4e)

Avec l'ajout de la règle suivante :

![image](https://github.com/TDerudder/VV-ISTIC-TP2/assets/113186590/2da8520f-860e-4bed-bc5d-7b9715fdf826)

Nous obtenons bien :

![image](https://github.com/TDerudder/VV-ISTIC-TP2/assets/113186590/bfdd53c0-ad1f-4874-9c0a-126298ebdc04)


