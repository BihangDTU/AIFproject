package dataStructure;

import java.util.HashMap;

public class RenamingInfo {
  HashMap<String,String> vType = new HashMap<>();
  HashMap<String,String> subs = new HashMap<>();
  public RenamingInfo(){}
  public RenamingInfo(HashMap<String, String> vType,
      HashMap<String, String> subs) {
    super();
    this.vType = vType;
    this.subs = subs;
  }
  public HashMap<String, String> getvType() {
    return vType;
  }
  public void setvType(HashMap<String, String> vType) {
    this.vType = vType;
  }
  public HashMap<String, String> getSubs() {
    return subs;
  }
  public void setSubs(HashMap<String, String> subs) {
    this.subs = subs;
  }
  @Override
  public String toString() {
    return "RenamingInfo [vType=" + vType + ", subs=" + subs + "]";
  }
}
