package dataStructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConcreteRules extends AST implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rulesName;
  private HashMap<String, String> varsTypes = new HashMap<>();
    // Sebastian: I think this will not work, because variable objects
    // are not a good key for a map (parsing the same variable
    // different types would yield different objects), rather the
    // variable name should be the key! Also String for the type may
    // be optimal (but let's discuss)

    
  private ArrayList<Term> LF = new ArrayList<>();
  private ArrayList<Condition> Splus = new ArrayList<>();
  private ArrayList<Condition> Snega = new ArrayList<>();
  private Freshs freshVars;
  private ArrayList<Term> RF = new ArrayList<>();
  private ArrayList<Condition> RS = new ArrayList<>();
  
  public ConcreteRules(){}
  public ConcreteRules(String rulesName, HashMap<String, String> varsTypes, ArrayList<Term> LF, ArrayList<Condition> Splus,
                       ArrayList<Condition> Snega, Freshs freshVars, ArrayList<Term> RF, ArrayList<Condition> RS) {
    this.rulesName = rulesName;
    this.varsTypes = varsTypes;
    this.LF = LF;
    this.Splus = Splus;
    this.Snega = Snega;
    this.freshVars = freshVars;
    this.RF = RF;
    this.RS = RS;
  }
  
  public String getRulesName() {
    return rulesName;
  }

  public void setRulesName(String rulesName) {
    this.rulesName = rulesName;
  }

  public HashMap<String, String> getVarsTypes() {
    return varsTypes;
  }

  public void setVarsTypes(HashMap<String, String> varsTypes) {
    this.varsTypes = varsTypes;
  }
  
  public void addVarsTypes(String var, String type){
    this.varsTypes.put(var, type);
  }

  public ArrayList<Term> getLF() {
    return LF;
  }

  public void setLF(ArrayList<Term> LF) {
    this.LF = LF;
  }
  
  public void addLF(Term term){
    this.LF.add(term);
  }

  public ArrayList<Condition> getSplus() {
    return Splus;
  }

  public void setSplus(ArrayList<Condition> Splus) {
    this.Splus = Splus;
  }
  
  public void addSplus(Condition cond){
    this.Splus.add(cond);
  }

  public ArrayList<Condition> getSnega() {
    return Snega;
  }

  public void setSnega(ArrayList<Condition> Snega) {
    this.Snega = Snega;
  }

  public void addSnega(Condition cond){
    this.Snega.add(cond);
  }

  public Freshs getNewFreshVars() {
    return freshVars;
  }

  public void setNewFreshVars(Freshs freshVars) {
    this.freshVars = freshVars;
  }
  
  public ArrayList<Term> getRF() {
    return RF;
  }

  public void setRF(ArrayList<Term> RF) {
    this.RF = RF;
  }
  
  public void addRF(Term term){
    this.RF.add(term);
  }

  public ArrayList<Condition> getRS() {
    return RS;
  }

  public void setRS(ArrayList<Condition> RS) {
    this.RS = RS;
  }
  
  public void addRS(Condition cond) {
    this.RS.add(cond);
  }
  
  @Override
  public String toString(){
    String concreteRules;
    int acc = 0;
    concreteRules = rulesName + "(";
    for (Map.Entry<String, String> entry : varsTypes.entrySet()) {
      acc++;
      if(acc == varsTypes.size()){
        concreteRules = concreteRules + entry.getKey() + ":" + entry.getValue();
      }else{
        concreteRules = concreteRules + entry.getKey() + ":" + entry.getValue() + ", ";
      }
    }
    concreteRules = concreteRules + ")\n";
    for(int i=0;i<LF.size();i++){
      if(i == 0){
        concreteRules = concreteRules + LF.get(i);
      }else{
        concreteRules = concreteRules + "." + LF.get(i);
      } 
    }
    for (int i=0;i<Splus.size();i++){
      concreteRules = concreteRules+ "." + Splus.get(i).getVar().toString() + " in " + Splus.get(i).getTerm().toString();
    }
    for (int i=0;i<Snega.size();i++){
      concreteRules = concreteRules + "." + Snega.get(i).getVar().toString() + " notin " + Snega.get(i).getTerm().toString();
    }
    if(freshVars.getFreshs().isEmpty()){
      concreteRules = concreteRules + " => ";
    }else{
      //concreteRules = concreteRules + " =[" + newVariable.toString() + "]=> ";
    	concreteRules += " =[";
      for(int i=0;i<freshVars.getFreshs().size();i++){
      	if(i<freshVars.getFreshs().size()-1){
      		concreteRules += freshVars.getFreshs().get(i) + ",";
      	}else{
      		concreteRules += freshVars.getFreshs().get(i) + "]=> ";
      	}
      }
    }
    for (int i=0;i<RS.size();i++){
      if(i == 0){
        concreteRules = concreteRules + RS.get(i).getVar().toString() + " in " + RS.get(i).getTerm().toString();
      }else{
        concreteRules = concreteRules+ "." + RS.get(i).getVar().toString() + " in " + RS.get(i).getTerm().toString();
      }
    }
    for(int i=0;i<RF.size();i++){
      if(RS.isEmpty() && i == 0){
        concreteRules = concreteRules + RF.get(i);
      }else{
        concreteRules = concreteRules + "." + RF.get(i);
      } 
    }
    return concreteRules + ";";
  }
  
  @Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof ConcreteRules)) {
        return false;
      }
    ConcreteRules concreteRules = (ConcreteRules) o;		
    return Objects.equals(rulesName, concreteRules.rulesName) &&
            Objects.equals(varsTypes, concreteRules.varsTypes) &&
            Objects.equals(LF, concreteRules.LF) &&
            Objects.equals(Splus, concreteRules.Splus) &&
            Objects.equals(Snega, concreteRules.Snega) &&
            Objects.equals(freshVars, concreteRules.freshVars) &&
            Objects.equals(RF, concreteRules.RF) &&
            Objects.equals(RS, concreteRules.RS);
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.rulesName);
    hash = 19 * hash + Objects.hashCode(this.varsTypes);
    hash = 19 * hash + Objects.hashCode(this.LF);
    hash = 19 * hash + Objects.hashCode(this.Splus);
    hash = 19 * hash + Objects.hashCode(this.Snega);
    hash = 19 * hash + Objects.hashCode(this.freshVars);
    hash = 19 * hash + Objects.hashCode(this.RF);
    hash = 19 * hash + Objects.hashCode(this.RS);
    return hash;
  }
}

