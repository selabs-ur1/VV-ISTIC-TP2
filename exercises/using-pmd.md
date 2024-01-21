# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

True Positive:

Here is a classic example of stacking numerous asserts in one test class:

///////////////////////////////////////////////////
/**
 * Tests {@link ObjectUtils#allNotNull(Object...)}.
 */
@Test
public void testAllNotNull() {
   assertFalse(ObjectUtils.allNotNull((Object) null));
   assertFalse(ObjectUtils.allNotNull((Object[]) null));
   assertFalse(ObjectUtils.allNotNull(null, null, null));
   assertFalse(ObjectUtils.allNotNull(null, FOO, BAR));
   assertFalse(ObjectUtils.allNotNull(FOO, BAR, null));
   assertFalse(ObjectUtils.allNotNull(FOO, BAR, null, FOO, BAR));

   assertTrue(ObjectUtils.allNotNull());
   assertTrue(ObjectUtils.allNotNull(FOO));
   assertTrue(ObjectUtils.allNotNull(FOO, BAR, 1, Boolean.TRUE, new Object(), new Object[]{}));
}
///////////////////////////////////////////////////    

The problem with this way of testing is that if one of the asserts fails, it won't be clear which one didn't pass; all we would know is that "The class 'testAllNotNull()' didn't pass." There is not even a custom error message to each assert that would hint to which test did not pass.

The solution to this issue is simply separating every case scenario into different test class, preferably with an explicit name for each one of them.

_________________________________________________________________________________________

False Positive: 

/////////////////////////////////////////////////// 
public class MultiSetUtilsTest {

   private String[] fullArray;
   private MultiSet<String> multiSet;

   @BeforeEach
    public void setUp() {
        fullArray = new String[]{
            "a", "a", "b", "c", "d", "d", "d"
        };
        multiSet = new HashMultiSet<>(Arrays.asList(fullArray));
    }

   /**
     * Tests {@link MultiSetUtils#emptyMultiSet()}.
     */
    @Test
    public void testEmptyMultiSet() {
        final MultiSet<Integer> empty = MultiSetUtils.emptyMultiSet();
        assertEquals(0, empty.size());
        assertThrows(UnsupportedOperationException.class, () -> empty.add(55),
              "Empty multi set must be read-only");
    }

   /**
     * Tests {@link MultiSetUtils#unmodifiableMultiSet(org.apache.commons.collections4.MultiSet) ()}.
     */
    @Test
    public void testUnmodifiableMultiSet() {
        final MultiSet<String> unmodifiable = MultiSetUtils.unmodifiableMultiSet(multiSet);
        assertEquals(multiSet, unmodifiable);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add("a"),
               "Empty multi set must be read-only");
        assertThrows(NullPointerException.class, () -> MultiSetUtils.unmodifiableMultiSet(null),
                "Expecting NPE");
    }
    ...
}
/////////////////////////////////////////////////// 


The above example was flagged by PMD while stating the issue as being "Declaring variables without initialization". Although the variable 'fullArray" never changes the value during the tests (and, therefore, could have been initialized immediately), there could be a couple of reasons why the choice was made to initialize it in the '@BeforeEach' section. One of those reasons could be the readability and the maintainability of the code: in more complex cases where there are numerous variables to be defined before testing, it is a good practice to initialize all the variable in the 'setup' section; in this way, the code is better organized and goes well in respect with the 'Separation of Concernes', where each block or region of code has a single and clear objective. Furthermore, some testing frameworks like JUnit (which is used in this example) have established conventions like initializing all the variables in the '@BeforeEach' block; by doing so, the developer adheres to the conventions of the testing framework and produces a code that is better structured.


