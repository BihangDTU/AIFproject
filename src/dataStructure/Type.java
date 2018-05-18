package dataStructure;
import java.util.ArrayList;
import java.util.List;

public class Type extends AST {
  private String userType;
  private List<String> agents = new ArrayList<String>();
	
  public Type(String userType, List<String> agents){
    this.userType = userType;
    this.agents = agents;
  }
	
  public String getUserType(){
    return userType;
  }
	
  public List<String> getAgents(){
    return agents;
  }

  @Override
  public String toString() {
    String s = userType + " = ";
    if(!agents.get(0).isEmpty() && Character.isUpperCase(agents.get(0).charAt(0))){
      for(int i=0;i<agents.size();i++){
        s += agents.get(i);
        if(i<agents.size()-1){
          s += " ++ ";
        }else{
          s += ";";
        }
      }
    }else{
      for(int j=0;j<agents.size();j++){
        if(j==0) s += "{";
        s += agents.get(j);
        if(j<agents.size()-1){
          s += ", ";
        }else{
          s += "};";
        }
      }
    }	
    return s;
  }
}
