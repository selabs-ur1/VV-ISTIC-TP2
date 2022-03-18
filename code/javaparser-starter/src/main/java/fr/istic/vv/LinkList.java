package fr.istic.vv;

import java.util.LinkedList;
import java.util.List;

public class LinkList {
    
    List<BiLink> list;

    LinkList() {
        this.list = new LinkedList();
    }

    public void add(BiLink link) {
        for(BiLink biLink : list) {
            if (biLink.equals(link)) return;
        }

        list.add(link);

    }

    public int getLinks() {
        return list.size();
    }

    @Override
    public String toString() {
        return list.toString();
    }

    double size() {
        return list.size();
    }
}
