package org.batah.model.stitches;

import java.util.ArrayList;
import org.batah.model.Row;
import org.batah.model.stitches.Stitch.Attachment;

public class StitchBuilder {

  Row row;

  public StitchBuilder(Row row) {
    this.row = row;
  }

//  public static Stitch buildStitch(String stitchName, Attachment attachment, Row row) {
//    return switch (stitchName) {
//      case "ch" -> new Chain(row);
//      case "dc" -> new DoubleCrochet(attachment, row);
//      case "sl" -> new Slip(attachment, row);
//      case "tr" -> new TrebleCrochet(attachment, row);
//      case "dtr" -> new DoubleTreble(attachment, row);
//      case "htr" -> new HalfTreble(attachment, row);
//      case "ttr" -> new TripleTreble(attachment, row);
//      default -> throw new IllegalArgumentException("Invalid stitch name: " + stitchName);
//    };
//  }

  public static Stitch buildStitch(String stitchName, Attachment attachment,
      ArrayList<StitchLoc> parentStitches, StitchLoc loc,
      Row row) {
    return switch (stitchName) {
      case "DoubleCrochet", "dc" -> new DoubleCrochet(attachment, parentStitches, loc, row);
      case "TrebleCrochet", "tr" -> new TrebleCrochet(attachment, parentStitches, loc, row);
      case "DoubleTreble", "dtr" -> new DoubleTreble(attachment, parentStitches, loc, row);
      case "HalfTreble", "htr" -> new HalfTreble(attachment, parentStitches, loc, row);
      case "TripleTreble", "ttr" -> new TripleTreble(attachment, parentStitches, loc, row);
      default -> throw new IllegalArgumentException("Invalid stitch name: " + stitchName);
    };
  }

}