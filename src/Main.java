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
  	
  	
  	/*FixpointsSort fps = new FixpointsSort();
  	List<FixedpointsWithType> tt = fps.getTimplies(fpAST);
  	for(FixedpointsWithType f : tt){
  		System.out.println(f.getvType().toString());
  		System.out.println(f.getTerm().toString());
  	}
  	System.out.println("--------------------------------");*/
  	/*Term t1 = tt.get(2).getTerm().getArguments().get(0);
  	Term t2 = tt.get(3).getTerm().getArguments().get(0);
  	System.out.println(t1);
  	System.out.println(t2);
  	boolean isTwoValHaveSame = fps.isTwoValHaveSameForm(t1, t2);
  	if(isTwoValHaveSame){
  		System.out.println("yes");
  	}else{
  		System.out.println("no");
  	}*/
  	/*ArrayList<Term> _zero = new ArrayList<>();
  	ArrayList<Term> args_SU = new ArrayList<>();
  	args_SU.add(new Variable("Server"));
  	args_SU.add(new Variable("User"));
  	ArrayList<Term> args_SH = new ArrayList<>();
  	args_SH.add(new Variable("Server"));
  	args_SH.add(new Variable("Honest"));
  	
  	ArrayList<Term> args_user = new ArrayList<>();
  	args_user.add(new Variable("User"));
  	
  	ArrayList<Term> args_Honest = new ArrayList<>();
  	args_Honest.add(new Variable("Honest"));
  	
  	Term zero = new Composed("0", _zero);
  	Term ring_User = new Composed("ring", args_user);
  	Term ring_Honest = new Composed("ring", args_Honest);
  	Term db__valid_User = new Composed("db__valid", args_SU);
  	Term db__valid_Honest = new Composed("db__valid", args_SH);
  	Term db__revoked = new Composed("db__revoked", args_SU);
  	
  	ArrayList<Term> val1_args = new ArrayList<>();
  	val1_args.add(ring_User);
  	val1_args.add(db__valid_User);
  	val1_args.add(zero);
  	Term val1 = new Composed("val",val1_args);
  	ArrayList<Term> val2_args = new ArrayList<>();
  	val2_args.add(ring_Honest);
  	val2_args.add(db__valid_Honest);
  	val2_args.add(zero);
  	Term val2 = new Composed("val",val2_args);
  	System.out.println(val1);
  	System.out.println(val2);
  	*/
  	//HashMap<String, String> ttmap = fps.getSubstitutionMap(val1, val2);
  	//System.out.println(ttmap);
  	/*if(fps.isVal1GeneralThanVal2(val1, val2, UserDefType)){
  		System.out.println("yes");
  	}else{
  		System.out.println("no");
  	}*/
  	
  /*	System.out.println("--------------------------------");
  	
  	System.out.println("---------------------------------------");
  	List<ArrayList<FixedpointsWithType>> fSorted = fps.fixedpointsSort(fpAST);
  	for(ArrayList<FixedpointsWithType> fs : fSorted){
  		for(FixedpointsWithType fp : fs){
  			//System.out.println(fp.getvType().toString());
    		System.out.println(fp.getTerm().toString());
  		}
  		System.out.println();
  	}
  	System.out.println("--------------------------");*/
