import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dataStructure.*;

public class KeyLifeCycle {
	List<Term> timplies = new ArrayList<Term>();
	
	public KeyLifeCycle(AST fpAST){
		for (Map.Entry<Integer, Fixpoint> entry : ((FixpointData)fpAST).getFixpoints().entrySet()) {
			if(entry.getValue().getTerm().getFactName().equals("timplies")){
				timplies.add(entry.getValue().getTerm());
			}
		}
	}
	
	public void printKeyLifeCycle(){
		for(int i=0;i<timplies.size();i++){
			System.out.print(timplies.get(i).getArguments().get(0).toString());
			System.out.print(" -->> ");
			System.out.println(timplies.get(i).getArguments().get(1).toString());
		}
	}

	/*@Override
	public String toString() {
		String s = "";
		for(Term t : timplies){
			//System.out.println(t.toString());
			s += t.toString() + "\n";
		}
		return s;
	}*/
}
