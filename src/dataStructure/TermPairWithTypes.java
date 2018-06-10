package dataStructure;

import dataStructure.FactWithType;


public class TermPairWithTypes {
  private FactWithType s;
  private FactWithType t;
  public TermPairWithTypes(FactWithType s, FactWithType t) {
    super();
    this.s = s;
    this.t = t;
  }
  public FactWithType gets() {
    return s;
  }
  public void sets(FactWithType s) {
    this.s = s;
  }
  public FactWithType gett() {
    return t;
  }
  public void sett(FactWithType t) {
    this.t = t;
  }
  @Override
  public String toString() {
    return "TermPair [s=" + s + ", t=" + t + "]";
  }
  
}
