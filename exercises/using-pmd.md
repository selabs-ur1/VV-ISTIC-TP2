# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

Vrais positif:

Message: "Flat3Map.java:1233:	ConsecutiveAppendsShouldReuse:	StringBuffer (or StringBuilder).append is called consecutively without reusing the target variable."

Cette issue est true positive car elle indique un problème de performance potentiel, en effet, l'appel à plusieurs reprise de append sans réutiliser la variable cible peut entrainer des allocations et réallocations inutiles de mémoire.
De nos jours, il s'agit d'une optimisation mineure, mais elle reste pertinente.

```java

// Code avant modification
buf.append(key3 == this ? "(this Map)" : key3);
buf.append('=');
buf.append(value3 == this ? "(this Map)" : value3);
buf.append(CollectionUtils.COMMA);

// Code après modification
tempBuf.append(key3 == this ? "(this Map)" : key3)
    .append('=')
    .append(value3 == this ? "(this Map)" : value3)
    .append(CollectionUtils.COMMA);

```

Faux positif:

Message: "map/IdentityMap.java:173:	CompareObjectsWithEquals:	Use equals() to compare object references."

```java

// Code montrant le faux positif
@Override
    protected boolean isEqualKey(final Object key1, final Object key2) {
        return key1 == key2;
    }

```

Il s'agit surrement d'un faux positif car la méthode isEqualKey est censée comparer les références des objets et non leur contenu. Dans ce cas, l'utilisation de == est correcte.