grammar CrochetPatternParser;

//repeat : '*' instruction (',' instruction)* '*' 'repeat' ;
instructions : instruction (',' instruction)*;

instruction : repeatInstructions        //# RepeatInstrs
              | chain                   //# ChainInstr
              | stitches attachment     //# StitchInstr
              | repeat                  //# RepeatMarker
              | skip                    //# SkipInstr
              | FINALTURN               //# FinalTurnInstr
              ;

repeatInstructions: 'repeat from' REP 'to' REP repeatTimes            # TimesRepeat
                    | 'repeat from' REP 'to' REP 'to last' stitches   # ToLastRepeat
                    ;

repeat : REP instructions ;

skip : 'skip' INT      # SkipCount
      | 'skip next' stitches # SkipNext
      | 'skip' stitches # SkipStitches
      ;

attachment : inNext | inEach | inFirst | inLast | inChain;

inNext : 'in next' INT            # InNextX
        |'in next' stitches       # InNextSt
        ;
inEach : 'in each' stitches ;
inFirst : 'in first' stitches ;
inLast : 'in last' stitches ;

inChain : 'in 1st' CHAINSTITCH 'from hook'        # InChain1
        | 'in 2nd' CHAINSTITCH 'from hook'        # InChain2
        | 'in 3rd' CHAINSTITCH 'from hook'        # InChain3
        | 'in' INT 'th' CHAINSTITCH 'from hook'   # InChainN
        ;

chain : CHAINSTITCH (INT)*
      | CHAINSTITCH INT 'up'
      ;

stitches : ((INT)* stitch)+ ;
stitch : GENERICSTITCH
        | STITCHTYPE
        | CHAINSTITCH
        | SLIPSTITCH
        ;

repeatTimes : INT 'times' | INT 'time' | 'once' ;

REP : '*'+ ;
GENERICSTITCH : 'st' | 'sts' | 'stitch' | 'stitches' ;
STITCHTYPE : 'sc' | 'dc' | 'tr' | 'hdc' | 'htr' | 'dtr' | 'ttr';
SLIPSTITCH : 'sl' | 'sl st' | 'slip stitch' ;
CHAINSTITCH : 'ch' | 'chain' | 'chain stitch' ;
FINALTURN : 'TURN' | 'turn' ;

INT : [0-9]+ ;
ID : [a-zA-Z]+ ;
WS : [ \t\r\n]+ -> skip ;