package org.batah.model.stitches;

import org.batah.model.Pattern;
import org.batah.model.Row;

public class HalfTreble extends Stitch {

  public HalfTreble(Attachment attachment, StitchLoc parentStitch, StitchLoc loc,
      Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
  }

  public HalfTreble (Attachment attachment, Row row) {
    super(attachment, row);
  }

  @Override
  public void drawStitch(Stitch stitch) {

  }
}