# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

Nous avons choisis d'effectuer l'analyse avec pmd sur le projet [Apache Commons Collections](https://github.com/apache/commons-collections). A l'aide de la commande :

```sh
pmd check -f html -R rulesets/java/quickstart.xml -d commons-collections/src/main/java >> result_pmd.html
```

**False positive :**

Dans le fichier `commons-collections/src/main/java/org/apache/commons/collections4/ListUtils.java` on retrouve un problème : `Use equals() to compare object references`. 
En utilisant "==" on compare deux objet en fonction de leurs **reference**. C'est pourquoi il est préférable d'utilise la méthode ".equals()" en la redéfinissant spécifiquement pour pouvoir comparer deux objets. Celà force donc à penser à une logique de comparaison. 

Mais ici en l'occurence dans la méthode `isEqualList(final Collection<?> list1, final Collection<?> list2)`, il s'agit d'une optimisation. En effet avant de parcourir l'ensemble des liste et de comparer chacun des éléments entres eux.
la méthode va comparer la référence des deux liste à l'aide de l'opérateur "==". Ainsi, si il s'agit de la même liste la méthode renverra true sans avoir parcouru les deux listes.



**True Positive :**


Dans le fichier `commons-collections/src/main/java/org/apache/commons/collections4/map/ConcurrentReferenceHashMap.java` aux lignes 485, 489 et 498 on retrouve le problème `Avoid assignments in operands`. Ce problème indique qu'il faut éviter les affectations dans des opérande, ce qui peut rendre le code plus compliqué à comprendre et à lire.

Pour corriger ce problème je remplacerait la méthode : 

```java
final void advance() {
    if (nextEntry != null && (nextEntry = nextEntry.next) != null) {
        return;
    }
    while (nextTableIndex >= 0) {
        if ((nextEntry = currentTable[nextTableIndex--]) != null) {
            return;
        }
    }
    while (nextSegmentIndex >= 0) {
        final Segment<K, V> seg = segments[nextSegmentIndex--];
        if (seg.count != 0) {
            currentTable = seg.table;
            for (int j = currentTable.length - 1; j >= 0; --j) {
                if ((nextEntry = currentTable[j]) != null) {
                    nextTableIndex = j - 1;
                    return;
                }
            }
        }
    }
}
```

Par celle ci :

```java
final void advance2() {
    if (nextEntry != null) {
        nextEntry = nextEntry.next;
        if(nextEntry != null){
        return;
        }
    }
    while (nextTableIndex >= 0) {
        nextEntry = currentTable[nextTableIndex--];
        if (nextEntry != null) {
            return;
        }
    }
    while (nextSegmentIndex >= 0) {
        final Segment<K, V> seg = segments[nextSegmentIndex--];
        if (seg.count != 0) {
            currentTable = seg.table;
            for (int j = currentTable.length - 1; j >= 0; --j) {
                nextEntry = currentTable[j];
                if (nextEntry != null) {
                    nextTableIndex = j - 1;
                    return;
                }
            }
        }
    }
}
``

