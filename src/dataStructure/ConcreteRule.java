package dataStructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConcreteRule extends AST implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rulesName;
  private HashMap<String, String> varsTypes = new HashMap<>();
  private List<Term> LF = new ArrayList<>();
  private List<Condition> Splus = new ArrayList<>();
  private List<Condition> Snega = new ArrayList<>();
  private Freshs freshVars;
  private List<Term> RF = new ArrayList<>();
  private List<Condition> RS = new ArrayList<>();
  
  public ConcreteRule(){}
  public ConcreteRule(String rulesName, HashMap<String, String> varsTypes, List<Term> LF, List<Condition> Splus,
                       List<Condition> Snega, Freshs freshVars, List<Term> RF, List<Condition> RS) {
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

  public List<Term> getLF() {
    return LF;
  }

  public void setLF(List<Term> LF) {
    this.LF = LF;
  }
  
  public void addLF(Term term){
    this.LF.add(term);
  }

  public List<Condition> getSplus() {
    return Splus;
  }

  public void setSplus(List<Condition> Splus) {
    this.Splus = Splus;
  }
  
  public void addSplus(Condition cond){
    this.Splus.add(cond);
  }

  public List<Condition> getSnega() {
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
  
  public List<Term> getRF() {
    return RF;
  }

  public void setRF(List<Term> RF) {
    this.RF = RF;
  }
  
  public void addRF(Term term){
    this.RF.add(term);
  }

  public List<Condition> getRS() {
    return RS;
  }

  public void setRS(List<Condition> RS) {
    this.RS = RS;
  }
  
  public void addRS(Condition cond) {
    this.RS.add(cond);
  }
  
  @Override
  public String toString(){
    String concreteRules = "";
    int acc = 0;
    concreteRules = rulesName + "(";
    for (Map.Entry<String, String> entry : varsTypes.entrySet()) {
      acc++;
      if(acc == varsTypes.size()){
        concreteRules += entry.getKey() + ":" + entry.getValue();
      }else{
        concreteRules += entry.getKey() + ":" + entry.getValue() + ", ";
      }
    }
    concreteRules += ")\n";
    for(int i=0;i<LF.size();i++){
      if(i == 0){
        concreteRules += LF.get(i);
      }else{
        concreteRules += "." + LF.get(i);
      } 
    }
    for (int i=0;i<Splus.size();i++){
      concreteRules += "." + Splus.get(i).getVar().toString() + " in " + Splus.get(i).getTerm().toString();
    }
    for (int i=0;i<Snega.size();i++){
      concreteRules += "." + Snega.get(i).getVar().toString() + " notin " + Snega.get(i).getTerm().toString();
    }
    if(freshVars.getFreshs().isEmpty()){
      concreteRules += " => ";
    }else{
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
        concreteRules += RS.get(i).getVar().toString() + " in " + RS.get(i).getTerm().toString();
      }else{
        concreteRules += "." + RS.get(i).getVar().toString() + " in " + RS.get(i).getTerm().toString();
      }
    }
    for(int i=0;i<RF.size();i++){
      if(RS.isEmpty() && i == 0){
        concreteRules += RF.get(i);
      }else{
        concreteRules += "." + RF.get(i);
      } 
    }
    return concreteRules + ";";
  }
  
  @Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof ConcreteRule)) {
        return false;
      }
    ConcreteRule concreteRules = (ConcreteRule) o;		
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

