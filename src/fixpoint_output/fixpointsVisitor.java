package fixpoint_output;
// Generated from .\fixpoints.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link fixpointsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface fixpointsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link fixpointsParser#fixPoints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFixPoints(fixpointsParser.FixPointsContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixpointsParser#terms}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerms(fixpointsParser.TermsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(fixpointsParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Zero}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZero(fixpointsParser.ZeroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWildcard(fixpointsParser.WildcardContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposed(fixpointsParser.ComposedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstraction(fixpointsParser.AbstractionContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixpointsParser#aiffixpoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAiffixpoint(fixpointsParser.AiffixpointContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixpointsParser#infers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfers(fixpointsParser.InfersContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixpointsParser#infer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfer(fixpointsParser.InferContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link fixpointsParser#fact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPFact(fixpointsParser.PFactContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixpointsParser#vardecs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardecs(fixpointsParser.VardecsContext ctx);
	/**
	 * Visit a parse tree produced by {@link fixpointsParser#vardec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardec(fixpointsParser.VardecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserDef(fixpointsParser.UserDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(fixpointsParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntyped(fixpointsParser.UntypedContext ctx);
}