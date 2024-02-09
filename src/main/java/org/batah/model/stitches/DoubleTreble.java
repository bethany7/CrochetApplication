package org.batah.model.stitches;

import org.batah.model.Pattern;
import org.batah.model.Row;

public class DoubleTreble extends Stitch {

  public DoubleTreble(Attachment attachment, StitchLoc parentStitch, StitchLoc loc,
      Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
  }

  public DoubleTreble(Attachment attachment, Row row) {
    super(attachment, row);
  }
}