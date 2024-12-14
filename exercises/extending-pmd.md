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
Avec cette règle xml qui vérifie les 3 if imbriqués,
```bash
pmd check -f text -R /home/kenzo/Documents/M2_cours_TP/VV/VV-ISTIC-TP2/exercice3.xml -d ../commons-collections/ -r report_ex3
```
j'obtiens ce résultat :

```
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1862:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1864:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1864:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1866:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1866:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1866:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1868:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1868:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1868:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1868:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/MapUtils.java:230:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/MapUtils.java:233:	exercice3:	Triple boucle if détectée
../commons-collections/src/main/java/org/apache/commons/collections4/MapUtils.java:236:	exercice3:	Triple boucle if détectée
```
une partie du fichier report_ex3
Il y a donc des triples boules if dans le fichier.