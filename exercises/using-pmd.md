# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

../VV/commons-collections/src/main/java/org/apache/commons/collections4/list/AbstractLinkedList.java:877:	CompareObjectsWithEquals:	Use equals() to compare object references.

```java
public int indexOf(final Object value) {
    int i = 0;
    for (Node<E> node = header.next; node != header; node = node.next) {
        if (isEqualValue(node.getValue(), value)) {
            return i;
        }
        i++;
    }
    return CollectionUtils.INDEX_NOT_FOUND;
}
```

l'erreur est sur la ligne 12 qui comporte le `if (isEqualValue(node.getValue(), value)) {`, le report propose alors de corriger en utilisant equals() à la place de isEqualValue. Mais c'est un false positive étant donné que plus tard, on voit que la méthode a été crée pour être une super class donc être override. 

```java
  /**
     * Compares two values for equals.
     * This implementation uses the equals method.
     * Subclasses can override this to match differently.
     *
     * @param value1  the first value to compare, may be null
     * @param value2  the second value to compare, may be null
     * @return true if equal
     */
    protected boolean isEqualValue(final Object value1, final Object value2) {
        return Objects.equals(value1, value2);
    }
```