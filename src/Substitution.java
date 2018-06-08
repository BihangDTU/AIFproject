import dataStructure.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Substitution implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private boolean unifierState = true;
  private HashMap<String, Term> substitution = new HashMap<>();

  public Substitution(){}
  

  public Substitution(HashMap<String, Term> substitution) {
    super();
    this.substitution = substitution;
  }


  public void setUnifierState(boolean unifierState){
    this.unifierState = unifierState;
  }

  public boolean getUnifierState() {
    return unifierState;
  }

  public void addSubstitution(String var, Term term) {
    substitution.put(var, term);
  }

  public void addAllSubstitution(HashMap<String, Term> sub) {
    substitution.putAll(sub);
  }

  public HashMap<String, Term> getSubstitution(){
    return substitution;
  }
  
  
  @Override
  public String toString() {
    return "Substitution [unifierState=" + unifierState + ", substitution="
        + substitution + "]";
  }


  @Override
  public boolean equals(Object o){
    if (o == this) return true;
    if (!(o instanceof Substitution)) {
      return false;
    }
    Substitution substitutions = (Substitution) o;		
    return (unifierState == substitutions.unifierState) &&
            Objects.equals(substitution, substitutions.substitution);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.unifierState);
    hash = 19 * hash + Objects.hashCode(this.substitution);
    return hash;
  }
}
