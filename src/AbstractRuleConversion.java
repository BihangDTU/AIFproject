import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataStructure.AIFdata;
import dataStructure.AST;
import dataStructure.AbstractRule;
import dataStructure.Composed;
import dataStructure.ConcreteRule;
import dataStructure.Condition;
import dataStructure.Term;
import dataStructure.Variable;


public class AbstractRuleConversion {
	
	DeepClone dClone = new DeepClone();
	StateTransition st = new StateTransition();
	
	/**
   * Returns a substituted term. 
   * only substitute the variables if it occurs in the map subs
   * @param  t    e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @param  subs e.g. {PK=pk, A=a}
   * @return e.g. iknows(sign(inv(pk),pair(a,NPK)))
   */
  public Term termSubs(Term t, HashMap<String,String> subs){   // need move to new calss
    Term t_copy = (Term)dClone.deepClone(t);
    if(!st.getVars(t).isEmpty()){
      if(t instanceof Variable){
        if(subs.containsKey(((Variable)t).getVarName())){
          return new Variable(subs.get(((Variable)t).getVarName()));		
        }
      }else{
        for(int i=0; i < t.getArguments().size(); i++){
          if(t.getArguments().get(i) instanceof Variable){
            String var = ((Variable)(t.getArguments().get(i))).getVarName();
            if(subs.containsKey(var)){
              t_copy.getArguments().set(i, new Variable(subs.get(var)));
            }
          }else{
            t_copy.getArguments().set(i, termSubs(t_copy.getArguments().get(i),subs));
          }
        }
      }
    }
    return t_copy;
  }
	public HashMap<String, Integer> getSetPosition(AST aifAST){
  	HashMap<String, Integer> setPosition = new HashMap<String, Integer>();
  	List<Term> sets = new ArrayList<Term>(((AIFdata)aifAST).getSets());
  	for(int i=0;i<sets.size();i++){
  		setPosition.put(sets.get(i).getFactName(), i);
  	}
  	return setPosition;
  }
	
	/* haven't test yet*/
  public Term concreteTermToAbsTerm(Term term,HashMap<String, String> varsTypes, List<Condition> conditions,HashMap<String, Integer> setPosition){
  	if(term instanceof Composed){
  		if(term.getArguments().isEmpty()){
  			return term;
  		}else{
  			Composed absTerm = new Composed(term.getFactName());
  			for(Term subTerm : term.getArguments()){
  				absTerm.setArguments(concreteTermToAbsTerm(subTerm,varsTypes,conditions,setPosition));
  			}
  			return absTerm;
  		}
  	}else if(term instanceof Variable){
  		if(varsTypes.get(term.getVarName()).equals("value")){
  			Composed val = new Composed("val");
  			Composed zero = new Composed("0");
  			Composed _ = new Composed("_");
  			for(int j=0;j<setPosition.size();j++){ // val initialize to val(_,_,_)
  				val.setArguments(_);
  			}
  			for(Condition c : conditions){
  				if(c.getVar().equals(term)){
  					int position = setPosition.get(c.getTerm().getFactName());
  					if(c.positive){
  						val.getArguments().set(position, c.getTerm());
  					}else{
  						val.getArguments().set(position, zero);
  					}
  				}
  			}
  			return val;
  		}
  	}	
  	return term;
  }
  
  /**/
  public AbstractRule concreteRuleToAbsRuleConversion(AST aifAST, String conRuleName){
  	HashMap<String, ConcreteRule> concreteRules = new HashMap<>(); 
		for(ConcreteRule cr: ((AIFdata)aifAST).getRules()){
			concreteRules.put(cr.getRulesName(), cr);
		}
		if(!concreteRules.containsKey(conRuleName)){
			System.err.println("Rule name not exsit.\n");
  	  System.exit(-1);
		}
		ConcreteRule conRule = concreteRules.get(conRuleName);
  	HashMap<String, Integer> setPosition =  getSetPosition(aifAST);
  	AbstractRule absRule = new AbstractRule();
  	absRule.setRulesName(conRule.getRulesName());
  	absRule.setVarsTypes(conRule.getVarsTypes());
  	List<Term> LF = new ArrayList<>();
  	List<Term> freshs = new ArrayList<>();
  	List<Term> RF = new ArrayList<>();
  	List<Condition> conditions_left = new ArrayList<>(conRule.getSplus());
  	conditions_left.addAll(conRule.getSnega());
  	for(Term lf : conRule.getLF()){
  		LF.add(concreteTermToAbsTerm(lf,conRule.getVarsTypes(),conditions_left,setPosition));
  	}
  	absRule.setLF(LF);
  	List<Condition> conditions_freshVars = new ArrayList<>();
  	for(Variable var : conRule.getNewFreshVars().getFreshs()){
  		for(Term set :((AIFdata)aifAST).getSets()){
  			conditions_freshVars.add(new Condition(var,set,false));
  		}
  	}
  	for(Term freshVar : conRule.getNewFreshVars().getFreshs()){
  		freshs.add(concreteTermToAbsTerm(freshVar,conRule.getVarsTypes(),conditions_freshVars,setPosition));
  	}
  	absRule.setFreshVars(freshs);
  	List<Condition> conditions_rs = new ArrayList<>(conRule.getRS());
  	for(Condition cp : conRule.getSplus()){
			if(!conRule.getRS().contains(cp)){
				conditions_rs.add(new Condition(cp.getVar(),cp.getTerm(),false));
			}
		}
  	List<Condition> conditions_freshVarsCopy = new ArrayList<>(conditions_freshVars);
  	for(Condition rs : conRule.getRS()){
  		for(Condition cn : conRule.getSnega()){
    		if(rs.getVar().equals(cn.getVar()) && !rs.getTerm().getFactName().equals(cn.getTerm().getFactName())){
    			conditions_rs.add(cn);
    		}
    	}
  		for(Condition cVarFresh : conditions_freshVars){
  			if(rs.getVar().equals(cVarFresh.getVar()) && rs.getTerm().getFactName().equals(cVarFresh.getTerm().getFactName())){
  				conditions_freshVarsCopy.remove(cVarFresh);
    		}
  		}
  	}
  	conditions_rs.addAll(conditions_freshVarsCopy);
  	for(Term rf : conRule.getRF()){
  		RF.add(concreteTermToAbsTerm(rf,conRule.getVarsTypes(),conditions_rs,setPosition));
  	}
  	absRule.setRF(RF);
  	return absRule;
  }
  
  public AbstractRule concreteRuleToAbsRuleConversion(AbstractRule absRule){
  	AbstractRule absRuleSubstituted = new AbstractRule();
  	absRuleSubstituted.setRulesName(absRule.getRulesName());
  	absRuleSubstituted.setVarsTypes(absRule.getVarsTypes());
  	absRuleSubstituted.setFreshVars(absRule.getFreshVars());
  	List<Term> LF = new ArrayList<>();
  	for(Term lf : absRule.getLF()){
  		LF.add(termSubs(lf,absRule.getVarsTypes()));
  	}
  	absRuleSubstituted.setLF(LF);
  	
  	List<Term> RF = new ArrayList<>();
  	for(Term rf : absRule.getRF()){
  		RF.add(termSubs(rf,absRule.getVarsTypes()));
  	}
  	absRuleSubstituted.setRF(RF);
  	return absRuleSubstituted;
  }

}
