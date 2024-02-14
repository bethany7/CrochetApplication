package org.batah.model.stitches;

import org.batah.model.*;

public class DoubleCrochet extends Stitch {

  public DoubleCrochet(Attachment attachment, StitchLoc parentStitch, StitchLoc loc, Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
  }

  public DoubleCrochet(Attachment attachment, Row row) {
    super(attachment, row);
  }

  @Override
  public void drawStitch(Stitch stitch) {

  }
}