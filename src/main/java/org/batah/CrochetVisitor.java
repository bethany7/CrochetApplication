package org.batah;


import org.batah.CrochetPatternParserParser.ChainContext;
import org.batah.CrochetPatternParserParser.ChainInstrContext;
import org.batah.CrochetPatternParserParser.ChainStitchContext;
import org.batah.CrochetPatternParserParser.FinalTurnInstrContext;
import org.batah.CrochetPatternParserParser.GenericStitchContext;
import org.batah.CrochetPatternParserParser.InChain1Context;
import org.batah.CrochetPatternParserParser.InChain2Context;
import org.batah.CrochetPatternParserParser.InChain3Context;
import org.batah.CrochetPatternParserParser.InChainDirContext;
import org.batah.CrochetPatternParserParser.InChainNContext;
import org.batah.CrochetPatternParserParser.InEachContext;
import org.batah.CrochetPatternParserParser.InEachDirContext;
import org.batah.CrochetPatternParserParser.InFirstContext;
import org.batah.CrochetPatternParserParser.InFirstDirContext;
import org.batah.CrochetPatternParserParser.InLastContext;
import org.batah.CrochetPatternParserParser.InLastDirContext;
import org.batah.CrochetPatternParserParser.InNextContext;
import org.batah.CrochetPatternParserParser.InNextDirContext;
import org.batah.CrochetPatternParserParser.InstructionContext;
import org.batah.CrochetPatternParserParser.InstructionsContext;
import org.batah.CrochetPatternParserParser.RepeatContext;
import org.batah.CrochetPatternParserParser.RepeatInstrsContext;
import org.batah.CrochetPatternParserParser.RepeatMarkerContext;
import org.batah.CrochetPatternParserParser.RepeatTimesContext;
import org.batah.CrochetPatternParserParser.SkipCountContext;
import org.batah.CrochetPatternParserParser.SkipInstrContext;
import org.batah.CrochetPatternParserParser.SkipNextContext;
import org.batah.CrochetPatternParserParser.SkipStitchesContext;
import org.batah.CrochetPatternParserParser.SlipStitchContext;
import org.batah.CrochetPatternParserParser.StitchContext;
import org.batah.CrochetPatternParserParser.StitchDirectionInstrContext;
import org.batah.CrochetPatternParserParser.StitchInstrContext;
import org.batah.CrochetPatternParserParser.StitchTypeContext;
import org.batah.CrochetPatternParserParser.StitchesContext;
import org.batah.CrochetPatternParserParser.TimesRepeatContext;
import org.batah.CrochetPatternParserParser.ToLastRepeatContext;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Chain;
import org.batah.model.stitches.Slip;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.Stitch.Attachment;
import org.batah.model.stitches.StitchBuilder;
import org.batah.model.stitches.StitchLoc;

public class CrochetVisitor<String> extends CrochetPatternParserBaseVisitor<String> {

