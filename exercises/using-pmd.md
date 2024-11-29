# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

1.	True positive

	Retour PMD:
		- Avoid using implementation types like 'Map'; use the interface instead
		
	
    	Description: 
		- Utiliser Map permet de passer n'importe quelle implémentation comme HashMap, TreeMap, ou ConcurrentHashMap sans modifier la signature de la méthode.

		
		
		
2.	False Positive:

	Retour PMD: 
		- This class has only private constructors and may be final (Ligne 36)

    	Description: 
		- Cette règle suggère de marquer les classes qui ont des constructeurs privés comme final.

    	Non nécessaire:
		- Cette règle peut être ignorée si on ne prévoie pas d'étendre ces classes à l'avenir et qu'elles sont conçues pour fonctionner uniquement de manière statique ou sans instanciation.
		
