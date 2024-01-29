# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer
**True positive :**
&
**False positive :** commons-collections\src\main\java\org\apache\commons\collections4\map\AbstractLinkedMap.java:565:
CompareObjectsWithEquals: Use equals() to compare object references.
PMD warns because the developper may not want to compare reference but value of reference. But in this case, the developer
actually want to compare the reference

```
    /**
     * Base Iterator that iterates in link order.
     */
    protected abstract static class LinkIterator<K, V> {

        /** The parent map */
        protected final AbstractLinkedMap<K, V> parent;
        /** The current (last returned) entry */
        protected LinkEntry<K, V> last;
        /** The next entry */
        protected LinkEntry<K, V> next;
        /** The modification count expected */
        protected int expectedModCount;

        protected LinkIterator(final AbstractLinkedMap<K, V> parent) {
            this.parent = parent;
            this.next = parent.header.after;
            this.expectedModCount = parent.modCount;
        }

        public boolean hasNext() {
            return next != parent.header;
        }
```