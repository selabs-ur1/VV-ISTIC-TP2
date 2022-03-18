# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false negative). Explain why you would not solve this issue.

## Answer

L'utilisation de PMD pour cette question se fait via la commande : `pmd.bat -f html -R rulesets/java/quickstart.xml > errorFile.html -d ./`, avec -f pour spécifier un format de sortie html, -R pour spécifier le(s) rulesets à utiliser, la redirection > vers le fichier html à générer, et -d pour donner le répertoire du code à analyser.

Pour cette question, nous avons choisi de traiter la library Apache CLI.

Un cas de vrai positif dans le fichier CommandLine.java, ligne 331 :

```java
public String[] getOptionValues(final Option option) {
        final List<String> values = new ArrayList<>();

        for (final Option processedOption : options) {
            if (processedOption.equals(option)) {
                values.addAll(processedOption.getValuesList());
            }
        }

        return values.isEmpty() ? null : values.toArray(new String[values.size()]);
    }
```

Car la méthode ``toArray(...)`` redimensionne la tableau à la bonne taille si jamais la liste ne rentre pas dans la tableau dont la taille a été allouée en amont (cf la doc de méthode ``toArray(...)``).

Un moyen de résoudre ce problème serait de passer à un tableau de peu d'éléments (par exemple 1) lors de l'instanciation, afin que le redimensionnement soit automatique. En effet, cela permet d'éviter d'allouer un tableau trop grand si la liste est plus petite.
```java
return values.isEmpty() ? null : values.toArray(new String[0]);
```

Cas d'un faux positif :
```java
public static Object getValueClass(final char ch) {
  switch (ch) {
  case '@':
      return PatternOptionBuilder.OBJECT_VALUE;
  case ':':
      return PatternOptionBuilder.STRING_VALUE;
  case '%':
      return PatternOptionBuilder.NUMBER_VALUE;
  case '+':
      return PatternOptionBuilder.CLASS_VALUE;
  case '#':
      return PatternOptionBuilder.DATE_VALUE;
  case '<':
      return PatternOptionBuilder.EXISTING_FILE_VALUE;
  case '>':
      return PatternOptionBuilder.FILE_VALUE;
  case '*':
      return PatternOptionBuilder.FILES_VALUE;
  case '/':
      return PatternOptionBuilder.URL_VALUE;
  }

  return null;
}
```
Selon nous, ce cas est un faux positif car il existe bien un cas par "défaut", mais de manière implicite. En effet, si aucune des valeurs n'a été détectée dans les différents cas énnoncés, alors il y a un ``return null;`` qui aurait été équivalent à avoir un ``default : return null;``, mais toujours selon nous, cela aurait changé la lisibilité du code car dans le cas d'une valeur nulle retournée par défaut, il est intéressant de la séparer des autres cas, qui eux retournent bien une valeur.
