package org.batah.model.stitches;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.SVGPath;
import org.batah.model.*;

public abstract class Stitch {

  Attachment attachment;
  ArrayList <StitchLoc> parentStitches = new ArrayList <StitchLoc> ();
  StitchLoc loc;
  Pattern pattern;

  double defaultStitchWidth = 0;
  double defaultStitchHeight = 0;

  public Stitch(Attachment attachment, ArrayList <StitchLoc> parentStitches, StitchLoc loc, Row row) {
    this.attachment = attachment;
    this.parentStitches = parentStitches;
    this.loc = loc;
    this.pattern = row.getPattern();

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

  public enum Attachment {
    INTO, BETWEEN, BEYOND, NONE
  }

}