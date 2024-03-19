package org.batah;

import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Stitch;

public class Unparser {

  Pattern pattern;

  public Unparser(Pattern pattern) {
    this.pattern = pattern;
  }

  public String unparseRow(Row row) {
    int numOfStitches = row.getStitchCount();
    int i = 0; //current stitch num
    int counter = 1; //number of times the current stitch has been repeated
    StringBuilder sb = new StringBuilder();
    while (i < numOfStitches) {
      Stitch stitch = row.getStitch(i);
      int j = i + 1; //next stitch num
      if (j < numOfStitches && stitch.getStitchName()
          .equals(row.getStitch(j).getStitchName())) {
        counter++;
        i++;
      } else {
        if (counter == 1) {
          sb.append(stitch.getStitchName()).append(", ");
          i++;
        } else if (stitch.getStitchName().equals("Chain")) {
          sb.append(stitch.getStitchName()).append(" ").append(counter).append(", ");
          counter = 1;
          i++;
        } else {
          sb.append(counter).append(" ").append(stitch.getStitchName()).append(", ");
          counter = 1;
          i++;
        }
      }
    }
    return sb.toString();
  }


}