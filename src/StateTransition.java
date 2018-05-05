import dataStructure.*;
import myException.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateTransition {
  private static int new_var_counter = 0;
  private static int new_timplies_counter = 0;
  private ArrayList<String> buildInTypes;
  private static boolean isAttackFound = false; 
  
  DeepClone dClone = new DeepClone();
  Mgu mgu = new Mgu();

  public ArrayList<String> getBuildInTypes() {
    return buildInTypes;
  }

  public void setBuildInTypes(ArrayList<String> buildInTypes) {
    this.buildInTypes = buildInTypes;
  }
  
  /**
   * Returns all possible combination of contrate type. 
   * @param  typeInfo e.g. {A=[a, i], S=[s]}
   * @return e.g. [{A=i, S=s}, {A=a, S=s}]
   */
  public Set<HashMap<String,Term>> userTypeSubstitution(HashMap<String, ArrayList<Term>> typeInfo){
    Set<HashMap<String,Term>> combinations = new HashSet<>();
    if(!typeInfo.isEmpty()){
      Set<HashMap<String,Term>> newCombinations;
      Set<String> keySet = typeInfo.keySet();
      ArrayList<String> keyList = new ArrayList<>(keySet);
      int index = 0; 
      for(Term i: typeInfo.get(keyList.get(0))) {
        HashMap<String,Term> newMap = new HashMap<>();
        newMap.put(keyList.get(0), i);
        combinations.add(newMap);
      }
      index++;
      while(index < keyList.size()) {
        ArrayList<Term> nextList = typeInfo.get(keyList.get(index));
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
   * Returns list of variables. 
   * @param  term e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @return e.g. [PK, A, NPK]
   */
  public ArrayList<Variable> getVars(Term term){    
    ArrayList<Variable> var = new ArrayList<>();
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
        if(subs.containsKey((Variable)t)){
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
  
  /**
   * Returns a new ConcreteRules which the variable have been substituted if the variable occurs in the map subs 
   * @param  r     a concrete rule   
   * @param  subs  e.g. {A=a, S=s}
   * @return       substituted concrete rule
   */
  public ConcreteRules ConcreteRulesSubstitution(ConcreteRules r, HashMap<String,Term> subs){
    ConcreteRules rule = (ConcreteRules)dClone.deepClone(r);
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
   * Return true if there is at least one variable in the rule
   * @param  rule  a concrete rule   
   * @return boolean  
   */
  public boolean isRuleContainsVar(ConcreteRules rule){
    for(Term lf : rule.getLF()){
      if(!getVars(lf).isEmpty()){
        return true;
      }
    }
    for(Term rf : rule.getRF()){
      if(!getVars(rf).isEmpty()){
        return true;
      }
    }
    for(Condition sp : rule.getSplus()){
      if(!(getVars(sp.getVar()).isEmpty()) || !(getVars(sp.getTerm()).isEmpty())){
        return true;
      }
    }
    for(Condition sn : rule.getSnega()){
      if(!(getVars(sn.getVar()).isEmpty()) || !(getVars(sn.getTerm()).isEmpty())){
        return true;
      }
    }
    for(Condition rs : rule.getRS()){
      if(!(getVars(rs.getVar()).isEmpty()) || !(getVars(rs.getTerm()).isEmpty())){
        return true;
      }
    }
    return false;
  }
  /**
   * Return true if there exist a negative condition occurs in the state 
   * @param  sn  a concrete rule
   * @param  state
   * @return boolean  
   */
  public boolean isSnegaInState(ArrayList<Condition> sn, State state){
    if(sn.isEmpty()){
      return false;
    }
    return state.getPositiveConditins().containsAll(sn);
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
  public boolean needExpandCondition(Condition condition){
    Composed c = new Composed("_");
    for(int i=0;i<condition.getTerm().getArguments().size();i++){
      if(condition.getTerm().getArguments().get(i).equals(c)){
        return true;
      }
    }
    return false;
  }
  
  /**
   *
   * @param condition  e.g. (PK,db_valid(User, Server))
   * @param subs       e.g. {User=a, Server=s}
   * @return (PK,db_valid(a,s))
   */
  public Condition conditionSubs(Condition condition, HashMap<String,Term> subs){
    Condition cond = new Condition();
    cond = (Condition)dClone.deepClone(condition);
    for(int i=0;i<condition.getTerm().getArguments().size();i++){
      if(subs.containsKey(condition.getTerm().getArguments().get(i))){
        cond.getTerm().getArguments().set(i, subs.get(condition.getTerm().getArguments().get(i)));
      }
    }
    return cond;
  }
  /*ring(User),db_valid(User,Server),db_revoked(User,Server)*/
  /*{ring->[User],db_valid->[User,Server],db_revoked->[User,Server]}*/
  
  /**
   *
   * @param condition         e.g. (PK,db_valid(_,_))
   * @param concreteTypeInfo  e.g. {Agent=[a, i, s], Honest=[a], User=[a, i], Server=[s], Dishon=[i]}
   * @param dbSet             e.g. [ring(User), db_valid(User,Server), db_revoked(User,Server)]
   * @return  [(PK,db_valid(a,s)), (PK,db_valid(i,s)), (PK,db_revoked(a,s)),(PK,db_revoked(i,s))]
   */
  public ArrayList<Condition> expandConditions(Condition condition,HashMap<String,ArrayList<Term>> concreteTypeInfo, ArrayList<Term> dbSet){
    ArrayList<Condition> conditions = new ArrayList<>();
    Condition cond = new Condition();
    cond = (Condition)dClone.deepClone(condition);
    HashMap<Variable,ArrayList<Term>> types = new HashMap<>();
    for (Map.Entry<String,ArrayList<Term>> entry : concreteTypeInfo.entrySet()) {
      Variable var = new Variable(entry.getKey());
      types.put(var, entry.getValue());
    }
    //System.out.println("concreteTypeInfo:" + concreteTypeInfo);
    //System.out.println("types:" + types);
    //Set<HashMap<Variable,Term>> expandTypes = userTypeSubstitution(types);
    HashMap<String,ArrayList<Term>> dbMap = new HashMap<>();
    for(Term db : dbSet){
      ArrayList<Term> users = new ArrayList<>();
      for(Term user : db.getArguments()){
        users.add(user);
      }
      dbMap.put(db.getFactName(), users);
    }
    if(needExpandCondition(condition)){
      //Set<HashMap<Variable,Term>> expandTypes = new HashSet<>();
      HashMap<String,ArrayList<Term>> subTypes = new HashMap<>();
      for(Map.Entry<String,ArrayList<Term>> entry : dbMap.entrySet()){
        if(condition.getTerm().getFactName().equals(entry.getKey())){
          for(int i=0; i<entry.getValue().size();i++){
            if(condition.getTerm().getArguments().get(i).getFactName().equals("_")){
              cond.getTerm().getArguments().set(i, entry.getValue().get(i));
              //Variable var = new Variable(entry.getValue().get(i).getVarName());
              String var = entry.getValue().get(i).getVarName();
              subTypes.put(var, types.get(var));
            }
          }
        }
      }
      //System.out.println("subTypes: " + subTypes);
      Set<HashMap<String,Term>> expandTypes = userTypeSubstitution(subTypes);
      //System.out.println("expandTypes: " + expandTypes);
      for(HashMap<String,Term> type : expandTypes){
        Condition c = conditionSubs(cond,type);
        conditions.add(c);
      }
    }else{
      conditions.add(cond);
    }
    //System.out.println(dbMap);
    //System.out.println(cond.getVar().toString() + " in " + cond.getTerm().toString());
    return conditions;
  }
  
  /**
   * Returns list of states after apply a AIF rule. 
   * apply the AIF rule for the state can add new some new facts and positive condition 
   * to the current state or remove positive condition from the current sate.
   * @param  state  current state
   * @param  rule   one of concreteRyles from AIF specification
   * @param  concreteTypeInfo e.g. {Agent=[a, i, s], Honest=[a], User=[a, i], Server=[s], Dishon=[i]}
   * @param  dbSet  e.g. [ring(User), db_valid(User,Server), db_revoked(User,Server)]
   * @return list of possible states
   */
  public ArrayList<State> ruleApply(State state, ConcreteRules rule,HashMap<String,ArrayList<Term>> concreteTypeInfo, ArrayList<Term> dbSet){
    ArrayList<State> newStates = new ArrayList<>();
    HashMap<String, String> varsTypes = new HashMap<>();
    HashMap<String, ArrayList<Term>> cTypeInfo = new HashMap<>();
    varsTypes = getUserDefineType(rule.getVarsTypes()); //only keep user define types
    for (Map.Entry<String, String> e : varsTypes.entrySet()) {
      cTypeInfo.put(e.getKey(), concreteTypeInfo.get(e.getValue()));
    }
    //System.out.println("cTypeInfo: " + cTypeInfo);
    /*get all prossible combination of user define types for substitution*/
    Set<HashMap<String,Term>> concreteTypeSubs = userTypeSubstitution(cTypeInfo); 
    //System.out.println("concreteTypeValue: " + concreteTypeSubs);
    Set<ConcreteRules> contreteRules = new HashSet();
    /* instantiate AIF rule with contrete instance of user define type */
    if(concreteTypeSubs.isEmpty()){
      contreteRules.add(rule);
    }else{
      for(HashMap<String,Term> ctype : concreteTypeSubs){ // need to update to multiple fresh variables
        if(!rule.getNewFreshVars().getFreshs().isEmpty()){ //check whether exist new generate variable.
          new_var_counter ++;
          String newConcreteVarStr = rule.getNewFreshVars().getFreshs().get(0).getVarName().toLowerCase() + "_" + new_var_counter;
          Term newConcreteVar = new Composed(newConcreteVarStr);
          ctype.put(rule.getNewFreshVars().getFreshs().get(0).getVarName(), newConcreteVar);
        }
        /*substitute contrete type values to the rule*/
        /*After substitution, contreteRules will contaions all rules which have been substituted with concrete user type value*/
        contreteRules.add(ConcreteRulesSubstitution(rule,ctype));
      }   
      // System.out.println("contreteRules: " + contreteRules.size());
    }
    ArrayList<ConcreteRules> contreteRulesList = new ArrayList<>(contreteRules);
    ArrayList<ConcreteRules> contreteRulesList_copy = (ArrayList<ConcreteRules>)dClone.deepClone(contreteRulesList);
    for(int i=0;i<contreteRulesList_copy.size();i++){
      /*if the database b set in position condition contains any types with "_"*/
      /* then remove that database and add all expanded database */
      for(Condition crp : contreteRulesList.get(i).getSplus()){
        if(needExpandCondition(crp)){
          contreteRulesList_copy.get(i).getSplus().addAll(expandConditions(crp,concreteTypeInfo,dbSet));
          contreteRulesList_copy.get(i).getSplus().remove(crp);
        }
      }
      /*if the database b set in negative condition contains any types with "_"*/
      /* then remove that database and add all expanded database */
      for(Condition crn : contreteRulesList.get(i).getSnega()){
        if(needExpandCondition(crn)){
          contreteRulesList_copy.get(i).getSnega().addAll(expandConditions(crn,concreteTypeInfo,dbSet));
          contreteRulesList_copy.get(i).getSnega().remove(crn);
        }
      }
      //System.out.println(contreteRulesList_copy.get(i).toString());
    }
    /*apply each substituded rule to the current state*/
    for(ConcreteRules cr : contreteRulesList_copy){
      /* no prerequest condition*/
      if(cr.getLF().isEmpty() && cr.getSplus().isEmpty() && cr.getSnega().isEmpty()){
        State newState = (State)dClone.deepClone(state);
        for(Term rf : cr.getRF()){
          newState.getFacts().add(rf);
        }   
        for(Condition rs : cr.getRS()){
          newState.getPositiveConditins().add(rs);
        }
        newStates.add(newState);
      }else{
        /* left hand side is not empty */
        ArrayList<ConcreteRules> contreteR_satisfy = new ArrayList<>();
        ArrayList<ConcreteRules> contreteR_satisfy_copy = new ArrayList<>();
        boolean isFirstFact = true;
        for(int j=0;j<cr.getLF().size();j++){
          for(Term fact : state.getFacts()){
            /* check all facts in the state which can be unified */
            try{
              Substitution valueSub = mgu.mgu(cr.getLF().get(j), fact, new Substitution());
              if(valueSub.getUnifierState()){
                if(isFirstFact){
                  contreteR_satisfy.add(ConcreteRulesSubstitution(cr,valueSub.getSubstitution()));
                  contreteR_satisfy_copy.add(ConcreteRulesSubstitution(cr,valueSub.getSubstitution()));
                }else{
                  for(int i=0;i<contreteR_satisfy.size();i++){
                    try{
                      Substitution vSub = mgu.mgu(contreteR_satisfy.get(i).getLF().get(j), fact, new Substitution());
                      if(vSub.getUnifierState() && !vSub.getSubstitution().isEmpty()){
                        //contreteR_satisfy_copy.remove(i);
                        contreteR_satisfy_copy.add(ConcreteRulesSubstitution(contreteR_satisfy.get(i),vSub.getSubstitution()));
                        //break;
                      }
                    }catch(UnificationFailedException e){}
                  }
                  contreteR_satisfy.clear();
                  contreteR_satisfy.addAll(contreteR_satisfy_copy);
                }  
              }
            }catch(UnificationFailedException e){/*do nothing*/}   
          }
          isFirstFact = false;
        }
        contreteR_satisfy_copy.clear();
        contreteR_satisfy_copy.addAll(contreteR_satisfy);
        /*for(ConcreteRules r_c : contreteR_satisfy_copy){
          System.out.println("rc: "+r_c.toString());
        }*/        
        for(ConcreteRules r_copy : contreteR_satisfy_copy){
          if(isRuleContainsVar(r_copy)){
            contreteR_satisfy.remove(r_copy);
          }
        }
        /*for(ConcreteRules r_c : contreteR_satisfy){
          System.out.println("rc: "+r_c.toString());
        }*/ 
        /* if the left hand side rule satisfy the state transform ocndition then we add RF
           and RS to the state, and remove positive condition from the state.
        */
        for(ConcreteRules r_satisfy : contreteR_satisfy){
          if(state.getPositiveConditins().containsAll(r_satisfy.getSplus()) 
                  && state.getFacts().containsAll(r_satisfy.getLF())
                  && !isSnegaInState(r_satisfy.getSnega(),state)){
            State newState = (State)dClone.deepClone(state);
            for(Term rf : r_satisfy.getRF()){
              newState.getFacts().add(rf);
            }   
            for(Condition rs : r_satisfy.getRS()){
              newState.getPositiveConditins().add(rs);
            }
            for(Condition sp : r_satisfy.getSplus()){
              newState.getPositiveConditins().remove(sp);
            }
            newStates.add(newState);
          }
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
   * @param dbSet             e.g. [ring(User), db_valid(User,Server), db_revoked(User,Server)]
   * @return  all states that can be explore by the current state according to the attack traces
   */
  public Node stateTransition(Node state, HashMap<String, ConcreteRules> rules,ArrayList<String> attackTraces,
                                     HashMap<String,ArrayList<Term>> concreteTypeInfo,ArrayList<Term> dbSet){
   // Node<State> newState = new Node<>(state.getState());
    //ArrayList<String> concreteAttackTreces = new ArrayList<>(); 
    String applyRuleName;
    ArrayList<String> attackTracesCopy = new ArrayList<>();
    attackTracesCopy.addAll(attackTraces);
    if(!attackTraces.isEmpty()){
      applyRuleName = attackTraces.get(0);
      attackTracesCopy.remove(0);
      //System.out.println(attackTracesCopy);
      ArrayList<State> childrenState = ruleApply(state.getState(), rules.get(applyRuleName), concreteTypeInfo, dbSet);
      //System.out.println(applyRuleName + ":");
      for(State child : childrenState){
        //System.out.println("  " + child);
        Node childState = new Node(child);
        childState.setRuleName(applyRuleName);
        stateTransition(childState,rules,attackTracesCopy,concreteTypeInfo,dbSet);
        state.addChild(childState);        
      }
    }
    return state;
  }
  
  /*public Node stateTrace(Node state, HashMap<String, ConcreteRules> rules,ArrayList<String> attackTraces,
                                     HashMap<String,ArrayList<Term>> concreteTypeInfo,ArrayList<Term> dbSet, ArrayList<ConcreteAttackTrace> trace){
   // Node<State> newState = new Node<>(state.getState());
    //ArrayList<String> concreteAttackTreces = new ArrayList<>(); 
    String applyRuleName;
    ArrayList<String> attackTracesCopy = new ArrayList<>();
    attackTracesCopy.addAll(attackTraces);
    ArrayList<ConcreteAttackTrace> tt = removeElementsForContreteList(trace,attackTracesCopy);
    System.out.println("-----------------");
    for(ConcreteAttackTrace t : tt){
      System.out.println(t.getRuleName());
      System.out.println(t.getState());
    }
    System.out.println("-----------------");
    if(!attackTraces.isEmpty()){
      applyRuleName = attackTraces.get(0);
      attackTracesCopy.remove(0);
      System.out.println(attackTracesCopy);
      ArrayList<State> childrenState = ruleApply(state.getState(), rules.get(applyRuleName), concreteTypeInfo, dbSet);
      //System.out.println(applyRuleName + ":");
      for(State child : childrenState){
        //System.out.println("  " + child);
        ConcreteAttackTrace newTrace = new ConcreteAttackTrace(applyRuleName,child);
        trace.add(newTrace);
        Node childState = new Node(child);
        stateTrace(childState,rules,attackTracesCopy,concreteTypeInfo,dbSet,trace);
        state.addChild(childState);        
      }
    }
    return state;
  }*/
  
   /*
   * @param concreteTypeInfo  e.g. {Agent=[a, i, s], Honest=[a], User=[a, i], Server=[s], Dishon=[i]}
   * @return  the set of concrete users e.g. [a,i,s]
   */
  public Set<Term> getConcreteTypeValue(HashMap<String,ArrayList<Term>> concreteTypeInfo){
    Set<Term> concreteValues = new HashSet<>();
    for (Map.Entry<String,ArrayList<Term>> entry : concreteTypeInfo.entrySet()) {
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
  public State concreteToAbstractState(State state,HashMap<String,ArrayList<Term>> concreteTypeInfo, HashMap<String, Integer> position){
    State absStae = new State();
    Set<Term> contreteUsers = new HashSet<>();
    contreteUsers = getConcreteTypeValue(concreteTypeInfo);
    for(Term fact : state.getFacts()){
       absStae.addFacts(AbsTermConversion(fact,state,contreteUsers,position));   
    }
    return absStae;
  }
  
 /* public ArrayList<ConcreteAttackTrace> removeElementsForContreteList(ArrayList<ConcreteAttackTrace> list, ArrayList<String>removeElements){
    //ArrayList<ConcreteAttackTrace> newList = (ArrayList<ConcreteAttackTrace>)dClone.deepClone(list);
    for(String e : removeElements){
      for(int i=0;i<list.size();i++){
        if(list.get(i).getRuleName().equals(e)){
          list.remove(i);
        }
      }
    }   
    return list;
  }
  */
/*  public boolean isAttackFound(State state){
    for(Term t : state.getFacts()){
      if(t.getFactName().equals("attack")){
        return true;
      }
    }
    return false;
  }*/
}
