import dataStructure.*;
import myException.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class Mgu {
	public Mgu(){}
	DeepClone dClone = new DeepClone();
	GlobalCounter gc = new GlobalCounter();
	
  public ArrayList<String> vars(Term term){    
    ArrayList<String> var = new ArrayList<>();
    if(term instanceof Variable){
      var.add(((Variable)term).getVarName());
      return var;
    }else if(term instanceof Composed){
      for(int i=0; i < term.getArguments().size(); i++){
        if(term.getArguments().get(i) instanceof Variable){
          var.add(((Variable)term.getArguments().get(i)).getVarName());
        }else if(term.getArguments().get(i) instanceof Composed){
          var.addAll(vars(term.getArguments().get(i)));
        }
      }
      return var;
    }		
    return var;
  }
  
  public Term termSubstituted(Term t, Substitution subs){
    Term t_copy = (Term)dClone.deepClone(t);
    if(!vars(t_copy).isEmpty()){
      if(t_copy instanceof Variable){
        if(subs.getSubstitution().containsKey(t_copy.getVarName())){
          return subs.getSubstitution().get(t_copy.getVarName());		
        }
      }else{
        for(int i=0; i < t_copy.getArguments().size(); i++){
          if(t_copy.getArguments().get(i) instanceof Variable){
            Variable var = (Variable)(t_copy.getArguments().get(i));
            if(subs.getSubstitution().containsKey(var.getVarName())){
              t_copy.getArguments().add(i, subs.getSubstitution().get(var.getVarName()));
              t_copy.getArguments().remove(i+1);
            }
          }else{
            t_copy.getArguments().add(i, termSubstituted(t_copy.getArguments().get(i),subs));
            t_copy.getArguments().remove(i+1);
          }
        }
      }
    }
    return t_copy;
  }
  
    
  public Term termSubs(Term t, HashMap<String,String> subs){   // need move to new calss
    Term t_copy = (Term)dClone.deepClone(t);
    if(!vars(t).isEmpty()){
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
   * Returns a substituted term. 
   * only substitute the variables if it occurs in the map subs
   * @param  t    e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @param  subs e.g. {PK=pk, A=a}
   * @return e.g. iknows(sign(inv(pk),pair(a,NPK)))
   */
  public Term termSubsWithType(Term t, HashMap<String,FactWithType> subs){   // need move to new calss
    Term t_copy = (Term)dClone.deepClone(t);
    if(!vars(t_copy).isEmpty()){
      if(t_copy instanceof Variable){
        if(subs.containsKey(t_copy.getVarName())){
          return subs.get(t_copy.getVarName()).getTerm();   
        }
      }else{
        for(int i=0; i < t_copy.getArguments().size(); i++){
          if(t_copy.getArguments().get(i) instanceof Variable){
            Variable var = (Variable)(t_copy.getArguments().get(i));
            if(subs.containsKey(var.getVarName())){
              t_copy.getArguments().add(i, subs.get(var.getVarName()).getTerm());
              t_copy.getArguments().remove(i+1);
            }
          }else{
            t_copy.getArguments().add(i, termSubsWithType(t_copy.getArguments().get(i),subs));
            t_copy.getArguments().remove(i+1);
          }
        }
      }
    }
    return t_copy;
  }
    
  public void mgu(List<TermPair> termPairs, Substitution subs) throws UnificationFailedException{
    if(!termPairs.isEmpty()){
      Term s = termPairs.get(0).gets();
      Term t = termPairs.get(0).gett();
      if(s.equals(t)){
        termPairs.remove(0);
        mgu(termPairs,subs);
      }
      else if(s instanceof Variable){
        if(vars(t).contains(((Variable)s).getVarName())){
          subs.setUnifierState(false);
          throw new UnificationFailedException();
        }else{
          /*update subs*/
          HashMap<String, Term> subsMap = new HashMap<>();
          subsMap.put(((Variable)s).getVarName(), t);
          Substitution newSub = new Substitution(subsMap);
          for(Map.Entry<String, Term> sub : subs.getSubstitution().entrySet()){
            Term termSubstituted = termSubstituted((Term)(sub.getValue()),newSub);
            subs.addSubstitution(sub.getKey(), termSubstituted);    
          }
          subs.addAllSubstitution(subsMap);
          /*update termPairs*/
          termPairs.remove(0);
          List<TermPair> termPairsSubstituted = new ArrayList<>();
          for(TermPair tp : termPairs){
            Term s1 = termSubstituted(tp.gets(),subs);
            Term t1 = termSubstituted(tp.gett(),subs);
            TermPair tpsubstituted = new TermPair(s1,t1);
            termPairsSubstituted.add(tpsubstituted);           
          }    
          mgu(termPairsSubstituted,subs);
        }   
      }else if(t instanceof Variable){
        if(vars(s).contains(((Variable)t).getVarName())){
          subs.setUnifierState(false);
          throw new UnificationFailedException();
        }else{
          /*update subs*/
          HashMap<String, Term> subsMap = new HashMap<>();
          subsMap.put(((Variable)t).getVarName(), s);
          Substitution newSub = new Substitution(subsMap);
          for(Map.Entry<String, Term> sub : subs.getSubstitution().entrySet()){
            Term termSubstituted = termSubstituted((Term)(sub.getValue()),newSub);
            subs.addSubstitution(sub.getKey(), termSubstituted);    
          }
          subs.addAllSubstitution(subsMap);
          /*update termPairs*/
          termPairs.remove(0);
          List<TermPair> termPairsSubstituted = new ArrayList<>();
          for(TermPair tp : termPairs){
            Term s1 = termSubstituted(tp.gets(),subs);
            Term t1 = termSubstituted(tp.gett(),subs);
            TermPair tpsubstituted = new TermPair(s1,t1);
            termPairsSubstituted.add(tpsubstituted);           
          }  
          mgu(termPairsSubstituted,subs);
        }
      }else{
        if(!((Composed)s).getFactName().equals(((Composed)t).getFactName())){
          subs.setUnifierState(false);
          throw new UnificationFailedException();
        }else{
          termPairs.remove(0);
          for(int i=0; i < s.getArguments().size(); i++){
            TermPair newTermPair = new TermPair(s.getArguments().get(i),t.getArguments().get(i));
            termPairs.add(newTermPair);
          }
          mgu(termPairs,subs);
        }      
      }
    }
  }
  
  
  public void mguWithTypes(List<TermPairWithTypes> termPairs,HashMap<String,List<String>> UserDefType ,Substitution subs) throws UnificationFailedException{
    if(!termPairs.isEmpty()){
      FactWithType s = termPairs.get(0).gets();
      FactWithType t = termPairs.get(0).gett();     
      if(s.getTerm().equals(t.getTerm())){
        termPairs.remove(0);
        mguWithTypes(termPairs,UserDefType,subs);
      }else if(s.getTerm() instanceof Variable){
        if(!(t.getTerm() instanceof Variable)){
          if(((Variable)s.getTerm()).getVarName().matches(".*PKDB.*")){
            termPairs.remove(0);
            mguWithTypes(termPairs,UserDefType,subs);
          }else{
            subs.setUnifierState(false);
            throw new UnificationFailedException();
          }
        }else{
          /*update subs*/
          HashMap<String, Term> subsMap = new HashMap<>();
          if(isV1GreateOrEqualV2(s,t,UserDefType)){
            subsMap.put(((Variable)s.getTerm()).getVarName(), t.getTerm());
          }else if(isV1GreateOrEqualV2(t,s,UserDefType)){
            subsMap.put(((Variable)t.getTerm()).getVarName(), s.getTerm());
          }else{
            subs.setUnifierState(false);
            throw new UnificationFailedException();
          }
          Substitution newSub = new Substitution(subsMap);
          for(Map.Entry<String, Term> sub : subs.getSubstitution().entrySet()){
            Term termSubstituted = termSubstituted((Term)(sub.getValue()),newSub);
            subs.addSubstitution(sub.getKey(), termSubstituted);    
          }
          subs.addAllSubstitution(subsMap);
          
          /*update user-defined types*/
          for(Map.Entry<String, Term> entity : subsMap.entrySet()){
            if(!s.getvType().containsKey(entity.getValue().getVarName())){
              if(t.getvType().containsKey(entity.getValue().getVarName())){
                s.getvType().put(entity.getValue().getVarName(), t.getvType().get(entity.getValue().getVarName()));
              }
            }
            if(!t.getvType().containsKey(entity.getValue().getVarName())){
              if(s.getvType().containsKey(entity.getValue().getVarName())){
                t.getvType().put(entity.getValue().getVarName(), s.getvType().get(entity.getValue().getVarName()));
              }
            }
          }
                 
          /*update termPairs*/
          termPairs.remove(0);
          List<TermPairWithTypes> termPairsSubstituted = new ArrayList<>();
          for(TermPairWithTypes tp : termPairs){
            Term s1 = termSubstituted(tp.gets().getTerm(),subs);
            FactWithType s1WithType = new FactWithType(tp.gets().getvType(),s1);
            Term t1 = termSubstituted(tp.gett().getTerm(),subs);
            FactWithType t1WithType = new FactWithType(tp.gett().getvType(),t1);
            TermPairWithTypes tpsubstituted = new TermPairWithTypes(s1WithType,t1WithType);
            termPairsSubstituted.add(tpsubstituted);           
          }
          //subs.addSubstitution(((Variable)s.getTerm()).getVarName(), t.getTerm());    
          mguWithTypes(termPairsSubstituted,UserDefType,subs);
        }   
      }else if(t.getTerm() instanceof Variable){
        if(!(s.getTerm() instanceof Variable)){
          if(((Variable)t.getTerm()).getVarName().matches(".*PKDB.*")){
            termPairs.remove(0);
            mguWithTypes(termPairs,UserDefType,subs);
          }else{
            subs.setUnifierState(false);
            throw new UnificationFailedException();
          }
        }else{
          /*update subs*/
          HashMap<String, Term> subsMap = new HashMap<>();
          if(isV1GreateOrEqualV2(t,s,UserDefType)){
            subsMap.put(((Variable)t.getTerm()).getVarName(), s.getTerm());
          }else if(isV1GreateOrEqualV2(s,t,UserDefType)){
            subsMap.put(((Variable)s.getTerm()).getVarName(), t.getTerm());
          }else{
            subs.setUnifierState(false);
            throw new UnificationFailedException();
          }
          subsMap.put(((Variable)t.getTerm()).getVarName(), s.getTerm());
          Substitution newSub = new Substitution(subsMap);
          for(Map.Entry<String, Term> sub : subs.getSubstitution().entrySet()){
            Term termSubstituted = termSubstituted((Term)(sub.getValue()),newSub);
            subs.addSubstitution(sub.getKey(), termSubstituted);    
          }
          subs.addAllSubstitution(subsMap);
          /*update user-defined types*/
          for(Map.Entry<String, Term> entity : subsMap.entrySet()){
            if(!s.getvType().containsKey(entity.getKey())){
              if(t.getvType().containsKey(entity.getKey())){
                s.getvType().put(entity.getValue().getVarName(), t.getvType().get(entity.getKey()));
              }
            }
            if(!t.getvType().containsKey(entity.getKey())){
              if(s.getvType().containsKey(entity.getKey())){
                t.getvType().put(entity.getValue().getVarName(), s.getvType().get(entity.getKey()));
              }
            }
          }
          /*update termPairs*/
          termPairs.remove(0);
          List<TermPairWithTypes> termPairsSubstituted = new ArrayList<>();
          for(TermPairWithTypes tp : termPairs){
            Term s1 = termSubstituted(tp.gets().getTerm(),subs);
            FactWithType s1WithType = new FactWithType(tp.gets().getvType(),s1);
            Term t1 = termSubstituted(tp.gett().getTerm(),subs);
            FactWithType t1WithType = new FactWithType(tp.gett().getvType(),t1);
            TermPairWithTypes tpsubstituted = new TermPairWithTypes(s1WithType,t1WithType);
            termPairsSubstituted.add(tpsubstituted);           
          }
          //subs.addSubstitution(((Variable)t.getTerm()).getVarName(), s.getTerm());    
          mguWithTypes(termPairsSubstituted,UserDefType,subs);
        }
      }else{
        if(!((Composed)s.getTerm()).getFactName().equals(((Composed)t.getTerm()).getFactName())){
          subs.setUnifierState(false);
          throw new UnificationFailedException();
        }else{
          termPairs.remove(0);
          for(int i=0; i < s.getTerm().getArguments().size(); i++){
            FactWithType s1WithTypes = new FactWithType(s.getvType(),s.getTerm().getArguments().get(i));
            FactWithType t1WithTypes = new FactWithType(t.getvType(),t.getTerm().getArguments().get(i));
            TermPairWithTypes newTermPairWithTypes = new TermPairWithTypes(s1WithTypes,t1WithTypes);
            termPairs.add(newTermPairWithTypes);
          }
          mguWithTypes(termPairs,UserDefType,subs);
        }      
      }
    }   
  }
  
  public void mguWithTypesPKDB(List<TermPairWithTypes> termPairs,HashMap<String,List<String>> UserDefType ,Substitution subs) throws UnificationFailedException{
    if(!termPairs.isEmpty()){
      FactWithType s = termPairs.get(0).gets();
      FactWithType t = termPairs.get(0).gett();     
      if(s.getTerm().equals(t.getTerm())){
        termPairs.remove(0);
        mguWithTypesPKDB(termPairs,UserDefType,subs);
      }else if(s.getTerm() instanceof Variable){
        if(!(t.getTerm() instanceof Variable)){
          if(vars(t.getTerm()).contains(((Variable)s.getTerm()).getVarName())){
            subs.setUnifierState(false);
            throw new UnificationFailedException();
          }else{ // may need update here
            if(((Variable)s.getTerm()).getVarName().matches(".*PKDB.*")){
              termPairs.remove(0);
              subs.getSubstitution().put(s.getTerm().getVarName(), t.getTerm());
              mguWithTypesPKDB(termPairs,UserDefType,subs);
            }else{
              subs.setUnifierState(false);
              throw new UnificationFailedException();
            }
          }                      
        }else{
          /*update subs*/
          HashMap<String, Term> subsMap = new HashMap<>();
          if(isV1GreateOrEqualV2(s,t,UserDefType)){
            subsMap.put(((Variable)s.getTerm()).getVarName(), t.getTerm());
          }else if(isV1GreateOrEqualV2(t,s,UserDefType)){
            subsMap.put(((Variable)t.getTerm()).getVarName(), s.getTerm());
          }else{
            subs.setUnifierState(false);
            throw new UnificationFailedException();
          }
          Substitution newSub = new Substitution(subsMap);
          for(Map.Entry<String, Term> sub : subs.getSubstitution().entrySet()){
            Term termSubstituted = termSubstituted((Term)(sub.getValue()),newSub);
            subs.addSubstitution(sub.getKey(), termSubstituted);    
          }
          subs.addAllSubstitution(subsMap);
          
          /*update user-defined types*/
          for(Map.Entry<String, Term> entity : subsMap.entrySet()){
            if(!s.getvType().containsKey(entity.getValue().getVarName())){
              if(t.getvType().containsKey(entity.getValue().getVarName())){
                s.getvType().put(entity.getValue().getVarName(), t.getvType().get(entity.getValue().getVarName()));
              }
            }
            if(!t.getvType().containsKey(entity.getValue().getVarName())){
              if(s.getvType().containsKey(entity.getValue().getVarName())){
                t.getvType().put(entity.getValue().getVarName(), s.getvType().get(entity.getValue().getVarName()));
              }
            }
          }
                 
          /*update termPairs*/
          termPairs.remove(0);
          List<TermPairWithTypes> termPairsSubstituted = new ArrayList<>();
          for(TermPairWithTypes tp : termPairs){
            Term s1 = termSubstituted(tp.gets().getTerm(),subs);
            FactWithType s1WithType = new FactWithType(tp.gets().getvType(),s1);
            Term t1 = termSubstituted(tp.gett().getTerm(),subs);
            FactWithType t1WithType = new FactWithType(tp.gett().getvType(),t1);
            TermPairWithTypes tpsubstituted = new TermPairWithTypes(s1WithType,t1WithType);
            termPairsSubstituted.add(tpsubstituted);           
          }
          //subs.addSubstitution(((Variable)s.getTerm()).getVarName(), t.getTerm());    
          mguWithTypesPKDB(termPairsSubstituted,UserDefType,subs);
        }   
      }else if(t.getTerm() instanceof Variable){
        if(!(s.getTerm() instanceof Variable)){
          if(vars(s.getTerm()).contains(((Variable)t.getTerm()).getVarName())){
            subs.setUnifierState(false);
            throw new UnificationFailedException();
          }else{ // may need update here
            if(((Variable)t.getTerm()).getVarName().matches(".*PKDB.*")){
              termPairs.remove(0);
              subs.getSubstitution().put(t.getTerm().getVarName(), s.getTerm());
              mguWithTypesPKDB(termPairs,UserDefType,subs);
            }else{
              subs.setUnifierState(false);
              throw new UnificationFailedException();
            }
          }
        }else{
          /*update subs*/
          HashMap<String, Term> subsMap = new HashMap<>();
          if(isV1GreateOrEqualV2(t,s,UserDefType)){
            subsMap.put(((Variable)t.getTerm()).getVarName(), s.getTerm());
          }else if(isV1GreateOrEqualV2(s,t,UserDefType)){
            subsMap.put(((Variable)s.getTerm()).getVarName(), t.getTerm());
          }else{
            subs.setUnifierState(false);
            throw new UnificationFailedException();
          }
          subsMap.put(((Variable)t.getTerm()).getVarName(), s.getTerm());
          Substitution newSub = new Substitution(subsMap);
          for(Map.Entry<String, Term> sub : subs.getSubstitution().entrySet()){
            Term termSubstituted = termSubstituted((Term)(sub.getValue()),newSub);
            subs.addSubstitution(sub.getKey(), termSubstituted);    
          }
          subs.addAllSubstitution(subsMap);
          /*update user-defined types*/
          for(Map.Entry<String, Term> entity : subsMap.entrySet()){
            if(!s.getvType().containsKey(entity.getKey())){
              if(t.getvType().containsKey(entity.getKey())){
                s.getvType().put(entity.getValue().getVarName(), t.getvType().get(entity.getKey()));
              }
            }
            if(!t.getvType().containsKey(entity.getKey())){
              if(s.getvType().containsKey(entity.getKey())){
                t.getvType().put(entity.getValue().getVarName(), s.getvType().get(entity.getKey()));
              }
            }
          }
          /*update termPairs*/
          termPairs.remove(0);
          List<TermPairWithTypes> termPairsSubstituted = new ArrayList<>();
          for(TermPairWithTypes tp : termPairs){
            Term s1 = termSubstituted(tp.gets().getTerm(),subs);
            FactWithType s1WithType = new FactWithType(tp.gets().getvType(),s1);
            Term t1 = termSubstituted(tp.gett().getTerm(),subs);
            FactWithType t1WithType = new FactWithType(tp.gett().getvType(),t1);
            TermPairWithTypes tpsubstituted = new TermPairWithTypes(s1WithType,t1WithType);
            termPairsSubstituted.add(tpsubstituted);           
          }
          //subs.addSubstitution(((Variable)t.getTerm()).getVarName(), s.getTerm());    
          mguWithTypesPKDB(termPairsSubstituted,UserDefType,subs);
        }
      }else{
        if(!((Composed)s.getTerm()).getFactName().equals(((Composed)t.getTerm()).getFactName())){
          subs.setUnifierState(false);
          throw new UnificationFailedException();
        }else{
          termPairs.remove(0);
          for(int i=0; i < s.getTerm().getArguments().size(); i++){
            FactWithType s1WithTypes = new FactWithType(s.getvType(),s.getTerm().getArguments().get(i));
            FactWithType t1WithTypes = new FactWithType(t.getvType(),t.getTerm().getArguments().get(i));
            TermPairWithTypes newTermPairWithTypes = new TermPairWithTypes(s1WithTypes,t1WithTypes);
            termPairs.add(newTermPairWithTypes);
          }
          mguWithTypesPKDB(termPairs,UserDefType,subs);
        }      
      }
    }   
  }
  
  public boolean isT1GreaterOrEqualT2(FactWithType t1, FactWithType t2, HashMap<String,List<String>> UserDefType,RenamingInfo renameInfo){
    FactWithType t1Renamed = renameTermVars(t1,renameInfo);
    FactWithType t2copy = (FactWithType)dClone.deepClone(t2);
    //System.out.println(t2Renamed);
    TermPairWithTypes termPair = new TermPairWithTypes(t1Renamed,t2copy);
    List<TermPairWithTypes> termPairs = new ArrayList<>();
    termPairs.add(termPair);
    Substitution subs = new Substitution();
    try{
      mguWithTypes(termPairs,UserDefType,subs);
    }catch(UnificationFailedException e){
      return false;
    }
    if(subs.getUnifierState()){
      ArrayList<String> dom = new ArrayList<>(dom(subs));
      ArrayList<String> fv = vars(t2copy.getTerm());
      dom.retainAll(fv);
      if(dom.isEmpty()){
        return true;
      }else{
        return false;
      }
    }    
    return false;
  }
  
  public ArrayList<String> dom(Substitution subs){
    ArrayList<String> keys = new ArrayList<>();
    for(Map.Entry<String, Term> entity : subs.getSubstitution().entrySet()){
      keys.add(entity.getKey());
    }
    return keys;
  }
  
  public FactWithType renameTermVars(FactWithType t, RenamingInfo renameInfo){
    FactWithType tcopy = (FactWithType)dClone.deepClone(t);
    HashSet<String> vars = new HashSet<>(vars(tcopy.getTerm()));
    HashMap<String,String> subs = new HashMap<>();
    for(String var : vars){
      gc.increaseCounter();
      String newVarName = var + gc.getCounter();
      subs.put(var, newVarName);
      tcopy.getvType().put(newVarName, tcopy.getvType().get(var));
      tcopy.getvType().remove(var);
    }
    renameInfo.setvType(tcopy.getvType());
    renameInfo.setSubs(subs);
    Term newTerm = termSubs(tcopy.getTerm(),subs);
    return new FactWithType(tcopy.getvType(),newTerm);
  }
   
  public boolean isV1GreateOrEqualV2(FactWithType V1, FactWithType V2, HashMap<String,List<String>> UserDefType){
    if(UserDefType.containsKey(V1.getvType().get(V1.getTerm().getVarName())) && UserDefType.containsKey(V2.getvType().get(V2.getTerm().getVarName()))){
      if(UserDefType.get(V1.getvType().get(V1.getTerm().getVarName())).containsAll(UserDefType.get(V2.getvType().get(V2.getTerm().getVarName())))){
        return true;
      }
    }
    return false;
  }
  
  public boolean unifyTwoFacts(Term t1,Term t2, Substitution subs){
    Term t1copy = (Term)dClone.deepClone(t1);
    Term t2copy = (Term)dClone.deepClone(t2);
    TermPair termPair = new TermPair(t1copy,t2copy);
    List<TermPair> termPairs = new ArrayList<>();
    termPairs.add(termPair);
    try{
      mgu(termPairs,subs);
    }catch(UnificationFailedException e){
      return false;
    }   
    return subs.getUnifierState();
  }
  
  public boolean unifyTwoFacts(FactWithType t1, FactWithType t2, Substitution subs,RenamingInfo renameInfo, HashMap<String,List<String>> UserDefType){
    FactWithType t1copy = (FactWithType)dClone.deepClone(t1);
    FactWithType t2Renamed = renameTermVars(t2,renameInfo);
    //System.out.println(t2Renamed);
    TermPairWithTypes termPair = new TermPairWithTypes(t1copy,t2Renamed);
    List<TermPairWithTypes> termPairs = new ArrayList<>();
    termPairs.add(termPair);
    try{
      mguWithTypes(termPairs,UserDefType,subs);
    }catch(UnificationFailedException e){
      return false;
    }   
    return subs.getUnifierState();
  }
  
  public boolean unifyTwoFactsPKDB(FactWithType t1, FactWithType t2, Substitution subs,RenamingInfo renameInfo, HashMap<String,List<String>> UserDefType){
    FactWithType t1copy = (FactWithType)dClone.deepClone(t1);
    FactWithType t2Renamed = renameTermVars(t2,renameInfo);
    TermPairWithTypes termPair = new TermPairWithTypes(t1copy,t2Renamed);
    List<TermPairWithTypes> termPairs = new ArrayList<>();
    termPairs.add(termPair);
    try{
      mguWithTypesPKDB(termPairs,UserDefType,subs);
    }catch(UnificationFailedException e){
      return false;
    }   
    return subs.getUnifierState();
  }
  
}
