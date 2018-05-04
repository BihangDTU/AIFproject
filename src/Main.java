import aifParser.*;
import fixpointParser.*;

import dataStructure.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{

    	// we expect exactly one argument: the name of the input file
    	if (args.length!=2) {
    	  System.err.println("\n");
    	  System.err.println("Please give as input argument a filename\n");
    	  System.exit(-1);
    	}
    	String filename1=args[0];
    	String filename2=args[1];
    
    	// open the input file
    	CharStream input1 = CharStreams.fromFileName(filename1);
    	CharStream input2 = CharStreams.fromFileName(filename2);
    	
    	// create a lexer/scanner
    	aifLexer lex1 = new aifLexer(input1);
    	fixpointsLexer lex2 = new fixpointsLexer(input2);
    	
    	// get the stream of tokens from the scanner
    	CommonTokenStream tokens1 = new CommonTokenStream(lex1);
    	CommonTokenStream tokens2 = new CommonTokenStream(lex2);
    	
    	// create a parser
    	aifParser parser1 = new aifParser(tokens1);
    	fixpointsParser parser2 = new fixpointsParser(tokens2);
    	
    	// and parse anything from the grammar for "start"
    	ParseTree parseTree1 = parser1.aif();
    	ParseTree parseTree2 = parser2.fixPoints();
    
    	// A maker for an Abstract Syntax Tree (AST) 
    	AifASTMaker aifASTmaker = new AifASTMaker();
    	AST aifAST=aifASTmaker.visit(parseTree1);
    	System.out.println(aifAST);
    	System.out.println("------------------------------------------------");
    	FixPointASTMaker fpASTmaker = new FixPointASTMaker();
    	AST fpAST=fpASTmaker.visit(parseTree2);
    	System.out.println(fpAST);
    	
    }
}
