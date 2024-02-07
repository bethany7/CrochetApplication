package org.batah;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;

public class Model {

  public class Stitch {

    StitchType stitchType;
    Attachment attachment;
    StitchLoc parentStitch;
    StitchLoc loc;

    Pattern pattern;

    public Stitch(StitchType stitchType, Attachment attachment, StitchLoc parentStitch,
        StitchLoc loc, Pattern pattern) {
      this.stitchType = stitchType;
      this.attachment = attachment;
      this.parentStitch = parentStitch;
      this.loc = loc;
      this.pattern = pattern;
    }

    public Stitch(StitchType stitchType, Attachment attachment, Row row) {
      this.stitchType = stitchType;
      this.attachment = attachment;
      this.loc = new StitchLoc(row.getRowNum(), (row.getStitchCount() + 1));
      this.pattern = row.pattern;
      if (row.getRowNum() == 1) {
        this.parentStitch = null;
      } else {
        Row prevRow = pattern.getRow(row.getRowNum() - 1);
        int parentStitchNum = (prevRow.getStitchCount() + 1) - this.loc.getStitchNum();
        this.parentStitch = new StitchLoc(prevRow.getRowNum(), parentStitchNum);
      }
    }

    public StitchType getStitchType() {
      return stitchType;
    }

    public Attachment getAttachment() {
      return attachment;
    }

    public StitchLoc getParentStitch() {
      return parentStitch;
    }

    public StitchLoc getLoc() {
      return loc;
    }

  }

  public static class StitchLoc {

    Pair<Integer, Integer> loc;

    public StitchLoc(int rowNum, int stitchNum) {
      this.loc = new Pair<>(rowNum, stitchNum);
    }

    public Pair<Integer, Integer> getLoc() {
      return loc;
    }

    public int getRowNum() {
      return loc.getValue0();
    }

    public int getStitchNum() {
      return loc.getValue1();
    }

  }

  public enum Attachment {
    INTO, BETWEEN, BEYOND, NONE
  }

  public enum StitchType {
    CHAIN, DOUBLE, HALF_DOUBLE, TREBLE, HALF_TREBLE, DOUBLE_TREBLE, DC2TOG, SLIP_STITCH
  }

  public class Row {

    int rowNum;
    ArrayList<Stitch> stitches;
    Pattern pattern;

    Triplet<Integer, Integer, ArrayList<Stitch>> row;

    public Row(Pattern pattern) {
      this.stitches = new ArrayList<>();
      this.rowNum = pattern.pattern.isEmpty() ? 1 : pattern.pattern.size() + 1;
      this.row = new Triplet<>(rowNum, 0, stitches);
      this.pattern = pattern;
    }

    public void addStitch(Row row, Stitch stitch) {
      row.stitches.add(stitch);
    }

    public int getRowNum() {
      return rowNum;
    }

    public int getStitchCount() {
      return stitches.size();
    }

    public Row getStitches() {
      return this;
    }
  }

  public class Pattern {

    ArrayList<Row> pattern;

    public Pattern() {
      this.pattern = new ArrayList<>();
    }

    public ArrayList<Row> getPattern() {
      return pattern;
    }

    public Row getRow(int rowNum) {
      return pattern.get(rowNum - 1);
    }

    public void addRow(Row row) {
      pattern.add(row);
    }

  }
}
