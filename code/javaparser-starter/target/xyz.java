package fr.benjamindanlos.vv2;

public class Aaaa{
    private String x = "";
    private String y = "";
    private String z = "";

    public void mX(){
        mY();
    }
    public void mY(){
        mZ();
    }
    public void mZ(){
        mX();
    }

    public String getx(){return x;}

}
