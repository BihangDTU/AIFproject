import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myException.UnificationFailedException;

import dataStructure.*;

public class FixpointsSort {
  StateTransition st = new StateTransition();  // need move to new class
  DeepClone dClone = new DeepClone();  // need move to new class
  StateTransition ST = new StateTransition();
  public FixpointsSort(){}
	
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
  /**
   * Returns a list of timplies from fixed-points output file.
   * @param  fpAST   dataStructure contains fixed points from output file  
   * @return  a list of timplies fixedpoints(the variable have been substituted with user define type)
   */
  public List<FactWithType> getTimplies(AST fpAST){
    List<FactWithType> timplies = new ArrayList<FactWithType>();
    for (Map.Entry<Integer, Fixedpoint> entry : ((FixedpointData)fpAST).getFixpoints().entrySet()) {
      if(entry.getValue().getTerm().getFactName().equals("timplies")){
        timplies.add(new FactWithType(entry.getValue().getvType(),entry.getValue().getTerm()));
      }
    }
    return timplies;
  }

  /**
   * Returns a list of timplies which contains all possible timplies
   * @param  fpAST   dataStructure contains fixed points from output file  
   * @return  a list of all possible implies (the variable have been substituted with user define type)
   */
  /*extend timplies, the types are considered here
   * (1) timpies(val(ring(Honest),0,0),val(ring(Honest),valid(Server,Honest),0))
   * (2) timpies(val(ring(Honest),valid(Server,Honest),0),val(0,valid(Server,Honest),0))
   * (3) timpies(val(0,valid(Server,Honest),0),val(0,0,revoked(Server,Honest)))
   *  
   *       Honest      Honest       Honest
   * (1,0,0) -> (1,1,0) -> (0,1,0) -> (0,0,1)
   * 
   * (1) timpies(val(ring(Honest),0,0),val(ring(Honest),valid(Server,Honest),0))
   * (2) timpies(val(ring(Honest),0,0),val(0,valid(Server,Honest),0))
   * (3) timpies(val(ring(Honest),0,0)),0,0,revoked(Server,Honest)))
   * (4) timpies(val(ring(Honest),valid(Server,Honest),0),val(0,valid(Server,Honest),0))
   * (5) timpies(val(ring(Honest),valid(Server,Honest),0),val(0,0,revoked(Server,Honest)))
   * (7) timpies(val(0,valid(Server,Honest),0),val(0,0,revoked(Server,Honest)))
   */
  public List<FactWithType> getExtendedTimplies(AST fpAST,HashMap<String,List<String>> UserDefType){
    HashMap<Term,HashSet<Term>> timpliesMap = new HashMap<Term,HashSet<Term>>(); 
    List<FactWithType> timplies = getTimplies(fpAST);
    /*substituted variable into user define types*/
    for(FactWithType timplie : timplies){
      timplie.setTerm(termSubs(timplie.getTerm(),timplie.getvType()));
    }
    for(FactWithType t : timplies){
      if(!timpliesMap.containsKey(t.getTerm().getArguments().get(0))){
        /*         (1)                  (2)                                     */
        /*timpies(val(ring(Honest),0,0),val(ring(Honest),valid(Server,Honest),0))*/
        /*if the key is not in map then put (1)val(...) as new key and (2)val(...) as value srore in to Map*/
        HashSet<Term> subVal = new HashSet<Term>();
        subVal.add(t.getTerm().getArguments().get(1));
        timpliesMap.put(t.getTerm().getArguments().get(0), subVal);
      }else{
        /*if the key already exists in the map then just append the (2)val(...) into the value list*/
        timpliesMap.get(t.getTerm().getArguments().get(0)).add(t.getTerm().getArguments().get(1));
      }
    }
    /*check whether each key corresponding value list contains the other keys in the map,
    if so, then also add the key's corresponding values into the the list*/
    int counter = 0;
    for(Map.Entry<Term,HashSet<Term>> tMap : timpliesMap.entrySet()){
      while(true){
        counter = 0;
        for(Map.Entry<Term,HashSet<Term>> map : timpliesMap.entrySet()){
          counter ++;
          if(timpliesMap.get(tMap.getKey()).contains(map.getKey())){
            int valueSize = timpliesMap.get(tMap.getKey()).size();
            timpliesMap.get(tMap.getKey()).addAll(timpliesMap.get(map.getKey()));
            if(valueSize < timpliesMap.get(tMap.getKey()).size()){
              counter --;
            }
          }
        }
        if(counter >= timpliesMap.size()){
          break;
        }
      }
    }		
    /*if any val(...) exist in the timpliesMap value list and can apply to the timplies then new timplies 
     * add into the extendedTimplies*/
    HashSet<FactWithType> extendedTimplies = new HashSet<>();
    for(Map.Entry<Term,HashSet<Term>> ts : timpliesMap.entrySet()){
      for(Term t : ts.getValue()){
        for(FactWithType fp : timplies){
          if(isTwoValHaveSameForm(t,fp.getTerm().getArguments().get(0))){
            if(isVal1GeneralThanVal2(fp.getTerm().getArguments().get(0),t,UserDefType)){
              ArrayList<Term> vals = new ArrayList<>();
              vals.add(ts.getKey());
              vals.add(fp.getTerm().getArguments().get(1));
              extendedTimplies.add(new FactWithType(fp.getvType(), new Composed("timplies",vals)));
            }
          }
        }
      }
    }
    extendedTimplies.addAll(timplies);
    List<FactWithType> extendedTimpliesList = new ArrayList<>(extendedTimplies);
    return extendedTimpliesList;
  }
 
