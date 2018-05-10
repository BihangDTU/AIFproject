package dataStructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FixedpointsWithType extends AST{
	private HashMap<String, String> vType = new HashMap<>();
  private Term term;
	public FixedpointsWithType(HashMap<String, String> vType, Term term) {
		super();
		this.vType = vType;
		this.term = term;
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
	
	
	@Override
	public String toString() {
		String s = "";
		 for (Map.Entry<String, String> var : vType.entrySet()) {
			 s += var.getKey() + ":" + var.getValue() + ".";
		 }
		 s += term.toString() + ";";
		return s;
	}
	@Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof FixedpointsWithType)) {
        return false;
      }
      FixedpointsWithType fp = (FixedpointsWithType) o;		
    return Objects.equals(vType, fp.vType) &&
            Objects.equals(term, fp.term);
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.vType);
    hash = 19 * hash + Objects.hashCode(this.term);
    return hash;
  }
  
}
