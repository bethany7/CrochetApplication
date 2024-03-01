package org.batah.model;

import java.util.ArrayList;

public class RowCoords {

  ArrayList<StitchCoords> stitchCoords;
  int rowNum;
  Pattern pattern;

  public RowCoords(Pattern pattern, int rowNum) {
    this.stitchCoords = new ArrayList<>();
    this.rowNum = rowNum;
    this.pattern = pattern;
  }

  public void addStitchCoords(StitchCoords stitchCoords) {
    this.stitchCoords.add(stitchCoords);
  }

  public StitchCoords getStitchCoords(int i) {
    return stitchCoords.get(i);
  }

  public int getRowNum() {
    return rowNum;
  }
}