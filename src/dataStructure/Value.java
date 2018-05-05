package dataStructure;
import java.util.ArrayList;


public class Value extends Term {
	private String value;

	public Value(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String getFactName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setArguments(Term arguments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Term> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVarName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCons() {
		// TODO Auto-generated method stub
		return null;
	}
}
