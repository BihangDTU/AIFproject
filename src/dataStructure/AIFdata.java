package dataStructure;
import java.util.ArrayList;
import java.util.List;

public class AIFdata extends AST {
	public List<Type> types;
	public List<Term> sets = new ArrayList<Term>();;
	public Functions functions;
	public Functions facts;
	public List<ConcreteRules> rules;
	public AIFdata(List<Type> types, List<Term> sets, Functions function, Functions facts, List<ConcreteRules> rules){
		this.types = types;
		this.sets = sets;
		this.functions = function;
		this.facts = facts;
		this.rules = rules;
	}
	AIFdata(List<ConcreteRules> rules){
		//this.types=types;
		this.rules=rules;
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
		for(ConcreteRules r:rules) s += r + "\n";
		return s;
	}
}
