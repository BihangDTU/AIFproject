import dataStructure.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StateTransition {
  private static int new_var_counter = 0;
  //private static int new_timplies_counter = 0;
  private HashSet<String> buildInTypes;
  //private static boolean isAttackFound = false; 
  
  DeepClone dClone = new DeepClone();
  Mgu mgu = new Mgu();

  public HashSet<String> getBuildInTypes() {
    return buildInTypes;
  }

  public void setBuildInTypes(HashSet<String> buildInTypes) {
    this.buildInTypes = buildInTypes;
  }
  
  /**
   * Returns all possible combination of contrate type. 
   * @param  typeInfo e.g. {A=[a, i], S=[s]}
   * @return e.g. [{A=i, S=s}, {A=a, S=s}]
   */
  public Set<HashMap<String,Term>> userTypeSubstitution(HashMap<String, List<Term>> typeInfo){
    Set<HashMap<String,Term>> combinations = new HashSet<>();
    if(!typeInfo.isEmpty()){
      Set<HashMap<String,Term>> newCombinations;
      Set<String> keySet = typeInfo.keySet();
      List<String> keyList = new ArrayList<>(keySet);
      int index = 0; 
      if(!keyList.isEmpty() && typeInfo.containsKey(keyList.get(0))){
      	for(Term i: typeInfo.get(keyList.get(0))) {
          HashMap<String,Term> newMap = new HashMap<>();
          newMap.put(keyList.get(0), i);
          combinations.add(newMap);
        }
      }
      index++;
      while(index < keyList.size()) {
        List<Term> nextList = typeInfo.get(keyList.get(index));
        newCombinations = new HashSet<>();
        for(HashMap<String,Term> first: combinations) {
          for(Term second: nextList) {
            HashMap<String,Term> newList = new HashMap<>();
            newList.putAll(first);
            newList.put(keyList.get(index),second);
            newCombinations.add(newList);
          }
        }
        combinations = newCombinations;
        index++;
      }
    }
    return combinations;
  }  
  
  /**
   * Returns a new ConcreteRules which the variable have been substituted if the variable occurs in the map subs 
   * @param  r     a concrete rule   
   * @param  subs  e.g. {A=a, S=s}
   * @return       substituted concrete rule
   */
  public ConcreteRule ConcreteRulesSubstitution(ConcreteRule r, HashMap<String,Term> subs){
    ConcreteRule rule = (ConcreteRule)dClone.deepClone(r);
    for(int i=0;i<r.getLF().size();i++){
      rule.getLF().set(i, termSubs(r.getLF().get(i),subs));   
    }
    for(int i=0;i<r.getSplus().size();i++){
      rule.getSplus().get(i).setVar(termSubs(r.getSplus().get(i).getVar(),subs));
      rule.getSplus().get(i).setTerm(termSubs(r.getSplus().get(i).getTerm(),subs));
    }
    for(int i=0;i<r.getSnega().size();i++){
      rule.getSnega().get(i).setVar(termSubs(r.getSnega().get(i).getVar(),subs));
      rule.getSnega().get(i).setTerm(termSubs(r.getSnega().get(i).getTerm(),subs));
    }
    for(int i=0;i<r.getRF().size();i++){
      rule.getRF().set(i, termSubs(r.getRF().get(i),subs));
    }
    for(int i=0;i<r.getRS().size();i++){
      rule.getRS().get(i).setVar(termSubs(r.getRS().get(i).getVar(),subs));
      rule.getRS().get(i).setTerm(termSubs(r.getRS().get(i).getTerm(),subs));
    }
    return rule;
  }
  
  /**
   * Return true if there exist a negative condition occurs in the state 
   * @param  sn  a concrete rule
   * @param  state
   * @return boolean  
   */
  public boolean isSnegaInState(List<Condition> sn, State state){
    if(sn.isEmpty()){
      return false;
    }
    for(Condition c : sn){
      if(isArbitrayType(c)){
        for(Condition splus : state.getPositiveConditins()){
          if(c.getVar().equals(splus.getVar()) && c.getTerm().getFactName().equals(splus.getTerm().getFactName())){
            return true;
          }
        }
      }else{
        if(state.getPositiveConditins().contains(c)){
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * create a new hashMap which only contains user define types (build in types are removed)
   * @param  types  e.g.{A=User, S=Server, PK=value}
   * @return remove build in types PK=value.  e.g.{A=User, S=Server}
   */
  public HashMap<String, String> getUserDefineType(HashMap<String, String> types){
    HashMap<String, String> userTypes = new HashMap<>();    
    userTypes = (HashMap<String, String>)dClone.deepClone(types);
    for (Map.Entry<String, String> entry : types.entrySet()) {
      if(buildInTypes.contains(entry.getValue())){
        userTypes.remove(entry.getKey());
      }
    }
    return userTypes;
  }
  
  /**
   * check whether the condition contains "_"
   * @param  condition  e.g. (PK,db_valid(_,_))
   * @return boolean
   */
  public boolean isArbitrayType(Condition condition){
    Variable c = new Variable("_");
    for(int i=0;i<condition.getTerm().getArguments().size();i++){
      if(condition.getTerm().getArguments().get(i).equals(c)){
        return true;
      }
    }
    return false;
  }
  
  /**
   * Returns a list of states after apply a AIF rule. 
   * apply the AIF rule for the state can add some new facts and positive condition 
   * to the current state or remove positive condition from the current sate.
   * @param  state  current state
   * @param  rule   one of concreteRyles from AIF specification
   * @param  concreteTypeInfo e.g. {Agent=[a, i, s], Honest=[a], User=[a, i], Server=[s], Dishon=[i]}
   * @return list of possible states
   */
  public List<State> cRuleApply(State state, ConcreteRule rule,HashMap<String,List<Term>> concreteTypeInfo){
    List<State> newStates = new ArrayList<>();
    HashMap<String, String> varsTypes = new HashMap<>();
    HashMap<String, List<Term>> cTypeInfo = new HashMap<>();
    varsTypes = getUserDefineType(rule.getVarsTypes()); //only keep user define types
    for (Map.Entry<String, String> e : varsTypes.entrySet()) {
      cTypeInfo.put(e.getKey(), concreteTypeInfo.get(e.getValue()));
    }
    /*get all prossible combination of user define types for substitution*/
    Set<HashMap<String,Term>> cTypesubstitutions = userTypeSubstitution(cTypeInfo); 
    for(HashMap<String,Term> ctype : cTypesubstitutions){ 
      if(!rule.getNewFreshVars().getFreshs().isEmpty()){ //check whether exist new generate variable.
        String newConcreteVarStr;
        Term newConcreteVar;
        for(int i=0;i<rule.getNewFreshVars().getFreshs().size();i++){
          new_var_counter ++;
          newConcreteVarStr = rule.getNewFreshVars().getFreshs().get(i).getVarName().toLowerCase() + "_" + new_var_counter;
          newConcreteVar = new Composed(newConcreteVarStr);
          ctype.put(rule.getNewFreshVars().getFreshs().get(i).getVarName(), newConcreteVar);
        }
      }
    }
    
    List<List<Substitution>> substitutions = new ArrayList<>(); 
    for(HashMap<String,Term> cTypeSubstitution : cTypesubstitutions){
      List<Substitution> subList = new ArrayList<>();
      if(rule.getLF().isEmpty()){
        subList.add(new Substitution(cTypeSubstitution));
        substitutions.add(subList);
      }else{
        for(Term lf : rule.getLF()){
          if(subList.isEmpty()){
            Term lfSubstituted = mgu.termSubstituted(lf, new Substitution(cTypeSubstitution)); 
            for(Term fact : state.getFacts()){
              Substitution subs = new Substitution();
              if(mgu.unifyTwoFacts(lfSubstituted,fact,subs)){
                subs.getSubstitution().putAll(cTypeSubstitution);
                subList.add(subs);
              }
            }
          }else{
            List<Substitution> subListCopy = (List<Substitution>)dClone.deepClone(subList);
            for(Substitution subs : subListCopy){
              Term lfSubstituted = mgu.termSubstituted(lf, subs);
              for(Term fact : state.getFacts()){
                Substitution subsCopy = (Substitution)dClone.deepClone(subs);
                if(mgu.unifyTwoFacts(lfSubstituted,fact,subsCopy)){
                  subList.add(subsCopy);
                }
              }
              subList.remove(subs);
            }
          }
        }
        substitutions.add(subList);
      }
      
    }
    
    for(List<Substitution> subsList : substitutions){
      for(Substitution subs : subsList){
        ConcreteRule cRuleSubstituted = ConcreteRulesSubstitution(rule,subs.getSubstitution());
        if(state.getFacts().containsAll(cRuleSubstituted.getLF())
            && state.getPositiveConditins().containsAll(cRuleSubstituted.getSplus())
            && !isSnegaInState(cRuleSubstituted.getSnega(),state)){
          State newState = (State)dClone.deepClone(state);
          newState.getFacts().addAll(cRuleSubstituted.getRF());
          newState.getPositiveConditins().addAll(cRuleSubstituted.getRS());
          for(Condition sp : cRuleSubstituted.getSplus()){
            newState.getPositiveConditins().remove(sp);
          }
          newStates.add(newState);
        }
      }
    }  
    return newStates;
  }
  
 
  /**
   *
   * @param state    current state node, normally is empty node
   * @param rules    concrete AIF rules, rule name as key
   * @param attackTraces   list of rule names represent the attack traces 
   * @param concreteTypeInfo  e.g. {Agent=[a, i, s], Honest=[a], User=[a, i], Server=[s], Dishon=[i]}
   * @return  all states that can be explore by the current state according to the attack traces
   */
  public Node stateTransition(Node state, HashMap<String, ConcreteRule> rules,List<String> attackTraces,HashMap<String,List<Term>> concreteTypeInfo){ 
    String applyRuleName;
    List<String> attackTracesCopy = new ArrayList<>();
    attackTracesCopy.addAll(attackTraces);
    if(!attackTraces.isEmpty()){
      applyRuleName = attackTraces.get(0);
      attackTracesCopy.remove(0);
      List<State> childrenState = cRuleApply(state.getState(), rules.get(applyRuleName), concreteTypeInfo);
      for(State child : childrenState){
        Node childState = new Node(child);
        childState.setRuleName(applyRuleName);
        stateTransition(childState,rules,attackTracesCopy,concreteTypeInfo);
        state.addChild(childState);        
      }
    }
    return state;
  }
  
   /*
   * @param concreteTypeInfo  e.g. {Agent=[a, i, s], Honest=[a], User=[a, i], Server=[s], Dishon=[i]}
   * @return  the set of concrete users e.g. [a,i,s]
   */
  public Set<Term> getConcreteTypeValue(HashMap<String,List<Term>> concreteTypeInfo){
    Set<Term> concreteValues = new HashSet<>();
    for (Map.Entry<String,List<Term>> entry : concreteTypeInfo.entrySet()) {
      concreteValues.addAll(entry.getValue());
    }
    return concreteValues;
  }
  
   /*
   * @param term    concrete AIF rules, rule name as key
   * @param state    current state
   * @param contreteUsers   e.g. [a,i,s]
   * @param position  e.g. {ring=0, db_valid=1, db_revoked=2}
   * @return  abstract term 
   */
  public Term AbsTermConversion(Term term,State state, Set<Term> contreteUsers,HashMap<String, Integer> position){
    if(term instanceof Composed){
      if(term.getArguments().isEmpty()){
        if(!contreteUsers.contains(term)){
          if(term.getFactName().equals("attack")){
            return term;
          }else{
            Composed val = new Composed("val");
          Term t0 = new Composed("0");
          for(int i=0;i<position.size();i++){
            val.setArguments(t0);
          }
          for(Condition c : state.getPositiveConditins()){
            if(c.getVar().equals(term)){
              int pos = position.get(c.getTerm().getFactName());
              val.getArguments().set(pos, c.getTerm());
            }
          }
          return val;
          }
        }else{
          return term;
        }
      }else{
        Composed absTerm = new Composed(term.getFactName());
        for(Term t : term.getArguments()){
          absTerm.setArguments(AbsTermConversion(t,state, contreteUsers,position));
        }
        return absTerm;
      }
    }
    return null;
  }
  
  /*
   * @param state    current state
   * @param concreteTypeInfo    e.g. {Agent=[a, i, s], Honest=[a], User=[a, i], Server=[s], Dishon=[i]}
   * @param position  e.g. {ring=0, db_valid=1, db_revoked=2}
   * @return  abstract state
   */
  public State concreteToAbstractState(State state,HashMap<String,List<Term>> concreteTypeInfo, HashMap<String, Integer> position){
    State absStae = new State();
    Set<Term> contreteUsers = new HashSet<>();
    contreteUsers = getConcreteTypeValue(concreteTypeInfo);
    for(Term fact : state.getFacts()){
       absStae.addFacts(AbsTermConversion(fact,state,contreteUsers,position));   
    }
    return absStae;
  }
  
  /**
   * Returns list of variables. 
   * @param  term e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @return e.g. [PK, A, NPK]
   */
  public List<Variable> getVars(Term term){    
    List<Variable> var = new ArrayList<>();
    if(term instanceof Variable){
      var.add(((Variable)term));
      return var;
    }else if(term instanceof Composed){
      for(int i=0; i < term.getArguments().size(); i++){
        if(term.getArguments().get(i) instanceof Variable){
          var.add(((Variable)term.getArguments().get(i)));
        }else if(term.getArguments().get(i) instanceof Composed){
          var.addAll(getVars(term.getArguments().get(i)));
        }
      }
      return var;
    }   
    return var;
  }
  
  /**
   * Returns a substituted term. 
   * only substitute the variables if it occurs in the map subs
   * @param  t    e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @param  subs e.g. {PK=pk, A=a}
   * @return e.g. iknows(sign(inv(pk),pair(a,NPK)))
   */
  public Term termSubs(Term t, HashMap<String,Term> subs){
    Term t_copy = (Term)dClone.deepClone(t);
    if(!getVars(t).isEmpty()){
      if(t instanceof Variable){
        if(subs.containsKey(((Variable)t).getVarName())){
          return subs.get(((Variable)t).getVarName());    
        }
      }else{
        for(int i=0; i < t.getArguments().size(); i++){
          if(t.getArguments().get(i) instanceof Variable){
            String var = ((Variable)(t.getArguments().get(i))).getVarName();
            if(subs.containsKey(var)){
              t_copy.getArguments().set(i, subs.get(var));
            }
          }else{
            t_copy.getArguments().set(i, termSubs(t_copy.getArguments().get(i),subs));
          }
        }
      }
    }
    return t_copy;
  }
}
