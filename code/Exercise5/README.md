La class [CyclicComplexityCalculator](src/main/java/fr/istic/vv/CyclicComplexityCalculator.java) implémente un visiteur pour pouvoir traverser le 
code et produire pour chaque méthode une analyse de la complexité comme demandé.
Nous chargons cette analyse dans un fichier csv. Un histogramme rudimentaire est aussi affiché dans la console.

Nous avons comparé l'histogramme des librairies "commons-lang" et "commons-collections", qui ont une taille sensiblement similaire,
et nous trouvons un résultat aussi relativement similaire, à noter que la première librairie est plus grande que la seconde mais possède pour autant 
moins de méthode avec une complexité de 2 ou 3 que la seconde, bien que possédant plus de méthode avec une complexité égale à 1.

Elle (commons-lang) contient également deux méthodes avec une complexité supérieur à 40 (41 et 42), cependant après lecture du code dans ces classes 
nous trouvons que code n'a pas nécessairement besoin d'être changé. La première méthode, avec 41 de complexité, possède un énorme switch avec très 
peu de code à l'intérieur, ce qui reste lisible, et la deuxième méthode, avec 42 de complexité, s'occupe de créer un nombre à partir d'une string 
et doit couvrir un tas de cas, qui n'ont pas nécessairement besoin d'être extrait dans une autre méthode, rendant possiblement plus pénible la 
lecture.