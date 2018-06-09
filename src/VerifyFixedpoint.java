import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myException.UnificationFailedException;

import dataStructure.AIFdata;
import dataStructure.AST;
import dataStructure.AbstractRule;
import dataStructure.Composed;
import dataStructure.ConcreteRule;
import dataStructure.Condition;
import dataStructure.FactWithType;
import dataStructure.FactWithTypeRuleName;
import dataStructure.RenamingInfo;
import dataStructure.Term;
import dataStructure.Timplies;
import dataStructure.Variable;


public class VerifyFixedpoint {
  
  Mgu mgu = new Mgu();
  FactsSort fs = new FactsSort();
  FixpointsSort fp = new FixpointsSort();
  StateTransition ST = new StateTransition();
  GlobalCounterForSetMember gcsm = new GlobalCounterForSetMember();
  DeepClone dClone = new DeepClone();
  public VerifyFixedpoint(){}
  
  public List<FactWithType> applyAbsRuleWithSatisfiedFacts(AST aifAST, AST fpAST, String conRuleName,HashMap<String,List<String>> UserDefType){
    List<FactWithType> newGenerateFacts = new ArrayList<>();
    ConcreteRule conRule = getConcreteRuleByRuleName(aifAST,conRuleName);
    System.out.println(conRule);
    System.out.println();
    AbstractRule absrule = concreteRuleToAbsRuleConversion(aifAST,conRuleName);
    System.out.println(absrule);
    System.out.println();
    //AbstractRule absruleSubstituted = absRuleSubstitution(absrule); 
    //System.out.println(absruleSubstituted);
    //System.out.println();
    List<FactWithTypeRuleName> facts = fs.getAllFactsFromFixedPoint(fpAST);
    //for(FactWithTypeRuleName f : fixedpoint){
    //  System.out.println(f);
    //}
    List<FactWithTypeRuleName> reducedFacts = fs.getReductedFixedpointWithRuleName(facts);
    //for(FactWithTypeRuleName f : reducedFacts){
    //  System.out.println(f);
    //}
    List<ArrayList<FactWithTypeRuleName>> factsSorted = fs.sortFacts(reducedFacts);
    for(ArrayList<FactWithTypeRuleName> fss : factsSorted){
      for(FactWithTypeRuleName ff : fss){
        System.out.println(ff);
      }
      System.out.println();
    }
    List<FactWithType> extendedTimplies = fp.getExtendedTimplies(fpAST,UserDefType);
    //for(FactWithType exted : extendedTimplies){
    //  System.out.println(exted);
    //}
    //List<ArrayList<FactWithTypeRuleName>> reductedfactsWithRuleName = fs.reduceDuplicateFacts(factsSorted,extendedTimplies,UserDefType);
    List<ArrayList<FactWithType>> reductedfacts = fs.fixedpointsWithoutDuplicateRuleName(factsSorted);
    /*for(ArrayList<FactWithType> gss : reductedfacts){
      for(FactWithType ggg : gss){
        System.out.println(ggg);
      }
      System.out.println();
    }*/
    List<ArrayList<FactWithType>> satisfiedFormFacts = findSatisfyFormFacts(absrule, reductedfacts);
    System.out.println();
    for(ArrayList<FactWithType> sfgs : satisfiedFormFacts){
      for(FactWithType sfg : sfgs){
        System.out.println(sfg);
      }
    }
    List<ArrayList<FactWithType>> satisfiedFacts = getSatisfyFacts(absrule,satisfiedFormFacts,UserDefType);
    System.out.println();
    for(ArrayList<FactWithType> sfgs : satisfiedFacts){
      for(FactWithType sfg : sfgs){
        System.out.println(sfg);
      }
    }
    
    List<ArrayList<HashMap<String,FactWithType>>> keyMapToValList = new ArrayList<>();
    List<ArrayList<HashMap<String,FactWithType>>> concreteKeyMapList = new ArrayList<>();
    for(int i=0;i<conRule.getLF().size();i++){
      ArrayList<HashMap<String,FactWithType>> keyMapToVal = new ArrayList<>();
      ArrayList<HashMap<String,FactWithType>> concreteKeyMapToVal = new ArrayList<>();
      HashMap<String,FactWithType> concreteKeyMap = getKeyMap(conRule.getLF().get(i), new FactWithType(absrule.getVarsTypes(),absrule.getLF().get(i)));
      concreteKeyMapToVal.add(concreteKeyMap);
      for(FactWithType fact : satisfiedFacts.get(i)){
        HashMap<String,FactWithType> keyMap = getKeyMap(conRule.getLF().get(i), new FactWithType(fact.getvType(),fact.getTerm()));
        keyMapToVal.add(keyMap);
      }
      keyMapToValList.add(keyMapToVal);
      concreteKeyMapList.add(concreteKeyMapToVal);
    }
    
    Set<HashMap<String,FactWithType>> newDerivedKeyMap = new HashSet<>();  
    int keyMapToValListSize = keyMapToValList.size();
    for(int i=0;i<keyMapToValListSize;i++){
      int keyMapSize = keyMapToValList.get(i).size();
      for(int j=0;j<keyMapSize;j++){
        HashMap<String,ArrayList<FactWithType>> derivedKeyMap = getDerivedKeyMap(keyMapToValList.get(i).get(j), extendedTimplies,UserDefType);
        System.out.println();
        System.out.println("derivedKeyMap");
        for(Map.Entry<String,ArrayList<FactWithType>> ddd : derivedKeyMap.entrySet()){
          System.out.println(ddd);
        }
               
        Set<HashMap<String,FactWithType>> newDerivedKeyMapsub = getAllCombinationMaps(derivedKeyMap);
        newDerivedKeyMap.addAll(newDerivedKeyMapsub);
     /*   for(HashMap<String,FactWithType> kk : gggg){
          System.out.println(kk);
        }*/
      }
      keyMapToValList.get(i).addAll(newDerivedKeyMap);
    }     
    
    //keyMapToValList.add(new ArrayList<HashMap<String,FactWithType>>(newDerivedKeyMap));
    System.out.println();
    System.out.println("keyMapToValList");
    for(ArrayList<HashMap<String,FactWithType>> gggg : keyMapToValList){
      for(HashMap<String,FactWithType> ggg : gggg){
        System.out.println(ggg);
      }
    }
    List<ArrayList<HashMap<String,FactWithType>>>  satisfiedKeys = removeUnsatisfyKeys(keyMapToValList,concreteKeyMapList,UserDefType); 
    System.out.println(satisfiedKeys);
    System.out.println();
    List<ArrayList<HashMap<String,FactWithType>>> reductedSatisfiedKeys = reduceDuplicateMaps(satisfiedKeys,extendedTimplies,UserDefType);
    System.out.println(reductedSatisfiedKeys);
    System.out.println();
    
    List<ArrayList<HashMap<String,FactWithType>>> keyMapCombination = new ArrayList<>(getCombinationKeyMap(reductedSatisfiedKeys));
    System.out.println(keyMapCombination);
    
    HashMap<String, HashSet<Term>> satisfiedKeysMap = new HashMap<>();  
    // only for display
    HashMap<String, ArrayList<FactWithType>> allPossibleKeys = new HashMap<>();
    for(ArrayList<FactWithType> factss : satisfiedFormFacts){
      for(FactWithType fact : factss){
        for(Term lf : conRule.getLF()){
          HashMap<String,FactWithType> keymap = getKeyMap(lf, fact);
          for(Map.Entry<String,FactWithType> entity : keymap.entrySet()){
            if(!allPossibleKeys.containsKey(entity.getKey())){
              ArrayList<FactWithType> fs = new ArrayList<>();
              fs.add(entity.getValue());
              allPossibleKeys.put(entity.getKey(), fs);
            }else{
              allPossibleKeys.get(entity.getKey()).add(entity.getValue());
            }
          }
        }
      }
    }
    
    for(ArrayList<HashMap<String,FactWithType>> keys : keyMapCombination){
      for(HashMap<String,FactWithType> keymap : keys){
        for(Map.Entry<String,FactWithType> entity : keymap.entrySet()){
          if(!satisfiedKeysMap.containsKey(entity.getKey())){
            HashSet<Term> terms = new HashSet<>();
            terms.add(mgu.termSubs(entity.getValue().getTerm(), entity.getValue().getvType()));
            satisfiedKeysMap.put(entity.getKey(), terms);
          }else{
            satisfiedKeysMap.get(entity.getKey()).add(mgu.termSubs(entity.getValue().getTerm(), entity.getValue().getvType()));
          }
        }
      }
    }
    
    //AbstractRule absruleSubstituted = absRuleSubstitution(absrule);
    System.out.println(absrule);
    System.out.println();
    System.out.println("All possible keys:");
    for(Map.Entry<String,ArrayList<FactWithType>> entity : allPossibleKeys.entrySet()){
      System.out.print(entity.getKey() + " --> {");
      for(int i=0;i<entity.getValue().size();i++){
        if(i < entity.getValue().size()-1){
          System.out.print(mgu.termSubs(entity.getValue().get(i).getTerm(), entity.getValue().get(i).getvType()).toString() + ",");
        }else{
          System.out.println(mgu.termSubs(entity.getValue().get(i).getTerm(), entity.getValue().get(i).getvType()).toString() + "}");
        }
      }
    }
    System.out.println();
    System.out.println("Satisfied keys:");
    for(Map.Entry<String,HashSet<Term>> entity : satisfiedKeysMap.entrySet()){
      System.out.print(entity.getKey() + " --> {");
      int valueSize = entity.getValue().size();
      int i = 0;
      for(Term t : entity.getValue()){
        i++;
        if(i < valueSize){
          System.out.print(t.toString() + ",");
        }else{
          System.out.println(t.toString() + "}");
        }
      }
    }
    System.out.println();
    
    for(ArrayList<HashMap<String,FactWithType>> keyMaps : keyMapCombination){     
      for(HashMap<String,FactWithType> keymap : keyMaps){
        for(int i=0;i<conRule.getNewFreshVars().getFreshs().size();i++){
          HashMap<String,String> freshVarType = new HashMap<String,String>(); 
          for(Map.Entry<String,FactWithType> entity : keymap.entrySet()){
            freshVarType.putAll(entity.getValue().getvType());
            break;
          }
          keymap.put(conRule.getNewFreshVars().getFreshs().get(i).getVarName(), new FactWithType(freshVarType,absrule.getFreshVars().get(i)));
        }
      }
    }
    
    //List<AbstractRule> abstractRules = new ArrayList<>();
    if(keyMapCombination.isEmpty() && absrule.getLF().isEmpty()){
      applySatisifyKeysToAbsRulesNew(conRule,absrule,new ArrayList<HashMap<String,FactWithType>>(),UserDefType);
    }
    for(ArrayList<HashMap<String,FactWithType>> keyMaps : keyMapCombination){ 
      applySatisifyKeysToAbsRulesNew(conRule,absrule,keyMaps,UserDefType);
      //applySatisifyKeysToAbsRules(conRule,absrule,keyMaps,UserDefType);
      //abstractRules.addAll(getAllSatisfiedAbsRules(conRule,absrule,keyMaps,UserDefType));
    }   
    
 /*   if(!keyMapCombination.isEmpty()){
      for(AbstractRule absR : abstractRules){
        HashMap<String,String> varsType = absR.getVarsTypes();
        for(Term rf : absR.getRF()){
          newGenerateFacts.add(new FactWithType(varsType,rf));
        }
      }
    }else{
      HashMap<String,String> varsType = absruleSubstituted.getVarsTypes();
      for(Term rf : absruleSubstituted.getRF()){
        if(!rf.getFactName().equals("attack")){
          newGenerateFacts.add(new FactWithType(varsType,rf)); 
        }
      }
    }*/
    System.out.println();
    for(FactWithType newG : newGenerateFacts){
      System.out.println(newG.toString());
    }
    System.out.println();
    for(FactWithType newG : newGenerateFacts){
      System.out.println(mgu.termSubs(newG.getTerm(), newG.getvType()).toString());
    }
    
    return newGenerateFacts;
  }
  
