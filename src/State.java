import dataStructure.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class State implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<Term> facts = new HashSet<>();
  private Set<Condition> positiveConditins = new HashSet<>();

  public State() {}
  
  public State(State state) {
    this.facts = state.facts;
    this.positiveConditins = state.positiveConditins;
  }
  
  public Set<Term> getFacts() {
    return facts;
  }

  public void setFacts(Set<Term> facts) {
    this.facts = facts;
  }
  
  public void addFacts(Term term){
    this.facts.add(term);
  }

  public Set<Condition> getPositiveConditins() {
    return positiveConditins;
  }

  public void setPositiveConditins(Set<Condition> positiveConditins) {
    this.positiveConditins = positiveConditins;
  } 
  
  public void addPositiveConditins(Condition cond){
    this.positiveConditins.add(cond);
  }
  @Override
  public String toString(){
    String stateStr = "";
    for(Term fact : facts){
      stateStr = stateStr + fact.toString() + ";\n";
    }
    for(Condition pc: positiveConditins){
      stateStr = stateStr + pc.getVar().toString() + " in " + pc.getTerm().toString() + ";\n";
    }
    return stateStr;
  }
  
  @Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof State)) {
        return false;
      }
    State state = (State) o;		
    return Objects.equals(facts, state.positiveConditins) &&
            Objects.equals(positiveConditins, state.positiveConditins);
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.facts);
    hash = 19 * hash + Objects.hashCode(this.positiveConditins);
    return hash;
  }
}