  /**
   * Returns a list of lists, each inner list contains list of timplies with same types.
   * @param  extendedTimplies  list of timplies with types from fixed points.
   *                           e.g. [A:Honest.S:Server.timplies(val(ring(Honest),db__valid(Server,Honest),0),val(0,db__valid(Server,Honest),0)),
   *                                 A:Dishon.S:Server.timplies(val(ring(Dishon),db__valid(Server,Dishon),0),val(ring(Dishon),0,db__revoked(Server,Dishon))),
   *                                 A:Honest.S:Server.timplies(val(ring(Honest),db__valid(Server,Honest),0),val(0,0,db__revoked(Server,Honest))),
   *                                 A:Honest.S:Server.timplies(val(ring(Honest),0,0),val(0,0,0)),
   *                                 A:Honest.S:Server.timplies(val(ring(Honest),0,0),val(ring(Honest),db__valid(Server,Honest),0)),
   *                                 A:Honest.S:Server.timplies(val(ring(Honest),0,0),val(0,db__valid(Server,Honest),0)),
   *                                 A:Honest.S:Server.timplies(val(ring(Honest),0,0),val(0,0,db__revoked(Server,Honest))),
   *                                 A:Honest.S:Server.timplies(val(0,0,0),val(0,db__valid(Server,Honest),0)),
   *                                 A:Honest.S:Server.timplies(val(0,0,0),val(0,0,db__revoked(Server,Honest))),
   *                                 A:Honest.S:Server.timplies(val(0,db__valid(Server,Honest),0),val(0,0,db__revoked(Server,Honest))),
   *                                 A:Honest.S:Server.timplies(val(ring(Honest),0,db__revoked(Server,Honest)),val(0,0,db__revoked(Server,Honest))),]
   * @return  e.g. [[A:Honest.S:Server.timplies(val(ring(Honest),db__valid(Server,Honest),0),val(0,db__valid(Server,Honest),0)),
   *                 A:Honest.S:Server.timplies(val(ring(Honest),db__valid(Server,Honest),0),val(0,0,db__revoked(Server,Honest))),
   *                 A:Honest.S:Server.timplies(val(ring(Honest),0,0),val(0,0,0)),
   *                 A:Honest.S:Server.timplies(val(ring(Honest),0,0),val(ring(Honest),db__valid(Server,Honest),0)),
   *                 A:Honest.S:Server.timplies(val(ring(Honest),0,0),val(0,db__valid(Server,Honest),0)),
   *                 A:Honest.S:Server.timplies(val(ring(Honest),0,0),val(0,0,db__revoked(Server,Honest))),
   *                 A:Honest.S:Server.timplies(val(0,0,0),val(0,db__valid(Server,Honest),0)),
   *                 A:Honest.S:Server.timplies(val(0,0,0),val(0,0,db__revoked(Server,Honest))),
   *                 A:Honest.S:Server.timplies(val(0,db__valid(Server,Honest),0),val(0,0,db__revoked(Server,Honest))),
   *                 A:Honest.S:Server.timplies(val(ring(Honest),0,db__revoked(Server,Honest)),val(0,0,db__revoked(Server,Honest))),],
   *                 [A:Dishon.S:Server.timplies(val(ring(Dishon),db__valid(Server,Dishon),0),val(ring(Dishon),0,db__revoked(Server,Dishon)))]]
   */
  public List<List<FactWithType>> classifyTimpliesInTypes(List<FactWithType> extendedTimplies){
    List<List<FactWithType>> classifiedTimplies = new ArrayList<>();
    List<FactWithType> unClassifyTimplies = new ArrayList<>(extendedTimplies);
    List<FactWithType> remainTimplies = new ArrayList<>(extendedTimplies);
    while(true){
      if(unClassifyTimplies.isEmpty()) break;
      ArrayList<FactWithType> timpliesClassified = new ArrayList<>();
      HashMap<String, String> vType = unClassifyTimplies.get(0).getvType();
      for(FactWithType t : unClassifyTimplies){
        if(vType.equals(t.getvType())){
          timpliesClassified.add(t);
          remainTimplies.remove(t);
        }
      }
      classifiedTimplies.add(timpliesClassified);
      unClassifyTimplies.clear();
      unClassifyTimplies.addAll(remainTimplies);
    }
    return classifiedTimplies;
  }
  
  /**
   * Display timplies in orders. 
   * @param  classifiedTimplies   expended timplies have been classified into different list according to types
   * @param  timplies             list of timplies with types from fixed points file.
   */
  public void timpliesOrdered(List<List<FactWithType>> classifiedTimplies,List<FactWithType> timplies){
    List<List<ArrayList<FactWithType>>> classifiedTimpliesSorted = new ArrayList<>();
    for(List<FactWithType> tList : classifiedTimplies){
      classifiedTimpliesSorted.add(timpliesSort(tList));
    }
    
    for(List<ArrayList<FactWithType>> t : classifiedTimpliesSorted){
      Collections.sort(t, new SortbyListSize());
    }
    
    for(FactWithType timplie : timplies){
      timplie.setTerm(termSubs(timplie.getTerm(),timplie.getvType()));
    }
    
    List<FactWithType> timpliesOrdered = new ArrayList<>();
    for(List<ArrayList<FactWithType>> ts : classifiedTimpliesSorted){
      for(ArrayList<FactWithType> t : ts){
        Term firstVal = t.get(0).getTerm().getArguments().get(0);
        for(FactWithType timplie : timplies){
          if(firstVal.equals(timplie.getTerm().getArguments().get(0))){
            timpliesOrdered.add(timplie);
          } 
        }
      }
    }
    
    for(FactWithType t : timpliesOrdered){
      System.out.println(t.getTerm().getArguments().get(0) + " -->> " + t.getTerm().getArguments().get(1));
    }
  }
	
  /**
   * 
   * @param  timplies   list of timplies with types from fixed points. 
   * @return return a list of lists, each inner list contains the timplies which has the same form.
   */
  public List<ArrayList<FactWithType>> timpliesSort(List<FactWithType> timplies){
    List<ArrayList<FactWithType>> timpliesSorted = new ArrayList<>();
    List<FactWithType> timpliesUnsort = new ArrayList<FactWithType>(timplies);
    List<FactWithType> factsUnsortCopy = new ArrayList<FactWithType>(timpliesUnsort);
    while(true){
      if(timpliesUnsort.isEmpty()){
        break;
      }else{
        FactWithType firstTimplies = timpliesUnsort.get(0);
        ArrayList<FactWithType> subTimpliesSorted = new ArrayList<>();
        for(FactWithType t : timpliesUnsort){
          if(isTwoValHaveSameForm(firstTimplies.getTerm().getArguments().get(0),t.getTerm().getArguments().get(0))){
            subTimpliesSorted.add(t);
            factsUnsortCopy.remove(t);
          }
        }
        timpliesSorted.add(subTimpliesSorted);
      }
      timpliesUnsort.clear();
      timpliesUnsort.addAll(factsUnsortCopy);
    }	
    return timpliesSorted;
  }
	
