package org.batah.model.stitches;

import org.batah.model.Pattern;
import org.batah.model.Row;

public class TrebleCrochet extends Stitch{

  public TrebleCrochet(Attachment attachment, StitchLoc parentStitch, StitchLoc loc,
      Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
  }

  public TrebleCrochet(Attachment attachment, Row row) {
    super(attachment, row);
  }

  @Override
  public void drawStitch(Stitch stitch) {

  }
}