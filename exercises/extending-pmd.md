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

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use

## Answer
Avec la règle rajoutée j’ai trouvé le code suivant qui imbrique 3 if.
```
public static <K> Number getNumber(final Map<? super K, ?> map, final K key) {
   if (map != null) {
       final Object answer = map.get(key);
       if (answer != null) {
           if (answer instanceof Number) {
               return (Number) answer;
           }
           if (answer instanceof String) {
               try {
                   final String text = (String) answer;
                   return NumberFormat.getInstance().parse(text);
               } catch (final ParseException e) { // NOPMD
                   // failure means null is returned
               }
           }
       }
   }
   return null;
}

```



