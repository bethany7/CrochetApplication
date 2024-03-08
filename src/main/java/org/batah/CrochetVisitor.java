package org.batah;

import java.util.ArrayList;
import org.batah.CrochetPatternParserParser.AllIncreasesContext;
import org.batah.CrochetPatternParserParser.ChainContext;
import org.batah.CrochetPatternParserParser.ChainInstrContext;
import org.batah.CrochetPatternParserParser.ChainStitchContext;
import org.batah.CrochetPatternParserParser.DecreaseContext;
import org.batah.CrochetPatternParserParser.GenericStitchContext;
import org.batah.CrochetPatternParserParser.InChain1Context;
import org.batah.CrochetPatternParserParser.InChain2Context;
import org.batah.CrochetPatternParserParser.InChain3Context;
import org.batah.CrochetPatternParserParser.InChainInstrContext;
import org.batah.CrochetPatternParserParser.InChainNContext;
import org.batah.CrochetPatternParserParser.InstructionsContext;
import org.batah.CrochetPatternParserParser.NIncreasesContext;
import org.batah.CrochetPatternParserParser.RepeatInstrsContext;
import org.batah.CrochetPatternParserParser.RepeatTimesContext;
import org.batah.CrochetPatternParserParser.SingleIncreaseContext;
import org.batah.CrochetPatternParserParser.SkipContext;
import org.batah.CrochetPatternParserParser.SlipStitchContext;
import org.batah.CrochetPatternParserParser.StitchDirectionInstrContext;
import org.batah.CrochetPatternParserParser.StitchInstrContext;
import org.batah.CrochetPatternParserParser.StitchTypeContext;
import org.batah.CrochetPatternParserParser.StitchesContext;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Chain;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.Stitch.Attachment;
import org.batah.model.stitches.StitchBuilder;
import org.batah.model.stitches.StitchLoc;

public class CrochetVisitor<String> extends CrochetPatternParserBaseVisitor<String> {


  Pattern pattern;
  StitchBuilder stitchBuilder;
  Row row;
  int skipCounter;
  int extraStitchCounter;

  public CrochetVisitor(Pattern pattern) {
    this.pattern = pattern;
  }

