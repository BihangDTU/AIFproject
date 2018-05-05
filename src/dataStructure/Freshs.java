package dataStructure;

import java.util.ArrayList;
import java.util.List;

public class Freshs extends AST {
	List<Variable> freshs = new ArrayList<Variable>();

	public Freshs(List<Variable> freshs) {
		super();
		this.freshs = freshs;
	}
	public Freshs() {}

	public List<Variable> getFreshs() {
		return freshs;
	}

	public void setFreshs(List<Variable> freshs) {
		this.freshs = freshs;
	}
	
}
