package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Freshs extends AST implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
