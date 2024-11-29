# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer
Selected project: commons-collections
### true positive:
./src/main/java/org/apache/commons/collections4/ListUtils.java:322:	CompareObjectsWithEquals:	Use equals() to compare object references.

```
...
* @see java.util.List
     * @param list1  the first list, may be null
     * @param list2  the second list, may be null
     * @return whether the lists are equal by value comparison
     */
    public static boolean isEqualList(final Collection<?> list1, final Collection<?> list2) {
        if (list1 == list2) {
            return true;
        }
...
```
In this case, the author of the code want to compare value, but == compare references.
Fix: if(list1.equals(list2))
### false positive
./src/main/java/org/apache/commons/collections4/bloomfilter/ArrayCountingBloomFilter.java:119:	UnusedPrivateMethod:	Avoid unused private methods such as 'add(int, int)'.

```
 @Override
    public boolean add(final CellExtractor other) {
        Objects.requireNonNull(other, "other");
        other.processCells(this::add);
        return isValid();
    }

    /**
     * Add to the cell for the bit index.
     *
     * @param idx the index
     * @param addend the amount to add
     * @return {@code true} always.
     */
    private boolean add(final int idx, final int addend) {
        try {
            final int updated = cells[idx] + addend;
            state |= updated;
            cells[idx] = updated;
            return true;
        } catch (final IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    String.format("Filter only accepts values in the [0,%d) range", getShape().getNumberOfBits()), e);
        }
    }
```
The private method add is used in line 32, but pmd doesn't seem to know this syntax