    Pattern pattern;
    StitchBuilder stitchBuilder;
    Row row;
    public CrochetVisitor(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public String visitInstructions(InstructionsContext ctx) {
        row = new Row(pattern);
        pattern.addRow(row);
        this.stitchBuilder = new StitchBuilder(row);
        return super.visitInstructions(ctx);
    }

    @Override
    public String visitRepeatInstrs(RepeatInstrsContext ctx) {
        return super.visitRepeatInstrs(ctx);
    }

    @Override
    public String visitChainInstr(ChainInstrContext ctx) {
        return super.visitChainInstr(ctx);
    }

    @Override
    public String visitStitchDirectionInstr(StitchDirectionInstrContext ctx) {
        String y = visit(ctx.stitches());
        String z = visit(ctx.direction());
        String[] newStitchParts = (String[]) y.toString().split(" ");
        String[] parentStitchParts = (String[]) z.toString().split(" ");
        int numOfParentStitch = Integer.parseInt((java.lang.String) parentStitchParts[0]);
        int numOfStitches;
        String stitchType;
        if (newStitchParts.length < 2) {
            stitchType = newStitchParts[0];
            Stitch stitch = StitchBuilder.buildStitch((java.lang.String) stitchType, Attachment.INTO, row);
            int parentRowNum = row.getRowNum() - 1;
            int parentRowLength = pattern.getRow(parentRowNum).getStitchCount();
            StitchLoc parentStitch = new StitchLoc(parentRowNum, parentRowLength - numOfParentStitch + 1);
            stitch.setParentStitch(parentStitch);
            row.addStitch(row, stitch);
        }
        else {
            numOfStitches = Integer.parseInt((java.lang.String) newStitchParts[0]);
            stitchType = newStitchParts[1];

            if (numOfStitches == numOfParentStitch) {
                for (int k = 0; k < numOfStitches; k++) {
                    Stitch stitch = StitchBuilder.buildStitch((java.lang.String) stitchType,
                        Attachment.INTO, row);
                    row.addStitch(row, stitch);
                }
            } else {
                System.out.println("Error: stitch count does not match parent stitch count");
            }
        }
        return super.visitStitchDirectionInstr(ctx);
    }

    @Override
    public String visitStitchInstr(StitchInstrContext ctx) {
        String x = visit(ctx.stitches());
        String[] parts = (String[]) x.toString().split(" ");
        if (parts.length < 2) {
            Stitch stitch = StitchBuilder.buildStitch((java.lang.String) x, Attachment.INTO, row);
            row.addStitch(row, stitch);
        }
        else {
            int i = Integer.parseInt((java.lang.String) parts[0]);
            String y = parts[1];
            for (int j = 0; j < i; j++) {
                Stitch stitch = StitchBuilder.buildStitch((java.lang.String) y, Attachment.INTO,
                    row);
                System.out.println(y);
                row.addStitch(row, stitch);
            }
        }
        return super.visitStitchInstr(ctx);
    }

    @Override
    public String visitRepeatMarker(RepeatMarkerContext ctx) {
        return super.visitRepeatMarker(ctx);
    }

    @Override
    public String visitSkipInstr(SkipInstrContext ctx) {
        return super.visitSkipInstr(ctx);
    }

    @Override
    public String visitFinalTurnInstr(FinalTurnInstrContext ctx) {
        return super.visitFinalTurnInstr(ctx);
    }

    @Override
    public String visitTimesRepeat(TimesRepeatContext ctx) {
        return super.visitTimesRepeat(ctx);
    }

    @Override
    public String visitToLastRepeat(ToLastRepeatContext ctx) {
        return super.visitToLastRepeat(ctx);
    }

    @Override
    public String visitRepeat(RepeatContext ctx) {
        return super.visitRepeat(ctx);
    }

    @Override
    public String visitSkipCount(SkipCountContext ctx) {
        return super.visitSkipCount(ctx);
    }

    @Override
    public String visitSkipNext(SkipNextContext ctx) {
        return super.visitSkipNext(ctx);
    }

    @Override
    public String visitSkipStitches(SkipStitchesContext ctx) {
        return super.visitSkipStitches(ctx);
    }


    @Override
    public String visitInNextDir(InNextDirContext ctx) {
        return super.visitInNextDir(ctx);
    }

    @Override
    public String visitInEachDir(InEachDirContext ctx) {
        return super.visitInEachDir(ctx);
    }

    @Override
    public String visitInFirstDir(InFirstDirContext ctx) {
        return super.visitInFirstDir(ctx);
    }

    @Override
    public String visitInLastDir(InLastDirContext ctx) {
        return super.visitInLastDir(ctx);
    }

    @Override
    public String visitInChainDir(InChainDirContext ctx) {
        return super.visitInChainDir(ctx);
    }

    @Override
    public String visitInNext(InNextContext ctx) {
        return super.visitInNext(ctx);
    }

    @Override
    public String visitInEach(InEachContext ctx) {
        return super.visitInEach(ctx);
    }

    @Override
    public String visitInFirst(InFirstContext ctx) {
        return super.visitInFirst(ctx);
    }

    @Override
    public String visitInLast(InLastContext ctx) {
        return super.visitInLast(ctx);
    }

    @Override
    public String visitInChain1(InChain1Context ctx) {
        return super.visitInChain1(ctx);
    }

    @Override
    public String visitInChain2(InChain2Context ctx) {
        return super.visitInChain2(ctx);
    }

    @Override
    public String visitInChain3(InChain3Context ctx) {
        return super.visitInChain3(ctx);
    }

    @Override
    public String visitInChainN(InChainNContext ctx) {
        var x = ctx.INT();
        return (String) x;
    }

    @Override
    public String visitChain(ChainContext ctx) {
        int i = Integer.parseInt(ctx.INT().getFirst().getText());
        for (int j = 0; j < i; j++) {
            Chain chain = new Chain(row);
            row.addStitch(row, chain);
        }
        return super.visitChain(ctx);
    }

    @Override
    public String visitStitches(StitchesContext ctx) {
        int i = ctx.getChildCount();
        if (i > 1) {
            String x = (String) ctx.INT(0).getText();
            String y = visit(ctx.stitch(0));
            return (String) (x + " " + y);
        }
        else {
            return visit(ctx.stitch(0));
        }
    }

    @Override
    public String visitGenericStitch(GenericStitchContext ctx) {
        return super.visitGenericStitch(ctx);
    }

    @Override
    public String visitStitchType(StitchTypeContext ctx) {
      return (String) ctx.getText();
    }

    @Override
    public String visitChainStitch(ChainStitchContext ctx) {
        return super.visitChainStitch(ctx);
    }

    @Override
    public String visitSlipStitch(SlipStitchContext ctx) {
        return (String) ctx.getText();
    }

    @Override
    public String visitRepeatTimes(RepeatTimesContext ctx) {
        return super.visitRepeatTimes(ctx);
    }
}