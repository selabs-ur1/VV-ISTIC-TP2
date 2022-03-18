package fr.istic.vv;

public class BiLink {
    String nodeA;
    String nodeB;

    BiLink(String nodeA,String nodeB) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    public boolean equals(BiLink link) {
        return (this.nodeA == link.nodeA && this.nodeB == link.nodeB) || 
        (this.nodeA == link.nodeB && this.nodeB == link.nodeA);
    }

    @Override
    public String toString() {
        return "["+nodeA+"," + nodeB+"]";
    }
}
