import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import dataStructure.*;

public class AttackTrace {
  private final Term attact = new Composed("attack");
  private ArrayList<String> attackTraces = new ArrayList<>();
  
  public String abstractAttackGraphLaTexCMD(int attackID, HashMap<Integer, Fixedpoint> facts){
    StringBuilder sbuf = new StringBuilder();
    Formatter fmt = new Formatter(sbuf);
    fmt.format("\\infer[" + facts.get(attackID).getRulesName() + "]");
    fmt.format("{" + facts.get(attackID).getTerm().toString() + "}");
    int LeftSideConditionsNum = facts.get(attackID).getLeftSideConditions().size();
    if(LeftSideConditionsNum > 0){
      for(int i=0; i<LeftSideConditionsNum;i++){
        fmt.format("\n");
        if(i==0){
          fmt.format("{" + abstractAttackGraphLaTexCMD(facts.get(attackID).getLeftSideConditions().get(i),facts));
        }else{
          fmt.format(" & " + abstractAttackGraphLaTexCMD(facts.get(attackID).getLeftSideConditions().get(i),facts));
        } 
        if(i==LeftSideConditionsNum-1){
          fmt.format("}");
        }
      }
    }else{
      fmt.format("\n");
      fmt.format("{empty}");
    }
    fmt.close();  
    return sbuf.toString();
  }
  
  public void abstractAttackTrace(HashMap<Integer, Fixedpoint> facts){
    boolean attack = false;
    int attackFactID = 0;
    for(Map.Entry<Integer, Fixedpoint> entry : facts.entrySet()){
      if(entry.getValue().getTerm().equals(attact)){
        attack = true;
        attackFactID = entry.getKey();
      }
    }
    if(attack){
      System.out.println("RESULT goal reachable: attack");
      System.out.println();
      System.out.print(abstractAttackGraphLaTexCMD(attackFactID,facts) + "\n");      
    }else{
      System.out.println("RESULT goal unreachable: attack.");
    }
  }
  
  public String concreteAttackGraphLaTexCMD(int attackID, HashMap<Integer, Fixedpoint> facts, HashMap<String, ConcreteRule> rules){
    StringBuilder sbuf = new StringBuilder();
    Formatter fmt = new Formatter(sbuf);
    String rulesName = facts.get(attackID).getRulesName();
    attackTraces.add(rulesName);
    fmt.format("\\infer[" + rulesName + "]");
    fmt.format("{");
    for(int j=0;j<rules.get(facts.get(attackID).getRulesName()).getRF().size();j++){
      if(j==0){
        fmt.format(rules.get(facts.get(attackID).getRulesName()).getRF().get(j).toString());
      }else{
        fmt.format("." + rules.get(facts.get(attackID).getRulesName()).getRF().get(j).toString());
      }
    }
    /*if(rules.get(facts.get(attackID).getRulesName()).getRS().size() > 0){
      for(int k=0;k<rules.get(facts.get(attackID).getRulesName()).getRS().size();k++){
        String var = rules.get(facts.get(attackID).getRulesName()).getRS().get(k).getVar().toString();
        String term = rules.get(facts.get(attackID).getRulesName()).getRS().get(k).getTerm().toString();
        fmt.format("." + var + " in " + term);
      }
    }*/
    fmt.format("}");
    int LeftSideConditionsNum = facts.get(attackID).getLeftSideConditions().size();
    if(LeftSideConditionsNum > 0){
      for(int i=0; i<LeftSideConditionsNum;i++){
        fmt.format("\n");
        if(i==0){
          fmt.format("{" + concreteAttackGraphLaTexCMD(facts.get(attackID).getLeftSideConditions().get(i),facts,rules));
        }else{
          fmt.format(" & " + concreteAttackGraphLaTexCMD(facts.get(attackID).getLeftSideConditions().get(i),facts,rules));
        } 
        if(i==LeftSideConditionsNum-1){
          fmt.format("}");
        }
      }
    }else{
      fmt.format("\n");
      fmt.format("{empty}");
    }
    fmt.close();
    return sbuf.toString();
  }
  
