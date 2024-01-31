grammar CrochetPatternParser;

//repeat : '*' instruction (',' instruction)* '*' 'repeat' ;
instructions : instruction (',' instruction)*;

instruction : repeatInstructions | chain | stitches attachment | repeat | skip | FINALTURN;
repeatInstructions: 'repeat from' REP 'to' REP repeatTimes
                    | 'repeat from' REP 'to' REP 'to last' stitches;
repeat : REP instructions ;
skip : 'skip' INT | 'skip next' stitches | 'skip' stitches ;

attachment : inNext | inEach | inFirst | inLast | inChain;
inNext : 'in next' INT |'in next' stitches ;
inEach : 'in each' stitches ;
inFirst : 'in first' stitches ;
inLast : 'in last' stitches ;
inChain : 'in 1st' CHAINSTITCH 'from hook' | 'in 2nd' CHAINSTITCH 'from hook'
    | 'in 3rd' CHAINSTITCH 'from hook' | 'in' INT 'th' CHAINSTITCH 'from hook' ;

chain : CHAINSTITCH (INT)* | CHAINSTITCH INT 'up';
stitches : ((INT)* stitch)+ ;
stitch : GENERICSTITCH | STITCHTYPE | CHAINSTITCH | SLIPSTITCH ;
repeatTimes : INT 'times' | INT 'time' | 'once' ;

REP : '*'+ ;
GENERICSTITCH : 'st' | 'sts' | 'stitch' | 'stitches' ;
STITCHTYPE : 'sc' | 'dc' | 'tr' ;
SLIPSTITCH : 'sl' | 'sl st' | 'slip stitch' ;
CHAINSTITCH : 'ch' | 'chain' | 'chain stitch' ;
FINALTURN : 'TURN' | 'turn' ;

INT : [0-9]+ ;
ID : [a-zA-Z]+ ;
WS : [ \t\r\n]+ -> skip ;
