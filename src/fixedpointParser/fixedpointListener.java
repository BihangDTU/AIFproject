package fixedpointParser;
// Generated from .\fixedpoint.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link fixedpointParser}.
 */
public interface fixedpointListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link fixedpointParser#fixedpoint}.
	 * @param ctx the parse tree
	 */
	void enterFixedpoint(fixedpointParser.FixedpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixedpointParser#fixedpoint}.
	 * @param ctx the parse tree
	 */
	void exitFixedpoint(fixedpointParser.FixedpointContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixedpointParser#terms}.
	 * @param ctx the parse tree
	 */
	void enterTerms(fixedpointParser.TermsContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixedpointParser#terms}.
	 * @param ctx the parse tree
	 */
	void exitTerms(fixedpointParser.TermsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void enterAtom(fixedpointParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void exitAtom(fixedpointParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Num}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void enterNum(fixedpointParser.NumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Num}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void exitNum(fixedpointParser.NumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(fixedpointParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Wildcard}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(fixedpointParser.WildcardContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void enterComposed(fixedpointParser.ComposedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Composed}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void exitComposed(fixedpointParser.ComposedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void enterAbstraction(fixedpointParser.AbstractionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link fixedpointParser#term}.
	 * @param ctx the parse tree
	 */
	void exitAbstraction(fixedpointParser.AbstractionContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixedpointParser#aiffixpoint}.
	 * @param ctx the parse tree
	 */
	void enterAiffixpoint(fixedpointParser.AiffixpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixedpointParser#aiffixpoint}.
	 * @param ctx the parse tree
	 */
	void exitAiffixpoint(fixedpointParser.AiffixpointContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixedpointParser#infers}.
	 * @param ctx the parse tree
	 */
	void enterInfers(fixedpointParser.InfersContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixedpointParser#infers}.
	 * @param ctx the parse tree
	 */
	void exitInfers(fixedpointParser.InfersContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixedpointParser#infer}.
	 * @param ctx the parse tree
	 */
	void enterInfer(fixedpointParser.InferContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixedpointParser#infer}.
	 * @param ctx the parse tree
	 */
	void exitInfer(fixedpointParser.InferContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link fixedpointParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterPFact(fixedpointParser.PFactContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PFact}
	 * labeled alternative in {@link fixedpointParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitPFact(fixedpointParser.PFactContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixedpointParser#vardecs}.
	 * @param ctx the parse tree
	 */
	void enterVardecs(fixedpointParser.VardecsContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixedpointParser#vardecs}.
	 * @param ctx the parse tree
	 */
	void exitVardecs(fixedpointParser.VardecsContext ctx);
	/**
	 * Enter a parse tree produced by {@link fixedpointParser#vardec}.
	 * @param ctx the parse tree
	 */
	void enterVardec(fixedpointParser.VardecContext ctx);
	/**
	 * Exit a parse tree produced by {@link fixedpointParser#vardec}.
	 * @param ctx the parse tree
	 */
	void exitVardec(fixedpointParser.VardecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterUserDef(fixedpointParser.UserDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserDef}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitUserDef(fixedpointParser.UserDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterValue(fixedpointParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitValue(fixedpointParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 */
	void enterUntyped(fixedpointParser.UntypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Untyped}
	 * labeled alternative in {@link fixedpointParser#vartype}.
	 * @param ctx the parse tree
	 */
	void exitUntyped(fixedpointParser.UntypedContext ctx);
}