import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dataStructure.*;

public class FixpointsSort {
	public FixpointsSort(){}
	
	public List<Term> factsSort(AST fpAST){
		List<Term> factsSorted = new ArrayList<>();
		List<Term> factsUnsort = new ArrayList<>();
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
						factsSorted.add(t);
						factsUnsortCopy.remove(t);
					}
				}
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
