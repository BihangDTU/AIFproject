import java.util.ArrayList;
import java.util.List;

import dataStructure.*;

public class Node {
  private State state = null;
  private ArrayList<Node> children = new ArrayList<>();
  private Node parent = null;  
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
  
  public boolean hasPath(Node root, List<Node> attackTraceArr)
  {
    /* if root state is NULL
       then there is no path*/
    if (root.getState() == null)
      return false;
    
    /* add the node's state in 'attackTraceArr' */
    attackTraceArr.add(root);    
       
    /* if the node contains attack fact, return true*/
    if (root.getState().getFacts().contains(new Composed("attack")))    
          return true;
       
    /* else check whether the required node lies
     in the subtree of the current node */
    for(Node n : root.getChildren()){
      if(hasPath(n,attackTraceArr)){
        return true;
      }
    }
       
    /*required node does not lie in the 
      subtree of the current node
      Thus, remove current node from 
      'attackTraceArr' and then return false */
    attackTraceArr.remove(attackTraceArr.size()-1);
    return false;            
  }
  
  public void printAttackPath(Node root)
  {
    /*list to store the "attack" path*/
    List<Node> attackTraceArr = new ArrayList<>();
       
    /* if required node 'attack' is present*/
    /* then print the path*/
    if (hasPath(root, attackTraceArr))
    {
      for(int i=0; i<attackTraceArr.size()-1; i++){
        if(!attackTraceArr.get(i).getState().getFacts().isEmpty()){
          System.out.println(attackTraceArr.get(i).getState());
        }else{
          System.out.println("Empty state;");
        }
        System.out.println("   |");
        System.out.println(attackTraceArr.get(i+1).getRuleName());
        System.out.println("   |");
        System.out.println("   v");
      }
      System.out.println(attackTraceArr.get(attackTraceArr.size()-1).getState()); 
    }else{/* "attack" is not present in the node tree*/
      System.out.println("No Path");    
    }
  }
   
  @Override
  public String toString() {
    String s = "";
    s += state.toString();
    s += "\n";
    s += ruleName;
    s += "\n";
    s += "chilren:";
    s += "\n";
    for(Node child : children){
      s += child.getState().toString();
      s += "\n";
    }
    return s;
  }
}
