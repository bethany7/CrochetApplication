grammar CrochetPatternParser;

//row : instructions+ ;
//repeat : '*' instruction (',' instruction)* '*' 'repeat' ;

instructions : instruction (',' instruction)*;

instructions2 : instruction (',' instruction)*;

instruction :  chain                   # ChainInstr
              | stitches inChain        # InChainInstr
              | stitches inChainSpace   # InChainSpaceInstr
              | decrease                # DecreaseInstr
              | increase                # IncreaseInstr
              | stitches direction     # StitchDirectionInstr
              | stitches                # StitchInstr
              | repeat                  # RepeatMarker
              | skip                    # SkipInstr
              | FINALTURN               # FinalTurnInstr
              ;

repeatInstructions: 'repeat from' REP 'to' REP repeatTimes            # TimesRepeat
                    | 'repeat' repeatTimes                           # ShortTimesRepeat
                    ;

repeat : REP instructions2 REP repeatInstructions;

skip : 'skip' INT
      | 'skip next' stitches
      | 'skip' stitches
      ;

direction  : inNext     # InNextDir
           | inEach     # InEachDir
           | inFirst    # InFirstDir
           | inLast     # InLastDir
          ;

inNext  : 'in next' INT
        | 'in next' stitches
        ;
inEach : 'in each' stitches ;
inFirst : 'in first' stitches ;
inLast : 'in last' stitches ;

inChain : 'in 1st' CHAINSTITCH 'from hook'        # InChain1
        | 'in 2nd' CHAINSTITCH 'from hook'        # InChain2
        | 'in 3rd' CHAINSTITCH 'from hook'        # InChain3
        | 'in' INT 'th' CHAINSTITCH 'from hook'   # InChainN
        ;

inChainSpace : 'in chain' INT 'space'
             | 'in' INT 'ch space'
             | 'in ch' INT 'sp'
             | 'in ch' INT 'space'
             | 'in' INT 'chain sp'
             | 'in chain' INT 'space'
             ;

chain : CHAINSTITCH (INT)*
      | CHAINSTITCH INT 'up'
      ;

decrease : (INT)* STITCHTYPE INT 'tog';
increase : stitches 'in same chain' (INT)* 'space'  # SingleIncreaseChainSpace
         | stitches 'in same ch' (INT)* 'sp'  # SingleIncreaseChainSpace
         | stitches 'in same chain' (INT)* 'sp'  # SingleIncreaseChainSpace
         | stitches 'in same ch' (INT)* 'space'  # SingleIncreaseChainSpace
         | stitches 'in same' stitch    # SingleIncrease
         | stitches 'in next' stitches  # NIncreases
         | stitches 'in each' stitch    # AllIncreases
         ;


stitches : ((INT)* stitch)+ ;
stitch : GENERICSTITCH    # GenericStitch
        | STITCHTYPE      # StitchType
        //| CHAINSTITCH     # ChainStitch
        | SLIPSTITCH      # SlipStitch
        ;

repeatTimes : INT 'times' | INT 'time' | 'once' ;

REP : '*'+ ;
GENERICSTITCH : 'st' | 'sts' | 'stitch' | 'stitches' ;
STITCHTYPE : 'dc' | 'tr' | 'hdc' | 'htr' | 'dtr' | 'ttr';
SLIPSTITCH : 'sl' | 'sl st' | 'slip stitch' ;
CHAINSTITCH : 'ch' | 'chain' | 'chain stitch' ;
FINALTURN : 'TURN' | 'turn' ;

INT : [0-9]+ ;
ID : [a-zA-Z]+ ;
WS : [ \t\r\n]+ -> skip ;