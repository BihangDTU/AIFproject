grammar fixpoints;

NUM : ('0'..'9')+ ;
ID  : ('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'_'|'0'..'9')* ;
WHITESPACE : [ \n\t\r]+ -> skip;
COMMENT : '%'~[\n]+'\n' -> skip;

fixPoints : aiffixpoint*
			EOF ;
     
terms : term (',' term)* ;
term : ID                   # Atom 
	 | NUM                  # Num
     | '_'                  # Wildcard
     | ID '(' terms ')'     # Composed
     | 'val' '('  terms ')' # Abstraction
     ;

aiffixpoint : '(' NUM ')' vd=vardecs? ('.')? fact '<=' inf=infers? ID ';'; 

infers : infer ('+' infer)*;
infer : '(' NUM ')';

fact : ID ('(' terms ')')? # PFact
		;

vardecs : vardec ('.' vardec)* ;
vardec  : ID ':' vartype ;

vartype : ID      # UserDef
        | 'value' # Value
        | 'untyped' # Untyped
		;
