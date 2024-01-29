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

Here is the rule that I used

```
<rule name="3IfStatement" language="java" message="Usage of at least 3 nested if statements" class="net.sourceforge.pmd.lang.rule.XPathRule">
        <description>Detection of three or more nested if statements in Java code. </description>
        <priority>3</priority>
        <properties>
            <property name="xpath">
                <value>
                    <![CDATA[ //IfStatement[ancestor::IfStatement[ancestor::IfStatement]] ]]> 
                </value>
            </property>
        </properties>
    </rule>
```

### Result :

commons-collections\src\main\java\org\apache\commons\collections4\set\CompositeSet.java:376:    3IfStatement:   Usage of at least 3 nested if statements

```
   public synchronized void addComposited(final Set<E> set) {
        if (set != null) {
            for (final Set<E> existingSet : getSets()) {
                final Collection<E> intersects = CollectionUtils.intersection(existingSet, set);
                if (!intersects.isEmpty()) {
                    if (this.mutator == null) {
                        throw new UnsupportedOperationException(
                                "Collision adding composited set with no SetMutator set");
                    }
                    getMutator().resolveCollision(this, existingSet, set, intersects);
                    if (!CollectionUtils.intersection(existingSet, set).isEmpty()) {
                        throw new IllegalArgumentException(
                                "Attempt to add illegal entry unresolved by SetMutator.resolveCollision()");
                    }
                }
            }
            all.add(set);
        }
    }
```

