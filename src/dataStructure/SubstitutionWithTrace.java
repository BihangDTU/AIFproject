package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubstitutionWithTrace implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private List<Integer> trace = new ArrayList<>(); 
  private Substitution subs = new Substitution();
  public SubstitutionWithTrace(){}
  public SubstitutionWithTrace(List<Integer> trace, Substitution subs) {
    super();
    this.trace = trace;
    this.subs = subs;
  }
  public List<Integer> getTrace() {
    return trace;
  }
  public void setTrace(List<Integer> trace) {
    this.trace = trace;
  }
  public Substitution getSubs() {
    return subs;
  }
  public void setSubs(Substitution subs) {
    this.subs = subs;
  }
  
  
  @Override
  public String toString() {
    return "SubstitutionWithTrace [trace=" + trace + ", subs=" + subs + "]";
  }
  @Override
  public boolean equals(Object o){
    if (o == this) return true;
    if (!(o instanceof SubstitutionWithTrace)) {
      return false;
    }
    SubstitutionWithTrace substitutions = (SubstitutionWithTrace) o;    
    return (Objects.equals(subs, substitutions.subs));
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.subs);
    return hash;
  }
  
}
