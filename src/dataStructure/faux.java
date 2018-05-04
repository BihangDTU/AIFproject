package dataStructure;

public class faux{ // collection of non-OO auxiliary functions (currently just error)
  public static void error(String msg){
  	System.err.println("Interpreter error: "+msg);
  	System.exit(-1);
  }
}
