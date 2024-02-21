package org.batah.model;

import java.util.ArrayList;

public class Pattern {

  ArrayList<Row> pattern;

  public Pattern() {
    this.pattern = new ArrayList<>();
  }

  public ArrayList<Row> getPattern() {
    return pattern;
  }

  public int getRowCount() {
    return pattern.size();
  }

  public Row getRow(int rowNum) {
    return pattern.get(rowNum - 1);
  }

  public void addRow(Row row) {
    pattern.add(row);
  }

  public int getMaxStitchCount() {
    int maxStitchCount = 0;
    for (Row row : pattern) {
      if (row.getStitchCount() > maxStitchCount) {
        maxStitchCount = row.getStitchCount();
      }
    }
    return maxStitchCount;
  }

}