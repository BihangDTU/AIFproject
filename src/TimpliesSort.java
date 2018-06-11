import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myException.UnificationFailedException;

import dataStructure.*;

public class TimpliesSort {
  StateTransition st = new StateTransition();  // need move to new class
  DeepClone dClone = new DeepClone();  // need move to new class
  StateTransition ST = new StateTransition();
  Mgu mgu = new Mgu();
  public TimpliesSort(){}
	
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
          //if(isTwoValsHaveSameForm(t,fp.getTerm().getArguments().get(0))){
            if(isVal1GeneralThanVal2(fp.getTerm().getArguments().get(0),t,UserDefType)){
              ArrayList<Term> vals = new ArrayList<>();
              vals.add(ts.getKey());
              vals.add(fp.getTerm().getArguments().get(1));
              extendedTimplies.add(new FactWithType(fp.getvType(), new Composed("timplies",vals)));
            }
          //}
        }
      }
    }
    extendedTimplies.addAll(timplies);
    List<FactWithType> extendedTimpliesList = new ArrayList<>(extendedTimplies);
    
    for(FactWithType fact : extendedTimpliesList){
      HashMap<String, String> vType = new HashMap<>();
      for(Map.Entry<String, String> entity : fact.getvType().entrySet()){
        vType.put(entity.getValue(), entity.getKey());
      }
      fact.setTerm(termSubs(fact.getTerm(),vType));
    }
      
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
      List<String> ran_vType = new ArrayList<>();
      for(Map.Entry<String, String> entity : vType.entrySet()){
        ran_vType.add(entity.getValue());
      }
      for(FactWithType t : unClassifyTimplies){
        List<String> ran_t = new ArrayList<>();
        for(Map.Entry<String, String> entity : t.getvType().entrySet()){
          ran_t.add(entity.getValue());
        }
        //if(ran_vType.containsAll(ran_t) && ran_vType.size() == ran_t.size()){
        if(ran_vType.containsAll(ran_t)){
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
          if(isTwoValsHaveSameForm(firstTimplies.getTerm().getArguments().get(0),t.getTerm().getArguments().get(0))){
          //if(mgu.isT1GreatOrEqualT2(new FactWithType(firstTimplies.getvType(),firstTimplies.getTerm().getArguments().get(0)), new FactWithType(t.getvType(),t.getTerm().getArguments().get(0)), UserDefType)
          //    || mgu.isT1GreatOrEqualT2(new FactWithType(t.getvType(),t.getTerm().getArguments().get(0)),new FactWithType(firstTimplies.getvType(),firstTimplies.getTerm().getArguments().get(0)), UserDefType)){
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
   * Display timplies in orders. 
   * @param  classifiedTimplies   expended timplies have been classified into different list according to types
   * @param  timplies             list of timplies with types from fixed points file.
   */
  public void printKeyLifeCycle(List<List<FactWithType>> classifiedTimplies,List<FactWithType> timplieslist){
    List<List<ArrayList<FactWithType>>> classifiedTimpliesSorted = new ArrayList<>();
    List<List<FactWithType>> classifiedTimpliesCopy = (List<List<FactWithType>>)dClone.deepClone(classifiedTimplies);
    List<FactWithType> timplies = (List<FactWithType>)dClone.deepClone(timplieslist);
    
    for(List<FactWithType> cts : classifiedTimpliesCopy){
      for(FactWithType ct : cts){
        ct.setTerm(termSubs(ct.getTerm(),ct.getvType()));
      }
    }
    for(FactWithType timplie : timplies){
      timplie.setTerm(termSubs(timplie.getTerm(),timplie.getvType()));
    }
    
    for(List<FactWithType> tList : classifiedTimpliesCopy){
      classifiedTimpliesSorted.add(timpliesSort(tList));
    }
 
    for(List<ArrayList<FactWithType>> t : classifiedTimpliesSorted){
      Collections.sort(t, new SortbyListSize());
    }

    List<FactWithType> timplieCopy = new ArrayList<>(timplies);
    //List<FactWithType> timpliesOrdered = new ArrayList<>();
    for(List<ArrayList<FactWithType>> tss : classifiedTimpliesSorted){
      for(ArrayList<FactWithType> ts : tss){     
        for(FactWithType t : ts){
          Term firstVal = t.getTerm().getArguments().get(0);
          Term secondVal = t.getTerm().getArguments().get(1);
          for(FactWithType timplie : timplies){
            if(firstVal.equals(timplie.getTerm().getArguments().get(0)) && secondVal.equals(timplie.getTerm().getArguments().get(1))){
              //timpliesOrdered.add(timplie);
              System.out.println(timplie.getTerm().getArguments().get(0) + " -->> " + timplie.getTerm().getArguments().get(1));
              timplieCopy.remove(timplie);
            } 
          }
          timplies.clear();
          timplies.addAll(timplieCopy);
        }
      }
    }
  
  }

  public boolean isTwoValsHaveSameForm(Term t1, Term t2){
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
   * Return true if val1 is general than val2, otherwise return false.
   * @param  val1  key set membership e.g. val(...)
   * @param  val2  key set membership e.g. val(...)
   * @param  UserDefType   user define types, using for compare types
   * @return boolean
   */
  private boolean isVal1GeneralThanVal2(Term val1, Term val2, HashMap<String,List<String>> UserDefType){
    if(!val1.getFactName().equals("val") || !val2.getFactName().equals("val")) return false;
    for(int i=0;i<val1.getArguments().size();i++){
      Term val1Arg = val1.getArguments().get(i);
      Term val2Arg = val2.getArguments().get(i);
      if(!(val1Arg.getFactName().equals("_") || val2Arg.getFactName().equals("_"))){ // need test
        if(!val1Arg.equals(val2Arg)){
          if(!val1Arg.getFactName().equals(val2Arg.getFactName())) return false;
          for(int j=0;j<val1Arg.getArguments().size();j++){
            String varType1 = val1Arg.getArguments().get(j).getVarName();
            String varType2 = val2Arg.getArguments().get(j).getVarName();
            if(UserDefType.containsKey(varType1) && UserDefType.containsKey(varType2)){
              if(!UserDefType.get(varType1).containsAll(UserDefType.get(varType2))){
                return false;
              }
            }else{
              return false;
            }
          }
        }
      }
    }
    return true;
  }
}
