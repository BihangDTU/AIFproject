package aifParser;
// Generated from .\aif.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link aifParser}.
 */
public interface aifListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link aifParser#aif}.
	 * @param ctx the parse tree
	 */
	void enterAif(aifParser.AifContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#aif}.
	 * @param ctx the parse tree
	 */
	void exitAif(aifParser.AifContext ctx);
	/**
	 * Enter a parse tree produced by {@link aifParser#typedec}.
	 * @param ctx the parse tree
	 */
	void enterTypedec(aifParser.TypedecContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#typedec}.
	 * @param ctx the parse tree
	 */
	void exitTypedec(aifParser.TypedecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Infinite}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 */
	void enterInfinite(aifParser.InfiniteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Infinite}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 */
	void exitInfinite(aifParser.InfiniteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Enumeration}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 */
	void enterEnumeration(aifParser.EnumerationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Enumeration}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 */
	void exitEnumeration(aifParser.EnumerationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Union}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 */
	void enterUnion(aifParser.UnionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Union}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 */
	void exitUnion(aifParser.UnionContext ctx);
	/**
	 * Enter a parse tree produced by {@link aifParser#terms}.
	 * @param ctx the parse tree
	 */
	void enterTerms(aifParser.TermsContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#terms}.
	 * @param ctx the parse tree
	 */
	void exitTerms(aifParser.TermsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 */
	void enterAtom(aifParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 */
	void exitAtom(aifParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(aifParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(aifParser.WildcardContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 */
	void enterComposed(aifParser.ComposedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 */
	void exitComposed(aifParser.ComposedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 */
	void enterAbstraction(aifParser.AbstractionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 */
	void exitAbstraction(aifParser.AbstractionContext ctx);
	/**
	 * Enter a parse tree produced by {@link aifParser#symdecs}.
	 * @param ctx the parse tree
	 */
	void enterSymdecs(aifParser.SymdecsContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#symdecs}.
	 * @param ctx the parse tree
	 */
	void exitSymdecs(aifParser.SymdecsContext ctx);
	/**
	 * Enter a parse tree produced by {@link aifParser#symdec}.
	 * @param ctx the parse tree
	 */
	void enterSymdec(aifParser.SymdecContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#symdec}.
	 * @param ctx the parse tree
	 */
	void exitSymdec(aifParser.SymdecContext ctx);
	/**
	 * Enter a parse tree produced by {@link aifParser#aifrule}.
	 * @param ctx the parse tree
	 */
	void enterAifrule(aifParser.AifruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#aifrule}.
	 * @param ctx the parse tree
	 */
	void exitAifrule(aifParser.AifruleContext ctx);
	/**
	 * Enter a parse tree produced by {@link aifParser#facts}.
	 * @param ctx the parse tree
	 */
	void enterFacts(aifParser.FactsContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#facts}.
	 * @param ctx the parse tree
	 */
	void exitFacts(aifParser.FactsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterPFact(aifParser.PFactContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitPFact(aifParser.PFactContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PosCond}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterPosCond(aifParser.PosCondContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PosCond}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitPosCond(aifParser.PosCondContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NegCond}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterNegCond(aifParser.NegCondContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NegCond}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitNegCond(aifParser.NegCondContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NoFresh}
	 * labeled alternative in {@link aifParser#arrow}.
	 * @param ctx the parse tree
	 */
	void enterNoFresh(aifParser.NoFreshContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NoFresh}
	 * labeled alternative in {@link aifParser#arrow}.
	 * @param ctx the parse tree
	 */
	void exitNoFresh(aifParser.NoFreshContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Fresh}
	 * labeled alternative in {@link aifParser#arrow}.
	 * @param ctx the parse tree
	 */
	void enterFresh(aifParser.FreshContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Fresh}
	 * labeled alternative in {@link aifParser#arrow}.
	 * @param ctx the parse tree
	 */
	void exitFresh(aifParser.FreshContext ctx);
	/**
	 * Enter a parse tree produced by {@link aifParser#vardecs}.
	 * @param ctx the parse tree
	 */
	void enterVardecs(aifParser.VardecsContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#vardecs}.
	 * @param ctx the parse tree
	 */
	void exitVardecs(aifParser.VardecsContext ctx);
	/**
	 * Enter a parse tree produced by {@link aifParser#vardec}.
	 * @param ctx the parse tree
	 */
	void enterVardec(aifParser.VardecContext ctx);
	/**
	 * Exit a parse tree produced by {@link aifParser#vardec}.
	 * @param ctx the parse tree
	 */
	void exitVardec(aifParser.VardecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterUserDef(aifParser.UserDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitUserDef(aifParser.UserDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterValue(aifParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitValue(aifParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterUntyped(aifParser.UntypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitUntyped(aifParser.UntypedContext ctx);
}