package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AbstractRule extends AST implements Serializable{
	/**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String rulesName;
  private HashMap<String, String> varsTypes = new HashMap<>();
  private List<Term> LF = new ArrayList<>();
  private List<Term> freshVars;
  private List<Term> RF = new ArrayList<>();
  private HashMap<String,Timplies> timplies = new HashMap<>();
  
  public AbstractRule(){}
	public AbstractRule(String rulesName, HashMap<String, String> varsTypes,
			List<Term> lF, List<Term> rF,List<Term> freshVars, HashMap<String,Timplies> timplies) {
		super();
		this.rulesName = rulesName;
		this.varsTypes = varsTypes;
		this.LF = lF;
		this.freshVars = freshVars;
		this.RF = rF;
		this.timplies = timplies;
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
	public List<Term> getLF() {
		return LF;
	}
	public void setLF(List<Term> lF) {
		LF = lF;
	}
	public List<Term> getFreshVars() {
		return freshVars;
	}
	public void setFreshVars(List<Term> freshVars) {
		this.freshVars = freshVars;
	}
	public List<Term> getRF() {
		return RF;
	}
	public void setRF(List<Term> rF) {
		RF = rF;
	}
  
	public HashMap<String,Timplies> getTimplies() {
		return timplies;
	}
	public void setTimplies(HashMap<String,Timplies> timplies) {
		this.timplies = timplies;
	}
	
  @Override
  public String toString(){
    String abstractRule = "";
    int acc = 0;
    abstractRule = rulesName + "(";
    for (Map.Entry<String, String> entry : varsTypes.entrySet()) {
      acc++;
      if(acc == varsTypes.size()){
      	abstractRule += entry.getKey() + ":" + entry.getValue();
      }else{
      	abstractRule += entry.getKey() + ":" + entry.getValue() + ", ";
      }
    }
    abstractRule += ")\n";
    for(int i=0;i<LF.size();i++){
      if(i == 0){
      	abstractRule += LF.get(i);
      }else{
      	abstractRule += "." + LF.get(i);
      } 
    }
    /*if(freshVars.isEmpty()){
    	abstractRule += " => ";
    }else{
    	abstractRule += " =[";
      for(int i=0;i<freshVars.size();i++){
      	if(i<freshVars.size()-1){
      		abstractRule += freshVars.get(i).toString() + ",";
      	}else{
      		abstractRule += freshVars.get(i).toString() + "]=> ";
      	}
      }
    }*/
    abstractRule += " --> ";
    for(int i=0;i<RF.size();i++){
      if(i == 0){
      	abstractRule += RF.get(i);
      }else{
      	abstractRule += "." + RF.get(i);
      } 
    }
  	for(Map.Entry<String, Timplies> tMap : timplies.entrySet()){
  	  if(tMap.getValue().isTimplies()){
  	    abstractRule += "." + tMap.getValue().toString();
  	  }
  	}
    return abstractRule + ";";
  }
	
	@Override
  public boolean equals(Object o){
    if (o == this) return true;
      if (!(o instanceof AbstractRule)) {
        return false;
      }
      AbstractRule abstractRule = (AbstractRule) o;		
    return Objects.equals(rulesName, abstractRule.rulesName) &&
            Objects.equals(varsTypes, abstractRule.varsTypes) &&
            Objects.equals(LF, abstractRule.LF) &&
           // Objects.equals(freshVars, abstractRule.freshVars) &&
            Objects.equals(RF, abstractRule.RF);
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.rulesName);
    hash = 19 * hash + Objects.hashCode(this.varsTypes);
    hash = 19 * hash + Objects.hashCode(this.LF);
   // hash = 19 * hash + Objects.hashCode(this.freshVars);
    hash = 19 * hash + Objects.hashCode(this.RF);
    return hash;
  }
}
