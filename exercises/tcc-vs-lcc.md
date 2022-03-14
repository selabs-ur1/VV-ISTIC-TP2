# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC et LCC peuvent être à égalité dans une classe, qui pour chaque attribut, a plusieurs méthodes qui utilise seulement cette attribut.
Par exemple :

```
Class Test{
	int a ;
	int b ;
	void a(){
		a++ ;
	}
	void afficheA(){
		System.out.println(a) ;
	}
	int getA(){
		return a ;
	}
	void b(){
		b++ ;
	}
	void afficheB(){
		System.out.println(b) ;
	}
	int getB(){
		return b ;
	}
}
```

Ici on a TCC = LCC = 6/15

Il est impossible d’avoir LCC < TCC car la formule de TCC = nombre de relation direct / nombre de relation max probable et LCC =  (nombre de relation direct + nombre de relation indirect) / nombre de relation max probable. Comme Il y a le nombre de relation direct dans les deux formules et le nombre de relation indirect ne peux être négatif alors LCC < TCC est impossible.