  /**
   * Returns a list which contains sorted fixed points in each lists. (timplies and occurs are removed)
   * @param  fpAST   dataStructure contains fixed points from output file  
   * @return  a list which contains sorted fixed points in each lists
   */
  public List<ArrayList<FactWithType>> fixedpointsSort(AST fpAST){
    List<ArrayList<FactWithType>> factsSorted = new ArrayList<ArrayList<FactWithType>>();
    List<FactWithType> facts = new ArrayList<FactWithType>();
    List<FactWithType> factsUnsort = new ArrayList<FactWithType>();
    for (Map.Entry<Integer, Fixedpoint> entry : ((FixedpointData)fpAST).getFixpoints().entrySet()) {
      factsUnsort.add(new FactWithType(entry.getValue().getvType(),entry.getValue().getTerm()));
    }		
    List<FactWithType> factsUnsortCopy = new ArrayList<FactWithType>(factsUnsort);
    /*remove timplies and occurs from fixed points*/
    for(FactWithType Fixedpoint : factsUnsort){
      if(Fixedpoint.getTerm().getFactName().equals("timplies") || Fixedpoint.getTerm().getFactName().equals("occurs")){
        factsUnsortCopy.remove(Fixedpoint);
      }
    }
    factsUnsort.clear();
    factsUnsort.addAll(factsUnsortCopy);
    /*sort fixed points into different lists then add to one list which contains lists of sorted fixed points.*/
    while(true){
      if(factsUnsort.isEmpty()){
        break;
      }else{
        FactWithType firstFact = factsUnsort.get(0);
        for(FactWithType t : factsUnsort){
          if(isTwoFactsHaveSameForm(firstFact.getTerm(),t.getTerm())){
            facts.add(t);
            factsUnsortCopy.remove(t);
          }
        }
        factsSorted.add(new ArrayList<>(facts));
        facts.clear();
      }
      factsUnsort.clear();
      factsUnsort.addAll(factsUnsortCopy);
    }	
    /*substituted user define types into fixed points */
    List<ArrayList<FactWithType>> factsSortedSubs = new ArrayList<ArrayList<FactWithType>>();
    for(ArrayList<FactWithType> fpts : factsSorted){
      ArrayList<FactWithType> factsSubs = new ArrayList<FactWithType>();
      for(FactWithType fpt : fpts){
        factsSubs.add(new FactWithType(fpt.getvType(),termSubs(fpt.getTerm(),fpt.getvType())));
      }
      factsSortedSubs.add(factsSubs);
    }
    return factsSortedSubs;
  }
	
  
  /**
   * @param  fixedPointsSorted   facts in the fixed-points file have been sorted into different Arraylist according to the same form fact.
   * @param  extendedTimplies    a list of extended timplies
   * @param  UserDefType         user define types, using for compare types
   * @return A list of lists, each inner list contains the same form fact (all facts which can be generate by timplies are removed from the list).
   */
  public List<ArrayList<FactWithType>> fixedpointsWithoutDuplicate(List<ArrayList<FactWithType>> fixedPointsSorted, List<FactWithType> extendedTimplies, HashMap<String,List<String>> UserDefType){
    List<ArrayList<FactWithType>> noDuplicateFacts = new ArrayList<>();
    List<ArrayList<FactWithType>> factsSorted_copy = (List<ArrayList<FactWithType>>)dClone.deepClone(fixedPointsSorted);
    for(ArrayList<FactWithType> facts : factsSorted_copy){
      if(facts.size() == 1){
        noDuplicateFacts.add(facts);
      }else{				
        HashMap<String, String> vType = new HashMap<>();
        Term flag = new Variable("EndFlag");
        FactWithType lastElementFlag = new FactWithType(vType,flag);
        facts.add(lastElementFlag);
        ArrayList<FactWithType> factsCopy = new ArrayList<FactWithType>(facts);
        while(true){
          if(facts.get(0).equals(lastElementFlag)) break;
          FactWithType firstTerm = facts.get(0);				
          for(FactWithType t : facts){
            if(canT1ImpliesT2(firstTerm,t,extendedTimplies, UserDefType)){
              factsCopy.remove(t);
            }
          }
          factsCopy.add(firstTerm);
          facts.clear();
          facts.addAll(factsCopy);		
        }		
        facts.remove(lastElementFlag);
        noDuplicateFacts.add(facts);
      }
    }	
    return noDuplicateFacts; 
  }	
	
  /**
   * Giveing two facts t1 and t2 with types, checking whether the T1 can implies T2
   * @param  t1  e.g. iknows(sign(inv(val(ring(User),db__valid(Server,User),0)),pair(User,val(ring(User),0,0))))
   * @param  t2  e.g. iknows(sign(inv(val(0,db__valid(Server,Honest),0)),pair(Honest,val(0,db__valid(Server,Honest),0))))
   * @param  timplies  e.g. [A:User.S:Server.timplies(val(ring(User),db__valid(Server,User),0),val(ring(User),0,0)),
   *                         A:Honest.S:Server.timplies(val(0,db__valid(Server,Honest),0),val(0,db__valid(Server,Honest),0))),
   *                         ...]
   * @param  UserDefType e.g. {Agent=[Honest, Server, Dishon], Honest=[Honest], User=[Honest, Dishon], Sts=[Sts], Server=[Server], Dishon=[Dishon]}  
   * @return boolean e.g. true
   */
  public boolean canT1ImpliesT2(FactWithType t1, FactWithType t2, List<FactWithType> timplies, HashMap<String,List<String>> UserDefType){ 
    if(t1.equals(t2)){
      return true;
    }
    else if((t1.getTerm() instanceof Composed) && (t2.getTerm() instanceof Composed)){
      if(t1.getTerm().getFactName().equals(t2.getTerm().getFactName()) && t1.getTerm().getArguments().size() == t2.getTerm().getArguments().size()){
        int argumentsSize = t1.getTerm().getArguments().size();
        for(int i=0;i<argumentsSize;i++){
          Term subT1 = t1.getTerm().getArguments().get(i);
          Term subT2 = t2.getTerm().getArguments().get(i);
          if((subT1 instanceof Composed) && (subT2 instanceof Composed)){
            if((subT1.getFactName().equals("val")) && (subT2.getFactName().equals("val"))){
              int counter = 0;
              for(FactWithType timp : timplies){
                counter ++;
                if(isTwoValHaveSameForm(subT1, timp.getTerm().getArguments().get(0)) && isTwoValHaveSameForm(subT2, timp.getTerm().getArguments().get(1))){
                  if(isVal1GeneralThanVal2(timp.getTerm().getArguments().get(0),subT1,UserDefType)){
                    HashMap<String,String> typeMap = getSubstitutionMap(timp.getTerm().getArguments().get(0),subT1);
                    Term impliesFromT1 = termSubs(timp.getTerm().getArguments().get(1),typeMap);										
                    if(isVal1GeneralThanVal2(impliesFromT1,subT2,UserDefType)){
                      counter --;
                    }else{
                      return false;
                    }
                  }else{
                    return false;
                  }
                }
              }
              if(counter == timplies.size()){
                return false;
              }
            }else{
              if(!subT1.getArguments().isEmpty() && !subT2.getArguments().isEmpty()){
                if(!canT1ImpliesT2(new FactWithType(t1.getvType(),subT1), new FactWithType(t2.getvType(),subT2), timplies, UserDefType)){
                  return false;
                }
              }else{
                if(!subT1.getFactName().equals(subT1.getFactName()))return false;
              }
            }					
          }else{ 
            if(!UserDefType.get(subT1.getVarName()).containsAll(UserDefType.get(subT2.getVarName()))) return false;
          }					
        }
      }else{
        return false;
      }		
    }else{
      return false;
    }
    return true;
  }

