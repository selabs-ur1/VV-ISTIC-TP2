# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer

Issue 'AbstractHashedMap.java:1296:  CloneMethodMustBePublic:        clone() method must be public if the class implements Cloneable.' is a false positive as AbstractHashedMap is an interface,  the children classes will implement this interface and clonable and have a public clone method

```java=
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractHashedMap<K, V> clone() {
        try {
            final AbstractHashedMap<K, V> cloned = (AbstractHashedMap<K, V>) super.clone();
            cloned.data = new HashEntry[data.length];
            cloned.entrySet = null;
            cloned.keySet = null;
            cloned.values = null;
            cloned.modCount = 0;
            cloned.size = 0;
            cloned.init();
            cloned.putAll(this);
            return cloned;
        } catch (final CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
```

Issue 'MapUtils.java:882:        ReturnEmptyCollectionRatherThanNull:    Return an empty collection rather than null.' is a true positive for me as usage of this method could generate NullPointerExceptions or force developpers to null check the result, instead of returning null, we could return an empty map.

```java=
    public static <K> Map<?, ?> getMap(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer instanceof Map) {
                return (Map<?, ?>) answer;
            }
        }
        //return null;
        return Collections.<?,?>emptyMap();
    }
```
