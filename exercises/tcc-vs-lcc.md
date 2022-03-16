# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC et LCC seront toujours égal dans une classe bean :
```
Class Test{
	private int a ;
	private int b ;

	public int getA(){
		return a ;
	}

	public void setA(int a){
		this.a = a ;
	}

	public int getB(){
		return b ;
	}

	public void setB(int b){
		this.b = b ;
	}
}
```
Ici on a TCC = LCC avec 2/6.

Il est impossible d’avoir LCC < TCC car les relations directs sont présentes dans les 2 formules
