# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer
Partie I:

Pour que TCC et LCC produisent la même valeur, il faut les circonstances suivantes :
- Toutes les méthodes de la classe accèdent directement aux mêmes attributs
- La classe est entièrement connectée
- La classe n'a qu'une seule méthode ou aucun attribut partagé

Partie II:
Regardez le fichier "Exercice1" dans le dossier "code" et lisez le fichier "LCC and TCC"

Partie III:
Il est en effet possible que LCC soit inférieur a TCC dans certains cas.
Cela peut se produire dans des situations où une classe a des méthodes qui sont peu liées entre elles, mais qui ont des accès indirects via d'autres méthodes ou classes
Pour avoir une preuve de cette assertion, veuillez regarder le morceau de code "MyClass" dans le dossier "code/Exercice1" ou l'on
peut voir un morceau de code avec LCC < TCC