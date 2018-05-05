import java.util.ArrayList;

public class AttackInfo {
  private String laTaxCMD;
  private ArrayList<String> attackTraces = new ArrayList<>();

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
}
