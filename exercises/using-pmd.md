# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer


Décrivez ci-dessous un problème trouvé par PMD que vous pensez devoir être résolu (vrai positif) et incluez ci-dessous les changements que vous ajouteriez au code source.

    Un problème trouvé par PMD que vous pensez devoir être résolu (vrai positif) :

    commons-collections/src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java:1318:	CompareObjectsWithEquals:	Use equals() to compare object references.


    Changements à ajouter au code source :

    public boolean isInternalNode() {
        return !left.equals(this) && !right.equals(this);
    }

Décrivez ci-dessous un problème trouvé par PMD qui ne mérite pas d'être résolu (faux positif). Expliquez pourquoi vous ne résoudriez pas ce problème.


    Problème trouvé par PMD qui ne mérite pas d'être résolu :

    commons-collections/src/main/java/org/apache/commons/collections4/trie/analyzer/StringKeyAnalyzer.java:76:	OneDeclarationPerLine:Use one line for each declaration, it enhances code readability.