package dataStructure;
import java.io.Serializable;
import java.util.Objects;

public class Condition extends AST implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Boolean positive = true;
    
  private Term var;
  private Term term;

  public Condition(){}
  public Condition(Term var, Term term) {
    this.var = var;
    this.term = term;
  }

    public Condition(Term var, Term term, Boolean positive) {
    this.var = var;
    this.term = term;
    this.positive = positive;
  }


    public Term getVar() {
    return var;
  }

  public void setVar(Term var) {
    this.var = var;
  }

  public Term getTerm() {
    return term;
  }

  public void setTerm(Term term) {
    this.term = term;
  }
  
  @Override
	public String toString() {
  	if(positive){
  		return var + " in " + term;
  	}else{
  		return var + " notin " + term;
  	}	
	}
	@Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof Condition)) {
        return false;
      }
    Condition condition = (Condition) o;		
    return Objects.equals(var, condition.var) &&
            Objects.equals(term, condition.term);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.var);
    hash = 19 * hash + Objects.hashCode(this.term);
    return hash;
  }
  
}

