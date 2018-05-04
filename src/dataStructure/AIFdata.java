package dataStructure;
import java.util.List;

public class AIFdata extends AST {
	public List<Type> types;
	public Terms sets;
	public Functions functions;
	public Functions facts;
	public List<ConcreteRules> rules;
	public AIFdata(List<Type> types, Terms sets, Functions function, Functions facts, List<ConcreteRules> rules){
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
		s += sets.toString() + "\n";
		s += functions.toString();
		s += facts.toString();
		for(ConcreteRules r:rules) s += r + "\n";
		return s;
	}
}
