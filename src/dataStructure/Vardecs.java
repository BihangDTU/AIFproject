package dataStructure;
import java.util.ArrayList;
import java.util.List;

public class Vardecs extends AST{
  private List<Vardec> vardecs = new ArrayList<Vardec>();
  
  public Vardecs(List<Vardec> vardecs) {
    super();
    this.vardecs = vardecs;
  }
  
  public List<Vardec> getVardecs() {
    return vardecs;
  }
  
  public void setVardecs(List<Vardec> vardecs) {
    this.vardecs = vardecs;
  }
}
