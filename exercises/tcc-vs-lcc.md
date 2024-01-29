# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC and LCC are metrics used to evaluate the Cohesion of a class, It is used to see how much the class respect the SOLID principle

TCC : will get all possible pairs of methods and will then verify if the two methods use atleast one instance variable of the class
then you will increment a counter by one. when you tested all the methods. you divide the number of Pairs by this counter.

LCC : LCC work almost the same except that the link doesn't need to be direct it can be indirect. At the end you divide the number of pairs by the counter.

```
class VVex1

  int A
  int B
  int C
  int D
  int E

  method 1 :
    some code using A
    some code using C

  method 2 :
    some code using B
    some code using D

  method 3 :
    some code using C
    some code using E
    
  method 4 :
    some code using B
    some code using D
    some code using E

  (mx = method x)
  (nb = methods count)
  Calcul TCC 
    m1 & m2 : nothing  :  +0
    m1 & m3 : C        :  +1
    m1 & m4 : nothing  :  +0
    m2 & m3 : nothing  :  +0
    m2 & m4 : B,D      :  +1
    m3 & m4 : E        :  +1
    counter = 3 
    TCC = counter/nb = 3/4 = 0.75
    
  Calcul LCC
    m1 & m2 : nothing  : m1->m3->m4->m2  : +1
    m1 & m3 : C        : m1->m3          : +1
    m1 & m4 : nothing  : m1->m3->m4      : +1
    m2 & m3 : nothing  : m2->m4->m3      : +1
    m2 & m4 : B,D      : m2->m4          : +1
    m3 & m4 : E        : m3->m4          : +1
    counter =  6
    LCC = counter/nb = 6/4 = 1.5
```
