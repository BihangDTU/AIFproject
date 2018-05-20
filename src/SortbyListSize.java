import java.util.ArrayList;
import java.util.Comparator;

import dataStructure.FactWithType;

public class SortbyListSize implements Comparator<ArrayList<FactWithType>>
{   
  public int compare(ArrayList<FactWithType> a, ArrayList<FactWithType> b)
  {
    return b.size() - a.size();
  }
}
