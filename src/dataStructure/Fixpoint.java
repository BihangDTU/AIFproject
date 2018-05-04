package dataStructure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Fixpoint extends AST {
	private int factID;
  private HashMap<String, String> vType = new HashMap<>();
  private Term term;
  private ArrayList<Integer> leftSideConditions = new ArrayList<>(); 
  private String rulesName;
  
  public Fixpoint(int factID, HashMap<String, String> vType, Term term, ArrayList<Integer> leftSideConditions, String rulesName){
    this.factID = factID;
    this.vType = vType;
    this.term = term;
    this.leftSideConditions = leftSideConditions;
    this.rulesName = rulesName;
  }

  public int getFactID() {
    return factID;
  }

  public void setFactID(int factID) {
    this.factID = factID;
  }

  public HashMap<String, String> getvType() {
    return vType;
  }

  public void setvType(HashMap<String, String> vType) {
    this.vType = vType;
  }
  
  public void addvType(String var, String type){
    this.vType.put(var,type);
  }

  public Term getTerm() {
    return term;
  }

  public void setTerm(Term term) {
    this.term = term;
  }

  public ArrayList<Integer> getLeftSideConditions() {
    return leftSideConditions;
  }

  public void setLeftSideConditions(ArrayList<Integer> leftSideConditions) {
    this.leftSideConditions = leftSideConditions;
  }
  
  public void addLeftSideConditions(int factID){
    this.leftSideConditions.add(factID);
  }

  public String getRulesName() {
    return rulesName;
  }

  public void setRulesName(String rulesName) {
    this.rulesName = rulesName;
  }
  
  
  @Override
	public String toString() {
  	String s= "";
  	s += "(" + factID + ") ";
  	for (Map.Entry<String, String> v : vType.entrySet()) {
  		 s += v.getKey() + ":" + v.getValue() + ".";
  	}
  	s += ((Composed)term).toString();
  	s += " <= ";
  	for(int i=0;i<leftSideConditions.size();i++){
  		if(i<leftSideConditions.size()-1){
  			s += "(" + leftSideConditions.get(i) + ") + ";
  		}else{
  			s += "(" + leftSideConditions.get(i) + ")";
  		}
  	}
  	s += rulesName;
		return s;
	}

	@Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof Fixpoint)) {
        return false;
      }
      Fixpoint fp = (Fixpoint) o;		
    return factID ==  fp.factID&&
            Objects.equals(vType, fp.vType) &&
            Objects.equals(term, fp.term) &&
            Objects.equals(leftSideConditions, fp.leftSideConditions) &&
            Objects.equals(rulesName, fp.rulesName);
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.factID);
    hash = 19 * hash + Objects.hashCode(this.vType);
    hash = 19 * hash + Objects.hashCode(this.term);
    hash = 19 * hash + Objects.hashCode(this.leftSideConditions);
    hash = 19 * hash + Objects.hashCode(this.rulesName);
    return hash;
  }
}
