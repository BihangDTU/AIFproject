package aifParser;
// Generated from .\aif.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link aifParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface aifVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link aifParser#aif}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAif(aifParser.AifContext ctx);
	/**
	 * Visit a parse tree produced by {@link aifParser#typedec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedec(aifParser.TypedecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Infinite}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfinite(aifParser.InfiniteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Enumeration}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumeration(aifParser.EnumerationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Union}
	 * labeled alternative in {@link aifParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnion(aifParser.UnionContext ctx);
	/**
	 * Visit a parse tree produced by {@link aifParser#terms}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerms(aifParser.TermsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(aifParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildcard(aifParser.WildcardContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposed(aifParser.ComposedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link aifParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstraction(aifParser.AbstractionContext ctx);
	/**
	 * Visit a parse tree produced by {@link aifParser#symdecs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymdecs(aifParser.SymdecsContext ctx);
	/**
	 * Visit a parse tree produced by {@link aifParser#symdec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymdec(aifParser.SymdecContext ctx);
	/**
	 * Visit a parse tree produced by {@link aifParser#aifrule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAifrule(aifParser.AifruleContext ctx);
	/**
	 * Visit a parse tree produced by {@link aifParser#facts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFacts(aifParser.FactsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPFact(aifParser.PFactContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PosCond}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPosCond(aifParser.PosCondContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NegCond}
	 * labeled alternative in {@link aifParser#fact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegCond(aifParser.NegCondContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NoFresh}
	 * labeled alternative in {@link aifParser#arrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoFresh(aifParser.NoFreshContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Fresh}
	 * labeled alternative in {@link aifParser#arrow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFresh(aifParser.FreshContext ctx);
	/**
	 * Visit a parse tree produced by {@link aifParser#vardecs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardecs(aifParser.VardecsContext ctx);
	/**
	 * Visit a parse tree produced by {@link aifParser#vardec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardec(aifParser.VardecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserDef(aifParser.UserDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(aifParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link aifParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntyped(aifParser.UntypedContext ctx);
}