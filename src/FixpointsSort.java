import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import dataStructure.*;

public class FixpointsSort {
	StateTransition st = new StateTransition();
	DeepClone dClone = new DeepClone();
	public FixpointsSort(){}
	
	 /**
   * Returns a substituted term. 
   * only substitute the variables if it occurs in the map subs
   * @param  t    e.g. iknows(sign(inv(PK),pair(A,NPK)))
   * @param  subs e.g. {PK=pk, A=a}
   * @return e.g. iknows(sign(inv(pk),pair(a,NPK)))
   */
  public Term termSubs(Term t, HashMap<String,String> subs){
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
   * Returns a list of timplies from output file.
   * @param  fpAST   dataStructure contains fixed points from output file  
   * @return  a list of timplies fixedpoints(the variable have been substituted with user define type)
   */
	public List<FixedpointsWithType> getTimplies(AST fpAST){
		List<FixedpointsWithType> timplies = new ArrayList<FixedpointsWithType>();
		for (Map.Entry<Integer, Fixedpoint> entry : ((FixedpointData)fpAST).getFixpoints().entrySet()) {
			if(entry.getValue().getTerm().getFactName().equals("timplies")){
				timplies.add(new FixedpointsWithType(entry.getValue().getvType(),entry.getValue().getTerm()));
			}
		}
		/*substituted variables with user define types
		  A:Honest.S:Server.iknows(inv(val(0,0,db__revoked(S,A)))) 
		  --> A:Honest.S:Server.iknows(inv(val(0,0,db__revoked(Server,Honest))))  
		*/
		/*List<FixedpointsWithType> timpliessubstituted = new ArrayList<FixedpointsWithType>();
		for(FixedpointsWithType fpt : timplies){
			timpliessubstituted .add(new FixedpointsWithType(fpt.getvType(),termSubs(fpt.getTerm(),fpt.getvType())));
		}	
		return timpliessubstituted;*/
		return timplies;
	}

	/**
   * Returns a list of timplies which contains all possible implies
   * @param  fpAST   dataStructure contains fixed points from output file  
   * @return  a list of all possible implies (the variable have been substituted with user define type)
   */
	public List<FixedpointsWithType> getExtendedTimplies(AST fpAST){
		HashMap<Term,HashSet<Term>> timpliesMap = new HashMap<Term,HashSet<Term>>(); 
		List<FixedpointsWithType> timplies = getTimplies(fpAST);
  	for(FixedpointsWithType t : timplies){
  		if(!timpliesMap.containsKey(t.getTerm().getArguments().get(0))){
  			/*if the key is not in map then put left val(...) as new key and right val(...) as value srote in to Map*/
  			HashSet<Term> subVal = new HashSet<Term>();
  			subVal.add(t.getTerm().getArguments().get(1));
  			timpliesMap.put(t.getTerm().getArguments().get(0), subVal);
  		}else{
  			/*if the key already exists in the map then just append the new value into the value list*/
  			timpliesMap.get(t.getTerm().getArguments().get(0)).add(t.getTerm().getArguments().get(1));
  		}
  	}
  	/*check whether each key corresponding value list contains the other keys in the map,
  	 if so, then also add the key's corresponding values into the the list, not consider variable type yet*/
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
  	/*extend timplies, the type are considered here
  	 * (1) timpies(val(ring(Agent),0,0),val(ring(Agent),valid(Server,Agent),0))
  	 * (2) timpies(val(ring(User),valid(Server,User),0),val(0,valid(Server,User),0))
  	 * (3) timpies(val(0,valid(Server,User),0),val(0,0,revoked(Server,User)))
  	 *  
  	 *       Agent      User       Honest
  	 * (1,0,0) -> (1,1,0) -> (0,1,0) -> (0,0,1)
  	 * 
  	 * (1) timpies(val(ring(Agent),0,0),val(ring(Agent),valid(Server,Agent),0))
  	 * (2) timpies(val(ring(User),0,0),val(ring(User),valid(Server,User),0))
  	 * (3) timpies(val(ring(Honest),0,0)),0,0,revoked(Server,Honest)))
  	 * (4) timpies(val(ring(User),valid(Server,User),0),val(0,valid(Server,User),0))
  	 * (5) timpies(val(ring(Honest),valid(Server,Honest),0),val(0,valid(Server,Honest),0))
  	 * (7) timpies(val(0,valid(Server,User),0),val(0,0,revoked(Server,User)))
  	 */
  	List<FixedpointsWithType> extendedTimplies = new ArrayList<>();
  	for(Map.Entry<Term,HashSet<Term>> ts : timpliesMap.entrySet()){
  		for(Term t : ts.getValue()){
  			for(FixedpointsWithType fp : timplies){
  				//if(t.equals(fp.getTerm().getArguments().get(0))){
  				if(isTwoValHaveSameForm(t,fp.getTerm().getArguments().get(0))){
  					ArrayList<Term> vals = new ArrayList<>();
  					vals.add(ts.getKey());
  					vals.add(fp.getTerm().getArguments().get(1));
  					extendedTimplies.add(new FixedpointsWithType(fp.getvType(), new Composed("timplies",vals)));
  				}
  			}
  		}
  	}
  	extendedTimplies.addAll(timplies);
  	List<FixedpointsWithType> extendedTimpliesSubstituted = new ArrayList<>();
  	for(FixedpointsWithType fpw: extendedTimplies){
  		extendedTimpliesSubstituted.add(new FixedpointsWithType(fpw.getvType(),termSubs(fpw.getTerm(),fpw.getvType())));
  	}
  	return extendedTimpliesSubstituted;
	}
	
	public List<FixedpointsWithType> factsWithoutDuplicate(List<ArrayList<FixedpointsWithType>> factsSorted, List<FixedpointsWithType> timplies, HashMap<String,List<String>> UserDefType){
		List<FixedpointsWithType> noDuplicateFacts = new ArrayList<FixedpointsWithType>();
		for(ArrayList<FixedpointsWithType> terms : factsSorted){
			if(terms.size() == 1){
				noDuplicateFacts.add(terms.get(0));
			}else{				
				HashMap<String, String> vType = new HashMap<>();
				Term flag = new Variable("Flag");
				FixedpointsWithType lastElementFlag = new FixedpointsWithType(vType,flag);
				terms.add(lastElementFlag);
				ArrayList<FixedpointsWithType> termsCopy = new ArrayList<FixedpointsWithType>(terms);
				while(true){
					if(terms.get(0).equals(lastElementFlag)) break;
					FixedpointsWithType firstTerm = terms.get(0);				
					for(FixedpointsWithType t : terms){
						if(canT1ImpliesT2(firstTerm,t,timplies, UserDefType)){
							termsCopy.remove(t);
						}
					}
					termsCopy.add(firstTerm);
					terms.clear();
					terms.addAll(termsCopy);		
				}		
				terms.remove(lastElementFlag);
				noDuplicateFacts.addAll(terms);
			}
		}	
		return noDuplicateFacts; 
	}	
	
	public boolean canT1ImpliesT2(FixedpointsWithType t1, FixedpointsWithType t2, List<FixedpointsWithType> timplies, HashMap<String,List<String>> UserDefType){ // need more test more cases
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
							for(FixedpointsWithType timp : timplies){
								counter ++;
								if(isTwoValHaveSameForm(subT1, timp.getTerm().getArguments().get(0)) && isTwoValHaveSameForm(subT2, timp.getTerm().getArguments().get(1))){
									//System.out.println(timp.getTerm().getArguments().get(0));
									//System.out.println(subT1);
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
								if(!canT1ImpliesT2(new FixedpointsWithType(t1.getvType(),subT1), new FixedpointsWithType(t2.getvType(),subT2), timplies, UserDefType)){
									return false;
								}
							}else{
								if(!subT1.getFactName().equals(subT1.getFactName())){
									return false;
								}
							}
						}					
					}else{
						if(!UserDefType.get(subT1.getVarName()).containsAll(UserDefType.get(subT2.getVarName()))){
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

	public List<ArrayList<FixedpointsWithType>> factsSort(AST fpAST){
		List<ArrayList<FixedpointsWithType>> factsSorted = new ArrayList<ArrayList<FixedpointsWithType>>();
		List<FixedpointsWithType> facts = new ArrayList<FixedpointsWithType>();
		List<FixedpointsWithType> factsUnsort = new ArrayList<FixedpointsWithType>();
		for (Map.Entry<Integer, Fixedpoint> entry : ((FixedpointData)fpAST).getFixpoints().entrySet()) {
			factsUnsort.add(new FixedpointsWithType(entry.getValue().getvType(),entry.getValue().getTerm()));
		}		
		List<FixedpointsWithType> factsUnsortCopy = new ArrayList<FixedpointsWithType>(factsUnsort);
		boolean isSortedComplete = false;
		while(!isSortedComplete){
			for(FixedpointsWithType firstTerm : factsUnsort){
				if(firstTerm.getTerm().getFactName().equals("timplies") || firstTerm.getTerm().getFactName().equals("occurs")){
					factsUnsortCopy.remove(firstTerm);
				}else{
					break;
				}
			}
			factsUnsort.clear();
			factsUnsort.addAll(factsUnsortCopy);
			if(factsUnsort.isEmpty()){
				isSortedComplete = true;
			}else{
				FixedpointsWithType firstFact = factsUnsort.get(0);
				for(FixedpointsWithType t : factsUnsort){
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
		List<ArrayList<FixedpointsWithType>> factsSortedSubs = new ArrayList<ArrayList<FixedpointsWithType>>();
		//ArrayList<FixedpointsWithType> factsSubs = new ArrayList<FixedpointsWithType>();
		for(ArrayList<FixedpointsWithType> fpts : factsSorted){
			ArrayList<FixedpointsWithType> factsSubs = new ArrayList<FixedpointsWithType>();
			for(FixedpointsWithType fpt : fpts){
				factsSubs.add(new FixedpointsWithType(fpt.getvType(),termSubs(fpt.getTerm(),fpt.getvType())));
			}
			factsSortedSubs.add(factsSubs);
		}
		return factsSortedSubs;
		//return factsSorted;
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
				if(!val1Arg.equals(val2Arg)){				
					for(int j=0;j<val1Arg.getArguments().size();j++){
						if(UserDefType.containsKey(val1Arg.getArguments().get(j).getVarName())){
							if(!UserDefType.get(val1Arg.getArguments().get(j).getVarName()).containsAll(UserDefType.get(val2Arg.getArguments().get(j).getVarName()))){
								return false;
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
				if(!t1.getArguments().get(i).getFactName().equals(t2.getArguments().get(i).getFactName())){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isTwoFactsHaveSameForm(Term t1, Term t2){
		if(t1.equals(t2)){
			return true;
		}else if((t1 instanceof Variable)){
			if(t2 instanceof Variable){
				return true;
			}else{
				return false;
			}		
		}else if((t1 instanceof Composed) && t1.getArguments().isEmpty()){
			if((t2 instanceof Composed) && t2.getArguments().isEmpty()){
				return true;
			}else{
				return false;
			}		
		}else {
			if(!(t2 instanceof Composed)) return false;
			if(((Composed)t1).getFactName().equals(((Composed)t2).getFactName()) && t1.getArguments().size() == t2.getArguments().size()){
				for(int i=0;i<t1.getArguments().size();i++){
					if((t1.getArguments().get(i) instanceof Composed) && (t2.getArguments().get(i) instanceof Composed)){
						if(t1.getArguments().get(i).getFactName().equals("val") && t2.getArguments().get(i).getFactName().equals("val")){
							if(t1.getArguments().get(i).getArguments().size() == t2.getArguments().get(i).getArguments().size())return true;
							else return false;
						}
					}
					if(!isTwoFactsHaveSameForm(t1.getArguments().get(i),t2.getArguments().get(i)))return false;
				}
			}else{
				return false;
			}		
		}
		return true;
	}

}
