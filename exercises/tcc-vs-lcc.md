# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

We know that : 

TCC = NDC / NP 
and LCC = NDC + NIC / NP

with NDC is the number of direct connections in the connection graph, NIC the number of indirect connections and NP the maximum number of possible connections.

In order to have TCC = LCC, we need to have no indirect connections, in order to have this, we need each public method to reach an exit node without passing through another public method

Example : //TODO: example method

LCC > TCC is impossible for every class as we cannot have a negative number of indirect connections.
