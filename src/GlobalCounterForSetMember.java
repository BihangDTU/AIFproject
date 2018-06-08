
public class GlobalCounterForSetMember {
  private static int counter = 0;
  public GlobalCounterForSetMember(){};
  public void increaseCounter(){
    counter++;
  }
  public int getCounter(){
    return counter;
  }
}
