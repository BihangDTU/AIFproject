package fixpointParser;
// Generated from .\fixpoints.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link fixpointsParser}.
 */
public interface fixpointsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link fixpointsParser#fixPoints}.
	 * @param ctx the parse tree
	 */
	void enterFixPoints(fixpointsParser.FixPointsContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixpointsParser#fixPoints}.
	 * @param ctx the parse tree
	 */
	void exitFixPoints(fixpointsParser.FixPointsContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixpointsParser#terms}.
	 * @param ctx the parse tree
	 */
	void enterTerms(fixpointsParser.TermsContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixpointsParser#terms}.
	 * @param ctx the parse tree
	 */
	void exitTerms(fixpointsParser.TermsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterAtom(fixpointsParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitAtom(fixpointsParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Zero}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterZero(fixpointsParser.ZeroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Zero}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitZero(fixpointsParser.ZeroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(fixpointsParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(fixpointsParser.WildcardContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterComposed(fixpointsParser.ComposedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitComposed(fixpointsParser.ComposedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterAbstraction(fixpointsParser.AbstractionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link fixpointsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitAbstraction(fixpointsParser.AbstractionContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixpointsParser#aiffixpoint}.
	 * @param ctx the parse tree
	 */
	void enterAiffixpoint(fixpointsParser.AiffixpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixpointsParser#aiffixpoint}.
	 * @param ctx the parse tree
	 */
	void exitAiffixpoint(fixpointsParser.AiffixpointContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixpointsParser#infers}.
	 * @param ctx the parse tree
	 */
	void enterInfers(fixpointsParser.InfersContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixpointsParser#infers}.
	 * @param ctx the parse tree
	 */
	void exitInfers(fixpointsParser.InfersContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixpointsParser#infer}.
	 * @param ctx the parse tree
	 */
	void enterInfer(fixpointsParser.InferContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixpointsParser#infer}.
	 * @param ctx the parse tree
	 */
	void exitInfer(fixpointsParser.InferContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link fixpointsParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterPFact(fixpointsParser.PFactContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link fixpointsParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitPFact(fixpointsParser.PFactContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixpointsParser#vardecs}.
	 * @param ctx the parse tree
	 */
	void enterVardecs(fixpointsParser.VardecsContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixpointsParser#vardecs}.
	 * @param ctx the parse tree
	 */
	void exitVardecs(fixpointsParser.VardecsContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixpointsParser#vardec}.
	 * @param ctx the parse tree
	 */
	void enterVardec(fixpointsParser.VardecContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixpointsParser#vardec}.
	 * @param ctx the parse tree
	 */
	void exitVardec(fixpointsParser.VardecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterUserDef(fixpointsParser.UserDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitUserDef(fixpointsParser.UserDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterValue(fixpointsParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitValue(fixpointsParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterUntyped(fixpointsParser.UntypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link fixpointsParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitUntyped(fixpointsParser.UntypedContext ctx);
}