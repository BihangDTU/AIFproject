import java.util.ArrayList;

public class AttackInfo {
  private String laTaxCMD;
  private ArrayList<String> attackTraces = new ArrayList<>();
  private boolean isAttackReachable = false;

  public String getLaTaxCMD() {
    return laTaxCMD;
  }

  public void setLaTaxCMD(String laTaxCMD) {
    this.laTaxCMD = laTaxCMD;
  }

  public ArrayList<String> getAttackTraces() {
    return attackTraces;
  }

  public void setAttackTraces(ArrayList<String> attackTraces) {
    this.attackTraces = attackTraces;
  }

  public boolean isAttackReachable() {
    return isAttackReachable;
  }

  public void setAttackReachable(boolean isAttackReachable) {
    this.isAttackReachable = isAttackReachable;
  }
  
}
