# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

PMD a identifié une erreur potentielle dans le code, liée à une opération d'assignation dans une condition. Plus précisément, l'opération "i++" est utilisée à l'intérieur d'une condition if (cells[i++] != 0). Cette pratique peut être source de confusion et pourrait être à l'origine de comportements indésirables.

Pour résoudre cette vraie positive, la modification recommandée est de séparer l'opération d'incrémentation de la condition, comme illustré ci-dessous :

Avant :
if (cells[i++] != 0) {
value |= BitMap.getLongBit(k);
}

Après :
if (cells[i] != 0) {
value |= BitMap.getLongBit(k);
}
i++;

Cette modification rend le code plus lisible et pourrait aider à prévenir des erreurs potentielles liées à l'opération d'assignation dans une condition, comme indiqué par PMD. Assurez-vous de tester les modifications pour garantir le bon fonctionnement du programme.

Dans ce code :
```
@Test
public void testFirstIteratorIsEmptyBug() {
   final List<String> empty = new ArrayList<>();
   final List<String> notEmpty = new ArrayList<>();
   notEmpty.add("A");
   notEmpty.add("B");
   notEmpty.add("C");
   final LazyIteratorChain<String> chain = new LazyIteratorChain<String>() {
       @Override
       protected Iterator<String> nextIterator(final int count) {
           switch (count) {
           case 1:
               return empty.iterator();
           case 2:
               return notEmpty.iterator();
           }
           return null;
       }
   };
   assertTrue(chain.hasNext(), "should have next");
   assertEquals("A", chain.next());
   assertTrue(chain.hasNext(), "should have next");
   assertEquals("B", chain.next());
   assertTrue(chain.hasNext(), "should have next");
   assertEquals("C", chain.next());
   assertFalse(chain.hasNext(), "should not have next");
}

```
PMD signale une possible violation de règle liée à l'utilisation d’un switch pour ce petit cas qui peut utiliser des ressources.

Résoudre cette issue n'apporterait pas de bénéfices significatifs, de plus le code est plus clair et plus simple à comprendre dans ce sens

