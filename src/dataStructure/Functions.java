package dataStructure;
import java.util.ArrayList;
import java.util.List;

public class Functions extends AST {
  List<Function> functions = new ArrayList<Function>();
  
  public Functions(Functions functions){
    this.functions = functions.getFunctions();
  }
  public Functions(){
  }
  
  public void addFunction(Function fun){
    functions.add(fun);
  }
  
  public List<Function> getFunctions() {
    return functions;
  }
  
  public void setFunctions(List<Function> functions) {
    this.functions = functions;
  }
  @Override
  public String toString(){
    String s="";
    for(int i=0;i<functions.size();i++){
      s+=functions.get(i).toString();
      if(i<functions.size()-1){
        s+=", ";
      }else{
        s+=";\n";
      }
    }
    return s;
  }
}
