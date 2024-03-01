package org.batah.model;

import java.util.ArrayList;

public class PatternCoords {

  Pattern pattern;
  ArrayList<RowCoords> rowCoordsList;
  public PatternCoords(Pattern pattern) {
    this.pattern = pattern;
    this.rowCoordsList = new ArrayList<>();
  }

  public void addRowCoords(RowCoords rowCoords) {
    this.rowCoordsList.add(rowCoords);
  }

  public ArrayList<RowCoords> getRowCoordsList() {
    return rowCoordsList;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public int getRowCount() {
    return pattern.getRowCount();
  }

  public StitchCoords getStitchCoords(int rowNum, int stitchNum) {
    return rowCoordsList.get(rowNum-1).getStitchCoords(stitchNum);
  }


}