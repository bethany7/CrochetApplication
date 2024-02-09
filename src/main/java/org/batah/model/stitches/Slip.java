package org.batah.model.stitches;

import org.batah.model.Pattern;
import org.batah.model.Row;

public class Slip extends Stitch {


  public Slip(Attachment attachment, StitchLoc parentStitch, StitchLoc loc,
      Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
  }

  public Slip(Attachment attachment, Row row) {
    super(attachment, row);
  }
}