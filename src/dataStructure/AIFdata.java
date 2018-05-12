package dataStructure;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AIFdata extends AST {
	private List<Type> types;
	private List<Term> sets = new ArrayList<Term>();;
	private Functions functions;
	private Functions facts;
	private List<ConcreteRule> rules;
	private HashSet<String> buildInTypes = new HashSet<String>();
	public AIFdata(List<Type> types, List<Term> sets, Functions function, 
									Functions facts, List<ConcreteRule> rules,HashSet<String> buildInTypes){
		this.types = types;
		this.sets = sets;
		this.functions = function;
		this.facts = facts;
		this.rules = rules;
		this.buildInTypes = buildInTypes;
	}
	public AIFdata(List<ConcreteRule> rules){
		//this.types=types;
		this.rules=rules;
	}
	
	public List<Type> getTypes() {
		return types;
	}
	public void setTypes(List<Type> types) {
		this.types = types;
	}
	public List<Term> getSets() {
		return sets;
	}
	public void setSets(List<Term> sets) {
		this.sets = sets;
	}
	public Functions getFunctions() {
		return functions;
	}
	public void setFunctions(Functions functions) {
		this.functions = functions;
	}
	public Functions getFacts() {
		return facts;
	}
	public void setFacts(Functions facts) {
		this.facts = facts;
	}
	public List<ConcreteRule> getRules() {
		return rules;
	}
	public void setRules(List<ConcreteRule> rules) {
		this.rules = rules;
	}
	
	public HashSet<String> getBuildInTypes() {
		return buildInTypes;
	}
	
	public void setBuildInTypes(HashSet<String> buildInTypes) {
		this.buildInTypes = buildInTypes;
	}
	
	@Override
	public String toString(){
		String s="";
		for(Type t:types) s += t + "\n";
		for(int i=0;i<sets.size();i++){
			if(i<sets.size()-1){
				s += sets.get(i).toString() + ", ";
			}else{
				s += sets.get(i).toString() + ";\n";
			}
		}
		s += functions.toString();
		s += facts.toString();
		for(ConcreteRule r:rules) s += r + "\n";
		return s;
	}
}
