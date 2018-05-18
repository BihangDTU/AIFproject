package dataStructure;
import java.util.HashMap;
import java.util.Map;

public class FixedpointData extends AST {
  private HashMap<Integer, Fixedpoint> fixedpoints = new HashMap<>();
  
  public FixedpointData(HashMap<Integer, Fixedpoint> fixpoints) {
    super();
    this.fixedpoints = fixpoints;
  }
  
  public HashMap<Integer, Fixedpoint> getFixpoints() {
    return fixedpoints;
  }
  
  public void setFixpoints(HashMap<Integer, Fixedpoint> fixpoints) {
    this.fixedpoints = fixpoints;
  }
  
  @Override
  public String toString() {
    String s="";
    for (Map.Entry<Integer, Fixedpoint> entry : fixedpoints.entrySet()) {
      Fixedpoint fp = entry.getValue();
      s += fp.toString() + ";\n";
    }
    return s;
  }
	
}
