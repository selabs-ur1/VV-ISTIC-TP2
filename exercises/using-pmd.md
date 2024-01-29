# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

#### True Positif

- Apache Commons Collections:
	
```java
public static <K, V> Set<V> getValuesAsSet(final MultiValuedMap<K, V> map, final K key) {
  if (map != null) {
    final Collection<V> col = map.get(key);
    if (col instanceof Set) {
      return (Set<V>) col;
    }
    return new HashSet<>(col);
  }
  return null;
}
```
- pmd :
  
MultiMapUtils.java:154:       ReturnEmptyCollectionRatherThanNull:	Return an empty collection rather than null.

- Explanation : 

This method is supposed to return a Set but return a null, so the code should be modified to return at least an empty Set.

#### False Positif

- Apache Common Math :

```java
public static double acosh(final double a) {
  return AccurateMath.log(a + Math.sqrt(a * a - 1));
}
```

- pmd :

AccurateMath.java:699:  UnnecessaryFullyQualifiedName:  Unnecessary qualifier 'AccurateMath': 'log' is already in scope

- Explanation :

It's necessary to use the class name to call the static methods that are defined in the same class or in an inner class. For example, we cannot write log(x) instead of AccurateMath.log(x), because it would cause a confusion with the log method of the Math class.