  public ConcreteRule getConcreteRuleByRuleName(AST aifAST,String conRuleName){
    ConcreteRule conRule = null;
    for(ConcreteRule cr: ((AIFdata)aifAST).getRules()){
      if(cr.getRulesName().equals(conRuleName)){
        conRule = cr;
        break;
      }
    }
    if(conRule == null){
      System.err.println("Rule name not exsit.\n");
      System.exit(-1);
    }
    return conRule;
  }
  
  
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
    HashMap<String,Term> freshs = new HashMap<>();
    List<Term> RF = new ArrayList<>();
    List<Condition> splus = new ArrayList<>(conRule.getSplus());
    List<Condition> conditions_left = new ArrayList<>(conRule.getSplus());
    conditions_left.addAll(conRule.getSnega());
    
    Substitution subs = new Substitution();
    for(Term lf : conRule.getLF()){
      Term termSubs = mgu.termSubstituted(lf, subs);
      Term absLF = cFactToAbsFact(termSubs,conRule.getVarsTypes(),conditions_left,setPosition);
      LF.add(absLF);  
      mgu.unifyTwoFacts(lf,absLF,subs);
    }
    absRule.setLF(LF);
    for(Term freshVar : conRule.getNewFreshVars().getFreshs()){
      Composed freshVal = new Composed("val");
      Composed zero = new Composed("0");
      for(int j=0;j<setPosition.size();j++){ // val initialize to val(0,0,0)
        freshVal.setArguments(zero);
      }
      freshs.put(freshVar.getVarName(), freshVal);
    }
    absRule.setFreshVars(freshs);
    Substitution subsCopy = (Substitution)dClone.deepClone(subs);
    subs.getSubstitution().putAll(freshs);
    List<Condition> conditions_rs = new ArrayList<>(conRule.getRS()); 
    
