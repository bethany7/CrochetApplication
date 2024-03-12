package org.batah.model.stitches;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.SVGPath;
import org.batah.model.*;

public abstract class Stitch implements Serializable {

  Attachment attachment;
  ArrayList<StitchLoc> parentStitches;
  StitchLoc loc;
  Pattern pattern;
  double defaultStitchWidth = 0;
  double defaultStitchHeight = 0;

  public Stitch(Attachment attachment, ArrayList<StitchLoc> parentStitches, StitchLoc loc,
      Row row) {
    this.attachment = attachment;
    this.parentStitches = parentStitches;
    this.loc = loc;
    this.pattern = row.getPattern();

  }

  @Override
  public String toString() {
    return '{' + this.getStitchName() + ", " + "attachment=" + attachment + ", parentStitches="
        + parentStitches + ", loc=" + loc + '}';
  }

//  public Stitch(Attachment attachment, Row row) {
//
//    this.attachment = attachment;
//    this.loc = new StitchLoc(row.getRowNum(), (row.getStitchCount() + 1));
//    this.pattern = row.getPattern();
//    if (row.getRowNum() != 1 && attachment != Attachment.NONE) {
//      Row prevRow = pattern.getRow(row.getRowNum() - 1);
//      int parentStitchNum = (prevRow.getStitchCount() + 1) - this.loc.getStitchNum();
//      parentStitches.add(new StitchLoc(prevRow.getRowNum(), parentStitchNum));
//    }
//  }

  public String getStitchName() {
    return this.getClass().getSimpleName();
  }

  public Attachment getAttachment() {
    return attachment;
  }

  public ArrayList<StitchLoc> getParentStitches() {
    return parentStitches;
  }

  public StitchLoc getParentStitch(int i) {
    return parentStitches.get(i);
  }

  public StitchLoc getLoc() {
    return loc;
  }

  public void setLoc(StitchLoc loc) {
    this.loc = loc;
  }

  public void addParentStitch(StitchLoc parentStitch) {
    parentStitches.add(parentStitch);
  }

  public void setParentStitches(ArrayList<StitchLoc> parentStitches) {
    this.parentStitches = parentStitches;
  }

  public SVGPath Draw() {
    // Draw the stitch
    return null;
  }

  public double getDefaultStitchHeight() {
    return this.defaultStitchHeight;
  }

  public double getDefaultStitchWidth() {
    return this.defaultStitchWidth;
  }

  public void redoLoc() {
    if (this.loc.getRowNum() != this.getParentStitch(0).getRowNum() + 1) {
      StitchLoc newLoc = new StitchLoc(this.getParentStitch(0).getRowNum() + 1,
          this.loc.getStitchNum());
      this.setLoc(newLoc);
    }
  }

  public int getFirstParentStitchNum() {
    return this.getParentStitch(0).getStitchNum();
  }

  public enum Attachment {
    INTO, BETWEEN, BEYOND, NONE
  }

}