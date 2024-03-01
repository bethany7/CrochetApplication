package org.batah.model.stitches;

import javafx.scene.shape.SVGPath;
import org.batah.model.Pattern;
import org.batah.model.Row;

public class DoubleTreble extends Stitch{


  public DoubleTreble(Attachment attachment, StitchLoc parentStitch, StitchLoc loc,
      Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
    this.defaultStitchWidth = 200;
    this.defaultStitchHeight = 320;
  }

  public DoubleTreble(Attachment attachment, Row row) {
    super(attachment, row);
    this.defaultStitchWidth = 200;
    this.defaultStitchHeight = 320;
  }

  public SVGPath Draw() {
    // Draw the stitch

    SVGPath path = new SVGPath();
    path.setFill(null);
    path.setStroke(javafx.scene.paint.Color.BLACK);
    path.setStrokeWidth(2);

    String content = "M 100,320 V 100,0 M 5,0 H 195,0 M 60,100 L 140, 160 M 60,130 L 140, 190";
//    String content = "M 100,300 V 100,0 M 5,0 H 195,0 M 60,120 L 140, 180";

    path.setContent(content);

    return path;

  }


}