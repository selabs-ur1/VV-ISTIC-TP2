# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer
Les métriques LCC et TCC peuvent avoir la même valeur lorsque :
* Toutes les paires sont directement connectées sans transitivité, dans notre exepmle, _methodA_ utilise les mêmes attributs que _methodB_ qui elle même utilise les même attributs que _methodC_, et _methodC_ est également reliée à _methodA_
* Le graphe est complet, tous les noeuds sont connectés entre eux.
* Les méthodes n'utilisent aucun attribut et donc n'en ne partagent aucun, donc **LCC = TCC = 0**
  
Ceci étant dit, la valeur de LCC ne peut être plus basse que celle de TCC.
![image](https://github.com/user-attachments/assets/358b77ef-3a2d-4549-b3ef-7a6c2c0adbc7)

