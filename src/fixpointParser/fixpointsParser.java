package fixpointParser;
// Generated from .\fixpoints.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class fixpointsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, NUM=14, ID=15, WHITESPACE=16, COMMENT=17;
	public static final int
		RULE_fixPoints = 0, RULE_terms = 1, RULE_term = 2, RULE_aiffixpoint = 3, 
		RULE_infers = 4, RULE_infer = 5, RULE_fact = 6, RULE_vardecs = 7, RULE_vardec = 8, 
		RULE_vartype = 9;
	public static final String[] ruleNames = {
		"fixPoints", "terms", "term", "aiffixpoint", "infers", "infer", "fact", 
		"vardecs", "vardec", "vartype"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'0'", "'_'", "'('", "')'", "'val'", "'.'", "'<='", "';'", 
		"'+'", "':'", "'value'", "'untyped'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "NUM", "ID", "WHITESPACE", "COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "fixpoints.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public fixpointsParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FixPointsContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(fixpointsParser.EOF, 0); }
		public List<AiffixpointContext> aiffixpoint() {
			return getRuleContexts(AiffixpointContext.class);
		}
		public AiffixpointContext aiffixpoint(int i) {
			return getRuleContext(AiffixpointContext.class,i);
		}
		public FixPointsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fixPoints; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterFixPoints(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitFixPoints(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitFixPoints(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FixPointsContext fixPoints() throws RecognitionException {
		FixPointsContext _localctx = new FixPointsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_fixPoints);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(20);
				aiffixpoint();
				}
				}
				setState(25);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(26);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermsContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TermsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terms; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterTerms(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitTerms(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitTerms(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermsContext terms() throws RecognitionException {
		TermsContext _localctx = new TermsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_terms);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			term();
			setState(33);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(29);
				match(T__0);
				setState(30);
				term();
				}
				}
				setState(35);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	 
		public TermContext() { }
		public void copyFrom(TermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ZeroContext extends TermContext {
		public ZeroContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterZero(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitZero(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitZero(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ComposedContext extends TermContext {
		public TerminalNode ID() { return getToken(fixpointsParser.ID, 0); }
		public TermsContext terms() {
			return getRuleContext(TermsContext.class,0);
		}
		public ComposedContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterComposed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitComposed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitComposed(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WildcardContext extends TermContext {
		public WildcardContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitWildcard(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitWildcard(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AbstractionContext extends TermContext {
		public TermsContext terms() {
			return getRuleContext(TermsContext.class,0);
		}
		public AbstractionContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterAbstraction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitAbstraction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitAbstraction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AtomContext extends TermContext {
		public TerminalNode ID() { return getToken(fixpointsParser.ID, 0); }
		public AtomContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_term);
		try {
			setState(49);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new AtomContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(36);
				match(ID);
				}
				break;
			case 2:
				_localctx = new ZeroContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(37);
				match(T__1);
				}
				break;
			case 3:
				_localctx = new WildcardContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(38);
				match(T__2);
				}
				break;
			case 4:
				_localctx = new ComposedContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(39);
				match(ID);
				setState(40);
				match(T__3);
				setState(41);
				terms();
				setState(42);
				match(T__4);
				}
				break;
			case 5:
				_localctx = new AbstractionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(44);
				match(T__5);
				setState(45);
				match(T__3);
				setState(46);
				terms();
				setState(47);
				match(T__4);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AiffixpointContext extends ParserRuleContext {
		public VardecsContext vd;
		public InfersContext inf;
		public TerminalNode NUM() { return getToken(fixpointsParser.NUM, 0); }
		public FactContext fact() {
			return getRuleContext(FactContext.class,0);
		}
		public TerminalNode ID() { return getToken(fixpointsParser.ID, 0); }
		public VardecsContext vardecs() {
			return getRuleContext(VardecsContext.class,0);
		}
		public InfersContext infers() {
			return getRuleContext(InfersContext.class,0);
		}
		public AiffixpointContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aiffixpoint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterAiffixpoint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitAiffixpoint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitAiffixpoint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AiffixpointContext aiffixpoint() throws RecognitionException {
		AiffixpointContext _localctx = new AiffixpointContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_aiffixpoint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			match(T__3);
			setState(52);
			match(NUM);
			setState(53);
			match(T__4);
			setState(55);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(54);
				((AiffixpointContext)_localctx).vd = vardecs();
				}
				break;
			}
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(57);
				match(T__6);
				}
			}

			setState(60);
			fact();
			setState(61);
			match(T__7);
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(62);
				((AiffixpointContext)_localctx).inf = infers();
				}
			}

			setState(65);
			match(ID);
			setState(66);
			match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InfersContext extends ParserRuleContext {
		public List<InferContext> infer() {
			return getRuleContexts(InferContext.class);
		}
		public InferContext infer(int i) {
			return getRuleContext(InferContext.class,i);
		}
		public InfersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_infers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterInfers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitInfers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitInfers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InfersContext infers() throws RecognitionException {
		InfersContext _localctx = new InfersContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_infers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			infer();
			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__9) {
				{
				{
				setState(69);
				match(T__9);
				setState(70);
				infer();
				}
				}
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InferContext extends ParserRuleContext {
		public TerminalNode NUM() { return getToken(fixpointsParser.NUM, 0); }
		public InferContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_infer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterInfer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitInfer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitInfer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InferContext infer() throws RecognitionException {
		InferContext _localctx = new InferContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_infer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(T__3);
			setState(77);
			match(NUM);
			setState(78);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FactContext extends ParserRuleContext {
		public FactContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fact; }
	 
		public FactContext() { }
		public void copyFrom(FactContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PFactContext extends FactContext {
		public TerminalNode ID() { return getToken(fixpointsParser.ID, 0); }
		public TermsContext terms() {
			return getRuleContext(TermsContext.class,0);
		}
		public PFactContext(FactContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterPFact(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitPFact(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitPFact(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactContext fact() throws RecognitionException {
		FactContext _localctx = new FactContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_fact);
		int _la;
		try {
			_localctx = new PFactContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(ID);
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(81);
				match(T__3);
				setState(82);
				terms();
				setState(83);
				match(T__4);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VardecsContext extends ParserRuleContext {
		public List<VardecContext> vardec() {
			return getRuleContexts(VardecContext.class);
		}
		public VardecContext vardec(int i) {
			return getRuleContext(VardecContext.class,i);
		}
		public VardecsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vardecs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterVardecs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitVardecs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitVardecs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VardecsContext vardecs() throws RecognitionException {
		VardecsContext _localctx = new VardecsContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_vardecs);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			vardec();
			setState(92);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(88);
					match(T__6);
					setState(89);
					vardec();
					}
					} 
				}
				setState(94);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VardecContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(fixpointsParser.ID, 0); }
		public VartypeContext vartype() {
			return getRuleContext(VartypeContext.class,0);
		}
		public VardecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vardec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterVardec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitVardec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitVardec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VardecContext vardec() throws RecognitionException {
		VardecContext _localctx = new VardecContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_vardec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(ID);
			setState(96);
			match(T__10);
			setState(97);
			vartype();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VartypeContext extends ParserRuleContext {
		public VartypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vartype; }
	 
		public VartypeContext() { }
		public void copyFrom(VartypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class UntypedContext extends VartypeContext {
		public UntypedContext(VartypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterUntyped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitUntyped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitUntyped(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ValueContext extends VartypeContext {
		public ValueContext(VartypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UserDefContext extends VartypeContext {
		public TerminalNode ID() { return getToken(fixpointsParser.ID, 0); }
		public UserDefContext(VartypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).enterUserDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof fixpointsListener ) ((fixpointsListener)listener).exitUserDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof fixpointsVisitor ) return ((fixpointsVisitor<? extends T>)visitor).visitUserDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VartypeContext vartype() throws RecognitionException {
		VartypeContext _localctx = new VartypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_vartype);
		try {
			setState(102);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new UserDefContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(99);
				match(ID);
				}
				break;
			case T__11:
				_localctx = new ValueContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				match(T__11);
				}
				break;
			case T__12:
				_localctx = new UntypedContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				match(T__12);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\23k\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3"+
		"\2\7\2\30\n\2\f\2\16\2\33\13\2\3\2\3\2\3\3\3\3\3\3\7\3\"\n\3\f\3\16\3"+
		"%\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\64\n\4"+
		"\3\5\3\5\3\5\3\5\5\5:\n\5\3\5\5\5=\n\5\3\5\3\5\3\5\5\5B\n\5\3\5\3\5\3"+
		"\5\3\6\3\6\3\6\7\6J\n\6\f\6\16\6M\13\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b"+
		"\3\b\5\bX\n\b\3\t\3\t\3\t\7\t]\n\t\f\t\16\t`\13\t\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\5\13i\n\13\3\13\2\2\f\2\4\6\b\n\f\16\20\22\24\2\2\2n\2\31\3"+
		"\2\2\2\4\36\3\2\2\2\6\63\3\2\2\2\b\65\3\2\2\2\nF\3\2\2\2\fN\3\2\2\2\16"+
		"R\3\2\2\2\20Y\3\2\2\2\22a\3\2\2\2\24h\3\2\2\2\26\30\5\b\5\2\27\26\3\2"+
		"\2\2\30\33\3\2\2\2\31\27\3\2\2\2\31\32\3\2\2\2\32\34\3\2\2\2\33\31\3\2"+
		"\2\2\34\35\7\2\2\3\35\3\3\2\2\2\36#\5\6\4\2\37 \7\3\2\2 \"\5\6\4\2!\37"+
		"\3\2\2\2\"%\3\2\2\2#!\3\2\2\2#$\3\2\2\2$\5\3\2\2\2%#\3\2\2\2&\64\7\21"+
		"\2\2\'\64\7\4\2\2(\64\7\5\2\2)*\7\21\2\2*+\7\6\2\2+,\5\4\3\2,-\7\7\2\2"+
		"-\64\3\2\2\2./\7\b\2\2/\60\7\6\2\2\60\61\5\4\3\2\61\62\7\7\2\2\62\64\3"+
		"\2\2\2\63&\3\2\2\2\63\'\3\2\2\2\63(\3\2\2\2\63)\3\2\2\2\63.\3\2\2\2\64"+
		"\7\3\2\2\2\65\66\7\6\2\2\66\67\7\20\2\2\679\7\7\2\28:\5\20\t\298\3\2\2"+
		"\29:\3\2\2\2:<\3\2\2\2;=\7\t\2\2<;\3\2\2\2<=\3\2\2\2=>\3\2\2\2>?\5\16"+
		"\b\2?A\7\n\2\2@B\5\n\6\2A@\3\2\2\2AB\3\2\2\2BC\3\2\2\2CD\7\21\2\2DE\7"+
		"\13\2\2E\t\3\2\2\2FK\5\f\7\2GH\7\f\2\2HJ\5\f\7\2IG\3\2\2\2JM\3\2\2\2K"+
		"I\3\2\2\2KL\3\2\2\2L\13\3\2\2\2MK\3\2\2\2NO\7\6\2\2OP\7\20\2\2PQ\7\7\2"+
		"\2Q\r\3\2\2\2RW\7\21\2\2ST\7\6\2\2TU\5\4\3\2UV\7\7\2\2VX\3\2\2\2WS\3\2"+
		"\2\2WX\3\2\2\2X\17\3\2\2\2Y^\5\22\n\2Z[\7\t\2\2[]\5\22\n\2\\Z\3\2\2\2"+
		"]`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_\21\3\2\2\2`^\3\2\2\2ab\7\21\2\2bc\7\r"+
		"\2\2cd\5\24\13\2d\23\3\2\2\2ei\7\21\2\2fi\7\16\2\2gi\7\17\2\2he\3\2\2"+
		"\2hf\3\2\2\2hg\3\2\2\2i\25\3\2\2\2\f\31#\639<AKW^h";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}