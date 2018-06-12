import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import dataStructure.*;

public class ComputeFixedpoint {
  FactsSort fs = new FactsSort();
  VerifyFixedpoint vf = new VerifyFixedpoint();
  Mgu mgu = new Mgu();
  DeepClone dClone = new DeepClone();
  IDcounter id = new IDcounter();
  GlobalCounterForSetMember gcsm = new GlobalCounterForSetMember();
  private static int maxVarsInSets;
  public ComputeFixedpoint(AST aifAST){
    maxVarsInSets = getMaxSizeSets(aifAST);
  };
  
  
  public List<AbstractRule> generateHornClause(AST aifAST,HashMap<String,List<String>> UserDefType){
    List<AbstractRule> hornClauses = new ArrayList<>();
    HashMap<String, ConcreteRule> concreteRules = new HashMap<>();
    for(ConcreteRule cr: ((AIFdata)aifAST).getRules()){
      concreteRules.put(cr.getRulesName(), cr);
    }
    //HashSet<String> ruleNames = fs.getRuleNamesHaveValueType(aifAST);
    HashSet<String> ruleNames = new HashSet<>();
    for(Map.Entry<String, ConcreteRule> entity : concreteRules.entrySet()){
      ruleNames.add(entity.getKey());
    }
    
    System.out.println("---------------------------------------------");
    

    for(String rName : ruleNames){
      AbstractRule absRule = vf.concreteRuleToAbsRuleConversion(aifAST,concreteRules,rName);
      hornClauses.add(absRule);
      if(!absRule.getTimplies().isEmpty()){
        AbstractRule contextClause = getContextClause(absRule);
        hornClauses.add(contextClause);
      }
    }
    for(AbstractRule hc : hornClauses){
      System.out.println(hc);
      System.out.println();
    }
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println();
    /*List<FactWithType> facts = new ArrayList<>();
    List<FactWithType> dd = applyHornClause(absrule,facts,UserDefType);
    System.out.println(dd);
    */   
    List<String> membershipName = vf.getSetMembershipName(aifAST);
        
    fixedpointCompute(hornClauses,UserDefType,membershipName);
    return hornClauses;
  }
  
