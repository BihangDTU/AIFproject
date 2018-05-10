package fixedpointParser;
import dataStructure.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class FixedPointASTMaker extends AbstractParseTreeVisitor<AST> implements fixedpointVisitor<AST> {
	
	@Override
	public AST visitFixedpoint(fixedpointParser.FixedpointContext ctx) {
		HashMap<Integer, Fixedpoint> fixpoints = new HashMap<>();
		for(fixedpointParser.AiffixpointContext fp : ctx.aiffixpoint()){
			Fixedpoint f = (Fixedpoint)visit(fp);
			fixpoints.put(f.getFactID(), f);
		}
		return new FixedpointData(fixpoints);
	}
	@Override
	public AST visitTerms(fixedpointParser.TermsContext ctx){
		faux.error("This should not be called.\n"); return null;
  	/*List<Term> terms = new ArrayList<>();
  	for(fixpointsParser.TermContext t : ctx.term()){
  		terms.add((Composed)visit(t));
  	}
  	return new Terms(terms);*/
	}
	@Override
	public AST visitAtom(fixedpointParser.AtomContext ctx){
		String id=ctx.ID().getText();
  	if (Character.isLowerCase(id.charAt(0)) || (id.charAt(0) == '0')) {
  		return new Composed(id,new ArrayList<Term>());
  	}
  	return new Variable(id);
	}
	@Override
	public AST visitNum(fixedpointParser.NumContext ctx){
		return new Composed(ctx.getText());
	}
	@Override
	public AST visitWildcard(fixedpointParser.WildcardContext ctx){
		return new Variable("_");
	}
	@Override
	public AST visitComposed(fixedpointParser.ComposedContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	for(fixedpointParser.TermContext t : ctx.terms().term())
  		ts.add((Term)visit(t));
  	return new Composed(ctx.ID().getText(),ts);
	}
	@Override
	public AST visitAbstraction(fixedpointParser.AbstractionContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	for(fixedpointParser.TermContext t : ctx.terms().term())
  		//System.out.println(visit(t).toString());
  		ts.add((Term)visit(t));
  	return new Composed("val",ts);
	}
	@Override
	public AST visitAiffixpoint(fixedpointParser.AiffixpointContext ctx){
		int factID = Integer.parseInt(ctx.NUM().getText());
	  HashMap<String, String> vType = new HashMap<>();
	  if(ctx.vd != null){
	  	for(fixedpointParser.VardecContext vd : ctx.vardecs().vardec()){
		  	vType.put(vd.ID().getText(), vd.vartype().getText());
		  }
	  }
	  Term fact = (Composed)visit(ctx.fact());
	  ArrayList<Integer> leftSideConditions = new ArrayList<>(); 
	  if(ctx.inf != null){
	  	for(fixedpointParser.InferContext inf : ctx.infers().infer()){
		  	leftSideConditions.add(Integer.parseInt(inf.NUM().getText()));
		  }
	  }
	  String rulesName = ctx.ID().getText();	  
		return new Fixedpoint(factID,vType,fact,leftSideConditions,rulesName);
	}
	@Override
	public AST visitInfers(fixedpointParser.InfersContext ctx){
		List<Num> nums = new ArrayList<Num>();
		for(fixedpointParser.InferContext inf : ctx.infer()){
			nums.add((Num)visit(inf));
		}
		return new NumList(nums);
	}
	@Override
	public AST visitInfer(fixedpointParser.InferContext ctx){
		return new Num(ctx.NUM().getText());
	}
	@Override
	public AST visitPFact(fixedpointParser.PFactContext ctx){
		ArrayList<Term> ts = new ArrayList<Term>();
  	if (ctx.terms()!=null){
  		for(fixedpointParser.TermContext t : ctx.terms().term())
  			ts.add((Term)visit(t));
  	}
  	return new Composed(ctx.ID().getText(),ts);
	}	
	@Override
	public AST visitVardecs(fixedpointParser.VardecsContext ctx){
		List<Vardec> vardecs = new ArrayList<Vardec>();
		for(fixedpointParser.VardecContext vd : ctx.vardec()){
			vardecs.add((Vardec)visit(vd));
		}
		return new Vardecs(vardecs);
	}	
	@Override
	public AST visitVardec(fixedpointParser.VardecContext ctx){
		Value type = (Value)visit(ctx.vartype());
		return new Vardec(ctx.ID().getText(),type.getValue());
	}	
	@Override
	public AST visitUserDef(fixedpointParser.UserDefContext ctx){
		return new Value(ctx.ID().getText());
	}
	@Override
	public AST visitValue(fixedpointParser.ValueContext ctx){
		return new Value(ctx.getText());
	}	
	@Override
	public AST visitUntyped(fixedpointParser.UntypedContext ctx){
		return new Value(ctx.getText());
	}	
}
