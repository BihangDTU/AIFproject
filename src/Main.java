import aifParser.*;
import fixpointParser.*;

import dataStructure.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws IOException{

  	// we expect exactly one argument: the name of the input file
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
  	fixpointsLexer lex2 = new fixpointsLexer(input2);
  	
  	// get the stream of tokens from the scanner
  	CommonTokenStream tokens1 = new CommonTokenStream(lex1);
  	CommonTokenStream tokens2 = new CommonTokenStream(lex2);
  	
  	// create a parser
  	aifParser parser1 = new aifParser(tokens1);
  	fixpointsParser parser2 = new fixpointsParser(tokens2);
  	
  	// and parse anything from the grammar for "start"
  	ParseTree parseTree1 = parser1.aif();
  	ParseTree parseTree2 = parser2.fixPoints();
  
  	// A maker for an Abstract Syntax Tree (AST) 
  	AifASTMaker aifASTmaker = new AifASTMaker();
  	AST aifAST=aifASTmaker.visit(parseTree1);
  	FixPointASTMaker fpASTmaker = new FixPointASTMaker();
  	AST fpAST=fpASTmaker.visit(parseTree2);

  	HashSet<String> buildInTypes = ((AIFdata)aifAST).getBuildInTypes();  
  	 	
  	//System.out.println(buildInTypes);
  	
  	Scanner scanner = new Scanner(System.in);
  	displayMenu();
  	invokeFunctions(scanner,aifAST,fpAST);
  }
  public static void displayMenu(){
  	System.out.println("--------------------------------------------------------");
  	System.out.println("->0.  Exit program                                    <-");
		System.out.println("->1.  Print .aifom file on Console                    <-");
		System.out.println("->2.  Print .HC file on Console                       <-");
		System.out.println("->3.  Generate abstract attack trace in LaTex Command <-");
		System.out.println("->4.  Genrate concrete attack in LaTex command        <-");
		System.out.println("->5.  Display Menue                                   <-");
		System.out.println("--------------------------------------------------------");
  }
  
	public static void invokeFunctions(Scanner scanner,AST aifAST,AST fpAST){
		boolean runing = true;
		while(runing) {
			System.out.println("Enter operation ID: ");
			//Scanner scanner = new Scanner(System.in);
			String functionIdStr = scanner.nextLine();
			//scanner.close();
			int functionId = Integer.parseInt(functionIdStr);
			switch(functionId) {
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
					abstractAttackTrace.abstractAttackTrace((((FixpointData)fpAST)).getFixpoints());
			  	System.out.println();
					break;
				case 4:
					AttackTrace concreteAttackTrace = new AttackTrace();
					HashMap<String, ConcreteRules> rules = new HashMap<>(); 
					for(ConcreteRules cr: ((AIFdata)aifAST).getRules()){
						rules.put(cr.getRulesName(), cr);
					}
					AttackInfo AttInfo = concreteAttackTrace.concreteAttackTrace(((FixpointData)fpAST).getFixpoints(),rules);
			    System.out.println("Attack trace: "+AttInfo.getAttackTraces());
			    System.out.println();
			    System.out.println("LaTex command:");
			    System.out.println(AttInfo.getLaTaxCMD());
			    System.out.println();
					break;
				case 5:
					displayMenu();
					break;
				case 6:

					break;
				case 7:

					break;
				default:
					System.out.println("Invalid operation ID. try again");
					break;	
			}
			
		}
	}

}
