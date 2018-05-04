package dataStructure;
import java.util.ArrayList;

public abstract class Term extends AST {
  abstract String getFactName();
  abstract void setArguments(Term arguments);
  abstract ArrayList<Term> getArguments();
  abstract String getVarName();
  abstract String getCons();
}

