package org.batah.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RowAttachmentCoords implements Serializable {

  ArrayList<StitchAttachmentCoords> stitchAttachmentCoords;
  int rowNum;
  Pattern pattern;

  public RowAttachmentCoords(Pattern pattern, int rowNum) {
    this.stitchAttachmentCoords = new ArrayList<>();
    this.rowNum = rowNum;
    this.pattern = pattern;
  }

  @Override
  public String toString() {
    return stitchAttachmentCoords.toString();
  }

  public void addStitchCoords(StitchAttachmentCoords stitchAttachmentCoords) {
    this.stitchAttachmentCoords.add(stitchAttachmentCoords);
  }

  public ArrayList<StitchAttachmentCoords> getStitchCoordsList() {
    return stitchAttachmentCoords;
  }

  public StitchAttachmentCoords getStitchCoords(int i) {
    return stitchAttachmentCoords.get(i);
  }

  public int getRowNum() {
    return rowNum;
  }
}