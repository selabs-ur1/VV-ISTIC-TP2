# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC mesures the tight cohesion of classes, that is when two classes are depedant in any way.  
LCC mesures the indirect cohesion of classes, that is for example when a class A depends on a class B that also depends on a class C, therefore A depends loosely on C.  
A TCC value >70% means that the code is unlikely to be split in subcomponents, and a value <50% means the class does too many different things.  
TCC and LCC might produce the same value for a given class when it is empty or when a group of classes are all inter-dependant making a star-shaped diagram.
