
public class GlobalCounter {
  private static int counter = 0;
  public GlobalCounter(){};
  public void increaseCounter(){
    counter++;
  }
  public int getCounter(){
    return counter;
  }
}
