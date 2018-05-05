import java.util.ArrayList;
import dataStructure.*;

public class Node {
  private State state = null;
  private ArrayList<Node> children = new ArrayList<>();
  private Node parent = null;  
  private static boolean isAttackFound = false;
  private String ruleName = "";
  
  public Node(State state){
    this.state = state;
  }
  
  public Node(){}
  
  public void addChild(Node child) {
    child.setParent(this);
    this.children.add(child);
	 }
 
  public void addChildren(ArrayList<Node> children) {
   //children.forEach(each -> each.setParent(this));
   for(Node n : children){
  	 n.setParent(this);
   }
   this.children.addAll(children);
  }

  public ArrayList<Node> getChildren() {
    return children;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public Node getParent() {
    return parent;
  }
  
  public boolean isRoot() {
    return (this.parent == null);
  }

  public boolean isLeaf() {
    return this.children.isEmpty();
  }

  public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }
  
  public void printTree(Node node, String appender) {
		  System.out.println(appender + node.getState());
		  //node.getChildren().forEach(each ->  printTree(each, appender + appender));
		  for(Node n : node.getChildren()){
		  	printTree(n, appender + appender);
		  }
	 }
  
   public void printAttack(Node node) {
    if(node.getState().getFacts().contains(new Composed("attack"))){
      isAttackFound = true;
      if(!node.getRuleName().equals("")){
        System.out.println("   |");
        System.out.println(node.getRuleName());
        System.out.println("   ↓");
      } 
      System.out.println(node.getState());
    }
    if(!isAttackFound){
      if(!node.getRuleName().equals("")){
        System.out.println("   |");
        System.out.println(node.getRuleName());
        System.out.println("   ↓");
      }     
      System.out.println(node.getState());
      if(!node.getState().getFacts().contains(new Composed("attack"))){
        //node.getChildren().forEach(each ->  printAttack(each));
      	for(Node n : node.getChildren()){
      		printAttack(n);
      	}
      }
    }
	 }

}
