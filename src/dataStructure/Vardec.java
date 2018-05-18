package dataStructure;

public class Vardec extends AST {
  private String var;
  private String type;
  public Vardec(String var, String type) {
    super();
    this.var = var;
    this.type = type;
  }
  public String getVar() {
    return var;
  }
  public void setVar(String var) {
    this.var = var;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
}
