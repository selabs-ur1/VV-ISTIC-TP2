# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer


### False positive

commons-collections\src\main\java\org\apache\commons\collections4\SetUtils.java:564:    LooseCoupling:  Avoid using implementation types like 'SetView'; use the interface instead

public static <E> SetView<E> union(final Set<? extends E> setA, final Set<? extends E> setB) {
        Objects.requireNonNull(setA, "setA");
        Objects.requireNonNull(setB, "setB");
        final SetView<E> bMinusA = difference(setB, setA);
        return new SetView<E>() {
            @Override
            public boolean contains(final Object o) {
                return setA.contains(o) || setB.contains(o);
            }
            @Override
            public Iterator<E> createIterator() {
                return IteratorUtils.chainedIterator(setA.iterator(), bMinusA.iterator());
            }
            @Override
            public boolean isEmpty() {
                return setA.isEmpty() && setB.isEmpty();
            }
            @Override
            public int size() {
                return setA.size() + bMinusA.size();
            }
        };
    }
    
There is no interface to use.

### True positive : 


commons-collections\src\main\java\org\apache\commons\collections4\multimap\AbstractMultiValuedMap.java:802:     ReturnEmptyCollectionRatherThanNull:    Return an empty collection rather than null.

@Override
public Collection<V> remove(final Object key) {
    final Collection<V> collection = decoratedMap.remove(key);
    if (collection == null) 
    {
        return null;
    }

This method is supposed to return a Collection but returns a null, so the code should be modified to return at least an empty Collection.