  public void fixedpointCompute(List<AbstractRule> hornClauses,HashMap<String,List<String>> UserDefType, List<String> membershipName){
    List<OutFact> fixedpoint = new ArrayList<>();
    //List<OutFact> allFacts = new ArrayList<>();
    while(true){
      /*System.out.println("----------------------------");
      System.out.println("FixedPoint: " + fixedpoint.size());
      System.out.println();
      for(OutFact oo : fixedpoint){
        System.out.println(oo);
      }
      System.out.println("----------------------------");
      System.out.println("Calculating!!!!!!!!!!");*/
      //HashSet<OutFact> newGenerateFact = new HashSet<>(getNewFacts(hornClauses,fixedpoint,UserDefType,membershipName));
      //allFacts.clear();
      //id.resetCounter();
      List<OutFact> newGenerateFact = getNewFacts(hornClauses,fixedpoint,UserDefType,membershipName);
      //allFacts.addAll(newGenerateFact);
      /*System.out.println("----------------------------");
      System.out.println("New generate: " + newGenerateFact.size());
      System.out.println();
      for(OutFact oo : newGenerateFact){
        System.out.println(oo);
      }
      System.out.println("----------------------------");*/
      List<OutFact> fixedpointCopy = new ArrayList<>(fixedpoint);
      fixedpointCopy.addAll(newGenerateFact);
     // System.out.println("reduceing!!!!!!!!!!!!!!!!!!!!");
      List<OutFact> facts = reduceDuplicateFacts(fixedpointCopy, UserDefType);
      /*System.out.println("----------------------------");
      System.out.println("reduced new generate facts: " + facts.size());
      System.out.println();
      for(OutFact oo : facts){
        System.out.println(oo);
      }
      System.out.println("----------------------------");*/
      if(fixedpoint.equals(facts)){
        break;
      }else{
        fixedpoint.clear();
        fixedpoint.addAll(facts);
      }
    }
    
    System.out.println("Fixed-point: " + fixedpoint.size());
    System.out.println("Computation done!");
    try {
      FileWriter writer = new FileWriter("Fixedpoint.txt", true);
      for(OutFact f : fixedpoint){
        writer.write(f.toString() + "\n");
      }
      
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
 /*   try {
      FileWriter writer = new FileWriter("AllFacts.txt", true);
      for(OutFact all : allFacts){
        writer.write(all.toString() + "\n");
      }
      
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }*/
  }
  
  public AbstractRule getContextClause(AbstractRule absRule){
    AbstractRule contextClause = new AbstractRule();
    if(absRule.getTimplies().isEmpty()){
      return contextClause;
    }  
    contextClause.setRulesName("timplies");
    HashMap<Term,Term> invTimplesMap = new HashMap<>();
    Substitution subs = new Substitution();
    List<Term> timpliesList = new ArrayList<>();
    for(Term timple : absRule.getTimplies()){
      Composed timplies = new Composed("timplies");
      gcsm.increaseCounter();
      String PKDB1 = "PKDB_" + gcsm.getCounter();
      Variable val1 = new Variable(PKDB1);
      gcsm.increaseCounter();
      String PKDB2 = "PKDB_" + gcsm.getCounter();
      Variable val2 = new Variable(PKDB2);
      timplies.setArguments(val1);
      timplies.setArguments(val2);
      timpliesList.add(timplies);
      mgu.unifyTwoFacts(timplies,timple,subs);
      invTimplesMap.put(timple.getArguments().get(0), timple.getArguments().get(1));
    }
    HashMap<Term,Term> valMap = new HashMap<>();
    HashMap<String,String> varTypes = new HashMap<>();
    for(Map.Entry<String, Term> entity : subs.getSubstitution().entrySet()){
      valMap.put(entity.getValue(), new Variable(entity.getKey()));
      varTypes.put(entity.getKey(), "membership");
    }
    contextClause.setVarsTypes(varTypes);
    Term subLF = termSubs(absRule.getLF().get(0),invTimplesMap);
    Term leftContex = termSubs(absRule.getLF().get(0),valMap);
    Term rightContex = termSubs(subLF,valMap);
    timpliesList.add(leftContex);
    contextClause.setLF(timpliesList);
    List<Term> rf = new ArrayList<>();
    rf.add(rightContex);
    contextClause.setRF(rf);   
    return contextClause;
  }
  
  public Term termSubs(Term t, HashMap<Term,Term> subs){  
    Term t_copy = (Term)dClone.deepClone(t);
    if(t instanceof Variable){
      return t;    
    }else{
      if(((Composed)t).getFactName().equals("val")){
        if(subs.containsKey(t)){
          return subs.get(t);   
        }
      }else{
        for(int i=0; i < t.getArguments().size(); i++){
          t_copy.getArguments().set(i, termSubs(t_copy.getArguments().get(i),subs));
        }
      }
    }
    return t_copy;
  }
  
  public List<OutFact> getNewFacts(List<AbstractRule> hornClauses,List<OutFact> facts,HashMap<String,List<String>> UserDefType, List<String> membershipName){
    List<OutFact> newGenerateFacts = new ArrayList<>();
    for(AbstractRule hc : hornClauses){
      List<OutFact> newFaccts = applyHornClause(hc,facts,UserDefType,membershipName);
      newGenerateFacts.addAll(newFaccts);
    }
    return newGenerateFacts;
  }
  
  public List<OutFact> applyHornClause(AbstractRule absRule, List<OutFact> facts,HashMap<String,List<String>> UserDefType,List<String> membershipName){
    List<OutFact> newGenerateFacts = new ArrayList<>();
    List<SubstitutionWithTrace> stisfySubtitutions = new ArrayList<>(); 
    HashMap<String,String> varsType = new HashMap<>(absRule.getVarsTypes());
    
    for(Term lf : absRule.getLF()){
      boolean isUnifySuccess = false;
      FactWithType lfWithType = new FactWithType(varsType ,lf);
      if(stisfySubtitutions.isEmpty()){
        for(OutFact fact : facts){
          Substitution subs = new Substitution();
          RenamingInfo renameInfo = new RenamingInfo();
          //subs.setUnifierState(true);
          if(mgu.unifyTwoFactsPKDB(lfWithType,fact.getfact(),subs,renameInfo,UserDefType,membershipName)){
            isUnifySuccess = true;
            varsType.putAll(renameInfo.getvType());
            List<Integer> trace = new ArrayList<>();
            trace.add(fact.getID());
            SubstitutionWithTrace subsTrace = new SubstitutionWithTrace(trace,subs);
            stisfySubtitutions.add(subsTrace);
          }
        }
        if(!isUnifySuccess){
          return newGenerateFacts;
        }
      }else{
        List<SubstitutionWithTrace> stisfySubtitutionsCopy = (List<SubstitutionWithTrace>)dClone.deepClone(stisfySubtitutions);
        for(SubstitutionWithTrace sub : stisfySubtitutionsCopy){
          FactWithType lfWithTypeSubs = new FactWithType(varsType ,mgu.termSubstituted(lf, sub.getSubs()));
          for(OutFact fact : facts){
            SubstitutionWithTrace subsCopy = (SubstitutionWithTrace)dClone.deepClone(sub);
            RenamingInfo renameInfo = new RenamingInfo();
            subsCopy.getSubs().setUnifierState(true);
            if(mgu.unifyTwoFactsPKDB(lfWithTypeSubs,fact.getfact(),subsCopy.getSubs(),renameInfo,UserDefType,membershipName)){
              isUnifySuccess = true;
              varsType.putAll(renameInfo.getvType());
              subsCopy.getTrace().add(fact.getID());
              stisfySubtitutions.add(subsCopy);
            }
          }
          stisfySubtitutions.remove(sub);
          if(!isUnifySuccess){
            return newGenerateFacts;
          }
        }
        
      }
    }  
    
    if(absRule.getLF().isEmpty()){
      for(Term rf : absRule.getRF()){
        HashMap<String,String> vTypes = new HashMap<>();
        List<String> fv = mgu.vars(rf);
        for(Map.Entry<String, String> entity : absRule.getVarsTypes().entrySet()){
          if(fv.contains(entity.getKey())){
            vTypes.put(entity.getKey(), entity.getValue());
          }
        }
        id.increaseCounter();
        List<Integer> trace = new ArrayList<>();    
        FactWithType rfWithType = new FactWithType(vTypes,rf);
        OutFact outputfact = new OutFact(id.getCounter(),rfWithType,trace,absRule.getRulesName());
        newGenerateFacts.add(outputfact);
      }
    }
    for(SubstitutionWithTrace substitution : stisfySubtitutions){
      boolean areTypeConsistent = true;
      for(Term rf : absRule.getRF()){
        Term rfFact = mgu.termSubstituted(rf, substitution.getSubs());
        HashMap<String,String> vTypes = new HashMap<>();
        List<String> fv = mgu.vars(rfFact);
        for(Map.Entry<String, String> entity : varsType.entrySet()){
          if(fv.contains(entity.getKey())){
            vTypes.put(entity.getKey(), entity.getValue());
          }
        }
        
        FactWithType rfFactWithTypes = new FactWithType();
        /*HashMap<String,String> varTypeMap = getVarsTypeSubstitutionMap(vTypes,UserDefType);
        if(!varTypeMap.isEmpty()){
          for(Map.Entry<String, String> entity : varTypeMap.entrySet()){
            vTypes.remove(entity.getKey());
          }
          Term rfFactSubstituted = mgu.termSubs(rfFact, varTypeMap);
          rfFactWithTypes.setvType(vTypes);
          rfFactWithTypes.setTerm(rfFactSubstituted);
        }else{*/
          rfFactWithTypes.setvType(vTypes);
          rfFactWithTypes.setTerm(rfFact);
        //}
        id.increaseCounter();
        OutFact outputfact = new OutFact(id.getCounter(),rfFactWithTypes,substitution.getTrace(),absRule.getRulesName());
      
        Term rfFactWithTypesSubs = mgu.termSubs(rfFact, vTypes);
        List<HashSet<String>> valTypes = new ArrayList<>();
        getValTypes(rfFactWithTypesSubs,valTypes);
        if(getMaxSize(valTypes)>maxVarsInSets){   // 2 is temp use here, need to calculate the number of variables in val(...)
          areTypeConsistent = false;
        }
        if(areTypeConsistent){
          newGenerateFacts.add(outputfact);
        }
      }

      for(Term timplie : absRule.getTimplies()){
        Term tim = mgu.termSubstituted(timplie, substitution.getSubs());
        HashMap<String,String> vTypes = new HashMap<>();
        List<String> fv = mgu.vars(tim);
        for(Map.Entry<String, String> entity : varsType.entrySet()){
          if(fv.contains(entity.getKey())){
            vTypes.put(entity.getKey(), entity.getValue());
          }
        }  
  
        FactWithType timplieWithTypes = new FactWithType();
        /*HashMap<String,String> varTypeMap = getVarsTypeSubstitutionMap(vTypes,UserDefType);
        if(!varTypeMap.isEmpty()){
          for(Map.Entry<String, String> entity : varTypeMap.entrySet()){
            vTypes.remove(entity.getKey());
          }
          Term timSubstituted = mgu.termSubs(tim, varTypeMap);
          timplieWithTypes.setvType(vTypes);
          timplieWithTypes.setTerm(timSubstituted);
        }else{*/
          timplieWithTypes.setvType(vTypes);
          timplieWithTypes.setTerm(tim);
        //}
        id.increaseCounter();
        OutFact outputfact = new OutFact(id.getCounter(),timplieWithTypes,substitution.getTrace(),absRule.getRulesName());
        
        Term rfFactWithTypesSubs = mgu.termSubs(tim, vTypes);
        List<HashSet<String>> valTypes = new ArrayList<>();
        getValTypes(rfFactWithTypesSubs,valTypes);
        if(getMaxSize(valTypes)> maxVarsInSets){   // 2 is temp use here, need to calculate the number of variables in val(...)
          areTypeConsistent = false;
        }
        if(areTypeConsistent){
          
          newGenerateFacts.add(outputfact);
        }
      }
      
      
    }
    //List<FactWithType>  reducedFacts =  fs.reduceDuplicateFactsNew(newGenerateFacts,extendedTimplies,UserDefType);
    
    return newGenerateFacts;
  }
  
  public HashSet<String> getValTypes(Term t, List<HashSet<String>> valTypes){
    HashSet<String> types = new HashSet<>();
    if(t instanceof Variable){
      return types;    
    }else{
      if(((Composed)t).getFactName().equals("val")){
        valTypes.add(new HashSet<>(mgu.vars(t)));
      }else{
        for(int i=0; i < t.getArguments().size(); i++){
          valTypes.add(getValTypes(t.getArguments().get(i),valTypes));
        }
      }
    }
    return types;
  }
  
  public int getMaxSizeSets(AST aifAST){
    List<Term> sets = ((AIFdata)aifAST).getSets();
    List<HashSet<String>> dbTypes = new ArrayList<>();
    for(Term db : sets){
      dbTypes.add(new HashSet<>(mgu.vars(db)));
    }
    return getMaxSize(dbTypes);
  }
  
  public int getMaxSize(List<HashSet<String>> valTypes){
    int maxSize = 0;
    for(HashSet<String> valT : valTypes){
      if(maxSize < valT.size()){
        maxSize = valT.size();
      }
    }
    return maxSize;
  }
  
  public List<OutFact> reduceDuplicateFacts(List<OutFact> facts, HashMap<String,List<String>> UserDefType){
    List<OutFact> reducedFacts = new ArrayList<>(facts);
    List<OutFact> factsCopy = new ArrayList<>(facts);
    if(facts.size() == 1){
      return facts;
    }else{     
      FactWithType EndFlag = new FactWithType(new HashMap<String,String>(), new Composed("_"));
      OutFact flag = new OutFact(0,EndFlag, new ArrayList<Integer>(),"Flag");
      factsCopy.add(flag);
      while(true){
        if(factsCopy.get(0).equals(flag)){
          //System.out.println("reduce done....");
          break;
        } 
        OutFact firstFact = factsCopy.get(0); 
        factsCopy.remove(0); 
        for(OutFact f : reducedFacts){
          if(mgu.isT1GreaterOrEqualT2(firstFact.getfact(), f.getfact(), UserDefType, new RenamingInfo())){
            factsCopy.remove(f);
          }
        }
        factsCopy.add(firstFact);
        reducedFacts.clear();
        reducedFacts.addAll(factsCopy);    
      }   
      reducedFacts.remove(flag);
    }
    return reducedFacts; 
  }
  
  /*public HashMap<String,String> getVarsTypeSubstitutionMap(HashMap<String,String> varTypeMap,HashMap<String,List<String>> UserDefType){
    HashMap<String,String> varTypeMapCopy = new HashMap<>(varTypeMap);
    HashMap<String,String> substitutiondMap = new HashMap<>();
    for(Map.Entry<String,String> entity : varTypeMap.entrySet()){
      for(Map.Entry<String,String> entityCopy : varTypeMapCopy.entrySet()){
        if(UserDefType.get(entity.getValue()).containsAll(UserDefType.get(entityCopy.getValue()))){
          if(!UserDefType.get(entityCopy.getValue()).containsAll(UserDefType.get(entity.getValue()))){
            if(!substitutiondMap.containsKey(entityCopy.getKey())){
              substitutiondMap.put(entityCopy.getKey(), entity.getKey());
            }else{
              String var = varTypeMap.get(substitutiondMap.get(entityCopy.getKey()));
              if(UserDefType.get(var).containsAll(UserDefType.get(entity.getValue()))){
                substitutiondMap.remove(entityCopy.getKey());
                substitutiondMap.put(entityCopy.getKey(), entity.getKey());
              }
            }
          }
        }
      }
    }
    return substitutiondMap;
  }*/

}
