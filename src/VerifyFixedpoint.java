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
import dataStructure.ConcreteFact;
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
  TimpliesSort fp = new TimpliesSort();
  StateTransition ST = new StateTransition();
  GlobalCounterForSetMember gcsm = new GlobalCounterForSetMember();
  DeepClone dClone = new DeepClone();
  public VerifyFixedpoint(){}
  
  public void verifyFixedpoint(AST aifAST, AST fpAST,HashMap<String,List<String>> UserDefType){
    List<FactWithTypeRuleName> facts = fs.getAllFactsFromFixedPoint(fpAST);
    List<FactWithTypeRuleName> reducedFacts = fs.getReductedFixedpointWithRuleName(facts);
    List<ArrayList<FactWithTypeRuleName>> factsSorted = fs.sortFacts(reducedFacts);
    List<FactWithType> extendedTimplies = fp.getExtendedTimplies(fpAST,UserDefType);
    List<FactWithType> timplies = fp.getTimplies(fpAST);
    List<List<FactWithType>> classifiedExtendedTimplies = fp.classifyTimpliesInTypes(extendedTimplies);
    List<ArrayList<FactWithType>> reductedfacts = fs.fixedpointsWithoutDuplicateRuleName(factsSorted);
    List<FactWithType> reductedfactsList = new ArrayList<>();
    List<ArrayList<FactWithTypeRuleName>> futherReductedfacts = fs.reduceDuplicateFacts(factsSorted,extendedTimplies,UserDefType);
    
    HashSet<String> ruleNames = fs.getRuleNamesHaveValueType(aifAST);
    //System.out.println(ruleNames);
    System.out.println();
    System.out.println("Keys Life-cycle:");
    fp.printKeyLifeCycle(classifiedExtendedTimplies,timplies);
    System.out.println();
    System.out.println();
    System.out.println("Reducted facts from Fixedpoint:");
    fs.printReductedFacts(futherReductedfacts,ruleNames);
    System.out.println();
    for(ArrayList<FactWithType> f : reductedfacts){
      reductedfactsList.addAll(f);
    }
    
    for(String ruleName : ruleNames){
      ConcreteRule conRule = getConcreteRuleByRuleName(aifAST,ruleName);
      AbstractRule absrule = concreteRuleToAbsRuleConversion(aifAST,ruleName);
      applyRule(conRule,absrule,reductedfactsList,extendedTimplies,UserDefType);
    }
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
    //absRule.setVarsTypes(conRule.getVarsTypes());
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
        subs.getSubstitution().get(sp.getVar().getVarName()).getArguments().set(setPosition.get(setName), zero);
      }
    }
    /*is the condition in RS then set it to "1"*/
    for(Condition rs : conditions_rs){
      if(subs.getSubstitution().containsKey(rs.getVar().getVarName())){
        String setName = rs.getTerm().getFactName();
        subs.getSubstitution().get(rs.getVar().getVarName()).getArguments().set(setPosition.get(setName), rs.getTerm());
      }
    }
    
    for(Term rf : conRule.getRF()){      
      Term absRF = mgu.termSubstituted(rf, subs);
      RF.add(absRF);
    }
    absRule.setRF(RF);
    HashMap<String,Timplies> timplies = new HashMap<>();
    for(Map.Entry<String, Term> sub : subsCopy.getSubstitution().entrySet()){
      Composed t = new Composed("timplies");
      t.setArguments(sub.getValue());
      t.setArguments(subs.getSubstitution().get(sub.getKey()));

      Timplies timplie = new Timplies(t); 
      timplies.put(sub.getKey(), timplie);
    }
    absRule.setTimplies(timplies);
    HashSet<String> fv = new HashSet<>();
    for(Term lf : absRule.getLF()){
      fv.addAll(mgu.vars(lf));
    }
    for(Term rf : absRule.getRF()){
      fv.addAll(mgu.vars(rf));
    }
    HashMap<String,String> absVarsType = new HashMap<>();
    for(Map.Entry<String, String> entity : conRule.getVarsTypes().entrySet()){
      if(fv.contains(entity.getKey())){
        absVarsType.put(entity.getKey(), entity.getValue());
      }
    }
    absRule.setVarsTypes(absVarsType);
     
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

  
  public List<FactWithType> applyRule(ConcreteRule conRule,AbstractRule absRule, List<FactWithType> facts,List<FactWithType> extendedTimplies,HashMap<String,List<String>> UserDefType){
    List<Substitution> stisfySubtitutions = new ArrayList<>(); 
    List<HashMap<String,FactWithType>> stisfiedKeys = new ArrayList<>();  
    HashMap<String,String> varsType = new HashMap<>(absRule.getVarsTypes());
    int counter = 0;
    for(Term lf : absRule.getLF()){
      counter++;
      FactWithType lfWithType = new FactWithType(varsType ,lf);
      if(stisfySubtitutions.isEmpty()){
        for(FactWithType fact : facts){
          Substitution subs = new Substitution();
          RenamingInfo renameInfo = new RenamingInfo();
          if(mgu.unifyTwoFactsPKDB(lfWithType,fact,subs,renameInfo,UserDefType)){
            varsType.putAll(renameInfo.getvType());
            stisfySubtitutions.add(subs);
            /* only for display satisfied keys for the user*/
            Term lfsubstituted = mgu.termSubstituted(lf, subs);
            FactWithType lfsubstitutedWithType = new FactWithType(varsType,lfsubstituted);
            stisfiedKeys.add(getKeyMap(conRule.getLF().get(counter-1), lfsubstitutedWithType));
          }
        }
      }else{
        List<Substitution> stisfySubtitutionsCopy = (List<Substitution>)dClone.deepClone(stisfySubtitutions);
        for(Substitution sub : stisfySubtitutionsCopy){
          Substitution subsCopy = (Substitution)dClone.deepClone(sub);
          FactWithType lfWithTypeSubs = new FactWithType(varsType ,mgu.termSubstituted(lf, sub));
          for(FactWithType fact : facts){
            RenamingInfo renameInfo = new RenamingInfo();
            if(mgu.unifyTwoFactsPKDB(lfWithTypeSubs,fact,subsCopy,renameInfo,UserDefType)){
              varsType.putAll(renameInfo.getvType());
              stisfySubtitutions.add(subsCopy);
              /* only for display satisfied keys for the user*/
              Term lfsubstituted = mgu.termSubstituted(lf, subsCopy);
              FactWithType lfsubstitutedWithType = new FactWithType(varsType,lfsubstituted);
              stisfiedKeys.add(getKeyMap(conRule.getLF().get(counter-1), lfsubstitutedWithType));
            }
          }
          stisfySubtitutions.remove(sub);
        }
        // need test here
        int mapSize=0;
        for(HashMap<String,FactWithType> keymaps : stisfiedKeys){
          if(keymaps.size()>mapSize){
            mapSize = keymaps.size();
          }
        }
        List<HashMap<String,FactWithType>> stisfiedKeysCopy = (List<HashMap<String,FactWithType>>)dClone.deepClone(stisfiedKeys);
        for(HashMap<String,FactWithType> keymap : stisfiedKeysCopy){
          if(keymap.size() < mapSize){
            stisfiedKeys.remove(keymap);
          }
        }
      }
    }  
    
    System.out.println(absRule);
    System.out.println();
    System.out.println("Satisified keys:");
    List<HashMap<String,FactWithType>> reducedKeyMap = reduceDuplicateMaps(stisfiedKeys,extendedTimplies,UserDefType);    
    for(HashMap<String,FactWithType> keymap : reducedKeyMap){
      for(Map.Entry<String, FactWithType> entity : keymap.entrySet()){
        System.out.println(entity.getKey() + " -> " + mgu.termSubs(entity.getValue().getTerm(), entity.getValue().getvType())+";");
      }
      System.out.println();
    }
    System.out.println();
    
    List<FactWithType> newGenerateFacts = new ArrayList<>();
    if(absRule.getLF().isEmpty()){
      for(Term rf : absRule.getRF()){
        HashMap<String,String> vTypes = new HashMap<>();
        List<String> fv = mgu.vars(rf);
        for(Map.Entry<String, String> entity : absRule.getVarsTypes().entrySet()){
          if(fv.contains(entity.getKey())){
            vTypes.put(entity.getKey(), entity.getValue());
          }
        }
        FactWithType rfWithType = new FactWithType(vTypes,rf);
        newGenerateFacts.add(rfWithType);
      }
    }
    for(Substitution substitution : stisfySubtitutions){
      for(Term rf : absRule.getRF()){
        Term rfFact = mgu.termSubstituted(rf, substitution);
        HashMap<String,String> vTypes = new HashMap<>();
        List<String> fv = mgu.vars(rfFact);
        for(Map.Entry<String, String> entity : varsType.entrySet()){
          if(fv.contains(entity.getKey())){
            vTypes.put(entity.getKey(), entity.getValue());
          }
        }       
        FactWithType rfFactWithTypes = new FactWithType(vTypes,rfFact); 
        newGenerateFacts.add(rfFactWithTypes);
      }
    }
    
    System.out.println("Facts generate by the rule of " + absRule.getRulesName()+":");
    List<FactWithType>  reducedFacts =  fs.reduceDuplicateFactsNew(newGenerateFacts,extendedTimplies,UserDefType);
    for(FactWithType f : reducedFacts){
      System.out.println(mgu.termSubs(f.getTerm(), f.getvType()).toString()+";");
    }   
    System.out.println();
    System.out.println("Convert to original facts:");
    for(FactWithType absFact : reducedFacts){
      Term subsAbsFact = mgu.termSubs(absFact.getTerm(), absFact.getvType());
      ConcreteFact cFacts = absFactToConcreteFact(subsAbsFact,conRule);
      System.out.println(cFacts);
    }
    System.out.println();
    System.out.println();
    
    return reducedFacts;
  }
  
  /**
   * Returns a map, mapping variable to val(...) 
   * @param  concreteTerm    e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @param  abstractTerm    e.g. A:Honest.S:Server.iknows(sign(inv(val(ring(A),db_valid(S,A))),pair(A,val(ring(A),0,0))))    
   * @return e.g. {PK=A:Honest.S:Server.val(ring(A),db_valid(S,A)), NPK=A:Honest.S:Server.val(ring(A),0,0)} 
   */
  public HashMap<String,FactWithType> getKeyMap(Term cFact, FactWithType abstractFact){
    HashMap<String,FactWithType> keyMap = new HashMap<>();
    if((cFact instanceof Variable) && (abstractFact.getTerm() instanceof Composed)){
      if(abstractFact.getTerm().getFactName().equals("val")){
        keyMap.put(((Variable)cFact).getVarName(), abstractFact);
      }
    }else if((cFact instanceof Composed) && (abstractFact.getTerm() instanceof Composed)){
      if(cFact.getFactName().equals(abstractFact.getTerm().getFactName())){
        if(!cFact.getArguments().isEmpty()){
          for(int i=0;i<cFact.getArguments().size();i++){
            keyMap.putAll(getKeyMap(cFact.getArguments().get(i), new FactWithType(abstractFact.getvType(),abstractFact.getTerm().getArguments().get(i))));
          }
        }
      }
    }
    return keyMap;
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
  
  public List<HashMap<String,FactWithType>> reduceDuplicateMaps(List<HashMap<String,FactWithType>> mapsDuplicate, List<FactWithType> extendedTimplies, HashMap<String,List<String>> UserDefType){
    List<HashMap<String,FactWithType>> reducedmaps = new ArrayList<>(mapsDuplicate);
    List<HashMap<String,FactWithType>> mapsDuplicateCopy = new ArrayList<>(mapsDuplicate);

    if(mapsDuplicate.size() == 1){
      return mapsDuplicate;
    }else{        
      HashMap<String, String> vType = new HashMap<>();
      Term flag = new Variable("EndFlag");
      FactWithType endFlag = new FactWithType(vType,flag);
      HashMap<String,FactWithType> endFlagMap = new HashMap<>();
      endFlagMap.put("flag", endFlag);
      mapsDuplicateCopy.add(endFlagMap);
      while(true){
        if(mapsDuplicateCopy.get(0).equals(endFlagMap)) break;
        HashMap<String,FactWithType> firstMap = mapsDuplicateCopy.get(0);  
        for(HashMap<String,FactWithType> m : reducedmaps){
          if(!m.equals(endFlagMap)){
            if(doesMap1impliesMap2(firstMap, m,extendedTimplies,UserDefType)){
              mapsDuplicateCopy.remove(m);
            }
          }
        }
        mapsDuplicateCopy.add(firstMap);
        reducedmaps.clear();
        reducedmaps.addAll(mapsDuplicateCopy);    
      }   
      reducedmaps.remove(endFlagMap);
    }  
    return reducedmaps; 
  }
  
  public ConcreteFact absFactToConcreteFact(Term absFact, ConcreteRule conRule){
    ConcreteFact conFact = new ConcreteFact();
    HashMap<Term,Variable> keyMap = new HashMap<>();
    Mgu mgu = new Mgu();
    for(Term rf : conRule.getRF()){
      Substitution vSub = new Substitution();
      mgu.unifyTwoFacts(rf, absFact, vSub);
      List<Condition> rs = new ArrayList<>();
      if(vSub.getUnifierState() && !vSub.getSubstitution().isEmpty()){
        for(Map.Entry<String, Term> subs : vSub.getSubstitution().entrySet()){
          keyMap.put(subs.getValue(), new Variable(subs.getKey()));
          if(subs.getValue() instanceof Composed){
            for(Term SetMember : subs.getValue().getArguments()){
              if(!SetMember.getFactName().equals("0")){
                rs.add(new Condition(new Variable(subs.getKey()),SetMember));
              }
            }
          }
        }
        conFact.setFact(absTermToConcreteTermSubs(absFact,keyMap));
        conFact.setRS(rs);
      }
    }
    return conFact;
  }
  
  public Term absTermToConcreteTermSubs(Term t, HashMap<Term,Variable> subs){
    Term tCopy = (Term)dClone.deepClone(t);
    if((t instanceof Composed) && t.getFactName().equals("val")){
      if(subs.containsKey((Composed)t)){
        return subs.get((Composed)t);    
      }
    }else{
      for(int i=0;i<t.getArguments().size();i++){
        if((t.getArguments().get(i) instanceof Composed) && t.getFactName().equals("val")){
          Term keyT = t.getArguments().get(i);
          if(subs.containsKey(keyT)){
            tCopy.getArguments().set(i, subs.get(keyT));
          }
        }else if(t.getArguments().get(i) instanceof Composed){
          tCopy.getArguments().set(i, absTermToConcreteTermSubs(tCopy.getArguments().get(i),subs));
        }
      }
    } 
    return tCopy;
  }
}
