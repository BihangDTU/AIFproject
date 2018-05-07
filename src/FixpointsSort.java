import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataStructure.*;

public class FixpointsSort {
	
	public FixpointsSort(){}
	

	
	public List<Term> KeyLifeCycle(AST fpAST){
		List<Term> timplies = new ArrayList<Term>();
		for (Map.Entry<Integer, Fixpoint> entry : ((FixpointData)fpAST).getFixpoints().entrySet()) {
			if(entry.getValue().getTerm().getFactName().equals("timplies")){
				timplies.add(entry.getValue().getTerm());
			}
		}
		return timplies;
	}
	
	public HashMap<Term,List<Term>> getTimpliesMap(AST fpAST){
		HashMap<Term,List<Term>> timpliesMap = new HashMap<Term,List<Term>>(); 
		List<Term> timplies = KeyLifeCycle(fpAST);
  	for(Term t : timplies){
  		if(!timpliesMap.containsKey(t.getArguments().get(0))){
  			List<Term> subVal = new ArrayList<Term>();
  			subVal.add(t.getArguments().get(1));
  			timpliesMap.put(t.getArguments().get(0), subVal);
  		}else{
  			timpliesMap.get(t.getArguments().get(0)).add(t.getArguments().get(1));
  		}
  	}
  	return timpliesMap;
	}
	
	/*public List<Term> factsWithoutDuplicate(List<ArrayList<Term>> factsSorted, HashMap<Term,List<Term>> timpliesMap){
		List<Term> noDuplicateFacts = new ArrayList<Term>();
		for(ArrayList<Term> terms : factsSorted){
			if(terms.size() == 1){
				noDuplicateFacts.add(terms.get(0));
			}else{				
				for(Term t : terms){
					
				}
			}
		}	
		return noDuplicateFacts; 
	}*/
	
	public boolean isT1InferT2(Term t1, Term t2, HashMap<Term,List<Term>> timpliesMap){ // need more test more cases
		if(t1.equals(t2)){
			return true;
		}else if((t1 instanceof Composed) && (t2 instanceof Composed)){
			if(t1.getFactName().equals(t2.getFactName()) && t1.getArguments().size() == t2.getArguments().size()){
				int argumentsSize = t1.getArguments().size();
				for(int i=0;i<argumentsSize;i++){
					if((t1.getArguments().get(i) instanceof Composed) && (t2.getArguments().get(i) instanceof Composed)){
						if(t1.getArguments().get(i).getFactName().equals("val") && t2.getArguments().get(i).getFactName().equals("val")){
							if(!timpliesMap.get(t1.getArguments().get(i)).contains(t2.getArguments().get(i))){
								return false;
							}
						}else{
							return isT1InferT2(t1.getArguments().get(i), t2.getArguments().get(i), timpliesMap);
						}
					}else{
						if(t1.getArguments().get(i).equals(t2.getArguments().get(i))){
							return true;
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
	
	public List<ArrayList<Term>> factsSort(AST fpAST){
		List<ArrayList<Term>> factsSorted = new ArrayList<ArrayList<Term>>();
		List<Term> facts = new ArrayList<Term>();
		List<Term> factsUnsort = new ArrayList<Term>();
		for (Map.Entry<Integer, Fixpoint> entry : ((FixpointData)fpAST).getFixpoints().entrySet()) {
			factsUnsort.add(entry.getValue().getTerm());
		}		
		List<Term> factsUnsortCopy = new ArrayList<Term>(factsUnsort);
		boolean isSortedComplete = false;
		while(!isSortedComplete){
			for(Term firstTerm : factsUnsort){
				if(firstTerm.getFactName().equals("timplies")){
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
				Term firstFact = factsUnsort.get(0);
				for(Term t : factsUnsort){
					if(isTwoFactsHaveSameForm(firstFact,t)){
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
		return factsSorted;
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
