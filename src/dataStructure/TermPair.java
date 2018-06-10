package dataStructure;

import dataStructure.Term;


public class TermPair {
  private Term s;
  private Term t;
  public TermPair(Term s, Term t) {
    super();
    this.s = s;
    this.t = t;
  }
  public Term gets() {
    return s;
  }
  public void sets(Term s) {
    this.s = s;
  }
  public Term gett() {
    return t;
  }
  public void sett(Term t) {
    this.t = t;
  }
  @Override
  public String toString() {
    return "TermPair [s=" + s + ", t=" + t + "]";
  }
  
}
