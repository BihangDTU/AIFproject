import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import dataStructure.AIFdata;
import dataStructure.AST;
import dataStructure.Composed;
import dataStructure.ConcreteRule;
import dataStructure.FactWithType;
import dataStructure.FactWithTypeRuleName;
import dataStructure.Fixedpoint;
import dataStructure.FixedpointData;
import dataStructure.RenamingInfo;
import dataStructure.Term;
import dataStructure.Variable;


public class FactsSort {
  Mgu mgu = new Mgu();
  DeepClone dClone = new DeepClone();
  
  public FactsSort(){}
  
  public boolean canVal1impliesVal2(FactWithType val1, FactWithType val2, List<FactWithType> timplies,HashMap<String,List<String>> UserDefType){
    if(mgu.termSubs(val1.getTerm(),val1.getvType()).equals(mgu.termSubs(val2.getTerm(),val2.getvType()))){
      return true;
    }
    if(val1.getTerm().getFactName()!="val" || val2.getTerm().getFactName()!="val"){
      return false;
    }
    RenamingInfo renameInfo = new RenamingInfo(); 
    for(FactWithType timplie : timplies){
      FactWithType tVal1 = new FactWithType(timplie.getvType(),timplie.getTerm().getArguments().get(0));
      if(mgu.isT1GreaterOrEqualT2(tVal1,val1,UserDefType,renameInfo)){
        Term tVal_2 = mgu.termSubs(timplie.getTerm().getArguments().get(1),renameInfo.getSubs());
        FactWithType tVal2 = new FactWithType(renameInfo.getvType(),tVal_2);
        if(mgu.isT1GreaterOrEqualT2(tVal2,val2,UserDefType, new RenamingInfo())){
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean canT1impliesT2(FactWithType t1, FactWithType t2, List<FactWithType> timplies,HashMap<String,List<String>> UserDefType){
    if(mgu.termSubs(t1.getTerm(),t1.getvType()).equals(mgu.termSubs(t2.getTerm(),t2.getvType()))){
      return true;
    }else if(t1.getTerm() instanceof Variable){
      if(!(t2.getTerm() instanceof Variable)){
        return false;
      }else{
        if(!mgu.isV1GreateOrEqualV2(t1,t2,UserDefType)){
          return false;
        }
      }
    }else if(t2.getTerm() instanceof Variable){
      if(!(t1.getTerm() instanceof Variable)){
        return false;
      }else{
        if(!mgu.isV1GreateOrEqualV2(t1,t2,UserDefType)){
          return false;
        }
      }
    }else{
      if(!t1.getTerm().getFactName().equals(t2.getTerm().getFactName())){
        return false;
      }
      if(t1.getTerm().getFactName().equals("val")){
        if(!canVal1impliesVal2(t1,t2,timplies,UserDefType)){
          return false;
        }
      }else{
        for(int i=0;i<t1.getTerm().getArguments().size();i++){
          FactWithType subT1 = new FactWithType(t1.getvType(),t1.getTerm().getArguments().get(i));
          FactWithType subT2 = new FactWithType(t2.getvType(),t2.getTerm().getArguments().get(i));
          if(!canT1impliesT2(subT1,subT2,timplies,UserDefType)){
            return false;
          }
        }
      }
    }
    return true;
  }
  
  /**
   * Check whether two facts have the same form. 
   * @param  t1  e.g. iknows(sign(inv(val(0,db__valid(Server,Honest),0)),pair(Honest,val(ring(Honest),0,0))))
   * @param  t2  e.g. iknows(sign(inv(val(ring(Dishon),db__valid(Server,Dishon),0)),pair(Agent,val(0,db__valid(Server,Dishon),0))));   
   * @return boolean e.g. true
   */
  public boolean isTwoFactsHaveSameForm(Term t1, Term t2){
    if(t1.equals(t2)){
      return true;
    }else if((t1 instanceof Variable)){
      if(t2 instanceof Variable) return true;
      else return false;
    }else {
      if(!((Composed)t1).getFactName().equals(((Composed)t2).getFactName())){
        return false;
      }else if(((Composed)t1).getFactName().equals("val")){
        return true;
      }else{
        for(int i=0;i<t1.getArguments().size();i++){
          if(!isTwoFactsHaveSameForm(t1.getArguments().get(i),t2.getArguments().get(i))){
            return false;
          }
        }
      }
    }
    return true;
  }
  
  public List<FactWithTypeRuleName> getAllFactsFromFixedPoint(AST fpAST){
    List<FactWithTypeRuleName> facts = new ArrayList<>();
    for (Map.Entry<Integer, Fixedpoint> entity : ((FixedpointData)fpAST).getFixpoints().entrySet()) {
      facts.add(new FactWithTypeRuleName(entity.getValue().getvType(),entity.getValue().getTerm(),entity.getValue().getRulesName()));
    }    
    return facts;
  }
  
  /**
   * Returns a list which contains sorted fixed points in each lists. (timplies and occurs are removed)
   * @param  fpAST   dataStructure contains fixed points from output file  
   * @return  a list which contains sorted fixed points in each lists
   */
  public List<ArrayList<FactWithTypeRuleName>> fixedpointsSort(List<FactWithTypeRuleName> facts){
    List<ArrayList<FactWithTypeRuleName>> factsSorted = new ArrayList<>();
    List<FactWithTypeRuleName> factsUnsort = new ArrayList<>(facts);
    List<FactWithTypeRuleName> factsUnsortCopy = new ArrayList<>();
    /*remove timplies and occurs from fixed points*/
    for(FactWithTypeRuleName f : facts){
      if((f.getTerm().getFactName().equals("timplies") || f.getTerm().getFactName().equals("occurs"))){
        factsUnsort.remove(f);
      }
    }
    factsUnsortCopy.addAll(factsUnsort);
    /*sort facts into different lists then add to one list which contains lists of sorted facts.*/
    while(true){
      if(factsUnsort.isEmpty()){
        break;
      }else{
        FactWithTypeRuleName firstFact = factsUnsort.get(0);
        List<FactWithTypeRuleName> subSortedFacts = new ArrayList<>();
        for(FactWithTypeRuleName t : factsUnsort){
          if(isTwoFactsHaveSameForm(firstFact.getTerm(),t.getTerm())){
            subSortedFacts.add(t);
            factsUnsortCopy.remove(t);
          }
        }
        factsSorted.add(new ArrayList<>(subSortedFacts));
      }
      factsUnsort.clear();
      factsUnsort.addAll(factsUnsortCopy);
    } 
    return factsSorted;
  }
  
  /**
   * @param  fixedPointsSorted   facts in the fixed-points file have been sorted into different Arraylist according to the same form fact.
   * @param  extendedTimplies    a list of extended timplies
   * @param  UserDefType         user define types, using for compare types
   * @return A list of lists, each inner list contains the same form fact (all facts which can be generate by timplies are removed from the list).
   */
  public List<ArrayList<FactWithTypeRuleName>> fixedpointsWithoutDuplicate(List<ArrayList<FactWithTypeRuleName>> fixedPointsSorted, List<FactWithType> extendedTimplies, HashMap<String,List<String>> UserDefType){
    List<ArrayList<FactWithTypeRuleName>> reducedFacts = new ArrayList<>();
    for(ArrayList<FactWithTypeRuleName> facts : fixedPointsSorted){
      if(facts.size() == 1){
        reducedFacts.add(facts);
      }else{        
        HashMap<String, String> vType = new HashMap<>();
        Term flag = new Variable("EndFlag");
        FactWithTypeRuleName endFlag = new FactWithTypeRuleName(vType,flag,"flag");
        facts.add(endFlag);
        ArrayList<FactWithTypeRuleName> factsCopy = new ArrayList<FactWithTypeRuleName>(facts);
        while(true){
          if(facts.get(0).equals(endFlag)) break;
          FactWithTypeRuleName firstTermWithRuleName = facts.get(0);  
          FactWithType firstTerm = new FactWithType(firstTermWithRuleName.getvType(),firstTermWithRuleName.getTerm());
          for(FactWithTypeRuleName t : facts){
            FactWithType tWithoutRuleName = new FactWithType(t.getvType(),t.getTerm());
            if(canT1impliesT2(firstTerm,tWithoutRuleName,extendedTimplies, UserDefType)){
              factsCopy.remove(t);
            }
          }
          factsCopy.add(firstTermWithRuleName);
          facts.clear();
          facts.addAll(factsCopy);    
        }   
        facts.remove(endFlag);
        reducedFacts.add(facts);
      }
    } 
    return reducedFacts; 
  }
  
  public List<ArrayList<FactWithType>> fixedpointsWithoutDuplicateRuleName(List<ArrayList<FactWithTypeRuleName>> facts){
    List<ArrayList<FactWithType>> factsWithRuleName = new ArrayList<>();
    for(ArrayList<FactWithTypeRuleName> fs : facts){
      ArrayList<FactWithType> subList = new ArrayList<>();
      for(FactWithTypeRuleName f : fs){
        subList.add(new FactWithType(f.getvType(),f.getTerm()));
      }
      factsWithRuleName.add(subList);
    }
    return factsWithRuleName;
  }
  
  public HashSet<String> getRuleNamesHaveValueType(AST aifAST){
    HashSet<String> ruleNames = new HashSet<>();
    for(ConcreteRule cr : ((AIFdata)aifAST).getRules()){
      for(Map.Entry<String, String> vType : cr.getVarsTypes().entrySet()){
        if(vType.getValue().equals("value")){
          ruleNames.add(cr.getRulesName());
        }
      }
    }   
    return ruleNames;
  }
  
  public void printReductedFacts(List<ArrayList<FactWithType>> facts){
    
  }
  

}
