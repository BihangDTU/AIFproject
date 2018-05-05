package dataStructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Composed extends Term implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String factName;
  private ArrayList<Term> arguments = new ArrayList<>();

    // Sebastian: There was no constructor for setting both (done
    // myself):
    
  public Composed(String factName,ArrayList<Term> arguments ) {
  	this.factName = factName;
  	this.arguments = arguments;
  }
    
  public Composed() {}
  
  public Composed(String factName) {
    this.factName = factName;
  }

  @Override
  public String getFactName() {
    return factName;
  }

  public void setFactName(String factName) {
    this.factName = factName;
  }

  public ArrayList<Term> getArguments() {
    return arguments;
  }

  @Override
  public void setArguments(Term arguments) {
    this.arguments.add(arguments);
  }

  @Override
  public String toString(){
    String composed;
    if(arguments.isEmpty()){
      composed = factName;
    }else{
      composed = factName + "(";
      for(int i=0; i<arguments.size(); i++){
        composed = composed +arguments.get(i).toString();	
        if(i != arguments.size() - 1){
          composed = composed + ",";
        }
      }
      composed = composed + ")";
    }
    return composed;
  }

  @Override
  public String getVarName() {
    return null;
  }

  @Override
  public String getCons() {
    return null;
  }

  @Override
  public boolean equals(Object o){
    if (o == this) return true;
    if (!(o instanceof Composed)) {
      return false;
    }
    Composed composed = (Composed) o;		
    return arguments.equals(composed.arguments) &&
            Objects.equals(factName, composed.factName);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.factName);
    hash = 19 * hash + Objects.hashCode(this.arguments);
    return hash;
  }
}
