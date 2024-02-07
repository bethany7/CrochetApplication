package org.batah;

import org.batah.CrochetPatternParserParser.AttachmentContext;
import org.batah.CrochetPatternParserParser.ChainContext;
import org.batah.CrochetPatternParserParser.InChain1Context;
import org.batah.CrochetPatternParserParser.InChain2Context;
import org.batah.CrochetPatternParserParser.InChain3Context;
import org.batah.CrochetPatternParserParser.InChainNContext;
import org.batah.CrochetPatternParserParser.InEachContext;
import org.batah.CrochetPatternParserParser.InFirstContext;
import org.batah.CrochetPatternParserParser.InLastContext;
import org.batah.CrochetPatternParserParser.InNextStContext;
import org.batah.CrochetPatternParserParser.InNextXContext;
import org.batah.CrochetPatternParserParser.InstructionContext;
import org.batah.CrochetPatternParserParser.InstructionsContext;
import org.batah.CrochetPatternParserParser.RepeatContext;
import org.batah.CrochetPatternParserParser.RepeatTimesContext;
import org.batah.CrochetPatternParserParser.SkipCountContext;
import org.batah.CrochetPatternParserParser.SkipNextContext;
import org.batah.CrochetPatternParserParser.SkipStitchesContext;
import org.batah.CrochetPatternParserParser.StitchContext;
import org.batah.CrochetPatternParserParser.StitchesContext;
import org.batah.CrochetPatternParserParser.TimesRepeatContext;
import org.batah.CrochetPatternParserParser.ToLastRepeatContext;

public class CrochetVisitor<String> extends CrochetPatternParserBaseVisitor<String> {


    int i = 0;
    @Override
    public String visitInstructions(InstructionsContext ctx) {
        return super.visitInstructions(ctx);
    }

    @Override
    public String visitInstruction(InstructionContext ctx) {
        System.out.println(i + " " + ctx.getText());
        i++;
        return super.visitInstruction(ctx);
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
    public String visitAttachment(AttachmentContext ctx) {
        return super.visitAttachment(ctx);
    }

    @Override
    public String visitInNextX(InNextXContext ctx) {
        return super.visitInNextX(ctx);
    }

    @Override
    public String visitInNextSt(InNextStContext ctx) {
        return super.visitInNextSt(ctx);
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
        return super.visitInChainN(ctx);
    }

    @Override
    public String visitChain(ChainContext ctx) {
        return super.visitChain(ctx);
    }

    @Override
    public String visitStitches(StitchesContext ctx) {
        return super.visitStitches(ctx);
    }

    @Override
    public String visitStitch(StitchContext ctx) {
        return super.visitStitch(ctx);
    }

    @Override
    public String visitRepeatTimes(RepeatTimesContext ctx) {
        return super.visitRepeatTimes(ctx);
    }
}