grammar CrochetPatternParser;

@header {
    package org.batah;
}

chain : CHAINSTITCH INT ;

stitches : (INT)* STITCH ;

instruction : chain | stitches attachment ;

skip : 'skip' INT | 'skip ' INT 'st' | 'skip next' INT | 'skip' stitches ;

attachment : inNext | inEach | inFirst | inLast ;

inNext : 'in next' INT |'in next' stitches ;
inEach : 'in each' STITCH ;
inFirst : 'in first' stitches ;
inLast : 'in last' stitches ;

STITCH : STITCHTYPE | CHAINSTITCH | SLIPSTITCH ;
SLIPSTITCH : 'sl' | 'sl st' | 'slip stitch' ;
CHAINSTITCH : 'ch' | 'chain' | 'chain stitch' ;
STITCHTYPE : 'st' | 'sts' | 'stitch' | 'stitches' | 'sc' | 'dc' | 'tr' ;


INT : [0-9]+ ;
ID : [a-zA-Z]+ ;
WS : [ \t\r\n]+ -> skip ;