  public AttackInfo concreteAttackTrace(HashMap<Integer, Fixedpoint> facts, HashMap<String, ConcreteRule> rules){
    attackTraces.clear();
    AttackInfo attackInfo = new AttackInfo();
    boolean attack = false;
    int attackFactID = 0;
    for(Map.Entry<Integer, Fixedpoint> entry : facts.entrySet()){
      if(entry.getValue().getTerm().equals(attact)){
        attack = true;
        attackFactID = entry.getKey();
      }
    }
    if(attack){
      System.out.println("RESULT goal reachable: attack");
      attackInfo.setLaTaxCMD(concreteAttackGraphLaTexCMD(attackFactID,facts,rules));
      Collections.reverse(attackTraces);
      attackInfo.setAttackTraces(attackTraces);
      //System.out.print(attackInfo.getLaTaxCMD() + "\n"); 
      //System.out.print(attackInfo.getAttackTraces() + "\n");
      attackInfo.setAttackReachable(true);
    }else{
      System.out.println("RESULT goal unreachable: attack.");
    }
    return attackInfo;
}
  
  public String concreteAttackGraphLaTexCMDtype(int attackID, HashMap<Integer, Fixedpoint> facts, HashMap<String, ConcreteRule> rules, HashMap<String, ArrayList<String>> userTypes){
    StringBuilder sbuf = new StringBuilder();
    Formatter fmt = new Formatter(sbuf);
    fmt.format("\\infer[" + facts.get(attackID).getRulesName() + "]");
    fmt.format("{");
    for(int j=0;j<rules.get(facts.get(attackID).getRulesName()).getRF().size();j++){
      if(j==0){
        fmt.format(rules.get(facts.get(attackID).getRulesName()).getRF().get(j).toString());
      }else{
        fmt.format("." + rules.get(facts.get(attackID).getRulesName()).getRF().get(j).toString());
      }
    }
    /*if(rules.get(facts.get(attackID).getRulesName()).getRS().size() > 0){
      for(int k=0;k<rules.get(facts.get(attackID).getRulesName()).getRS().size();k++){
        fmt.format("." + rules.get(facts.get(attackID).getRulesName()).getRS().get(k).toString());
      }
    }*/
    fmt.format("}");
    int LeftSideConditionsNum = facts.get(attackID).getLeftSideConditions().size();
    if(LeftSideConditionsNum > 0){
      for(int i=0; i<LeftSideConditionsNum;i++){
        fmt.format("\n");
        if(i==0){
          fmt.format("{" + concreteAttackGraphLaTexCMDtype(facts.get(attackID).getLeftSideConditions().get(i),facts,rules,userTypes));
        }else{
          fmt.format(" & " + concreteAttackGraphLaTexCMDtype(facts.get(attackID).getLeftSideConditions().get(i),facts,rules,userTypes));
        } 
        if(i==LeftSideConditionsNum-1){
          fmt.format("}");
        }
      }
    }else{
      fmt.format("\n");
      fmt.format("{empty}");
    }
    fmt.close();
    return sbuf.toString();
  }
  
  public void concreteAttackTraceType(HashMap<Integer, Fixedpoint> facts, HashMap<String, ConcreteRule> rules,HashMap<String, ArrayList<String>> userTypes){
    boolean attack = false;
    int attackFactID = 0;
    for(Map.Entry<Integer, Fixedpoint> entry : facts.entrySet()){
      if(entry.getValue().getTerm().equals(attact)){
        attack = true;
        attackFactID = entry.getKey();
      }
    }
    if(attack){
      System.out.println("RESULT goal reachable: attack");
      System.out.print(concreteAttackGraphLaTexCMDtype(attackFactID,facts,rules,userTypes) + "\n");      
    }else{
      System.out.println("RESULT goal unreachable: attack.");
    }
  } 
}
