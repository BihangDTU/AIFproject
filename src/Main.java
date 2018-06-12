import aifParser.*;
import fixedpointParser.*;
import dataStructure.*;

import myException.UnificationFailedException;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;

import java.io.FileWriter;
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
      for(String agent : ty.getAgents()){
        if(Character.isLowerCase(agent.charAt(0))){
          agents.add(new Composed(agent));
        }else if(!Character.isUpperCase(agent.charAt(0))){
          agents.add(new Composed(ty.getUserType().toLowerCase()));
        }else{
          agents.addAll(UserType.get(agent));
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
    
    
    System.out.println("-----------------------------------------------------------");
    /*HashSet<String> buildInTypes = ((AIFdata)aifAST).getBuildInTypes();
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
    Node node1 = ST.stateTransition(stateNode,concreteRules,attackInfo.getAttackTraces(),UserType);
    System.out.println();
    //node1.printTree(node1, "   ");
    System.out.println();
    node1.printAttackPath(node1);
    System.out.println();
    System.out.println();
    AttackTrace abstractAttackTrace = new AttackTrace(); 
    System.out.println("LaTex command:");
    abstractAttackTrace.abstractAttackTrace((((FixedpointData)fpAST)).getFixpoints());
    System.out.println();
   // if(!attackInfo.isAttackReachable()){
      VerifyFixedpoint vf = new VerifyFixedpoint(); 
      vf.verifyFixedpoint(aifAST,fpAST,UserDefType);
   // }
    
    */
    
    ComputeFixedpoint cp = new ComputeFixedpoint(aifAST);
   cp.generateHornClause(aifAST,UserDefType);
   
    
  };
  
}