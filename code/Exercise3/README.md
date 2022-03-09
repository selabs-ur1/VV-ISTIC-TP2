# Code of your exercise
```java
package fr.benjamindanlos.foo;

class Foo{
    public void bar(int x, int y, int z){
        if(x==y){
            x++;
            if(y==z){
                z--;
                if(x==z){
                    System.out.println("bar");
                }
            }
        }
    }
}
```

```xpath
//IfStatement[.//IfStatement[.//IfStatement]]
```
