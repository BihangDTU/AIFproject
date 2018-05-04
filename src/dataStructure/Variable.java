package dataStructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Variable extends Term implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String varName;

  public Variable(String varName) {
    this.varName = varName;
  }

  @Override
  public String getVarName() {
    return varName;
  }

  @Override
  String getFactName() {
    return null;
  }

  @Override
  void setArguments(Term arguments) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  String getCons() {
    return null;
  }

  @Override
  ArrayList<Term> getArguments() {
    return null;
  }

  @Override
  public String toString(){
    return varName;
  }

  @Override
  public boolean equals(Object o){
    if (o == this) return true;
    if (!(o instanceof Variable)) {
      return false;
    }
    Variable var = (Variable) o;
    return Objects.equals(varName, var.varName);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + Objects.hashCode(this.varName);
    return hash;
  }
}

