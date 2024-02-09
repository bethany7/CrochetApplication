package org.batah.model.stitches;

import org.javatuples.Pair;

public class StitchLoc {

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