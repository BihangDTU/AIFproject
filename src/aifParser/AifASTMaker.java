package aifParser;
import dataStructure.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AifASTMaker extends AbstractParseTreeVisitor<AST> implements aifVisitor<AST>{
	@Override
	public AST visitAif(aifParser.AifContext ctx){
  	// TODO: extract and store the other information from a
  	// specification (sets, types, functions)
  	List<Type> types = new ArrayList<Type>();
  	HashSet<String> buildInTypes=new HashSet<String>();
  	for(aifParser.TypedecContext t : ctx.typedec()){
  		types.add((Type)visit(t));
  	}
  	Functions functions = new Functions((Functions)visit(ctx.symdecs(0)));
  	Functions facts = new Functions((Functions)visit(ctx.symdecs(1))); 
  	List<Term> sets = new ArrayList<Term>();
  	for(aifParser.TermContext set : ctx.terms().term()){
  		sets.add((Term)visit(set));
  	}
  	//Terms sets = (Terms)visit(ctx.terms());
  	List<ConcreteRules> rules = new ArrayList<ConcreteRules>();
  	for(aifParser.AifruleContext r : ctx.aifrule()){
  		ConcreteRules cr = (ConcreteRules)visit(r); 
  		rules.add(cr);
  		for (Map.Entry<String, String> varsType : cr.getVarsTypes().entrySet()) {
  			if(Character.isLowerCase(varsType.getValue().charAt(0))){
  				buildInTypes.add(varsType.getValue());
  			}
  		}
  	}
  	return new AIFdata(types,sets,functions,facts,rules,buildInTypes);
  };

  // TODO: implement (when AST has a way to store the information)
  @Override
  public AST visitTypedec(aifParser.TypedecContext ctx){
  	String id = ctx.ID().getText();
  	List<String> types = new ArrayList<String>();
  	ListTypes ts = (ListTypes)visit(ctx.type());
  	for(String t : ts.getTypes()){
  		types.add(t);
  	}
  	return new Type(id,types);
  };
  @Override
  public AST visitInfinite(aifParser.InfiniteContext ctx){
  	String inf = ctx.getText();
  	List<String> types = new ArrayList<String>();
  	types.add(inf);
  	return new ListTypes(types);
  };
  @Override
  public AST visitEnumeration(aifParser.EnumerationContext ctx){
  	List<String> types = new ArrayList<String>();
  	for(TerminalNode t  : ctx.ID()){
  		types.add(t.toString());
  	}
  	return new ListTypes(types);
  };
  @Override
  public AST visitUnion(aifParser.UnionContext ctx){
  	List<String> types = new ArrayList<String>();
  	for(TerminalNode t : ctx.ID()){
  		types.add(t.toString());
  	}
  	return new ListTypes(types);
  };

  // Terms: the outermost should not be called
  @Override
  public AST visitTerms(aifParser.TermsContext ctx){
  	faux.error("This should not be called.\n"); return null;
  };
  @Override
  public AST visitAtom(aifParser.AtomContext ctx){
  	String id=ctx.ID().getText();
  	if (Character.isLowerCase(id.charAt(0))) {
  		// assuming constants should be composed -- correct?
  		return new Composed(id,new ArrayList<Term>());
  	}
  	return new Variable(id);
  };
  @Override
  public AST visitWildcard(aifParser.WildcardContext ctx){
  	return new Variable("_"); // There is nothing in the AST for this
  };
  @Override
  public AST visitComposed(aifParser.ComposedContext ctx){
  	ArrayList<Term> ts = new ArrayList<Term>();
  	for(aifParser.TermContext t : ctx.terms().term())
  		ts.add((Term)visit(t));
  	return new Composed(ctx.ID().getText(),ts);
  };
  @Override
  public AST visitAbstraction(aifParser.AbstractionContext ctx){
  	ArrayList<Term> ts = new ArrayList<Term>();
  	for(aifParser.TermContext t : ctx.terms().term())
  		ts.add((Term)visit(t));
  	return new Composed("val",ts);
  };

  // Symbol declarations missing in the AST currently
  @Override
  public AST visitSymdecs(aifParser.SymdecsContext ctx){
  	Functions fs = new Functions();
  	for(int i=0;i<ctx.symdec().size();i++){
  		fs.addFunction((Function)visit(ctx.symdec(i)));
  	}
  	return fs;
  };
  @Override
  public AST visitSymdec(aifParser.SymdecContext ctx){
  	String funName = ctx.ID().getText();
  	String num = ctx.NUM().getText();
  	return new Function(funName, num);
  };

  // Parsing of a rule
  @Override
  public AST visitAifrule(aifParser.AifruleContext ctx){
  	String rulesName=ctx.ID().getText();
  	HashMap<String, String> varsTypes=new HashMap<String,String>(); 

  	ArrayList<Term> LF=new ArrayList<Term>();
  	ArrayList<Condition> Splus=new ArrayList<Condition>();
  	ArrayList<Condition> Snega=new ArrayList<Condition>();
  	//Variable newVariable=(Variable) visit(ctx.arrow());
  	Freshs freshs = (Freshs)visit(ctx.arrow());
  	ArrayList<Term> RF=new ArrayList<Term>();
  	ArrayList<Condition> RS=new ArrayList<Condition>();

  	for(aifParser.VardecContext vd : ctx.vardecs().vardec()){
  		varsTypes.put(vd.ID().getText(),vd.vartype().getText());
  		// the "new Variable" won't work, please see my comment in the AST
  		// also "vd.varype().getText()" literally returns the text of the
  		// declaration, because the AST you have defined does not have structure for the
  		// different kinds of types!
  	}

  	if (ctx.lhs!=null){
  		for(aifParser.FactContext f : ctx.lhs.fact()){
  			AST parsed_f = visit(f);
  			if (parsed_f instanceof Composed)
  				LF.add((Composed)parsed_f);
  			else{
  				Condition c= (Condition) parsed_f;
  				if (c.positive)
  					Splus.add(c);
  				else
  					Snega.add(c);
  			}
  		}
  	}

  	for(aifParser.FactContext f : ctx.rhs.fact()){
  		AST parsed_f = visit(f);
  		if (parsed_f instanceof Composed)
  			RF.add((Composed)parsed_f);
  		else{
  			Condition c= (Condition) parsed_f;
  			if (c.positive)
  				RS.add(c);
  			else
  				faux.error("Negative Condition on RHS");
  		}
  	}

  	return new ConcreteRules(rulesName,varsTypes,LF,Splus,Snega,freshs,RF,RS);
  };
  @Override
  public AST visitFacts(aifParser.FactsContext ctx){
  	faux.error("This should not be called.\n"); return null;
  };
  @Override
  public AST visitPFact(aifParser.PFactContext ctx){
  	ArrayList<Term> ts = new ArrayList<Term>();
  	if (ctx.terms()!=null){
  		for(aifParser.TermContext t : ctx.terms().term())
  			ts.add((Term)visit(t));
  	}
  	return new Composed(ctx.ID().getText(),ts);
  };
  @Override
  public AST visitPosCond(aifParser.PosCondContext ctx){
  	return new Condition(new Variable(ctx.ID().getText()),(Term)visit(ctx.term()),true);
  };
  @Override
  public AST visitNegCond(aifParser.NegCondContext ctx){
  	return new Condition(new Variable(ctx.ID().getText()),(Term)visit(ctx.term()),false);
  };
  @Override
  public AST visitNoFresh(aifParser.NoFreshContext ctx){
  	return new Freshs();
  };
  @Override
  public AST visitFresh(aifParser.FreshContext ctx){
  	// TODO: handle more than one variable!
  	List<Variable> vars = new ArrayList<Variable>();
  	for(TerminalNode var : ctx.ID()){
  		vars.add(new Variable(var.getText()));
  	}
  	return new Freshs(vars);
  };
  @Override
  public AST visitVardecs(aifParser.VardecsContext ctx){
  	faux.error("This should not be called.\n"); return null;
  };
  @Override
  public AST visitVardec(aifParser.VardecContext ctx){
  	return new Vardec(ctx.ID().getText(),ctx.getText());
  };

  // Following have nothing in the AST to store
  @Override
  public AST visitUserDef(aifParser.UserDefContext ctx){
  	return new Value(ctx.ID().getText());
  };
  @Override
  public AST visitValue(aifParser.ValueContext ctx){
  	return new Value(ctx.getText());
  };
  @Override
  public AST visitUntyped(aifParser.UntypedContext ctx){
  	return new Value(ctx.getText());
  };
}

