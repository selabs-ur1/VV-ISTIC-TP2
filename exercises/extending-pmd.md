# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

Test de ma regle : 
```
public class NestedIfTest {
    public void testNestedIf(int a, int b, int c) {
        if (a > 0) {
            if (b > 0) {
                if (c > 0) {
                    System.out.println("All values are positive!");
                }
            }
        }
    }
}
```
Règle :
```
<rule name="IfRule"
      language="java"
      message="Number of imbriquation of if more than 3"
      class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="xpath">
         <value>
<![CDATA[
//IfStatement[descendant::Block/IfStatement[descendant::Block/IfStatement]]
]]>
         </value>
      </property>
   </properties>
</rule>
```

En utilisant la règle dans https://github.com/apache/commons-collections.git on observe :

```
C:\Users\alexa\OneDrive\Bureau\Cours\TMP\commons-collections\src\main\java\org\apache\commons\collections4\trie\AbstractPatriciaTrie.java:2041: IfRule: Number of imbriquation of if more than 3
```
```
if (!KeyAnalyzer.isOutOfBoundsIndex(bitIndex)) {
            if (KeyAnalyzer.isValidBitIndex(bitIndex)) { // in 99.999...9% the case
                /* NEW KEY+VALUE TUPLE */
                final TrieEntry<K, V> t = new TrieEntry<>(key, value, bitIndex);
                addEntry(t, lengthInBits);
                incrementSize();
                return null;
            }
            if (KeyAnalyzer.isNullBitKey(bitIndex)) {
                // A bits of the Key are zero. The only place to
                // store such a Key is the root Node!

                /* NULL BIT KEY */
                if (root.isEmpty()) {
                    incrementSize();
                } else {
                    incrementModCount();
                }
                return root.setKeyValue(key, value);

            }
            if (KeyAnalyzer.isEqualBitKey(bitIndex) && found != root) { // NOPMD
                incrementModCount();
                return found.setKeyValue(key, value);
            }
        }
```