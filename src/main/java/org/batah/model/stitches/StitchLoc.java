package org.batah.model.stitches;

import java.io.Serializable;
import org.javatuples.Pair;

public class StitchLoc implements Serializable {

  Pair<Integer, Integer> loc;

  public StitchLoc(int rowNum, int stitchNum) {
    this.loc = new Pair<>(rowNum, stitchNum);
  }

  @Override
  public String toString() {
    return "" + loc.getValue0() + "," + loc.getValue1();
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