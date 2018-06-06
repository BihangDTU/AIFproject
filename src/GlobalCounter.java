
public class GlobalCounter {
  private static int counter;
  public GlobalCounter(){};
  public void increaseCounter(){
    counter++;
  }
  public int getCounter(){
    return counter;
  }
}
