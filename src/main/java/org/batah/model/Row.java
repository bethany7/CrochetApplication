package org.batah.model;

import java.util.ArrayList;
import org.batah.model.stitches.Stitch;
import org.javatuples.Triplet;

public class Row {

  int rowNum;
  ArrayList<Stitch> stitches;
  Pattern pattern;

  //Triplet<Integer, Integer, ArrayList<Stitch>> row;

  public Row(Pattern pattern) {
    this.stitches = new ArrayList<>();
    this.rowNum = pattern.pattern.isEmpty() ? 1 : pattern.pattern.size() + 1;
    //this.row = new Triplet<>(rowNum, 0, stitches);
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

  public Pattern getPattern() {
    return pattern;
  }

}