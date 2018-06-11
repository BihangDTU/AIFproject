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
  public ComputeFixedpoint(){};
  
  
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
    
    //ConcreteRule crule = concreteRules.get("serverUpdateKey");
    AbstractRule absrule = vf.concreteRuleToAbsRuleConversion(aifAST,concreteRules,"keyReg");
    //System.out.println(crule);
    System.out.println();
    //System.out.println(absrule);
    System.out.println();
    
    //AbstractRule contextClause = getContextClause(absrule);
    //System.out.println(contextClause);
    

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
/*    List<FactWithType> emptyList = new ArrayList<>();
    List<FactWithType> f1 = getNewFacts(hornClauses,emptyList,UserDefType,membershipName);
    List<FactWithType> f2 = getNewFacts(hornClauses,f1,UserDefType,membershipName);
    List<FactWithType> f3 = getNewFacts(hornClauses,f2,UserDefType,membershipName);
    List<FactWithType> f4 = getNewFacts(hornClauses,f3,UserDefType,membershipName);
    List<FactWithType> f5 = getNewFacts(hornClauses,f4,UserDefType,membershipName);
    for(FactWithType fact : f5){
      System.out.println(fact);
    }
    
 */   
    fixedpointCompute(hornClauses,UserDefType,membershipName);
    return hornClauses;
  }
  
  public void fixedpointCompute(List<AbstractRule> hornClauses,HashMap<String,List<String>> UserDefType, List<String> membershipName){
    List<OutFact> fixedpoint = new ArrayList<>();

    while(true){
      List<OutFact> newGenerateFacts = getNewFacts(hornClauses,fixedpoint,UserDefType,membershipName);
      List<OutFact> fixedpointCopy = new ArrayList<>(fixedpoint);
      List<OutFact> newGenerateFactsCopy = new ArrayList<>(newGenerateFacts);
      for(OutFact factF : fixedpointCopy){
        for(OutFact factN : newGenerateFactsCopy){
          if(mgu.isT1GreaterOrEqualT2(factF.getfact(), factN.getfact(), UserDefType, new RenamingInfo())){
            newGenerateFacts.remove(factN);
          }else if(mgu.isT1GreaterOrEqualT2(factN.getfact(), factF.getfact(), UserDefType, new RenamingInfo())){
            fixedpoint.remove(factF);
          }
        }
      }
      if(newGenerateFacts.isEmpty()){
        break;
      }else{
        fixedpoint.addAll(newGenerateFacts);
      }
    }
    for(OutFact f : fixedpoint){
      System.out.println(f);
    }
    
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
          SubstitutionWithTrace subsCopy = (SubstitutionWithTrace)dClone.deepClone(sub);
          FactWithType lfWithTypeSubs = new FactWithType(varsType ,mgu.termSubstituted(lf, sub.getSubs()));
          for(OutFact fact : facts){
            RenamingInfo renameInfo = new RenamingInfo();
            if(mgu.unifyTwoFactsPKDB(lfWithTypeSubs,fact.getfact(),subsCopy.getSubs(),renameInfo,UserDefType,membershipName)){
              isUnifySuccess = true;
              varsType.putAll(renameInfo.getvType());
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
      for(Term rf : absRule.getRF()){
        Term rfFact = mgu.termSubstituted(rf, substitution.getSubs());
        HashMap<String,String> vTypes = new HashMap<>();
        List<String> fv = mgu.vars(rfFact);
        for(Map.Entry<String, String> entity : varsType.entrySet()){
          if(fv.contains(entity.getKey())){
            vTypes.put(entity.getKey(), entity.getValue());
          }
        }       
        FactWithType rfFactWithTypes = new FactWithType(vTypes,rfFact); 
        OutFact outputfact = new OutFact(id.getCounter(),rfFactWithTypes,substitution.getTrace(),absRule.getRulesName());
        id.increaseCounter();
        newGenerateFacts.add(outputfact);
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
        FactWithType timplieWithTypes = new FactWithType(vTypes,tim); 
        OutFact outputfact = new OutFact(id.getCounter(),timplieWithTypes,substitution.getTrace(),absRule.getRulesName());
        id.increaseCounter();
        newGenerateFacts.add(outputfact);
      }
      
      
    }
    //List<FactWithType>  reducedFacts =  fs.reduceDuplicateFactsNew(newGenerateFacts,extendedTimplies,UserDefType);
    
    return newGenerateFacts;
  }

}
