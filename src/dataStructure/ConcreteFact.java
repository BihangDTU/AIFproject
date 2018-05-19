package dataStructure;

import java.util.ArrayList;
import java.util.List;

public class ConcreteFact {
  private Term fact;
  private List<Condition> RS = new ArrayList<>();
  
  public ConcreteFact(){}
  public ConcreteFact(Term fact, List<Condition> rS) {
    super();
    this.fact = fact;
    RS = rS;
  }
  public Term getFact() {
    return fact;
  }
  public void setFact(Term fact) {
    this.fact = fact;
  }
  public List<Condition> getRS() {
    return RS;
  }
  public void setRS(List<Condition> rS) {
    RS = rS;
  }
  @Override
  public String toString() {
    String s = "";
    if(fact == null){
      return s;
    }
    s += fact.toString();
    for (int i=0;i<RS.size();i++){
       s += "." + RS.get(i).getVar().toString() + " in " + RS.get(i).getTerm().toString();
    }
    s += ";";
    return s;
  }
  
  
}
