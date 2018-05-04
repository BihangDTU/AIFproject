package dataStructure;
import java.util.ArrayList;
import java.util.List;

public class Terms extends AST {
	List<Term> terms = new ArrayList<>();

	public Terms(List<Term> terms) {
		super();
		this.terms = terms;
	}

	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	@Override
	public String toString() {
		String s="";
		for(int i=0;i<terms.size();i++){
			if(i<terms.size()-1){
				s += terms.get(i).toString() + ", ";
			}else{
				s += terms.get(i).toString() + ";";
			}
		}
		return s;
	}
	
}