  private HashMap<String,String> getSubstitutionMap(Term val1, Term val2){
    HashMap<String,String> map = new HashMap<String,String>();
    if(isTwoValHaveSameForm(val1,val2) && (val1.getArguments().size() == val2.getArguments().size())){
      for(int i=0;i<val1.getArguments().size();i++){
        if(!val1.getArguments().get(i).getFactName().equals("0")){
          for(int j=0;j<val1.getArguments().get(i).getArguments().size();j++){
            String var1 = val1.getArguments().get(i).getArguments().get(j).getVarName();
            String var2 = val2.getArguments().get(i).getArguments().get(j).getVarName();
            map.put(var1, var2);
          }
        }
      }
    }
    return map;
  }
	
  private boolean isVal1GeneralThanVal2(Term val1, Term val2, HashMap<String,List<String>> UserDefType){
    if(val1.getFactName().equals("val") && val2.getFactName().equals("val")){
      for(int i=0;i<val1.getArguments().size();i++){
        Term val1Arg = val1.getArguments().get(i);
        Term val2Arg = val2.getArguments().get(i);
        if(!(val1Arg.getFactName().equals("_") || val2Arg.getFactName().equals("_"))){ // need test
          if(!val1Arg.equals(val2Arg)){
            if(val1Arg.getFactName().equals(val2Arg.getFactName())){
              for(int j=0;j<val1Arg.getArguments().size();j++){
                if(UserDefType.containsKey(val1Arg.getArguments().get(j).getVarName()) && UserDefType.containsKey(val2Arg.getArguments().get(j).getVarName())){
                  if(!UserDefType.get(val1Arg.getArguments().get(j).getVarName()).containsAll(UserDefType.get(val2Arg.getArguments().get(j).getVarName()))){
                    return false;
                   }
                  }else{
                    return false;
                  }
              }
            }else{
              return false;
            }
          }
        }
      }
    }else{
      return false;
    }
    return true;
  }
	