    /*if the positive condition in the left hand side but not in right hand side then set it to 0*/
    Composed zero = new Composed("0");
    for(Condition sp : splus){
      if(!conditions_rs.contains(sp)){
        String setName = sp.getTerm().getFactName();
        subsCopy.getSubstitution().get(sp.getVar().getVarName()).getArguments().set(setPosition.get(setName), zero);
      }
    }
    /*is the condition in RS then set it to "1"*/
    for(Condition rs : conditions_rs){
      if(subsCopy.getSubstitution().containsKey(rs.getVar().getVarName())){
        String setName = rs.getTerm().getFactName();
        subsCopy.getSubstitution().get(rs.getVar().getVarName()).getArguments().set(setPosition.get(setName), rs.getTerm());
      }
    }
    
    for(Term rf : conRule.getRF()){      
      Term absRF = mgu.termSubstituted(rf, subsCopy);
      RF.add(absRF);
    }
    absRule.setRF(RF);
    HashMap<String,Timplies> timplies = new HashMap<>();
    for(Map.Entry<String, Term> sub : subsCopy.getSubstitution().entrySet()){
      //if(!conRule.getNewFreshVars().getFreshs().contains(new Variable(sub.getKey()))){
      Composed t = new Composed("timplies");
      t.setArguments(sub.getValue());
      t.setArguments(subsCopy.getSubstitution().get(sub.getKey()));

      Timplies timplie = new Timplies(t); 
      timplies.put(sub.getKey(), timplie);
     // }
    }
    absRule.setTimplies(timplies); 
    return absRule;
  }
  
  public HashMap<String, Integer> getSetPosition(AST aifAST){
    HashMap<String, Integer> setPosition = new HashMap<String, Integer>();
    List<Term> sets = new ArrayList<Term>(((AIFdata)aifAST).getSets());
    for(int i=0;i<sets.size();i++){
      setPosition.put(sets.get(i).getFactName(), i);
    }
    return setPosition;
  }
  
  
  public Term cFactToAbsFact(Term term,HashMap<String, String> varsTypes, List<Condition> conditions,HashMap<String, Integer> setPosition){
    if(term instanceof Variable){
      if(varsTypes.get(term.getVarName()).equals("value")){
        Composed val = new Composed("val");
        Composed zero = new Composed("0");
        for(int j=0;j<setPosition.size();j++){ 
          gcsm.increaseCounter();
          String PKDB = "PKDB_" + gcsm.getCounter();
          Variable _ = new Variable(PKDB);
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
      }else{
        return term;
      }
    }else{
      if(term.getArguments().isEmpty()){ // constant
        return term;
      }else{
        Composed absTerm = new Composed(term.getFactName());
        for(Term subTerm : term.getArguments()){
          absTerm.setArguments(cFactToAbsFact(subTerm,varsTypes,conditions,setPosition));
        }
        return absTerm;
      }
    }
  }
  
  
  public AbstractRule absRuleSubstitution(AbstractRule absRule){
    AbstractRule absRuleSubstituted = new AbstractRule();
    absRuleSubstituted.setRulesName(absRule.getRulesName());
    absRuleSubstituted.setVarsTypes(absRule.getVarsTypes());
    absRuleSubstituted.setFreshVars(absRule.getFreshVars());
    List<Term> LF = new ArrayList<>();
    for(Term lf : absRule.getLF()){
      LF.add(mgu.termSubs(lf,absRule.getVarsTypes()));
    }
    absRuleSubstituted.setLF(LF);
    
    List<Term> RF = new ArrayList<>();
    for(Term rf : absRule.getRF()){
      RF.add(mgu.termSubs(rf,absRule.getVarsTypes()));
    }
    absRuleSubstituted.setRF(RF);
    
    HashMap<String,Timplies> timplies = new HashMap<>();
    //for(Term timplie : absRule.getTimplies()){
    //  timplies.add(termSubs(timplie,absRule.getVarsTypes()));
    //}
    for(Map.Entry<String, Timplies> timplie : absRule.getTimplies().entrySet()){
      if(timplie.getValue().isTimplies()){
        timplies.put(timplie.getKey().toString(), new Timplies(mgu.termSubs(timplie.getValue().getTimplies(),absRule.getVarsTypes())));
      }else{
        timplies.put(timplie.getKey().toString(), new Timplies(false,mgu.termSubs(timplie.getValue().getTimplies(),absRule.getVarsTypes())));
      }
    }
    absRuleSubstituted.setTimplies(timplies);
    return absRuleSubstituted;
  }
  
  public List<ArrayList<FactWithType>> findSatisfyFormFacts(AbstractRule absRule, List<ArrayList<FactWithType>> factsSorted){
    List<ArrayList<FactWithType>> satisfiedFacts = new ArrayList<>();
    for(Term lf : absRule.getLF()){
      ArrayList<FactWithType> factHasSameForm = new ArrayList<>();
      for(ArrayList<FactWithType> factList : factsSorted){
        if(!factList.isEmpty() && fs.isTwoFactsHaveSameForm(lf,factList.get(0).getTerm())){
          factHasSameForm.addAll(factList);
          break;
        }
      }
      satisfiedFacts.add(factHasSameForm);
    }   
    return satisfiedFacts;
  }
  
  public List<ArrayList<FactWithType>> getSatisfyFacts(AbstractRule absRule, List<ArrayList<FactWithType>> facts,HashMap<String,List<String>> UserDefType){
    List<ArrayList<FactWithType>> satisfiedFacts = new ArrayList<>();
    for(int i=0;i<facts.size();i++){
      ArrayList<FactWithType> subList = new ArrayList<>();
      for(FactWithType fact : facts.get(i)){
        FactWithType lf = new FactWithType(absRule.getVarsTypes() ,absRule.getLF().get(i));
        Substitution subs = new Substitution();
        RenamingInfo renameInfo = new RenamingInfo();
        if(mgu.unifyTwoFacts(lf,fact,subs,renameInfo,UserDefType)){
          FactWithType renamedFact = new FactWithType(renameInfo.getvType(),mgu.termSubs(fact.getTerm(), renameInfo.getSubs()));
          FactWithType substitutedFact = new FactWithType(renamedFact.getvType(), mgu.termSubstituted(renamedFact.getTerm(), subs));
          ArrayList<String> fv = mgu.vars(substitutedFact.getTerm());
          HashMap<String,String> vTypesCopy = new HashMap<>(substitutedFact.getvType());
          // remove keys (variables) which are not present in term.  
          for(Map.Entry<String, String> entity : vTypesCopy.entrySet()){
            if(!fv.contains(entity.getKey())){
              substitutedFact.getvType().remove(entity.getKey());
            }
          }
          subList.add(substitutedFact);
        }
      }
      satisfiedFacts.add(subList);
    }
    return satisfiedFacts;
  }
  
  /**
   * Returns a map, mapping variable to val(...) 
   * @param  concreteTerm    e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @param  abstractTerm    e.g. A:Honest.S:Server.iknows(sign(inv(val(ring(A),db_valid(S,A))),pair(A,val(ring(A),0,0))))    
   * @return e.g. {PK=A:Honest.S:Server.val(ring(A),db_valid(S,A)), NPK=A:Honest.S:Server.val(ring(A),0,0)} 
   */
  public HashMap<String,FactWithType> getKeyMap(Term concreteTerm, FactWithType abstractTerm){
    HashMap<String,FactWithType> keyMap = new HashMap<>();
    if((concreteTerm instanceof Variable) && (abstractTerm.getTerm() instanceof Composed)){
      if(abstractTerm.getTerm().getFactName().equals("val")){
        keyMap.put(((Variable)concreteTerm).getVarName(), abstractTerm);
      }
    }else if((concreteTerm instanceof Composed) && (abstractTerm.getTerm() instanceof Composed)){
      if(concreteTerm.getFactName().equals(abstractTerm.getTerm().getFactName())){
        if(!concreteTerm.getArguments().isEmpty()){
          for(int i=0;i<concreteTerm.getArguments().size();i++){
            keyMap.putAll(getKeyMap(concreteTerm.getArguments().get(i), new FactWithType(abstractTerm.getvType(),abstractTerm.getTerm().getArguments().get(i))));
          }
        }
      }
    }
    return keyMap;
  }
  
  public HashMap<String,ArrayList<FactWithType>> getDerivedKeyMap(HashMap<String,FactWithType> keyMap,List<FactWithType> timplies,HashMap<String,List<String>> UserDefType){
    HashMap<String,ArrayList<FactWithType>> impliedkeyMap = new HashMap<>();  
    for(Map.Entry<String,FactWithType> entity : keyMap.entrySet()){
      ArrayList<FactWithType> impliedKey = new ArrayList<>();
      for(FactWithType timplie : timplies){
        Term timplieVal1 = timplie.getTerm().getArguments().get(0);
        FactWithType timplieVal1Type = new FactWithType(timplie.getvType(),timplieVal1);
        Term timplieVal2 = timplie.getTerm().getArguments().get(1);
        //FactWithType timplieVal2Type = new FactWithType(timplie.getvType(),timplieVal2);
        FactWithType lfVal = new FactWithType(entity.getValue().getvType(),entity.getValue().getTerm());
        Substitution subs = new Substitution();
        RenamingInfo renameInfo = new RenamingInfo();
        if(mgu.isT1GreaterOrEqualT2(timplieVal1Type,lfVal,UserDefType,renameInfo)){
          Term renamedTimpliesVal2 = mgu.termSubs(timplieVal2, renameInfo.getSubs());
          FactWithType substitutedTimpliesVal2 = new FactWithType(renameInfo.getvType(), renamedTimpliesVal2);
          
          //Term tVal_2 = mgu.termSubs(timplie.getTerm().getArguments().get(1),renameInfo.getSubs());
          //FactWithType tVal2 = new FactWithType(renameInfo.getvType(),tVal_2);
          
          
          ArrayList<String> fv = mgu.vars(substitutedTimpliesVal2.getTerm());
          //HashMap<String,String> vTypesCopy = new HashMap<>(substitutedTimpliesVal2.getvType());
          HashMap<String,String> vTypesCopy = (HashMap<String,String>)dClone.deepClone(substitutedTimpliesVal2.getvType());
          // remove keys (variables) which are not present in term.  
          for(Map.Entry<String, String> en : vTypesCopy.entrySet()){
            if(!fv.contains(en.getKey())){
              substitutedTimpliesVal2.getvType().remove(en.getKey());
            }
          }
          impliedKey.add(substitutedTimpliesVal2);
        }
      }
      impliedkeyMap.put(entity.getKey(), impliedKey);
    }
    return impliedkeyMap;
  }
  
  public List<ArrayList<HashMap<String,FactWithType>>> removeUnsatisfyKeys(List<ArrayList<HashMap<String,FactWithType>>> keyList,List<ArrayList<HashMap<String,FactWithType>>> keys,HashMap<String,List<String>> UserDefType){
    List<ArrayList<HashMap<String,FactWithType>>> satisfiedKeys = (List<ArrayList<HashMap<String,FactWithType>>>)dClone.deepClone(keyList);
    //List<ArrayList<HashMap<String,FactWithType>>> satisfiedKeys = new ArrayList<>(keyList);
    for(int i=0;i<keyList.size();i++){
      for(int j=0;j<keyList.get(i).size();j++){
        for(Map.Entry<String,FactWithType> entity : keys.get(i).get(0).entrySet()){// need more test here
          if(keyList.get(i).get(j).containsKey(entity.getKey())){
            FactWithType val1 = keyList.get(i).get(j).get(entity.getKey());
            FactWithType val2 = entity.getValue();
            if(!mgu.isT1GreaterOrEqualT2(val2, val1, UserDefType, new RenamingInfo())){  
              satisfiedKeys.get(i).remove(keyList.get(i).get(j));
            }
          }
        }
      }
    }   
    return satisfiedKeys;
  }
  
  /**
   * Returns all possible combination of contrate type.   // need update
   * @param  typeInfo e.g. {A=[a, i], S=[s]}
   * @return e.g. [{A=i, S=s}, {A=a, S=s}]
   */ //List<ArrayList<HashMap<Variable,Term>>>
  //public Set<HashMap<String,FactWithType>> getCombinationMap(HashMap<String, List<FactWithType>> typeInfo){
  public Set<ArrayList<HashMap<String,FactWithType>>> getCombinationKeyMap(List<ArrayList<HashMap<String,FactWithType>>> lists){
    Set<ArrayList<HashMap<String,FactWithType>>> combinations = new HashSet<ArrayList<HashMap<String,FactWithType>>>();
    Set<ArrayList<HashMap<String,FactWithType>>> newCombinations;
    if(lists.isEmpty()){
      return combinations;
    }
    int index = 0;
    // extract each of the integers in the first list
    // and add each to ints as a new list
    for(HashMap<String,FactWithType> i: lists.get(0)) {
      ArrayList<HashMap<String,FactWithType>> newList = new ArrayList<>();
      newList.add(i);
      combinations.add(newList);
    }
    index++;
    while(index < lists.size()) {
      List<HashMap<String,FactWithType>> nextList = lists.get(index);
      newCombinations = new HashSet<ArrayList<HashMap<String,FactWithType>>>();
      for(ArrayList<HashMap<String,FactWithType>> first: combinations) {
        for(HashMap<String,FactWithType> second: nextList) {
          ArrayList<HashMap<String,FactWithType>> newList = new ArrayList<>();
          newList.addAll(first);
          newList.add(second);
          newCombinations.add(newList);
        }
      }
      combinations = newCombinations;
      index++;
    }
    
    return combinations;
  }
  
   
  public HashMap<String,Term> getFactSubstitutiondMap(Term f1, Term f2){
    HashMap<String,Term> subsMap = new HashMap<>();
    if(f1.equals(f2)){
      return subsMap;
    }else if(f1 instanceof Variable && f2 instanceof Variable){
      if(!f1.equals(f2)){
        subsMap.put(f1.getVarName(), f2);
      }
    }else if(f1 instanceof Composed && f2 instanceof Composed){
      if(f1.getArguments().size() == f2.getArguments().size()){
        for(int i=0;i<f1.getArguments().size();i++){
          subsMap.putAll(getFactSubstitutiondMap(f1.getArguments().get(i), f2.getArguments().get(i)));
        }
      }
    }
    return subsMap;
  }
  

  public List<FactWithType> applySatisifyKeysToAbsRulesNew(ConcreteRule concreteRule, AbstractRule absRule, ArrayList<HashMap<String,FactWithType>> satisfiedKeys,HashMap<String,List<String>> UserDefType){
    List<FactWithType> newGenerateFacts = new ArrayList<>();
    //AbstractRule absRuleCopy = (AbstractRule)dClone.deepClone(absRule);
    List<FactWithType> lfFacts = new ArrayList<>();
    for(int i=0; i<concreteRule.getLF().size();i++){
      Term subTerm = mgu.termSubsWithType(concreteRule.getLF().get(i), satisfiedKeys.get(i));
      HashMap<String,String> vType = new HashMap<>(); 
      ArrayList<String> fv = mgu.vars(subTerm);
      for(Map.Entry<String,FactWithType> entity : satisfiedKeys.get(i).entrySet()){
        for(Map.Entry<String, String> varmap : entity.getValue().getvType().entrySet()){
          if(fv.contains(varmap.getKey()) && !vType.containsKey(varmap.getKey())){
            vType.put(varmap.getKey(), varmap.getValue());
          }
        } 
      }
      for(Map.Entry<String, String> varmap : absRule.getVarsTypes().entrySet()){
        if(fv.contains(varmap.getKey()) && !vType.containsKey(varmap.getKey())){
          vType.put(varmap.getKey(), varmap.getValue());
        }
      }
      FactWithType subTermWithType = new FactWithType(vType,subTerm);
      lfFacts.add(subTermWithType);
     
      System.out.println();
    }
    
    HashMap<String,String> varTpers = new HashMap<>();
    Substitution subs = new Substitution();
    for(int i=0;i<lfFacts.size();i++){
      Term abslfFactSubs = mgu.termSubstituted(absRule.getLF().get(i), subs);
      // need test here
      HashMap<String,String> vType = new HashMap<>(); 
      ArrayList<String> fv = mgu.vars(abslfFactSubs);    
      for(Map.Entry<String,FactWithType> entity : satisfiedKeys.get(i).entrySet()){
        for(Map.Entry<String, String> varmap : entity.getValue().getvType().entrySet()){
          if(fv.contains(varmap.getKey()) && !vType.containsKey(varmap.getKey())){
            vType.put(varmap.getKey(), varmap.getValue());
          }
        } 
      }
      for(Map.Entry<String, String> varmap : absRule.getVarsTypes().entrySet()){
        if(fv.contains(varmap.getKey()) && !vType.containsKey(varmap.getKey())){
          vType.put(varmap.getKey(), varmap.getValue());
        }
      } 
      
      FactWithType abslfFact = new FactWithType(vType,abslfFactSubs);
      
      //FactWithType abslfFact = new FactWithType(absRule.getVarsTypes(),absRule.getLF().get(i));
      RenamingInfo renameInfo = new RenamingInfo();
      if(mgu.unifyTwoFactsPKDB(abslfFact,lfFacts.get(i), subs, renameInfo, UserDefType)){
        lfFacts.get(i).setvType(renameInfo.getvType());
        lfFacts.get(i).setTerm(mgu.termSubs(lfFacts.get(i).getTerm(),renameInfo.getSubs()));
      }
      varTpers.putAll(lfFacts.get(i).getvType());  // variables type using for RF
    }
    
    varTpers.putAll(absRule.getVarsTypes()); // may not need.
    for(int i=0;i<absRule.getRF().size();i++){
      Term rfFact = mgu.termSubstituted(absRule.getRF().get(i), subs);
      FactWithType rfFactWithTypes = new FactWithType(varTpers,rfFact); 
      newGenerateFacts.add(rfFactWithTypes);
    }
    for(FactWithType hh : newGenerateFacts){
      System.out.println(mgu.termSubs(hh.getTerm(), hh.getvType()).toString());
    }
    
    return newGenerateFacts;
  }
  
  /**
   * Returns all possible combination of contrate type. 
   * @param  typeInfo e.g. {A=[a, i], S=[s]}
   * @return e.g. [{A=i, S=s}, {A=a, S=s}]
   */
  public Set<HashMap<String,FactWithType>> getAllCombinationMaps(HashMap<String, ArrayList<FactWithType>> keyMaptoList){
    Set<HashMap<String,FactWithType>> combinations = new HashSet<>();
    if(!keyMaptoList.isEmpty()){
      Set<HashMap<String,FactWithType>> newCombinations;
      Set<String> keySet = keyMaptoList.keySet();
      List<String> keyList = new ArrayList<>(keySet);
      int index = 0; 
      if(!keyList.isEmpty() && keyMaptoList.containsKey(keyList.get(0))){
        for(FactWithType i: keyMaptoList.get(keyList.get(0))) {
          HashMap<String,FactWithType> newMap = new HashMap<>();
          newMap.put(keyList.get(0), i);
          combinations.add(newMap);
        }
      }
      index++;
      while(index < keyList.size()) {
        List<FactWithType> nextList = keyMaptoList.get(keyList.get(index));
        newCombinations = new HashSet<>();
        for(HashMap<String,FactWithType> first: combinations) {
          for(FactWithType second: nextList) {
            HashMap<String,FactWithType> newList = new HashMap<>();
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
  
  public boolean doesMap1impliesMap2(HashMap<String,FactWithType> map1, HashMap<String,FactWithType>map2,List<FactWithType> extendedTimplies,HashMap<String,List<String>> UserDefType){
    if(map1.size() != map2.size()){return false;} // if two map have different size return false
    HashSet<String> map1keys = new HashSet<>(map1.keySet());
    HashSet<String> map2keys = new HashSet<>(map2.keySet());
    if(!map1keys.equals(map2keys)){return false;} // if two map have different keys then return false
    // if every entity in map2 can be implies by corresponding entity in map1, then return true
    for(Map.Entry<String,FactWithType> entity : map1.entrySet()){
      if(!fs.canT1impliesT2(entity.getValue(), map2.get(entity.getKey()), extendedTimplies, UserDefType)){
        return false;
      }
    }  
    return true;
  }
  
  public List<ArrayList<HashMap<String,FactWithType>>> reduceDuplicateMaps(List<ArrayList<HashMap<String,FactWithType>>> mapsDuplicate, List<FactWithType> extendedTimplies, HashMap<String,List<String>> UserDefType){
    List<ArrayList<HashMap<String,FactWithType>>> reducedmaps = new ArrayList<>();

    for(ArrayList<HashMap<String,FactWithType>> mapsList : mapsDuplicate){
      ArrayList<HashMap<String,FactWithType>> maps = new ArrayList<>(mapsList);
      if(maps.size() == 1){
        reducedmaps.add(maps);
      }else{        
        HashMap<String, String> vType = new HashMap<>();
        Term flag = new Variable("EndFlag");
        FactWithType endFlag = new FactWithType(vType,flag);
        HashMap<String,FactWithType> endFlagMap = new HashMap<>();
        endFlagMap.put("flag", endFlag);
        maps.add(endFlagMap);
        ArrayList<HashMap<String,FactWithType>> mapsCopy = new ArrayList<HashMap<String,FactWithType>>(maps);
        while(true){
          if(maps.get(0).equals(endFlagMap)) break;
          HashMap<String,FactWithType> firstMap = maps.get(0);  
          for(HashMap<String,FactWithType> m : maps){
            if(!m.equals(endFlagMap)){
              if(doesMap1impliesMap2(firstMap, m,extendedTimplies,UserDefType)){
                mapsCopy.remove(m);
              }
            }
          }
          mapsCopy.add(firstMap);
          maps.clear();
          maps.addAll(mapsCopy);    
        }   
        maps.remove(endFlagMap);
        reducedmaps.add(maps);
      }
    } 
    return reducedmaps; 
  }
}
