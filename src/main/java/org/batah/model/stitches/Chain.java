package org.batah.model.stitches;

import org.batah.model.Row;

public class Chain extends Stitch {

  public Chain(Attachment attachment, Row row) {
    super(attachment, row);
  }

  public Chain(Row row) {
    super(Attachment.NONE, row);
  }

  @Override
  public void drawStitch(Stitch stitch) {

  }
}