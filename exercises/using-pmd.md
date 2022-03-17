# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer

Commande Windows:
```
.\pmd.bat -d C:\Users\Erwann\IdeaProjects\commons-lang\src\main\java\ -f html -R rulesets/java/quickstart.xml > 'C:\Users\Erwann\Desktop\M2 CCNa\VV\errorFile.html'
```

Commande Linux en ayant ajouté le path dans le .bashrc:
```
run.sh pmd -d ./ -f html -R rulesets/java/quickstart.xml > errorfolder2.html

```

#### Décrivez un vrai positif et quelles modifications apportées?

Après analyse avec PMD sur le projet Apache Commons Lang, un des vrais positifs qui ressort porte sur l'emploi de '\==' pour vérifier une égalité. Or, lorsque l'on compare des objets, il vaut mieux employer la méthode Equals et non "==".

Code originel:
```java
public static boolean equals(final Annotation a1, final Annotation a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }
        ...
```
Code corrigé après emploi de PMD:
```java
public static boolean equals(final Annotation a1, final Annotation a2) {
        if (a1.equals(a2)) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }
        ...
```

#### Décrivez un problème trouvé par PMD qui ne vaut pas la peine d'être résolu (faux positif)?
Un des problèmes remonté par la commande PMD portait sur l'utilisation d'assertTrue au lieu d'assertEquals dans un des fichiers du dossier test de math - legacy (`AkimaSplineInterpolatorTest.java`)

Commande sous Linux :
```
run.sh pmd -d ./ -R category/java/bestpractices.xml/UseAssertEqualsInsteadOfAssertTrue -f html > errorfolder.html
```
Code :
```java
for (int i = 0; i < numberOfElements; i++) {
            currentX = xValues[i];
            expected = f.value(currentX);
            actual = interpolation.value( currentX );
            assertTrue( Precision.equals( expected, actual ) );
        }
```
Normalement, dans ce contexte, il est conseillé d'employer assertEquals. Or, à la lecture des lignes de code, nous constatons que la fonction equals est surchargée pour la classe Precision. En effet, il s'agit d'une interface contenant des opérations de comparaison pour les doubles permettant de considérer des valeurs égales mêmes si celles-ci ne sont pas exactement égales. Ainsi, l'emploi d'assertTrue a tout son sens dans ce contexte.
Il est donc important de ne pas résoudre des faux positifs car il s'agit d'éléments du code considérés comme erronés au regard de la règle employée. Pour autant, après relecture, ces éléments s'avèrent être corrects mais ont été détectés en tant qu'erreurs.

#### Pourquoi ne pas résoudre un faux négatif?
Contrairement aux faux positifs, les faux négatifs sont repérés par le reviewer ou le développeur à la relecture du code et viennent attester en quelque sorte de l'absence de certaines conditions testées. Il vaut donc mieux trouver quelles sont les conditions testées à rajouter dans le fichier ruleset.xml, afin de révéler ce faux négatif, plutôt que de le corriger et de passer à côté d'autres erreurs potentielles dans le code.
