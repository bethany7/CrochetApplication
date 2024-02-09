package org.batah.model.stitches;

import org.batah.model.Pattern;
import org.batah.model.Row;

public class TripleTreble extends Stitch {

  public TripleTreble(Attachment attachment, StitchLoc parentStitch, StitchLoc loc,
      Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
  }

  public TripleTreble(Attachment attachment, Row row) {
    super(attachment, row);
  }
}