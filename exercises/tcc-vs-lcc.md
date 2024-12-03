# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer
There are 2 possible case for TCC and LCC produce the same value
- when for all nodes in the cohesion graph, there is
  only one or no arc. This prevents the graph from having any indirect connections between
  2 nodes.
- when the graph is a complete graph, i.e., every node is connected to every other node.

Example of a class that has the same TCC and LCC value:
```java
public class User {
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
```
In this class, there is no connection between the fields, so the cohesion graph is a complete graph. Therefore, the TCC and LCC values are both 0.

LCC cannot be lower than TCC because TCC is a subset of LCC.