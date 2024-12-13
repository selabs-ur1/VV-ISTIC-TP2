# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

Nous avons trouvé un vrai possitif dans la classe IterableUtils à la ligne 1139, l'erreur remonter par PMD est UnnecessaryConstructor cette erreur est explique comme ceci : Cette règle détecte les cas où un constructeur n'est pas nécessaire, c'est-à-dire lorsqu'il n'y a qu'un seul constructeur et que celui-ci est identique au constructeur par défaut. Le constructeur par défaut doit avoir le même modificateur d'accès que la classe déclarante. Dans un type enum, le constructeur par défaut est implicitement privé.

Ici le code ressemble à ça : 

     /**
         * Make private in 5.0.
         *
         * @deprecated TODO Make private in 5.0.
         */
        @Deprecated
        public IterableUtils() {
            // empty
        }
C'est un vrai positif car même dans le code il est reconnu comme 'Deprecated', et pour corriger ça il faudra enlever le constructeur et vérifier qu'il n'est pas utilisé autre part pour éviter tout problème.

Et un faux positif se trouve dans la classe MapUtils à la ligne 878, l'erreur est ReturnEmptyCollectionRatherThanNull, c'est-à-dire qu'il y a un 'return null' à la place d'un return Collection.isEmpty().

Dans le code ça ressemble à ça : 

    public static <K> Map<?, ?> getMap(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer instanceof Map) {
                return (Map<?, ?>) answer;
            }
        }
        return null;
    }

Ici un return null peut-être nécessaire car si l'objet est null un traitement plus loin ne sera pas fait alors que si l'objet est vide cela pour entraîner une erreur dans le traitement réalisé.
 
