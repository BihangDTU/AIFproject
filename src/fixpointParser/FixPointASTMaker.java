package fixpointParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import dataStructure.AST;
import dataStructure.Composed;
import dataStructure.Fixpoint;
import dataStructure.FixpointData;
import dataStructure.Num;
import dataStructure.NumList;
import dataStructure.Term;
import dataStructure.Terms;
import dataStructure.Value;
import dataStructure.Vardec;
import dataStructure.Vardecs;
import dataStructure.Variable;

public class FixPointASTMaker extends AbstractParseTreeVisitor<AST> implements fixpointsVisitor<AST> {
	
	@Override
	public AST visitFixPoints(fixpointsParser.FixPointsContext ctx){
		HashMap<Integer, Fixpoint> fixpoints = new HashMap<>();
		for(fixpointsParser.AiffixpointContext fp : ctx.aiffixpoint()){
			Fixpoint f = (Fixpoint)visit(fp);
			fixpoints.put(f.getFactID(), f);
		}
		return new FixpointData(fixpoints);
	}
	@Override
	public AST visitTerms(fixpointsParser.TermsContext ctx){
		//faux.error("This should not be called.\n"); return null;
  	List<Term> terms = new ArrayList<>();
  	for(fixpointsParser.TermContext t : ctx.term()){
  		terms.add((Composed)visit(t));
  	}
  	return new Terms(terms);
	}
	@Override
	public AST visitAtom(fixpointsParser.AtomContext ctx){
		String id=ctx.ID().getText();
  	if (Character.isLowerCase(id.charAt(0))) {
  		// assuming constants should be composed -- correct?
  		return new Composed(id,new ArrayList<Term>());
  	}
  	return new Variable(id);
	}
	@Override
	public AST visitZero(fixpointsParser.ZeroContext ctx){
		return new Value(ctx.getText());
	}
	@Override
	public AST visitWildcard(fixpointsParser.WildcardContext ctx){
		return new Variable("_");
	}
	@Override
	public AST visitComposed(fixpointsParser.ComposedContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	for(fixpointsParser.TermContext t : ctx.terms().term())
  		ts.add((Term)visit(t));
  	return new Composed(ctx.ID().getText(),ts);
	}
	@Override
	public AST visitAbstraction(fixpointsParser.AbstractionContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	for(fixpointsParser.TermContext t : ctx.terms().term())
  		//System.out.println(visit(t).toString());
  		ts.add((Term)visit(t));
  	return new Composed("val",ts);
	}
	@Override
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
	@Override
	public AST visitInfers(fixpointsParser.InfersContext ctx){
		List<Num> nums = new ArrayList<Num>();
		for(fixpointsParser.InferContext inf : ctx.infer()){
			nums.add((Num)visit(inf));
		}
		return new NumList(nums);
	}
	@Override
	public AST visitInfer(fixpointsParser.InferContext ctx){
		return new Num(ctx.NUM().getText());
	}
	@Override
	public AST visitPFact(fixpointsParser.PFactContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	if (ctx.terms()!=null){
  		for(fixpointsParser.TermContext t : ctx.terms().term())
  			ts.add((Term)visit(t));
  	}
  	return new Composed(ctx.ID().getText(),ts);
	}	
	@Override
	public AST visitVardecs(fixpointsParser.VardecsContext ctx){
		List<Vardec> vardecs = new ArrayList<Vardec>();
		for(fixpointsParser.VardecContext vd : ctx.vardec()){
			vardecs.add((Vardec)visit(vd));
		}
		return new Vardecs(vardecs);
	}	
	@Override
	public AST visitVardec(fixpointsParser.VardecContext ctx){
		Value type = (Value)visit(ctx.vartype());
		return new Vardec(ctx.ID().getText(),type.getValue());
	}	
	@Override
	public AST visitUserDef(fixpointsParser.UserDefContext ctx){
		return new Value(ctx.ID().getText());
	}
	@Override
	public AST visitValue(fixpointsParser.ValueContext ctx){
		return new Value(ctx.getText());
	}	
	@Override
	public AST visitUntyped(fixpointsParser.UntypedContext ctx){
		return new Value(ctx.getText());
	}	
}