/*  	System.out.println(fSorted.get(0).get(0).toString());
  	System.out.println(fSorted.get(0).get(1).toString());
  	if(fps.canT1ImpliesT2(fSorted.get(0).get(0), fSorted.get(0).get(1), tt, UserDefType)){
  		System.out.println("yes");
  	}else{
  		System.out.println("no");
  	}
  	System.out.println("---------------------------------------");*/
 /* 	HashMap<Term,HashSet<Term>> tmap = fps.getTimpliesMap(fpAST);
  	for(Map.Entry<Term,HashSet<Term>> entry : tmap.entrySet()){
  		System.out.print(entry.getKey().toString());
  		System.out.print("  ---->  ");
  		System.out.println(entry.getValue().toString());
  	}*/
  	/*List<FixedpointsWithType> gggg =  fps.getExtendedTimplies(fpAST);
  	for(FixedpointsWithType pp : gggg){
  		System.out.print(pp.getTerm().getArguments().get(0));
  		System.out.print(" ---->");
  		System.out.println(pp.getTerm().getArguments().get(1));
  	}
  	System.out.println("---------------------------------------");
  	List<FixedpointsWithType> timpliesSorted = fps.timpliesSort(gggg);
  	for(FixedpointsWithType t : timpliesSorted){
  		System.out.print(t.getTerm().getArguments().get(0));
  		System.out.print(" ---->");
  		System.out.println(t.getTerm().getArguments().get(1));
  	}
  	System.out.println("---------------------------------------");
  	List<FixedpointsWithType> fixpointWithoutDuplicate = fps.fixedpointsWithoutDuplicate(fSorted, gggg, UserDefType);
  	for(FixedpointsWithType fpw : fixpointWithoutDuplicate){
  		System.out.println(fpw.getTerm().toString());
  	}
  	*/
  	System.out.println("---------------------------------------");
  	
  	FixpointsSort ST = new FixpointsSort();

  	System.out.println("---------------------------------------");
  	HashMap<String, ConcreteRule> concreteRules = new HashMap<>(); 
		for(ConcreteRule cr: ((AIFdata)aifAST).getRules()){
			concreteRules.put(cr.getRulesName(), cr);
		}
		System.out.println(concreteRules.get("serverUpdateKey"));
		AbstractRule absrules = ST.concreteRuleToAbsRuleConversion(aifAST,"serverUpdateKey");
		System.out.println(absrules);
		System.out.println();
		AbstractRule absrule = ST.absRuleSubstitution(absrules); 
		System.out.println(absrule);
		System.out.println("---------------------------------------");
		/*List<ArrayList<FixedpointsWithType>> factsSort = ST.fixedpointsSort(fpAST);
		for(ArrayList<FixedpointsWithType> ts : factsSort){
  		for(FixedpointsWithType t : ts){
  			System.out.println(t.getTerm().toString() + ";");
  		}
  		System.out.println();
  	}
		System.out.println("---------------------------------------");
		ST.applyAbstractRule(absrule,factsSort,UserDefType);
		*/
		
		System.out.println("---------------------------------------");
		HashSet<FixedpointsWithType> result = ST.applyAbsRules(aifAST,fpAST,"serverUpdateKey",UserDefType);
		for(FixedpointsWithType f : result){
			System.out.println(f.getTerm());
		}
		FixpointsSort fp = new FixpointsSort();
		List<FixedpointsWithType> timplies = fp.getExtendedTimplies(fpAST);
		List<FixedpointsWithType> sortedTimplies = fp.timpliesSort(timplies);
		ArrayList<FixedpointsWithType> resultWithoutDuplicate = new ArrayList<>(result);
		List<ArrayList<FixedpointsWithType>> resultWithoutDuplicate1 = new ArrayList<>();
		resultWithoutDuplicate1.add(resultWithoutDuplicate);
		List<FixedpointsWithType> fixpointWithoutDuplicate = fp.fixedpointsWithoutDuplicate(resultWithoutDuplicate1, sortedTimplies, UserDefType);
		
		System.out.println();
		System.out.println("Without duplicate:");
		for(FixedpointsWithType ff : fixpointWithoutDuplicate){
			System.out.println(ff.getTerm());
		}
		
		
		
		
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
		System.out.println("->6.  Print sorted fixpoints                                <-");
		System.out.println("->7.  Print key life-cycle and fixpoints without duplicate  <-");
		System.out.println("->8.  Display Menue                                         <-");
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
  			    node1.printAttack(node1);
  					break;
  				case 6:
  					FixpointsSort fps = new FixpointsSort();
  					List<ArrayList<FixedpointsWithType>> factsSort = fps.fixedpointsSort(fpAST);
  					System.out.println("Sorted Fixpoints:");
  			  	for(ArrayList<FixedpointsWithType> ts : factsSort){
  			  		for(FixedpointsWithType t : ts){
  			  			System.out.println(t.getTerm().toString() + ";");
  			  		}
  			  		System.out.println();
  			  	}
  					break;
  				case 7:
  					FixpointsSort fp = new FixpointsSort();
  					List<FixedpointsWithType> timplies = fp.getExtendedTimplies(fpAST);
  					List<FixedpointsWithType> sortedTimplies = fp.timpliesSort(timplies);
  					List<ArrayList<FixedpointsWithType>> fixedpointsSorted = fp.fixedpointsSort(fpAST);
  					System.out.println("Key life-cycle:");
  			  	for(FixedpointsWithType t : sortedTimplies){
  			  		//System.out.println(t.getvType().toString());
  			  		System.out.print(t.getTerm().getArguments().get(0));
  			  		System.out.print(" ---> ");
  			  		System.out.println(t.getTerm().getArguments().get(1) + ";");
  			  	}
  			  	List<FixedpointsWithType> fixpointWithoutDuplicate = fp.fixedpointsWithoutDuplicate(fixedpointsSorted, sortedTimplies, UserDefType);
  			  	System.out.println();
  			  	System.out.println("Fixpoints without duplicate:");
  			  	for(FixedpointsWithType fpw : fixpointWithoutDuplicate){
  			  		System.out.println(fpw.getTerm().toString());
  			  	}	
  			  	System.out.println();
  					break;
  				case 8:
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

}
