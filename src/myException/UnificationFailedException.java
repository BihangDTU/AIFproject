package myException;

public class UnificationFailedException extends Exception {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//public UnificationFailedException(){}
  public void message(){
    System.out.println("Unification failed.");
  } 
}
