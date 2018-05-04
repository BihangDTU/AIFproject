import fixpoint_output.*;
import dataStructure.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws IOException{

		// we expect exactly one argument: the name of the input file
		if (args.length!=1) {
		    System.err.println("\n");
		    System.err.println("Please give as input argument a filename\n");
		    System.exit(-1);
		}
		String filename=args[0];

		// open the input file
		CharStream input = CharStreams.fromFileName(filename);
		
		// create a lexer/scanner
		fixpointsLexer lex = new fixpointsLexer(input);
		
		// get the stream of tokens from the scanner
		CommonTokenStream tokens = new CommonTokenStream(lex);
		
		// create a parser
		fixpointsParser parser = new fixpointsParser(tokens);
		
		// and parse anything from the grammar for "start"
		ParseTree parseTree = parser.fixPoints();

		// A maker for an Abstract Syntax Tree (AST) 
		FixPointASTMaker astmaker = new FixPointASTMaker();
		AST ast=astmaker.visit(parseTree);
		System.out.println(ast);
	}
}


class FixPointASTMaker extends AbstractParseTreeVisitor<AST> implements fixpointsVisitor<AST> {
	
	public AST visitFixPoints(fixpointsParser.FixPointsContext ctx){
		HashMap<Integer, Fixpoint> fixpoints = new HashMap<>();
		for(fixpointsParser.AiffixpointContext fp : ctx.aiffixpoint()){
			Fixpoint f = (Fixpoint)visit(fp);
			fixpoints.put(f.getFactID(), f);
		}
		return new FixpointData(fixpoints);
	}
	
	public AST visitTerms(fixpointsParser.TermsContext ctx){
		//faux.error("This should not be called.\n"); return null;
  	List<Term> terms = new ArrayList<>();
  	for(fixpointsParser.TermContext t : ctx.term()){
  		terms.add((Composed)visit(t));
  	}
  	return new Terms(terms);
	}
	
	public AST visitAtom(fixpointsParser.AtomContext ctx){
		String id=ctx.ID().getText();
  	if (Character.isLowerCase(id.charAt(0))) {
  		// assuming constants should be composed -- correct?
  		return new Composed(id,new ArrayList<Term>());
  	}
  	return new Variable(id);
	}
	
	public AST visitZero(fixpointsParser.ZeroContext ctx){
		return new Value(ctx.getText());
	}
	
	public AST visitWildcard(fixpointsParser.WildcardContext ctx){
		return new Variable("_");
	}
	
	public AST visitComposed(fixpointsParser.ComposedContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	for(fixpointsParser.TermContext t : ctx.terms().term())
  		ts.add((Term)visit(t));
  	return new Composed(ctx.ID().getText(),ts);
	}
	
	public AST visitAbstraction(fixpointsParser.AbstractionContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	for(fixpointsParser.TermContext t : ctx.terms().term())
  		//System.out.println(visit(t).toString());
  		ts.add((Term)visit(t));
  	return new Composed("val",ts);
	}
	
	public AST visitAiffixpoint(fixpointsParser.AiffixpointContext ctx){
		int factID = Integer.parseInt(ctx.NUM().getText());
	  HashMap<String, String> vType = new HashMap<>();
	  if(ctx.vd != null){
	  	for(fixpointsParser.VardecContext vd : ctx.vardecs().vardec()){
		  	vType.put(vd.ID().getText(), vd.vartype().getText());
		  }
	  }
	  Term fact = (Composed)visit(ctx.fact());
	  ArrayList<Integer> leftSideConditions = new ArrayList<>(); 
	  if(ctx.inf != null){
	  	for(fixpointsParser.InferContext inf : ctx.infers().infer()){
		  	leftSideConditions.add(Integer.parseInt(inf.NUM().getText()));
		  }
	  }
	  String rulesName = ctx.ID().getText();	  
		return new Fixpoint(factID,vType,fact,leftSideConditions,rulesName);
	}
	
	public AST visitInfers(fixpointsParser.InfersContext ctx){
		List<Num> nums = new ArrayList<Num>();
		for(fixpointsParser.InferContext inf : ctx.infer()){
			nums.add((Num)visit(inf));
		}
		return new NumList(nums);
	}

	public AST visitInfer(fixpointsParser.InferContext ctx){
		return new Num(ctx.NUM().getText());
	}
	
	public AST visitPFact(fixpointsParser.PFactContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	if (ctx.terms()!=null){
  		for(fixpointsParser.TermContext t : ctx.terms().term())
  			ts.add((Term)visit(t));
  	}
  	return new Composed(ctx.ID().getText(),ts);
	}	
	
	public AST visitVardecs(fixpointsParser.VardecsContext ctx){
		List<Vardec> vardecs = new ArrayList<Vardec>();
		for(fixpointsParser.VardecContext vd : ctx.vardec()){
			vardecs.add((Vardec)visit(vd));
		}
		return new Vardecs(vardecs);
	}	
	
	public AST visitVardec(fixpointsParser.VardecContext ctx){
		Value type = (Value)visit(ctx.vartype());
		return new Vardec(ctx.ID().getText(),type.getValue());
	}	
	
	public AST visitUserDef(fixpointsParser.UserDefContext ctx){
		return new Value(ctx.ID().getText());
	}
	
	public AST visitValue(fixpointsParser.ValueContext ctx){
		return new Value(ctx.getText());
	}	
	
	public AST visitUntyped(fixpointsParser.UntypedContext ctx){
		return new Value(ctx.getText());
	}	

}