  @Override
  public String visitInstructions(InstructionsContext ctx) {
    row = new Row(pattern);
    pattern.addRow(row);
    this.stitchBuilder = new StitchBuilder(row);
    skipCounter = 0;
    extraStitchCounter = 0;
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

//  @Override
//  public String visitStitchDirectionInstr(StitchDirectionInstrContext ctx) {
//    String y = visit(ctx.stitches());
//    String z = visit(ctx.direction());
//    String[] newStitchParts = (String[]) y.toString().split(" ");
//    String[] parentStitchParts = (String[]) z.toString().split(" ");
//    int numOfParentStitch = Integer.parseInt((java.lang.String) parentStitchParts[0]);
//    int numOfStitches;
//    String stitchType;
//    if (newStitchParts.length < 2) {
//      stitchType = newStitchParts[0];
//      int parentRowNum = row.getRowNum() - 1;
//      int parentRowLength = pattern.getRow(parentRowNum).getStitchCount();
//      ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
//      StitchLoc parentStitch = new StitchLoc(parentRowNum, parentRowLength - numOfParentStitch + 1);
//      parentStitches.add(parentStitch);
//      Stitch stitch = StitchBuilder.buildStitchExact((java.lang.String) stitchType, Attachment.INTO,
//          parentStitches, new StitchLoc(row.getRowNum(), row.getStitchCount() + 1),
//          row);
//      row.addStitch(row, stitch);
//    } else {
//      numOfStitches = Integer.parseInt((java.lang.String) newStitchParts[0]);
//      stitchType = newStitchParts[1];
//
//      if (numOfStitches == numOfParentStitch) {
//        for (int k = 0; k < numOfStitches; k++) {
//          Stitch stitch = StitchBuilder.buildStitch((java.lang.String) stitchType,
//              Attachment.INTO, row);
//          row.addStitch(row, stitch);
//        }
//      } else {
//        System.out.println("Error: stitch count does not match parent stitch count");
//      }
//    }
//    return super.visitStitchDirectionInstr(ctx);
//  }

  @Override
  public String visitStitchDirectionInstr(StitchDirectionInstrContext ctx) {
    String y = visit(ctx.stitches());
    String z = visit(ctx.direction());
    String[] newStitchParts = (String[]) y.toString().split(" ");
    String[] directionParts = (String[]) z.toString().split(" ");

    String newStitchType;
    int numParentStitches;
    if (newStitchParts.length > 1) {
      newStitchType = newStitchParts[1];
    } else {
      newStitchType = newStitchParts[0];
    }
    if (directionParts.length > 1) {
      numParentStitches = Integer.parseInt((java.lang.String) directionParts[0]);
    } else {
      numParentStitches = 1;
    }
    Row prevRow = pattern.getRow(row.getRowNum() - 1);
    for (int k = 0; k < numParentStitches; k++) {
      int thisStitchNum = (row.getStitchCount() + 1);
      ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
      int parentStitchNum =
          (prevRow.getStitchCount() + 1) - thisStitchNum - skipCounter + extraStitchCounter;
      parentStitches.add(new StitchLoc(prevRow.getRowNum(), parentStitchNum));
      Stitch stitch = StitchBuilder.buildStitch((java.lang.String) newStitchType, Attachment.INTO,
          parentStitches, new StitchLoc(row.getRowNum(), row.getStitchCount() + 1), row);
      row.addStitch(row, stitch);
    }
    return super.visitStitchDirectionInstr(ctx);
  }

  @Override
  public String visitStitchInstr(StitchInstrContext ctx) {
    String x = visit(ctx.stitches());
    String[] parts = (String[]) x.toString().split(" ");

    Row prevRow = pattern.getRow(row.getRowNum() - 1);

    if (parts.length < 2) {
      int thisStitchNum = (row.getStitchCount() + 1);
      ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
      int parentStitchNum =
          (prevRow.getStitchCount() + 1) - thisStitchNum - skipCounter + extraStitchCounter;
      parentStitches.add(new StitchLoc(prevRow.getRowNum(), parentStitchNum));
      Stitch stitch = StitchBuilder.buildStitch((java.lang.String) x, Attachment.INTO,
          parentStitches, new StitchLoc(row.getRowNum(), row.getStitchCount() + 1), row);
      row.addStitch(row, stitch);
    } else {
      int i = Integer.parseInt((java.lang.String) parts[0]);
      String y = parts[1];
      for (int j = 0; j < i; j++) {
        int thisStitchNum = (row.getStitchCount() + 1);
        ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
        int parentStitchNum =
            (prevRow.getStitchCount() + 1) - thisStitchNum - skipCounter + extraStitchCounter;
        parentStitches.add(new StitchLoc(prevRow.getRowNum(), parentStitchNum));
        Stitch stitch = StitchBuilder.buildStitch((java.lang.String) y, Attachment.INTO,
            parentStitches, new StitchLoc(row.getRowNum(), thisStitchNum),
            row);
        row.addStitch(row, stitch);
      }
    }
    return super.visitStitchInstr(ctx);
  }

  //  @Override
//  public String visitDecreaseInstr(DecreaseInstrContext ctx) {
//    return super.visitDecreaseInstr(ctx);
//  }
//
//  @Override
//  public String visitIncreaseInstr(IncreaseInstrContext ctx) {
//    return super.visitIncreaseInstr(ctx);
//  }
//
//
  @Override
  public String visitSingleIncrease(SingleIncreaseContext ctx) {
    String stitchType = (String) ctx.STITCHTYPE().getText();
    int incNum = Integer.parseInt(ctx.INT().getText());
    Row prevRow = pattern.getRow(row.getRowNum() - 1);
    int thisStitchNum = (row.getStitchCount() + 1);
    ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
    int parentStitchNum =
        (prevRow.getStitchCount() + 1) - thisStitchNum - skipCounter + extraStitchCounter;
    parentStitches.add(new StitchLoc(prevRow.getRowNum(), parentStitchNum));

    Stitch stitch1 = StitchBuilder.buildStitch((java.lang.String) stitchType, Attachment.INTO,
        parentStitches, new StitchLoc(row.getRowNum(), row.getStitchCount() + 1), row);
    row.addStitch(row, stitch1);

    for (int k = 1; k < incNum; k++) {
      StitchLoc thisStitch = new StitchLoc(row.getRowNum(), row.getStitchCount() + 1);
      Stitch stitchX = StitchBuilder.buildStitch((java.lang.String) stitchType,
          Attachment.INTO, parentStitches, thisStitch, row);
      row.addStitch(row, stitchX);
      extraStitchCounter++;
    }
    return super.visitSingleIncrease(ctx);
  }

  @Override
  public String visitNIncreases(NIncreasesContext ctx) {
    String stitchType = (String) ctx.STITCHTYPE().getText();
    int incNum = Integer.parseInt(ctx.INT().getText());
    String stitches = visit(ctx.stitches());
    String[] parts = (String[]) stitches.toString().split(" ");
    int repeats;
    if (parts.length > 1) {
      repeats = Integer.parseInt((java.lang.String) parts[0]);
    } else {
      repeats = 1;
    }
    for (int i = 0; i < repeats; i++) {
      Row prevRow = pattern.getRow(row.getRowNum() - 1);
      int thisStitchNum = (row.getStitchCount() + 1);
      ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
      int parentStitchNum =
          (prevRow.getStitchCount() + 1) - thisStitchNum - skipCounter + extraStitchCounter;
      //System.out.println("thisStitchNum " + thisStitchNum + " parentStitchNum: " + parentStitchNum);
      parentStitches.add(new StitchLoc(prevRow.getRowNum(), parentStitchNum));
      Stitch stitch1 = StitchBuilder.buildStitch((java.lang.String) stitchType, Attachment.INTO,
          parentStitches,
          new StitchLoc(row.getRowNum(), thisStitchNum), row);
      row.addStitch(row, stitch1);

      for (int k = 1; k < incNum; k++) {
        StitchLoc thisStitch = new StitchLoc(row.getRowNum(), row.getStitchCount() + 1);
        Stitch stitchX = StitchBuilder.buildStitch((java.lang.String) stitchType,
            Attachment.INTO, parentStitches, thisStitch, row);
        row.addStitch(row, stitchX);
        extraStitchCounter++;
      }
    }
    return super.visitNIncreases(ctx);
  }

  @Override
  public String visitAllIncreases(AllIncreasesContext ctx) {
    return super.visitAllIncreases(ctx);
  }

  @Override
  public String visitDecrease(DecreaseContext ctx) {
    String stitchType = (String) ctx.STITCHTYPE().getText();
    int decNum;
    int numTimes;
    if (ctx.getChildCount() > 3) {
      decNum = Integer.parseInt(ctx.INT(1).getText());
      numTimes = Integer.parseInt(ctx.INT(0).getText());
    } else {
      decNum = Integer.parseInt(ctx.INT(0).getText());
      numTimes = 1;
    }
    for (int k = 0; k < numTimes; k++) {
      ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
      Row prevRow = pattern.getRow(row.getRowNum() - 1);
      int thisStitchNum = (row.getStitchCount() - 1);
      int firstParent =
          (prevRow.getStitchCount() + 1) - thisStitchNum - skipCounter + extraStitchCounter;
      for (int j = 0; j < decNum; j++) {
        StitchLoc parentStitch = new StitchLoc(prevRow.getRowNum(), firstParent + j);
        parentStitches.add(parentStitch);
      }
      Stitch stitch = StitchBuilder.buildStitch((java.lang.String) stitchType, Attachment.INTO,
          parentStitches, new StitchLoc(row.getRowNum(), thisStitchNum), row);
      row.addStitch(row, stitch);
    }
    return super.visitDecrease(ctx);
  }

  //
//  @Override
//  public String visitRepeatMarker(RepeatMarkerContext ctx) {
//    return super.visitRepeatMarker(ctx);
//  }
//
//  @Override
//  public String visitSkipInstr(SkipInstrContext ctx) {
//    return super.visitSkipInstr(ctx);
//  }
//
//  @Override
//  public String visitFinalTurnInstr(FinalTurnInstrContext ctx) {
//    return super.visitFinalTurnInstr(ctx);
//  }
//
//  @Override
//  public String visitTimesRepeat(TimesRepeatContext ctx) {
//    return super.visitTimesRepeat(ctx);
//  }
//
//  @Override
//  public String visitToLastRepeat(ToLastRepeatContext ctx) {
//    return super.visitToLastRepeat(ctx);
//  }
//
//  @Override
//  public String visitRepeat(RepeatContext ctx) {
//    return super.visitRepeat(ctx);
//  }
//
  @Override
  public String visitSkip(SkipContext ctx) {
    if (ctx.INT() != null) {
      skipCounter += Integer.parseInt(ctx.INT().getText());
    } else {
      var x = visit(ctx.stitches());
      String[] parts = (String[]) x.toString().split(" ");
      if (parts.length > 1) {
        skipCounter += Integer.parseInt((java.lang.String) parts[0]);
      } else {
        skipCounter += 1;
      }
    }

    return super.visitSkip(ctx);
  }

  //
//  @Override
//  public String visitInNextDir(InNextDirContext ctx) {
//
//    return super.visitInNextDir(ctx);
//  }
//
//  @Override
//  public String visitInEachDir(InEachDirContext ctx) {
//    return super.visitInEachDir(ctx);
//  }
//
//  @Override
//  public String visitInFirstDir(InFirstDirContext ctx) {
//    return super.visitInFirstDir(ctx);
//  }
//
//  @Override
//  public String visitInLastDir(InLastDirContext ctx) {
//    return super.visitInLastDir(ctx);
//  }
//
  @Override
  public String visitInChainInstr(InChainInstrContext ctx) {
    String chain = (String) visit(ctx.inChain()).toString();
    int i = Integer.parseInt((java.lang.String) chain);
    String newStitch = visit(ctx.stitches());
    String[] parts = (String[]) newStitch.toString().split(" ");
    String stitchType;
    if (parts.length > 1) {
      stitchType = parts[1];
    } else {
      stitchType = parts[0];
    }
    ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
    Row parentRow = pattern.getRow(row.getRowNum() - 1);
    StitchLoc parentStitch = new StitchLoc(parentRow.getRowNum(),
        parentRow.getStitchCount() - i + 1);
    parentStitches.add(parentStitch);
    Stitch stitch = StitchBuilder.buildStitch((java.lang.String) stitchType, Attachment.INTO,
        parentStitches, new StitchLoc(row.getRowNum(), 1),
        row);
    row.addStitch(row, stitch);

    return super.visitInChainInstr(ctx);
  }

  //
//  @Override
//  public String visitInNext(InNextContext ctx) {
//    return super.visitInNext(ctx);
//  }
//
//  @Override
//  public String visitInEach(InEachContext ctx) {
//    return super.visitInEach(ctx);
//  }
//
//  @Override
//  public String visitInFirst(InFirstContext ctx) {
//    return super.visitInFirst(ctx);
//  }
//
//  @Override
//  public String visitInLast(InLastContext ctx) {
//    return super.visitInLast(ctx);
//  }
//
  @Override
  public String visitInChain1(InChain1Context ctx) {
    return (String) "1";
  }

  @Override
  public String visitInChain2(InChain2Context ctx) {
    return (String) "2";
  }

  @Override
  public String visitInChain3(InChain3Context ctx) {
    return (String) "3";
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
      ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
      int thisStitchNum = (row.getStitchCount() + 1);
      if (this.row.getRowNum() != 1) {
        Row prevRow = pattern.getRow(row.getRowNum() - 1);
        int parentStitchNum =
            (prevRow.getStitchCount() + 1) - thisStitchNum - skipCounter + extraStitchCounter;
        parentStitches.add(new StitchLoc(prevRow.getRowNum(), parentStitchNum));
      } else {
        parentStitches.add(new StitchLoc(0, 0));
      }
      Chain chain = new Chain(Attachment.NONE, parentStitches,
          new StitchLoc(row.getRowNum(), thisStitchNum), row);
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
    } else {
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