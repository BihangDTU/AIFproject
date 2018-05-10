package fixedpointParser;
// Generated from .\fixedpoint.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link fixedpointParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface fixedpointVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link fixedpointParser#fixedpoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFixedpoint(fixedpointParser.FixedpointContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixedpointParser#terms}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerms(fixedpointParser.TermsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(fixedpointParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Num}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNum(fixedpointParser.NumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildcard(fixedpointParser.WildcardContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposed(fixedpointParser.ComposedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstraction(fixedpointParser.AbstractionContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixedpointParser#aiffixpoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAiffixpoint(fixedpointParser.AiffixpointContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixedpointParser#infers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfers(fixedpointParser.InfersContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixedpointParser#infer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfer(fixedpointParser.InferContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link fixedpointParser#fact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPFact(fixedpointParser.PFactContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixedpointParser#vardecs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardecs(fixedpointParser.VardecsContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixedpointParser#vardec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardec(fixedpointParser.VardecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserDef(fixedpointParser.UserDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(fixedpointParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntyped(fixedpointParser.UntypedContext ctx);
}