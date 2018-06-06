package dataStructure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FactWithTypeRuleName extends AST implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private HashMap<String, String> vType = new HashMap<>();
  private Term term;
  private String ruleName;
  public FactWithTypeRuleName(HashMap<String, String> vType, Term term, String ruleName) {
    super();
    this.vType = vType;
    this.term = term;
    this.ruleName = ruleName;
  }
  public HashMap<String, String> getvType() {
    return vType;
  }
  public void setvType(HashMap<String, String> vType) {
    this.vType = vType;
  }
  public Term getTerm() {
    return term;
  }
  public void setTerm(Term term) {
    this.term = term;
  }
  
  public String getRuleName() {
    return ruleName;
  }
  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }
  @Override
  public String toString() {
    String s = "";
    for (Map.Entry<String, String> var : vType.entrySet()) {
      s += var.getKey() + ":" + var.getValue() + ".";
    }
    s += term.toString() + ".";
    s += ruleName + ";";
    return s;
  }
  @Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof FactWithTypeRuleName)) {
        return false;
      }
      FactWithTypeRuleName fp = (FactWithTypeRuleName) o;   
      return Objects.equals(vType, fp.vType) &&
            Objects.equals(term, fp.term);
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.vType);
    hash = 19 * hash + Objects.hashCode(this.term);
    hash = 19 * hash + Objects.hashCode(this.ruleName);
    return hash;
  }
}
