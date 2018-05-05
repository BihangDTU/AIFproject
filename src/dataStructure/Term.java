package dataStructure;
import java.util.ArrayList;

public abstract class Term extends AST {
	public abstract String getFactName();
	public abstract void setArguments(Term arguments);
  public abstract ArrayList<Term> getArguments();
  public abstract String getVarName();
  public abstract String getCons();
}

