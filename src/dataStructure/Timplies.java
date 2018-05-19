package dataStructure;

import java.io.Serializable;

public class Timplies implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private boolean isTimplies = true;
  private Term timplies;
  
  public Timplies(boolean isTimplies, Term timplies) {
    super();
    this.isTimplies = isTimplies;
    this.timplies = timplies;
  }
  public Timplies(Term timplies) {
    super();
    this.timplies = timplies;
  }
  public Timplies() {
    super();
  }
  public boolean isTimplies() {
    return isTimplies;
  }
  public void setTimplies(boolean isTimplies) {
    this.isTimplies = isTimplies;
  }
  public Term getTimplies() {
    return timplies;
  }
  public void setTimplies(Term timplies) {
    this.timplies = timplies;
  }
  @Override
  public String toString() {
    String s = timplies.getArguments().get(0) + " -->> " + timplies.getArguments().get(1);
    return s;
  }
}
