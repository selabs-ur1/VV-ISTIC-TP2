package fr.istic.vv;

public class NestedIfStatement {

    public void method(){
        int i = 0;
        if (i<=0){
            if (i>=0){
                if (i==0){
                    i++;
                }
            }
        }
    }

}