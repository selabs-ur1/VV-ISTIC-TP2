# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

According to [aivosto.com](https://www.aivosto.com/project/help/pm-oo-cohesion.html):

> TCC and LCC definitions
>
> NP = maximum number of possible connections
> = N * (N − 1) / 2 where N is the number of methods
>
> NDC = number of direct connections (number of edges in the connection graph)
>
> NIC = number of indirect connections
>
> Tight class cohesion TCC = NDC / NP
>
> Loose class cohesion LCC = (NDC + NIC) / NP

Thus, one class where `TCC == LCC == 1`:

```
class Logger
{
	public String test = "";

	public void setTest(String s)
	{
		this.test = s;
	}

	public String getTest()
	{
		return this.test;
	}
}
```

According to the documentation linked above, since `LCC = TCC + (NIC / NP)` and `NIC / NP >= 0`, then `LCC >= TCC`.
