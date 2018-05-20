import aifParser.*;
import fixedpointParser.*;
import dataStructure.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws IOException{
  
  	// we expect exactly two argument: the name of the input file
  if (args.length!=2) {
    System.err.println("\n");
    System.err.println("Please give as input argument a filename\n");
    System.exit(-1);
  }
  String filename1=args[0];
  String filename2=args[1];
  
  // open the input file
  CharStream input1 = CharStreams.fromFileName(filename1);
  CharStream input2 = CharStreams.fromFileName(filename2);
  
  // create a lexer/scanner
  aifLexer lex1 = new aifLexer(input1);
  fixedpointLexer lex2 = new fixedpointLexer(input2);
  
  // get the stream of tokens from the scanner
  CommonTokenStream tokens1 = new CommonTokenStream(lex1);
  CommonTokenStream tokens2 = new CommonTokenStream(lex2);
  
  // create a parser
  aifParser parser1 = new aifParser(tokens1);
  fixedpointParser parser2 = new fixedpointParser(tokens2);
  
  // and parse anything from the grammar for "start"
  ParseTree parseTree1 = parser1.aif();
  ParseTree parseTree2 = parser2.fixedpoint();
  
  // A maker for an Abstract Syntax Tree (AST) 
  AifASTMaker aifASTmaker = new AifASTMaker();
  AST aifAST=aifASTmaker.visit(parseTree1);
  FixedPointASTMaker fpASTmaker = new FixedPointASTMaker();
  AST fpAST=fpASTmaker.visit(parseTree2);
  
  /*get user define types. for infinite agents {...}, 
    only use lower case of it's type as agent e.g. Honest = {...} to Honest = {honest} 
    e.g. {Agent=[server, dishon, honest], Honest=[honest], User=[dishon, honest], 
    Sts=[valid, revoked], Server=[server], Dishon=[dishon]}
  */
  HashMap<String,List<Term>> UserType = new HashMap<>();
  for(Type ty : ((AIFdata)aifAST).getTypes()){
    List<Term> agents = new ArrayList<Term>();
    if(!ty.getAgents().isEmpty()){
      for(String agent : ty.getAgents()){
        if(Character.isLowerCase(agent.charAt(0))){
          agents.add(new Composed(agent));
        }else if(!Character.isUpperCase(agent.charAt(0))){
          agents.add(new Composed(ty.getUserType().toLowerCase()));
        }else{
          agents.addAll(UserType.get(agent));
        }
      }
    }
    //remove duplicate agents in agent List 
    UserType.put(ty.getUserType(), new ArrayList<>(new HashSet<>(agents)));
  }
  System.out.println(UserType);
  
  
  HashMap<String,List<String>> UserDefType = new HashMap<>();
  for(Type ty : ((AIFdata)aifAST).getTypes()){
    List<String> types = new ArrayList<String>();
    if(!ty.getAgents().isEmpty()){
      if(Character.isUpperCase(ty.getAgents().get(0).charAt(0))){
        //types.add(ty.getUserType());
        for(String t : ty.getAgents()){
          types.add(t);
        }
      }else{
        types.add(ty.getUserType());
      }
    }
    UserDefType.put(ty.getUserType(), new ArrayList<>(new HashSet<>(types)));
  }
  System.out.println(UserDefType);
  System.out.println("--------------------------------------------");
  
  
  displayMenu();
  invokeFunctions(aifAST,fpAST,UserType,UserDefType);
  };
  
  public static void displayMenu(){
    System.out.println("--------------------------------------------------------------");
    System.out.println("->0.  Exit program                                          <-");
    System.out.println("->1.  Print .aifom file on Console                          <-");
    System.out.println("->2.  Print .HC file on Console                             <-");
    System.out.println("->3.  Generate abstract attack trace in LaTex Command       <-");
    System.out.println("->4.  Genrate concrete attack in LaTex command              <-");
    System.out.println("->5.  Genrate concrete attack with state transition         <-");
    System.out.println("->6.  Print sorted fixed-points                             <-");
    System.out.println("->7.  Print key life-cycle and fixpoints without duplicate  <-");
    System.out.println("->8.  Check facts                                           <-");
    System.out.println("->9.  Display Menue                                         <-");
    System.out.println("--------------------------------------------------------------");
  }
  
  public static void invokeFunctions(AST aifAST,AST fpAST,HashMap<String,List<Term>> UserType, HashMap<String,List<String>> UserDefType){
    Scanner scanner = new Scanner(System.in);
    boolean runing = true;
    while(runing) {
      System.out.println("Enter operation ID: ");
      try {
        int operationID = scanner.nextInt();// it only allow to input number, need to fix
        switch(operationID) {
          case 0:
          	runing = false;
          	System.out.println("Program exited");
          	break;
          case 1:
            System.out.println(aifAST);
            break;
          case 2:
            System.out.println(fpAST);
            break;
          case 3:
          	AttackTrace abstractAttackTrace = new AttackTrace(); 
          	System.out.println("LaTex command:");
          	abstractAttackTrace.abstractAttackTrace((((FixedpointData)fpAST)).getFixpoints());
          	System.out.println();
          	break;
          case 4:
          	AttackTrace concreteAttackTrace = new AttackTrace();
          	HashMap<String, ConcreteRule> rules = new HashMap<>(); 
          	for(ConcreteRule cr: ((AIFdata)aifAST).getRules()){
          	  rules.put(cr.getRulesName(), cr);
          	}
          	AttackInfo AttInfo = concreteAttackTrace.concreteAttackTrace(((FixedpointData)fpAST).getFixpoints(),rules);
            System.out.println("Attack trace: "+AttInfo.getAttackTraces());
            System.out.println();
            System.out.println("LaTex command:");
            System.out.println(AttInfo.getLaTaxCMD());
            System.out.println();
          	break;
          case 5:
            //get aif build in types e.g. {value, untyped}
          	HashSet<String> buildInTypes = ((AIFdata)aifAST).getBuildInTypes();
          	StateTransition ST = new StateTransition();
            ST.setBuildInTypes(buildInTypes);
            State state = new State();
            Node stateNode = new Node(state);
          	
            AttackTrace cAttackTrace = new AttackTrace();
          	HashMap<String, ConcreteRule> concreteRules = new HashMap<>(); 
          	for(ConcreteRule cr: ((AIFdata)aifAST).getRules()){
          		concreteRules.put(cr.getRulesName(), cr);
          	}
          	AttackInfo attackInfo = cAttackTrace.concreteAttackTrace(((FixedpointData)fpAST).getFixpoints(),concreteRules);
          	Node node1 = ST.stateTransition(stateNode,concreteRules,attackInfo.getAttackTraces(),UserType,((AIFdata)aifAST).getSets());
          	System.out.println();
          	System.out.println();
          	node1.printAttackPath(node1);
          	break;
          case 6:
          	FixpointsSort fps = new FixpointsSort();
          	List<ArrayList<FactWithType>> factsSort = fps.fixedpointsSort(fpAST);
          	System.out.println("Sorted Fixpoints:");
          	for(ArrayList<FactWithType> ts : factsSort){
          		for(FactWithType t : ts){
          			System.out.println(t.getTerm().toString() + ";");
          		}
          		System.out.println();
          	}
          	break;
          case 7:
          	FixpointsSort fp = new FixpointsSort();
          	List<FactWithType> timps = fp.getTimplies(fpAST);
          	List<FactWithType> timplies = fp.getExtendedTimplies(fpAST,UserDefType);
          	List<ArrayList<FactWithType>> sortedTimplies = fp.timpliesSort(timplies);
          	List<ArrayList<FactWithType>> fixedpointsSorted = fp.fixedpointsSort(fpAST);
          	List<List<FactWithType>> timpliesClassified = fp.classifyTimpliesInTypes(timplies);
          	          	
          	System.out.println("Key life-cycle:");
          	fp.timpliesOrdered(timpliesClassified,timps);
          	System.out.println();
          	//for(ArrayList<FactWithType> ts : sortedTimplies){
          	//  for(FactWithType t : ts){
          	//    System.out.println(t);
          	    /*System.out.println(t.getvType().toString());
                System.out.print(t.getTerm().getArguments().get(0));
                System.out.print(" ---> ");
                System.out.println(t.getTerm().getArguments().get(1) + ";"); */
          	//  }
          	//  System.out.println();
          	//}
          	List<ArrayList<FactWithType>> fixpointWithoutDuplicate = fp.fixedpointsWithoutDuplicate(fixedpointsSorted,timplies, UserDefType);
          	System.out.println();
          	System.out.println("Fixpoints without duplicate:");
          	for(ArrayList<FactWithType> fpw : fixpointWithoutDuplicate){
          		for(FactWithType f : fpw){
          			System.out.println(f.getTerm().toString());
          		}
          	}	
          	System.out.println();
          	break;
          case 8:
            checkFact(aifAST,fpAST,UserDefType);
            break;
          case 9:
            displayMenu();
            break;
          default:
            System.out.println("Invalid operation ID. try again");
            break;	
    		}
    	}catch (java.util.InputMismatchException e){
    	  System.err.println("Invalid input. Program exited");
    	  System.exit(-1);
    	}
    }
    scanner.close();
  }
  
  public static void checkFact(AST aifAST,AST fpAST, HashMap<String,List<String>> UserDefType){
    Scanner scanner = new Scanner(System.in);
    while(true) {
      System.out.println("Enter rule name ('exit' for exit this function): ");
      String ruleName = scanner.nextLine();
      if(ruleName.equals("exit")) break;
      
      FixpointsSort ST = new FixpointsSort();
      List<FactWithType> newGenerateFacts = ST.applyAbsRuleWithSatisfiedFacts(aifAST,fpAST,ruleName,UserDefType);
      HashSet<FactWithType> newGenerateFactsWithoutDuplicate = new HashSet<>(newGenerateFacts);
      
      System.out.println("Facts generated by apply rule with satisfied keys:");
      for(FactWithType t : newGenerateFactsWithoutDuplicate){
        System.out.println(t.getTerm().toString());
      }
      System.out.println();
      ConcreteRule concreteRule =  ST.getConcreteRuleByRuleName(aifAST,ruleName);
      
      for(FactWithType t : newGenerateFactsWithoutDuplicate){
        ConcreteFact ff = ST.absFactToConcreteFact(t, concreteRule);
        System.out.println(ff.toString());
      }
      System.out.println();
      System.out.println();
    }
    
    scanner.close();
  }

}
