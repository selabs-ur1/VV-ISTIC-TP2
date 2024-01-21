# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such a class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Here is a simple class that represents binary operations between two doubles. As we can see, all the functions from within this class use both of the global variables; therefore, TCC = LCC = 6/6 = 1.

////////////////////
public class BinOp {
	
	private double x;
	private double y;
	
	public BinOp(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double add() {
		return x + y;
	}
	
	public double sub() {
		return x - y;
	}
	
	public double mult() {
		return x * y;
	}
	
	public double div() {
		return x/y;
	}
}
////////////////////

As for the second question, LCC cannot be lower TCC; at most, the LCC is as small as TCC, but not vice versa. Since LCC takes into account all the pairs of nodes that are connected (directly or not), it will take into account not only the nodes that are directly connected (which are taken into account by TCC), but also the nodes that are indirectly connected and are not present in the measuring of TCC. The case where LCC = TCC is case where a class will have as many pairs of directly connected nodes as indirectly connected (e.g., a class with Iab, Icd, Ief).