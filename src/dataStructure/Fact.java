package dataStructure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class Fact extends AST {
  private int factID;
  private HashMap<Variable, String> vType = new HashMap<>();
  private Term term;
  private ArrayList<Integer> leftSideConditions = new ArrayList<>(); 
  private String rulesName;
  
  public Fact(int factID, HashMap<Variable, String> vType, Term term, ArrayList<Integer> leftSideConditions, String rulesName){
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

  public HashMap<Variable, String> getvType() {
    return vType;
  }

  public void setvType(HashMap<Variable, String> vType) {
    this.vType = vType;
  }
  
  public void addvType(Variable var, String type){
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
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof Fact)) {
        return false;
      }
    Fact fact = (Fact) o;		
    return factID ==  fact.factID&&
            Objects.equals(vType, fact.vType) &&
            Objects.equals(term, fact.term) &&
            Objects.equals(leftSideConditions, fact.leftSideConditions) &&
            Objects.equals(rulesName, fact.rulesName);
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

