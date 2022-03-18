# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC and LCC produce the same value if there is no indirect link between their pair of methods.
This is impossible that LCC is lower than TCC because nodes used by LCC are used by TCC, where TCC also accept indirect links
      
     class genericObject {
      private int x;
      private int y;
      
      public int getX(){
      return x;
      }
      
      public int getY(){
      return y;
      }
      
      public void setX(int newX){
      x=newX;
      }
      
      public void setY(int newY){
      y=newY;
      }
     }
