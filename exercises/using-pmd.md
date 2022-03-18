# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer

Le projet choisi : [Apache Commons Collections](https://github.com/apache/commons-collections)

#### true positive :
issue :
C:..\commons-collections-master\src\main\java\org\apache\commons\collections4\ArrayUtils.java:39: ClassWithOnlyPrivateConstructorsShouldBeFinal: A class which only has private constructors should be final

Dans la classe ArrayUtils, il y a un constructeur vide et 3 méthodes static. Cette classe n'a pas de sous-classes, on le met en final.
Changement ajouté:
final class ArrayUtils {...}

#### false negative :
issue :
C:..\commons-collections-master\src\main\java\org\apache\commons\collections4\ArrayStack.java:155: UselessParentheses: Useless parentheses.
C:..\commons-collections-master\src\main\java\org\apache\commons\collections4\ArrayStack.java:156: UselessParentheses: Useless parentheses.

code source :
155 if ((object == null && current == null) ||
156 (object != null && object.equals(current))) {

Mettre une condition longue en 2 lignes permet d'une meilleure lisibilité et visibilité de code, qui est une bonne pratique.
