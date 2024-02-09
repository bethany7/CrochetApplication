package org.batah.model.stitches;

import org.batah.model.*;

public abstract class Stitch {

  Attachment attachment;
  StitchLoc parentStitch;
  StitchLoc loc;
  Pattern pattern;

  public Stitch(Attachment attachment, StitchLoc parentStitch,
      StitchLoc loc, Pattern pattern) {
    this.attachment = attachment;
    this.parentStitch = parentStitch;
    this.loc = loc;
    this.pattern = pattern;
  }

  public Stitch(Attachment attachment, Row row) {

    this.attachment = attachment;
    this.loc = new StitchLoc(row.getRowNum(), (row.getStitchCount() + 1));
    this.pattern = row.getPattern();
    if (row.getRowNum() == 1 || attachment == Attachment.NONE) {
      this.parentStitch = null;
    } else {
      Row prevRow = pattern.getRow(row.getRowNum() - 1);
      int parentStitchNum = (prevRow.getStitchCount() + 1) - this.loc.getStitchNum();
      this.parentStitch = new StitchLoc(prevRow.getRowNum(), parentStitchNum);
    }
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

  public enum Attachment {
    INTO, BETWEEN, BEYOND, NONE
  }

}