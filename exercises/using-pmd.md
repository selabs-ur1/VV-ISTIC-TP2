# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

J'ai choisi : https://github.com/apache/commons-collections.git

### Vrai positif :

```
@Override
        public boolean containsKey(final Object key) {
            if (!inRange(castKey(key))) {
                return false;
            }

            return AbstractPatriciaTrie.this.containsKey(key);
        }
```

Message de PMD
```
commons-collections\src\main\java\org\apache\commons\collections4\trie\AbstractPatriciaTrie.java:65:       SimplifyBooleanReturns:     This if statement can be replaced by `return !{condition} || {elseBranch};`
```
La correction du code en fonction : 

```
@Override
public boolean containsKey(final Object key) {
    return !inRange(castKey(key)) || AbstractPatriciaTrie.this.containsKey(key);
}
```
### Faux positif

```
C:\Users\...\Flat3Map.java:1233: CompareObjectsWithEquals: Use equals() to compare object references.
```
Est un faux positif, car l'on peut très bien simplement comparer les références sans avoir besoin de comparer le contenu de l'objet.