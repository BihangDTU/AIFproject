package dataStructure;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class OutFact extends AST implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private int ID;
  private FactWithType fact;
  private List<Integer> trace;
  private String ruleName;
  
  public OutFact(){}
  
  public OutFact(int iD, FactWithType fact, List<Integer> trace,
      String ruleName) {
    super();
    ID = iD;
    this.fact = fact;
    this.trace = trace;
    this.ruleName = ruleName;
  }
  
  public int getID() {
    return ID;
  }

  public void setID(int iD) {
    ID = iD;
  }

  public FactWithType getfact() {
    return fact;
  }

  public void setfact(FactWithType fact) {
    this.fact = fact;
  }

  public List<Integer> getTrace() {
    return trace;
  }

  public void setTrace(List<Integer> trace) {
    this.trace = trace;
  }

  public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  @Override
  public String toString() {
    String s = "(" + ID + ") ";   
    s += fact.toString() + " <= ";
    for(int i=0;i<trace.size();i++){
      if(i<trace.size()-1){
        s += "(" + trace.get(i) + ") + ";
      }else{
        s += "(" + trace.get(i) + ") " + ruleName + ";";
      }
    }
    if(trace.size() == 0){
      s += " " + ruleName + ";";
    }
    return s;
  }
  
  @Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof OutFact)) {
        return false;
      }
      OutFact of = (OutFact) o;   
      return Objects.equals(fact, of.fact);
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.fact);
    return hash;
  }
}
