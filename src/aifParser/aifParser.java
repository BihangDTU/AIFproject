package aifParser;
// Generated from aif.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class aifParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, NUM=30, ID=31, WHITESPACE=32, 
		COMMENT=33;
	public static final int
		RULE_aif = 0, RULE_typedec = 1, RULE_type = 2, RULE_terms = 3, RULE_term = 4, 
		RULE_symdecs = 5, RULE_symdec = 6, RULE_aifrule = 7, RULE_facts = 8, RULE_fact = 9, 
		RULE_arrow = 10, RULE_vardecs = 11, RULE_vardec = 12, RULE_vartype = 13;
	public static final String[] ruleNames = {
		"aif", "typedec", "type", "terms", "term", "symdecs", "symdec", "aifrule", 
		"facts", "fact", "arrow", "vardecs", "vardec", "vartype"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'Problem:'", "';'", "'Types:'", "'Sets:'", "'Functions:'", "'Facts:'", 
		"'Rules:'", "'='", "'{...}'", "'{'", "','", "'}'", "'++'", "'_'", "'('", 
		"')'", "'val'", "'['", "']'", "'/'", "'.'", "'in'", "'notin'", "'=>'", 
		"'=['", "']=>'", "':'", "'value'", "'untyped'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, "NUM", "ID", "WHITESPACE", "COMMENT"
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
	public String getGrammarFileName() { return "aif.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public aifParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class AifContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public TermsContext terms() {
			return getRuleContext(TermsContext.class,0);
		}
		public List<SymdecsContext> symdecs() {
			return getRuleContexts(SymdecsContext.class);
		}
		public SymdecsContext symdecs(int i) {
			return getRuleContext(SymdecsContext.class,i);
		}
		public TerminalNode EOF() { return getToken(aifParser.EOF, 0); }
		public List<TypedecContext> typedec() {
			return getRuleContexts(TypedecContext.class);
		}
		public TypedecContext typedec(int i) {
			return getRuleContext(TypedecContext.class,i);
		}
		public List<AifruleContext> aifrule() {
			return getRuleContexts(AifruleContext.class);
		}
		public AifruleContext aifrule(int i) {
			return getRuleContext(AifruleContext.class,i);
		}
		public AifContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aif; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterAif(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitAif(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitAif(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AifContext aif() throws RecognitionException {
		AifContext _localctx = new AifContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_aif);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			match(T__0);
			setState(29);
			match(ID);
			setState(30);
			match(T__1);
			setState(31);
			match(T__2);
			setState(35);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(32);
				typedec();
				}
				}
				setState(37);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(38);
			match(T__3);
			setState(39);
			terms();
			setState(40);
			match(T__1);
			setState(41);
			match(T__4);
			setState(42);
			symdecs();
			setState(43);
			match(T__1);
			setState(44);
			match(T__5);
			setState(45);
			symdecs();
			setState(46);
			match(T__1);
			setState(47);
			match(T__6);
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(48);
				aifrule();
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54);
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

	public static class TypedecContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypedecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterTypedec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitTypedec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitTypedec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedecContext typedec() throws RecognitionException {
		TypedecContext _localctx = new TypedecContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_typedec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(ID);
			setState(57);
			match(T__7);
			setState(58);
			type();
			setState(59);
			match(T__1);
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

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class EnumerationContext extends TypeContext {
		public List<TerminalNode> ID() { return getTokens(aifParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(aifParser.ID, i);
		}
		public EnumerationContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterEnumeration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitEnumeration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitEnumeration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InfiniteContext extends TypeContext {
		public InfiniteContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterInfinite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitInfinite(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitInfinite(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnionContext extends TypeContext {
		public List<TerminalNode> ID() { return getTokens(aifParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(aifParser.ID, i);
		}
		public UnionContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterUnion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitUnion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitUnion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_type);
		int _la;
		try {
			setState(80);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__8:
				_localctx = new InfiniteContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				match(T__8);
				}
				break;
			case T__9:
				_localctx = new EnumerationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(62);
				match(T__9);
				setState(63);
				match(ID);
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__10) {
					{
					{
					setState(64);
					match(T__10);
					setState(65);
					match(ID);
					}
					}
					setState(70);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(71);
				match(T__11);
				}
				break;
			case ID:
				_localctx = new UnionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				match(ID);
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__12) {
					{
					{
					setState(73);
					match(T__12);
					setState(74);
					match(ID);
					}
					}
					setState(79);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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
			if ( listener instanceof aifListener ) ((aifListener)listener).enterTerms(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitTerms(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitTerms(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermsContext terms() throws RecognitionException {
		TermsContext _localctx = new TermsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_terms);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			term();
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(83);
				match(T__10);
				setState(84);
				term();
				}
				}
				setState(89);
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
	public static class ComposedContext extends TermContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public TermsContext terms() {
			return getRuleContext(TermsContext.class,0);
		}
		public ComposedContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterComposed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitComposed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitComposed(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WildcardContext extends TermContext {
		public WildcardContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitWildcard(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitWildcard(this);
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
			if ( listener instanceof aifListener ) ((aifListener)listener).enterAbstraction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitAbstraction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitAbstraction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AtomContext extends TermContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public AtomContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_term);
		try {
			setState(102);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				_localctx = new AtomContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(90);
				match(ID);
				}
				break;
			case 2:
				_localctx = new WildcardContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(91);
				match(T__13);
				}
				break;
			case 3:
				_localctx = new ComposedContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(92);
				match(ID);
				setState(93);
				match(T__14);
				setState(94);
				terms();
				setState(95);
				match(T__15);
				}
				break;
			case 4:
				_localctx = new AbstractionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(97);
				match(T__16);
				setState(98);
				match(T__17);
				setState(99);
				terms();
				setState(100);
				match(T__18);
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

	public static class SymdecsContext extends ParserRuleContext {
		public List<SymdecContext> symdec() {
			return getRuleContexts(SymdecContext.class);
		}
		public SymdecContext symdec(int i) {
			return getRuleContext(SymdecContext.class,i);
		}
		public SymdecsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_symdecs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterSymdecs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitSymdecs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitSymdecs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SymdecsContext symdecs() throws RecognitionException {
		SymdecsContext _localctx = new SymdecsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_symdecs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			symdec();
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(105);
				match(T__10);
				setState(106);
				symdec();
				}
				}
				setState(111);
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

	public static class SymdecContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public TerminalNode NUM() { return getToken(aifParser.NUM, 0); }
		public SymdecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_symdec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterSymdec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitSymdec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitSymdec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SymdecContext symdec() throws RecognitionException {
		SymdecContext _localctx = new SymdecContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_symdec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(ID);
			setState(113);
			match(T__19);
			setState(114);
			match(NUM);
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

	public static class AifruleContext extends ParserRuleContext {
		public FactsContext lhs;
		public FactsContext rhs;
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public VardecsContext vardecs() {
			return getRuleContext(VardecsContext.class,0);
		}
		public ArrowContext arrow() {
			return getRuleContext(ArrowContext.class,0);
		}
		public List<FactsContext> facts() {
			return getRuleContexts(FactsContext.class);
		}
		public FactsContext facts(int i) {
			return getRuleContext(FactsContext.class,i);
		}
		public AifruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aifrule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterAifrule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitAifrule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitAifrule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AifruleContext aifrule() throws RecognitionException {
		AifruleContext _localctx = new AifruleContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_aifrule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(ID);
			setState(117);
			match(T__14);
			setState(118);
			vardecs();
			setState(119);
			match(T__15);
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(120);
				((AifruleContext)_localctx).lhs = facts();
				}
			}

			setState(123);
			arrow();
			setState(124);
			((AifruleContext)_localctx).rhs = facts();
			setState(125);
			match(T__1);
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

	public static class FactsContext extends ParserRuleContext {
		public List<FactContext> fact() {
			return getRuleContexts(FactContext.class);
		}
		public FactContext fact(int i) {
			return getRuleContext(FactContext.class,i);
		}
		public FactsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_facts; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterFacts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitFacts(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitFacts(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactsContext facts() throws RecognitionException {
		FactsContext _localctx = new FactsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_facts);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			fact();
			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__20) {
				{
				{
				setState(128);
				match(T__20);
				setState(129);
				fact();
				}
				}
				setState(134);
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
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public TermsContext terms() {
			return getRuleContext(TermsContext.class,0);
		}
		public PFactContext(FactContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterPFact(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitPFact(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitPFact(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PosCondContext extends FactContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public PosCondContext(FactContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterPosCond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitPosCond(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitPosCond(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NegCondContext extends FactContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public NegCondContext(FactContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterNegCond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitNegCond(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitNegCond(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactContext fact() throws RecognitionException {
		FactContext _localctx = new FactContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_fact);
		int _la;
		try {
			setState(148);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				_localctx = new PFactContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(135);
				match(ID);
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__14) {
					{
					setState(136);
					match(T__14);
					setState(137);
					terms();
					setState(138);
					match(T__15);
					}
				}

				}
				break;
			case 2:
				_localctx = new PosCondContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(142);
				match(ID);
				setState(143);
				match(T__21);
				setState(144);
				term();
				}
				break;
			case 3:
				_localctx = new NegCondContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(145);
				match(ID);
				setState(146);
				match(T__22);
				setState(147);
				term();
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

	public static class ArrowContext extends ParserRuleContext {
		public ArrowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrow; }
	 
		public ArrowContext() { }
		public void copyFrom(ArrowContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FreshContext extends ArrowContext {
		public List<TerminalNode> ID() { return getTokens(aifParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(aifParser.ID, i);
		}
		public FreshContext(ArrowContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterFresh(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitFresh(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitFresh(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NoFreshContext extends ArrowContext {
		public NoFreshContext(ArrowContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterNoFresh(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitNoFresh(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitNoFresh(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrowContext arrow() throws RecognitionException {
		ArrowContext _localctx = new ArrowContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_arrow);
		int _la;
		try {
			setState(161);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				_localctx = new NoFreshContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(150);
				match(T__23);
				}
				break;
			case T__24:
				_localctx = new FreshContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(151);
				match(T__24);
				setState(152);
				match(ID);
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__10) {
					{
					{
					setState(153);
					match(T__10);
					setState(154);
					match(ID);
					}
					}
					setState(159);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(160);
				match(T__25);
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
			if ( listener instanceof aifListener ) ((aifListener)listener).enterVardecs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitVardecs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitVardecs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VardecsContext vardecs() throws RecognitionException {
		VardecsContext _localctx = new VardecsContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_vardecs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			vardec();
			setState(168);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(164);
				match(T__10);
				setState(165);
				vardec();
				}
				}
				setState(170);
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

	public static class VardecContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public VartypeContext vartype() {
			return getRuleContext(VartypeContext.class,0);
		}
		public VardecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vardec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterVardec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitVardec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitVardec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VardecContext vardec() throws RecognitionException {
		VardecContext _localctx = new VardecContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_vardec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(ID);
			setState(172);
			match(T__26);
			setState(173);
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
			if ( listener instanceof aifListener ) ((aifListener)listener).enterUntyped(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitUntyped(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitUntyped(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ValueContext extends VartypeContext {
		public ValueContext(VartypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UserDefContext extends VartypeContext {
		public TerminalNode ID() { return getToken(aifParser.ID, 0); }
		public UserDefContext(VartypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).enterUserDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof aifListener ) ((aifListener)listener).exitUserDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof aifVisitor ) return ((aifVisitor<? extends T>)visitor).visitUserDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VartypeContext vartype() throws RecognitionException {
		VartypeContext _localctx = new VartypeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_vartype);
		try {
			setState(178);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new UserDefContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				match(ID);
				}
				break;
			case T__27:
				_localctx = new ValueContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(176);
				match(T__27);
				}
				break;
			case T__28:
				_localctx = new UntypedContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(177);
				match(T__28);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3#\u00b7\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\2\3\2\7\2$\n\2\f"+
		"\2\16\2\'\13\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2\64\n\2"+
		"\f\2\16\2\67\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\7\4"+
		"E\n\4\f\4\16\4H\13\4\3\4\3\4\3\4\3\4\7\4N\n\4\f\4\16\4Q\13\4\5\4S\n\4"+
		"\3\5\3\5\3\5\7\5X\n\5\f\5\16\5[\13\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\5\6i\n\6\3\7\3\7\3\7\7\7n\n\7\f\7\16\7q\13\7\3\b\3\b\3"+
		"\b\3\b\3\t\3\t\3\t\3\t\3\t\5\t|\n\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\7\n\u0085"+
		"\n\n\f\n\16\n\u0088\13\n\3\13\3\13\3\13\3\13\3\13\5\13\u008f\n\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\5\13\u0097\n\13\3\f\3\f\3\f\3\f\3\f\7\f\u009e"+
		"\n\f\f\f\16\f\u00a1\13\f\3\f\5\f\u00a4\n\f\3\r\3\r\3\r\7\r\u00a9\n\r\f"+
		"\r\16\r\u00ac\13\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\5\17\u00b5\n\17"+
		"\3\17\2\2\20\2\4\6\b\n\f\16\20\22\24\26\30\32\34\2\2\2\u00bd\2\36\3\2"+
		"\2\2\4:\3\2\2\2\6R\3\2\2\2\bT\3\2\2\2\nh\3\2\2\2\fj\3\2\2\2\16r\3\2\2"+
		"\2\20v\3\2\2\2\22\u0081\3\2\2\2\24\u0096\3\2\2\2\26\u00a3\3\2\2\2\30\u00a5"+
		"\3\2\2\2\32\u00ad\3\2\2\2\34\u00b4\3\2\2\2\36\37\7\3\2\2\37 \7!\2\2 !"+
		"\7\4\2\2!%\7\5\2\2\"$\5\4\3\2#\"\3\2\2\2$\'\3\2\2\2%#\3\2\2\2%&\3\2\2"+
		"\2&(\3\2\2\2\'%\3\2\2\2()\7\6\2\2)*\5\b\5\2*+\7\4\2\2+,\7\7\2\2,-\5\f"+
		"\7\2-.\7\4\2\2./\7\b\2\2/\60\5\f\7\2\60\61\7\4\2\2\61\65\7\t\2\2\62\64"+
		"\5\20\t\2\63\62\3\2\2\2\64\67\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668"+
		"\3\2\2\2\67\65\3\2\2\289\7\2\2\39\3\3\2\2\2:;\7!\2\2;<\7\n\2\2<=\5\6\4"+
		"\2=>\7\4\2\2>\5\3\2\2\2?S\7\13\2\2@A\7\f\2\2AF\7!\2\2BC\7\r\2\2CE\7!\2"+
		"\2DB\3\2\2\2EH\3\2\2\2FD\3\2\2\2FG\3\2\2\2GI\3\2\2\2HF\3\2\2\2IS\7\16"+
		"\2\2JO\7!\2\2KL\7\17\2\2LN\7!\2\2MK\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2"+
		"\2\2PS\3\2\2\2QO\3\2\2\2R?\3\2\2\2R@\3\2\2\2RJ\3\2\2\2S\7\3\2\2\2TY\5"+
		"\n\6\2UV\7\r\2\2VX\5\n\6\2WU\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z\t"+
		"\3\2\2\2[Y\3\2\2\2\\i\7!\2\2]i\7\20\2\2^_\7!\2\2_`\7\21\2\2`a\5\b\5\2"+
		"ab\7\22\2\2bi\3\2\2\2cd\7\23\2\2de\7\24\2\2ef\5\b\5\2fg\7\25\2\2gi\3\2"+
		"\2\2h\\\3\2\2\2h]\3\2\2\2h^\3\2\2\2hc\3\2\2\2i\13\3\2\2\2jo\5\16\b\2k"+
		"l\7\r\2\2ln\5\16\b\2mk\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2p\r\3\2\2"+
		"\2qo\3\2\2\2rs\7!\2\2st\7\26\2\2tu\7 \2\2u\17\3\2\2\2vw\7!\2\2wx\7\21"+
		"\2\2xy\5\30\r\2y{\7\22\2\2z|\5\22\n\2{z\3\2\2\2{|\3\2\2\2|}\3\2\2\2}~"+
		"\5\26\f\2~\177\5\22\n\2\177\u0080\7\4\2\2\u0080\21\3\2\2\2\u0081\u0086"+
		"\5\24\13\2\u0082\u0083\7\27\2\2\u0083\u0085\5\24\13\2\u0084\u0082\3\2"+
		"\2\2\u0085\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087"+
		"\23\3\2\2\2\u0088\u0086\3\2\2\2\u0089\u008e\7!\2\2\u008a\u008b\7\21\2"+
		"\2\u008b\u008c\5\b\5\2\u008c\u008d\7\22\2\2\u008d\u008f\3\2\2\2\u008e"+
		"\u008a\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0097\3\2\2\2\u0090\u0091\7!"+
		"\2\2\u0091\u0092\7\30\2\2\u0092\u0097\5\n\6\2\u0093\u0094\7!\2\2\u0094"+
		"\u0095\7\31\2\2\u0095\u0097\5\n\6\2\u0096\u0089\3\2\2\2\u0096\u0090\3"+
		"\2\2\2\u0096\u0093\3\2\2\2\u0097\25\3\2\2\2\u0098\u00a4\7\32\2\2\u0099"+
		"\u009a\7\33\2\2\u009a\u009f\7!\2\2\u009b\u009c\7\r\2\2\u009c\u009e\7!"+
		"\2\2\u009d\u009b\3\2\2\2\u009e\u00a1\3\2\2\2\u009f\u009d\3\2\2\2\u009f"+
		"\u00a0\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u009f\3\2\2\2\u00a2\u00a4\7\34"+
		"\2\2\u00a3\u0098\3\2\2\2\u00a3\u0099\3\2\2\2\u00a4\27\3\2\2\2\u00a5\u00aa"+
		"\5\32\16\2\u00a6\u00a7\7\r\2\2\u00a7\u00a9\5\32\16\2\u00a8\u00a6\3\2\2"+
		"\2\u00a9\u00ac\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\31"+
		"\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\u00ae\7!\2\2\u00ae\u00af\7\35\2\2\u00af"+
		"\u00b0\5\34\17\2\u00b0\33\3\2\2\2\u00b1\u00b5\7!\2\2\u00b2\u00b5\7\36"+
		"\2\2\u00b3\u00b5\7\37\2\2\u00b4\u00b1\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4"+
		"\u00b3\3\2\2\2\u00b5\35\3\2\2\2\22%\65FORYho{\u0086\u008e\u0096\u009f"+
		"\u00a3\u00aa\u00b4";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}