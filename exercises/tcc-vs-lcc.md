# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

For LCC = TCC :

    - TCC=LCC=1 case :
    
        - All methods must use at least one common attribute.
        
    - TCC=LCC<1 case :
    
        - the class must have several attributes and the methods must use :
        
            - the same set of attributes
            
            
            - either a different set whose union with the attribute sets of the other methods is empty.
            

In summary: There must be only direct links in the cohesion graph of the class, not preventing the methods from separating into several independent nodes.
