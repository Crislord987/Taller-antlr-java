grammar LabeledExpr;

prog: stat+ ;

stat: expr NEWLINE        # printExpr
    | ID '=' expr NEWLINE # assign
    | NEWLINE             # blank
    ;

expr: '-' expr                  # Negative
    | expr op=('*'|'/') expr    # MulDiv
    | expr op=('+'|'-') expr    # AddSub
    | expr '^' expr             # Power
    | 'sqrt' '(' expr ')'       # Sqrt
    | FLOAT                     # float
    | ID                        # id
    | '(' expr ')'              # parens
    ;

MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
POW : '^' ;
SQRT : 'sqrt' ;

ID  : [a-zA-Z]+ ;
FLOAT : '-'?[0-9]+('.'[0-9]+)? ;
NEWLINE : '\r'? '\n' ;
WS : [ \t]+ -> skip ;

