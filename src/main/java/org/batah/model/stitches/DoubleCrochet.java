package org.batah.model.stitches;

import javafx.scene.shape.SVGPath;
import org.batah.model.*;

public class DoubleCrochet extends Stitch {

  public DoubleCrochet(Attachment attachment, StitchLoc parentStitch, StitchLoc loc,
      Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
    this.defaultStitchWidth = 100;
    this.defaultStitchHeight = 100;
  }

  public DoubleCrochet(Attachment attachment, Row row) {
    super(attachment, row);
    this.defaultStitchWidth = 100;
    this.defaultStitchHeight = 100;
  }

  public SVGPath Draw() {
    // Draw the stitch

    SVGPath path = new SVGPath();

    path.setFill(null);
    path.setStroke(javafx.scene.paint.Color.BLACK);
    path.setStrokeWidth(2);

    String content = "M 50,100 V 50,0 M 5,50 H 95,50";
    path.setContent(content);

    return path;


  }

}