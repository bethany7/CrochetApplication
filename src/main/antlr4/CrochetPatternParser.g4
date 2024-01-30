grammar CrochetPatternParser;


chain : CHAINSTITCH INT ;

stitches : INT STITCH ;

instruction : chain | stitches attachment ;

skip : 'skip' INT | 'skip ' INT 'st' | 'skip next' INT | 'skip' STITCH | 'skip' INT STITCH;

attachment : inNext | inEach | inFirst | inLast ;

inNext : 'in next' INT | 'in next' (INT)* STITCH | 'in next' STITCH ;
inEach : 'in each' | 'in each' STITCH ;
inFirst : 'in first' STITCH ;
inLast : 'in last' STITCH ;

STITCH : STITCHTYPE | CHAINSTITCH | SLIPSTITCH ;
SLIPSTITCH : 'sl' | 'sl st' | 'slip stitch' ;
CHAINSTITCH : 'ch' | 'chain' | 'chain stitch' ;
STITCHTYPE : 'st' | 'sts' | 'stitch' | 'stitches' | 'sc' | 'dc' | 'tr' ;


INT : [0-9]+ ;
ID : [a-zA-Z]+ ;
WS : [ \t\r\n]+ -> skip ;