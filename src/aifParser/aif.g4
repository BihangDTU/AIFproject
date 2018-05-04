grammar aif;

NUM : ('0'..'9')+ ;
ID  : ('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'_'|'0'..'9')* ;
WHITESPACE : [ \n\t\r]+ -> skip;
COMMENT : '%'~[\n]+'\n' -> skip;

aif : 'Problem:' ID ';'
      'Types:' typedec*
      'Sets:' terms ';'
      'Functions:' symdecs ';'
      'Facts:' symdecs ';'
      'Rules:' aifrule*
      EOF ;

typedec : ID '=' type ';' ;
type : '{...}'              # Infinite
     | '{' ID (',' ID)* '}' # Enumeration
     | ID ('++' ID)*        # Union
     ;
     
terms : term (',' term)* ;
term : ID                   # Atom 
     | '_'                  # Wildcard
     | ID '(' terms ')'     # Composed
     | 'val' '['  terms ']' # Abstraction
     ;

symdecs : symdec (',' symdec)* ;
symdec :  ID '/' NUM ;

aifrule : ID '(' vardecs ')' lhs=facts? arrow rhs=facts ';' ;

facts : fact ('.' fact)* ;
fact : ID ('(' terms ')')? # PFact
     | ID 'in' term        # PosCond
     | ID 'notin' term     # NegCond
     ;
     
arrow : '=>'                    # NoFresh
      | '=[' ID (',' ID)* ']=>' # Fresh
      ;

vardecs : vardec (',' vardec)* ;
vardec  : ID ':' vartype ;

vartype : ID      # UserDef
        | 'value' # Value
        | 'untyped' # Untyped
	;