  public boolean isTwoValHaveSameForm(Term t1, Term t2){
    if(!(t1 instanceof Composed) || !(t2 instanceof Composed) || (t1.getArguments().size() != t2.getArguments().size())){
      return false;
    }else {
      for(int i=0;i<t1.getArguments().size();i++){
        if(!(t1.getArguments().get(i).getFactName().equals("_") || t2.getArguments().get(i).getFactName().equals("_"))){ // need testing
          if(!t1.getArguments().get(i).getFactName().equals(t2.getArguments().get(i).getFactName())){
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
      if(t1.getArguments().isEmpty()){
        if((t2 instanceof Composed) && t2.getArguments().isEmpty())return true;
        else return false;
      }
      if(!(t2 instanceof Composed)) return false;
      if(((Composed)t1).getFactName().equals(((Composed)t2).getFactName()) && t1.getArguments().size() == t2.getArguments().size()){
        if(((Composed)t1).getFactName().equals("val")){
          if(t1.getArguments().size() == t2.getArguments().size())return true;
          else return false;
        }else{
          for(int i=0;i<t1.getArguments().size();i++){
            if((t1.getArguments().get(i) instanceof Composed) && (t2.getArguments().get(i) instanceof Composed)){
              if(t1.getArguments().get(i).getFactName().equals("val") && t2.getArguments().get(i).getFactName().equals("val")){
                if(t1.getArguments().get(i).getArguments().size() == t2.getArguments().get(i).getArguments().size())return true;
                else return false;
              }
            }
            if(!isTwoFactsHaveSameForm(t1.getArguments().get(i),t2.getArguments().get(i)))return false;
          }
        }
      }else{
        return false;
      }		
    }
    return true;
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
  
  public HashMap<String,Timplies> getTimpliesFromConcreteRule(ConcreteRule concreteRule, HashMap<String, Integer> setPosition){
    HashMap<String,Timplies> timpliesMap = new HashMap<>();
    List<Condition> consitionLeft = new ArrayList<>(concreteRule.getSplus());
    consitionLeft.addAll(concreteRule.getSnega());
    List<Condition> consitionRS = new ArrayList<>(concreteRule.getRS());
    for(Condition cp : concreteRule.getSplus()){
      if(!consitionRS.contains(cp)){
        consitionRS.add(new Condition(cp.getVar(),cp.getTerm(),false));
      }
    }
    consitionRS.addAll(concreteRule.getSnega());
    for(Condition crs : concreteRule.getRS()){
      for(Condition cn : concreteRule.getSnega()){
        if(crs.getVar().equals(cn.getVar()) && crs.getTerm().getFactName().equals(cn.getTerm().getFactName())){
          consitionRS.remove(cn);
        }
      }
    }
    HashSet<Term> vars = new HashSet<>();
    for(Condition cl : consitionLeft){
      vars.add(cl.getVar());
    }
    if(!concreteRule.getNewFreshVars().getFreshs().isEmpty()){ // need improve later
      vars.addAll(concreteRule.getNewFreshVars().getFreshs());
    }
    if(!concreteRule.getRF().contains(new Composed("attack"))){
      for(Term var : vars){
        Composed val1 = new Composed("val");
        Composed val2 = new Composed("val");
        Composed zero = new Composed("0");
        Composed _ = new Composed("_");
        if(concreteRule.getNewFreshVars().getFreshs().contains(var)){
          for(int j=0;j<setPosition.size();j++){ // val initialize to val(0,0,0)
            val1.setArguments(zero);
            val2.setArguments(zero);
          }
        }else{
          for(int j=0;j<setPosition.size();j++){ // val initialize to val(_,_,_)
            val1.setArguments(_);
            val2.setArguments(_);
          }
        }
  		
        for(Condition c1 : consitionLeft){
          if(c1.getVar().equals(var)){
            int position = setPosition.get(c1.getTerm().getFactName());
            if(c1.positive){
              val1.getArguments().set(position, c1.getTerm());
            }else{
              val1.getArguments().set(position, zero);
            }
          }
        }
        for(Condition c2 : consitionRS){
          if(c2.getVar().equals(var)){
            int position = setPosition.get(c2.getTerm().getFactName());
            if(c2.positive){
              val2.getArguments().set(position, c2.getTerm());
            }else{
              val2.getArguments().set(position, zero);
            }
          }
        }
        ArrayList<Term> arguments = new ArrayList<>();
        arguments.add(val1);
        arguments.add(val2);
        if(concreteRule.getNewFreshVars().getFreshs().contains(var)){
          timpliesMap.put(var.toString(),new Timplies(false,new Composed("timplies",arguments)));
        }else{
          timpliesMap.put(var.toString(),new Timplies(new Composed("timplies",arguments)));
        }
      }
    }	
    return timpliesMap;
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
    absRule.setTimplies(getTimpliesFromConcreteRule(conRule,setPosition)); 
    return absRule;
  }
  
  public AbstractRule absRuleSubstitution(AbstractRule absRule){
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
  	
    HashMap<String,Timplies> timplies = new HashMap<>();
    //for(Term timplie : absRule.getTimplies()){
    //	timplies.add(termSubs(timplie,absRule.getVarsTypes()));
    //}
    for(Map.Entry<String, Timplies> timplie : absRule.getTimplies().entrySet()){
      if(timplie.getValue().isTimplies()){
        timplies.put(timplie.getKey().toString(), new Timplies(termSubs(timplie.getValue().getTimplies(),absRule.getVarsTypes())));
      }else{
        timplies.put(timplie.getKey().toString(), new Timplies(false,termSubs(timplie.getValue().getTimplies(),absRule.getVarsTypes())));
      }
    }
    absRuleSubstituted.setTimplies(timplies);
    return absRuleSubstituted;
  }  
  /**
   * Returns all possible combination of contrate type. 
   * @param  typeInfo e.g. {A=[a, i], S=[s]}
   * @return e.g. [{A=i, S=s}, {A=a, S=s}]
   */
  public Set<HashMap<String,FactWithType>> userTypeSubstitution(HashMap<String, List<FactWithType>> typeInfo){
    Set<HashMap<String,FactWithType>> combinations = new HashSet<>();
    if(!typeInfo.isEmpty()){
      Set<HashMap<String,FactWithType>> newCombinations;
      Set<String> keySet = typeInfo.keySet();
      List<String> keyList = new ArrayList<>(keySet);
      int index = 0; 
      if(!keyList.isEmpty() && typeInfo.containsKey(keyList.get(0))){
      	for(FactWithType i: typeInfo.get(keyList.get(0))) {
          HashMap<String,FactWithType> newMap = new HashMap<>();
          newMap.put(keyList.get(0), i);
          combinations.add(newMap);
        }
      }
      index++;
      while(index < keyList.size()) {
        List<FactWithType> nextList = typeInfo.get(keyList.get(index));
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
  
  public List<AbstractRule> getAllSatisfiedAbsRules(ConcreteRule concreteRule, AbstractRule absRule, ArrayList<HashMap<String,FactWithType>> satisfiedKeys,HashMap<String,List<String>> UserDefType){
    List<AbstractRule> satisfiedAbsRules = new ArrayList<>();
  	
    HashMap<String, Term> tempMap = new HashMap<>();
    HashMap<String, Term> userType = new HashMap<>();
    HashMap<String, HashMap<String,String>> newVarTypes = new HashMap<>();
    for(HashMap<String,FactWithType> key : satisfiedKeys){
      for(Map.Entry<String, FactWithType> userDefType : key.entrySet()){
        HashMap<String, String> newTypes = new HashMap<>();
        for(Map.Entry<String, String> types : userDefType.getValue().getvType().entrySet()){
          String k = "";
          if(types.getKey().contains("_")){
            k = types.getKey().substring(0,types.getKey().indexOf("_"));
          }else{
            k = types.getKey();
          }
          newTypes.put(k, types.getValue());
          if(!userType.containsKey(k)){
            userType.put(k, new Variable(types.getValue()));
          }else{
            if(UserDefType.containsKey(userType.get(k).getVarName()) && UserDefType.containsKey(types.getValue())){
              if(!UserDefType.get((userType.get(k)).getVarName()).containsAll(UserDefType.get(types.getValue()))){
                userType.put(k, new Variable(types.getValue()));
                }
              }			
            }
          }
        newVarTypes.put(userDefType.getKey(), newTypes);
        }
      for(Map.Entry<String, FactWithType> s : key.entrySet()){
        tempMap.put(s.getKey(), s.getValue().getTerm());
      }
      for(Map.Entry<String, Term> uType : userType.entrySet()){
        if(UserDefType.containsKey(userType.get(uType.getKey()).getVarName()) && UserDefType.containsKey(uType.getValue().getVarName())){
          if(!UserDefType.get(userType.get(uType.getKey()).getVarName()).containsAll(UserDefType.get(uType.getValue().getVarName()))){
            tempMap.put(uType.getKey(), uType.getValue());
          }
        }  
      } 
    }
    AbstractRule absRule_copy = (AbstractRule)dClone.deepClone(absRule);
    for(int i=0;i<absRule.getLF().size();i++){
      absRule_copy.getLF().set(i, ST.termSubs(concreteRule.getLF().get(i), tempMap));
    }
		
    HashMap<String,Term> newTimpliesMap = getTimpliesMap(tempMap,absRule.getTimplies());
    HashMap<String,Term> newTimpliesMapSubstituted = new HashMap<>(); 
		
    for(Map.Entry<String, Term> TimpliesMap : newTimpliesMap.entrySet()){
      newTimpliesMapSubstituted.put(TimpliesMap.getKey(), termSubs(TimpliesMap.getValue(),newVarTypes.get(TimpliesMap.getKey())));
    }
    //newTimpliesMapSubstituted.putAll(userType);
    for(int j=0;j<absRule.getRF().size();j++){
      absRule_copy.getRF().set(j, ST.termSubs(concreteRule.getRF().get(j),newTimpliesMapSubstituted));
    }	
    for(Map.Entry<String, Timplies> timplie : absRule_copy.getTimplies().entrySet()){
      timplie.getValue().getTimplies().getArguments().set(0, tempMap.get(timplie.getKey()));
      timplie.getValue().getTimplies().getArguments().set(1, newTimpliesMapSubstituted.get(timplie.getKey()));		
    }
    satisfiedAbsRules.add(absRule_copy); 		
    return satisfiedAbsRules;
  }
  
  public HashMap<String,Term> getTimpliesMap(HashMap<String,Term> keyMap,HashMap<String, Timplies> timplies){
    HashMap<String,Term> newTimpliesMap = new HashMap<>();
    for(Map.Entry<String, Term> key : keyMap.entrySet()){
      if(timplies.containsKey(key.getKey())){
        Term newVal2 = (Term)dClone.deepClone(timplies.get(key.getKey()).getTimplies().getArguments().get(1));
        for(int i=0;i<newVal2.getArguments().size();i++){
          if(newVal2.getArguments().get(i).getFactName().equals("_")){
            newVal2.getArguments().set(i, key.getValue().getArguments().get(i));
          }
        }
        newTimpliesMap.put(key.getKey(), newVal2);
      }
    }
    return newTimpliesMap;
  }
  
  public List<FactWithType> keysWithoutDuplicate(List<FactWithType> key, List<FactWithType> timplies, HashMap<String,List<String>> UserDefType){
    List<FactWithType> noDuplicatekeys = new ArrayList<FactWithType>();
    List<FactWithType> keys = new ArrayList<>(key);
    if(keys.size() == 1){
      noDuplicatekeys.addAll(keys);
    }else{				
      FactWithType lastElementFlag = new FactWithType(new HashMap<String, String>(), new Variable("Flag"));
      keys.add(lastElementFlag);
      ArrayList<FactWithType> keysCopy = new ArrayList<FactWithType>(keys);
      while(true){
        if(keys.get(0).equals(lastElementFlag)) break;
        FactWithType firstTerm = keys.get(0);				
        for(FactWithType t : keys){
          if(canVal1ImpliesVal2(firstTerm.getTerm(),t.getTerm(),timplies, UserDefType)){
            keysCopy.remove(t);
          }
        }
        keysCopy.add(firstTerm);
        keys.clear();
        keys.addAll(keysCopy);		
      }		
      keys.remove(lastElementFlag);
      noDuplicatekeys.addAll(keys);
    }
    return noDuplicatekeys; 
  }
  
  public boolean canVal1ImpliesVal2(Term val1, Term val2,List<FactWithType> timplies, HashMap<String,List<String>> UserDefType){
    if(val1.equals(val2)){
      return true;
    } else if((val1 instanceof Composed) && (val2 instanceof Composed) && val1.getFactName().equals("val") 
        && val2.getFactName().equals("val") && val1.getArguments().size() == val2.getArguments().size()){			
      for(FactWithType timp : timplies){
        if(isVal1GeneralThanVal2(timp.getTerm().getArguments().get(0),val1,UserDefType)){
          HashMap<String,String> typeMap = getSubstitutionMap(timp.getTerm().getArguments().get(0),val1);
          Term impliesFromVal1 = termSubs(timp.getTerm().getArguments().get(1),typeMap);										
          if(isVal1GeneralThanVal2(impliesFromVal1,val2,UserDefType)){
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Returns a map, mapping variable to val(...) 
   * @param  concreteTerm    e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @param  abstractTerm    e.g. A:Honest.S:Server.iknows(sign(inv(val(ring(A),db_valid(S,A))),pair(A,val(ring(A),0,0))))    
   * @return e.g. {PK=A:Honest.S:Server.val(ring(A),db_valid(S,A)), NPK=A:Honest.S:Server.val(ring(A),0,0)} 
   */
  private HashMap<String,FactWithType> getKeyMap(Term concreteTerm, FactWithType abstractTerm){
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
   
  public List<FactWithType> applyAbsRuleWithSatisfiedFacts(AST aifAST, AST fpAST, String conRuleName,HashMap<String,List<String>> UserDefType){
    List<FactWithType> newGenerateFacts = new ArrayList<>();
    ConcreteRule conRule = getConcreteRuleByRuleName(aifAST,conRuleName);
    AbstractRule absrule = concreteRuleToAbsRuleConversion(aifAST,conRuleName);
    AbstractRule absruleSubstituted = absRuleSubstitution(absrule); 
		
    List<ArrayList<FactWithType>> fixedpointsSorted = fixedpointsSort(fpAST);
    List<FactWithType> timplies = getExtendedTimplies(fpAST,UserDefType);
    //List<FactWithType> sortedTimplies = timpliesSort(timplies);
    List<ArrayList<FactWithType>> fixpointWithoutDuplicate = fixedpointsWithoutDuplicate(fixedpointsSorted, timplies, UserDefType);
		
    List<ArrayList<FactWithType>> satisfiedFormFacts = findSatisfyFormFacts(absruleSubstituted, fixpointWithoutDuplicate);
    List<ArrayList<FactWithType>> satisfiedFacts = getSatisfyFacts(absruleSubstituted,satisfiedFormFacts,timplies,UserDefType);
		
    List<ArrayList<HashMap<String,FactWithType>>> keyMapToValList = new ArrayList<>();
    List<ArrayList<HashMap<String,FactWithType>>> concreteKeyMapList = new ArrayList<>();
    for(int i=0;i<conRule.getLF().size();i++){
      ArrayList<HashMap<String,FactWithType>> keyMapToVal = new ArrayList<>();
      ArrayList<HashMap<String,FactWithType>> concreteKeyMapToVal = new ArrayList<>();
      HashMap<String,FactWithType> concreteKeyMap = getKeyMap(conRule.getLF().get(i), new FactWithType(absruleSubstituted.getVarsTypes(),absruleSubstituted.getLF().get(i)));
      concreteKeyMapToVal.add(concreteKeyMap);
      for(FactWithType fact : satisfiedFacts.get(i)){
        HashMap<String,FactWithType> keyMap = getKeyMap(conRule.getLF().get(i), new FactWithType(fact.getvType(),fact.getTerm()));
        keyMapToVal.add(keyMap);
      }
      keyMapToValList.add(keyMapToVal);
      concreteKeyMapList.add(concreteKeyMapToVal);
    }
    int keyMapToValListSize = keyMapToValList.size();
    for(int i=0;i<keyMapToValListSize;i++){
      int keyMapSize = keyMapToValList.get(i).size();
      for(int j=0;j<keyMapSize;j++){
        HashMap<String,ArrayList<FactWithType>> derivedKeyMap = getDerivedKeyMap(keyMapToValList.get(i).get(j), timplies,UserDefType);
        for(Map.Entry<String,ArrayList<FactWithType>> entity : derivedKeyMap.entrySet()){
          entity.getValue().add(new FactWithType(keyMapToValList.get(i).get(j).get(entity.getKey()).getvType(),keyMapToValList.get(i).get(j).get(entity.getKey()).getTerm()));
        }
				
        boolean emptyKeyFlag = false;
        for(Map.Entry<String,ArrayList<FactWithType>> entity : derivedKeyMap.entrySet()){
          if(entity.getValue().isEmpty()){
            emptyKeyFlag = true;
          }
        }
        if(!emptyKeyFlag){
          ArrayList<HashMap<String,FactWithType>> newDerivedkeyMapCombination = new ArrayList<>(getDerivedCombinationKeyMap(derivedKeyMap));
          for(HashMap<String,FactWithType> gg : newDerivedkeyMapCombination){
            if(!keyMapToValList.get(i).contains(gg)){  //only add new keys
              keyMapToValList.get(i).add(gg);
            }
          }
        }
      }			
    }
		
    List<ArrayList<HashMap<String,FactWithType>>>  satisfiedKeys = removeUnsatisfyKeys(keyMapToValList,concreteKeyMapList,UserDefType);	
    List<ArrayList<HashMap<String,FactWithType>>> keyMapCombination = new ArrayList<>(getCombinationKeyMap(satisfiedKeys));
		
    HashMap<String, HashSet<Term>> satisfiedKeysMap = new HashMap<>();
		
		
    // only for display
    HashMap<String, ArrayList<FactWithType>> allPossibleKeys = new HashMap<>();
    for(ArrayList<FactWithType> facts : satisfiedFormFacts){
      for(FactWithType fact : facts){
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
            terms.add(entity.getValue().getTerm());
            satisfiedKeysMap.put(entity.getKey(), terms);
          }else{
            satisfiedKeysMap.get(entity.getKey()).add(entity.getValue().getTerm());
          }
        }
      }
    }
    System.out.println(absruleSubstituted);
    System.out.println();
    System.out.println("All possible keys:");
    for(Map.Entry<String,ArrayList<FactWithType>> entity : allPossibleKeys.entrySet()){
      System.out.print(entity.getKey() + " --> {");
      for(int i=0;i<entity.getValue().size();i++){
        if(i < entity.getValue().size()-1){
          System.out.print(entity.getValue().get(i).getTerm().toString() + ",");
        }else{
          System.out.println(entity.getValue().get(i).getTerm().toString() + "}");
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
    /*
    for(ArrayList<HashMap<String,FactWithType>> keys : keyMapCombination){
      for(HashMap<String,FactWithType> keymap : keys){
        for(Map.Entry<String,FactWithType> entity : keymap.entrySet()){
          System.out.println(entity.getKey() + " --> " + entity.getValue().getTerm());
        }
        System.out.println();
     }
    }
     */	

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
			
    List<AbstractRule> abstractRules = new ArrayList<>();
    for(ArrayList<HashMap<String,FactWithType>> keyMaps : keyMapCombination){			
      abstractRules.addAll(getAllSatisfiedAbsRules(conRule,absrule,keyMaps,UserDefType));
    }		
    if(!keyMapCombination.isEmpty()){
      for(AbstractRule absR : abstractRules){
        HashMap<String,String> varsType = absR.getVarsTypes();
        for(Term rf : absR.getRF()){
          newGenerateFacts.add(new FactWithType(varsType,rf));
        }
      }
    }else{
      HashMap<String,String> varsType = absrule.getVarsTypes();
      for(Term rf : absrule.getRF()){
        newGenerateFacts.add(new FactWithType(varsType,rf));
      }
    }	
    return newGenerateFacts;
  }
   
  public List<ArrayList<HashMap<String,FactWithType>>> removeUnsatisfyKeys(List<ArrayList<HashMap<String,FactWithType>>> keyList,List<ArrayList<HashMap<String,FactWithType>>> keys,HashMap<String,List<String>> UserDefType){
    List<ArrayList<HashMap<String,FactWithType>>> satisfiedKeys = (List<ArrayList<HashMap<String,FactWithType>>>)dClone.deepClone(keyList);
    for(int i=0;i<keyList.size();i++){
      for(int j=0;j<keyList.get(i).size();j++){
        for(Map.Entry<String,FactWithType> entity : keys.get(i).get(0).entrySet()){// need more test here
          if(!(isVal1GeneralThanVal2(keyList.get(i).get(j).get(entity.getKey()).getTerm(),entity.getValue().getTerm(),UserDefType)
               || isVal1GeneralThanVal2(entity.getValue().getTerm(),keyList.get(i).get(j).get(entity.getKey()).getTerm(),UserDefType))){
            satisfiedKeys.get(i).remove(keyList.get(i).get(j));
          }
        }
      }
    }  	
    return satisfiedKeys;
  }
  
  public HashMap<String,ArrayList<FactWithType>> getDerivedKeyMap(HashMap<String,FactWithType> keyMap,List<FactWithType> timplies,HashMap<String,List<String>> UserDefType){
    HashMap<String,ArrayList<FactWithType>> impliedkeyMap = new HashMap<>();	
    for(Map.Entry<String,FactWithType> entity : keyMap.entrySet()){
      ArrayList<FactWithType> impliedKey = new ArrayList<>();
      for(int i=0;i<timplies.size();i++){
        Term timplieVal1 = timplies.get(i).getTerm().getArguments().get(0);
        Term timplieVal2 = timplies.get(i).getTerm().getArguments().get(1);
        if(isVal1GeneralThanVal2(timplieVal1,entity.getValue().getTerm(),UserDefType)){
          impliedKey.add(new FactWithType(timplies.get(i).getvType(),timplieVal2));
        }
      }
      impliedkeyMap.put(entity.getKey(), impliedKey);
    }
    return impliedkeyMap;
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
  
  
  public List<ArrayList<FactWithType>> findSatisfyFormFacts(AbstractRule absRule, List<ArrayList<FactWithType>> factsSorted){
    List<ArrayList<FactWithType>> satisfiedFacts = new ArrayList<>();
    for(Term lf : absRule.getLF()){
      ArrayList<FactWithType> factHasSameForm = new ArrayList<>();
      for(ArrayList<FactWithType> factList : factsSorted){
        if(!factList.isEmpty() && isTwoFactsHaveSameForm(lf,factList.get(0).getTerm())){
          factHasSameForm.addAll(factList);
          break;
        }
      }
      satisfiedFacts.add(factHasSameForm);
    } 	
    return satisfiedFacts;
  }
  
  public List<ArrayList<FactWithType>> getSatisfyFacts(AbstractRule absRule, List<ArrayList<FactWithType>> facts,List<FactWithType> timplies,HashMap<String,List<String>> UserDefType){
    List<ArrayList<FactWithType>> satisfiedFacts = (List<ArrayList<FactWithType>>)dClone.deepClone(facts);
    for(int i=0;i<facts.size();i++){
      for(FactWithType fact : facts.get(i)){
        if(!isFactSatisfied(fact, new FactWithType(absRule.getVarsTypes() ,absRule.getLF().get(i)),timplies,UserDefType)){
          satisfiedFacts.get(i).remove(fact);
        }
      }
    }
    return satisfiedFacts;
  }
  
  public boolean isFactSatisfied(FactWithType fact, FactWithType FactForm,List<FactWithType> timplies, HashMap<String,List<String>> UserDefType){
    if(!isTwoFactsHaveSameForm(fact.getTerm(),FactForm.getTerm())){
      return false;
    }else if(isT1SatisfyT2(fact.getTerm(),FactForm.getTerm(),UserDefType)){
      return true;
    }else{
      if(canT1ImpliesT2Copy(fact,FactForm,timplies,UserDefType)){  // need test
        return true;
      }
    }
    return false;
  }

  public boolean isT1SatisfyT2(Term t1, Term t2, HashMap<String,List<String>> UserDefType){
    if(!isTwoFactsHaveSameForm(t1,t2)){
      return false;
    }else{
      if(t1 instanceof Composed){
        if(t1.getFactName().equals("val")){
          if(!((isVal1GeneralThanVal2(t1,t2,UserDefType)) || isVal1GeneralThanVal2(t2,t1,UserDefType))){  // need more test here
            return false;
          }
        }else{
          if(!t1.getArguments().isEmpty()){
            for(int i=0;i<t1.getArguments().size();i++){
              if(!isT1SatisfyT2(t1.getArguments().get(i),t2.getArguments().get(i),UserDefType)){
                return false;
              }
            }
          }else{
            if(!t1.getFactName().equals(t2.getFactName())){ // constant
              return false;
            }
          }
        }
      }else{  // it's variable
        if(!UserDefType.get(t2.getVarName()).containsAll((UserDefType.get(t1.getVarName())))){
          return false;
        }
      }
    }
    return true;
  }

  public List<FactWithType> applyAbstractRule(AbstractRule absRule, List<ArrayList<FactWithType>> fixedpointsSorted,HashMap<String,List<String>> UserDefType){
    List<FactWithType> newGegarateFixedpoints = new ArrayList<>();
    List<ArrayList<FactWithType>> satisfiedFixedpoints = new ArrayList<>();
    for(Term lf : absRule.getLF()){
      ArrayList<FactWithType> fixedpointsSatisfy = new ArrayList<>();
      for(ArrayList<FactWithType> sortFixedPoints : fixedpointsSorted){
        for(FactWithType fixedpoint : sortFixedPoints){
          if(isT1SatisfyT2(fixedpoint.getTerm(), lf, UserDefType)){
            fixedpointsSatisfy.add(fixedpoint);
          }
        }
        if(!fixedpointsSatisfy.isEmpty()){
          break;
        }
      }
      satisfiedFixedpoints.add(fixedpointsSatisfy);
    }
  	
    for(ArrayList<FactWithType> flist : satisfiedFixedpoints){
      for(FactWithType f : flist){
        System.out.println(f.getTerm().toString());
      }
    }
    return newGegarateFixedpoints;
  }
  
  
  public boolean canT1ImpliesT2Copy(FactWithType t1, FactWithType t2, List<FactWithType> timplies, HashMap<String,List<String>> UserDefType){ // need more test more cases
    if(t1.equals(t2)){
      return true;
    }
    else if((t1.getTerm() instanceof Composed) && (t2.getTerm() instanceof Composed)){
      if(t1.getTerm().getFactName().equals(t2.getTerm().getFactName()) && t1.getTerm().getArguments().size() == t2.getTerm().getArguments().size()){
        int argumentsSize = t1.getTerm().getArguments().size();
        for(int i=0;i<argumentsSize;i++){
          Term subT1 = t1.getTerm().getArguments().get(i);
          Term subT2 = t2.getTerm().getArguments().get(i);
          if((subT1 instanceof Composed) && (subT2 instanceof Composed)){
            if((subT1.getFactName().equals("val")) && (subT2.getFactName().equals("val"))){
              int counter = 0;
              for(FactWithType timp : timplies){
                counter ++;
                if(isTwoValHaveSameForm(subT1, timp.getTerm().getArguments().get(0)) && isTwoValHaveSameForm(subT2, timp.getTerm().getArguments().get(1))){
                  //System.out.println(timp.getTerm().getArguments().get(0));
                  //System.out.println(subT1);
                  if(isVal1GeneralThanVal2(timp.getTerm().getArguments().get(0),subT1,UserDefType)){
                    HashMap<String,String> typeMap = getSubstitutionMap(timp.getTerm().getArguments().get(0),subT1);
                    Term impliesFromT1 = termSubs(timp.getTerm().getArguments().get(1),typeMap);										
                    if(isVal1GeneralThanVal2(subT2,impliesFromT1,UserDefType)){
                      counter --;
                    }else{
                      return false;
                    }
                  }else{
                    return false;
                  }
                }
              }
              if(counter == timplies.size()){
                return false;
              }
            }else{
              if(!subT1.getArguments().isEmpty() && !subT2.getArguments().isEmpty()){
                if(!canT1ImpliesT2Copy(new FactWithType(t1.getvType(),subT1), new FactWithType(t2.getvType(),subT2), timplies, UserDefType)){
                  return false;
                }
              }else{
                if(!subT1.getFactName().equals(subT1.getFactName())){
                  return false;
                }
              }
            }					
          }else{
            if(!UserDefType.get(subT2.getVarName()).containsAll(UserDefType.get(subT1.getVarName()))){
              return false;
            }
          }					
        }
      }else{
        return false;
      }		
    }else{
      return false;
    }
    return true;
  }
  
  /**
   * Returns all possible combination of contrate type.  // need update
   * @param  typeInfo e.g. {A=[a, i], S=[s]}
   * @return e.g. [{A=i, S=s}, {A=a, S=s}]
   */
  public Set<HashMap<String,FactWithType>> getDerivedCombinationKeyMap(HashMap<String, ArrayList<FactWithType>> keyMap){
    Set<HashMap<String,FactWithType>> combinations = new HashSet<>();
    if(!keyMap.isEmpty()){
      Set<HashMap<String,FactWithType>> newCombinations;
      Set<String> keySet = keyMap.keySet();
      List<String> keyList = new ArrayList<>(keySet);
      int index = 0; 
      if(!keyList.isEmpty() && keyMap.containsKey(keyList.get(0))){
      	for(FactWithType i: keyMap.get(keyList.get(0))) {
          HashMap<String,FactWithType> newMap = new HashMap<>();
          newMap.put(keyList.get(0), i);
          combinations.add(newMap);
        }
      }
      index++;
      while(index < keyList.size()) {
        List<FactWithType> nextList = keyMap.get(keyList.get(index));
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
  
  public ConcreteFact absFactToConcreteFact(FactWithType absFact, ConcreteRule conRule){
    ConcreteFact conFact = new ConcreteFact();
    HashMap<Term,Variable> keyMap = new HashMap<>();
    Mgu mgu = new Mgu();
    for(Term rf : conRule.getRF()){
      try{
        Substitution vSub = mgu.mgu(rf, absFact.getTerm(), new Substitution());
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
          conFact.setFact(absTermToConcreteTermSubs(absFact.getTerm(),keyMap));
          conFact.setRS(rs);
        }
      }catch(UnificationFailedException e){}
      
    }
    return conFact;
  }
  
  /**
   * Returns a substituted term.   // need update
   * only substitute the variables if it occurs in the map subs
   * @param  t    e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @param  subs e.g. {PK=pk, A=a}
   * @return e.g. iknows(sign(inv(pk),pair(a,NPK)))
   */
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

}
