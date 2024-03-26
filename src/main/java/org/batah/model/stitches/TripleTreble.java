package org.batah.model.stitches;

import java.util.ArrayList;
import javafx.scene.shape.SVGPath;
import org.batah.model.Row;

public class TripleTreble extends Stitch {

  public TripleTreble(Attachment attachment, ArrayList<StitchLoc> parentStitches, StitchLoc loc,
      Row row) {
    super(attachment, parentStitches, loc, row);
    this.defaultStitchWidth = 200;
    this.defaultStitchHeight = 360;
  }

  //  public TripleTreble(Attachment attachment, Row row) {
//    super(attachment, row);
//    this.defaultStitchWidth = 200;
//    this.defaultStitchHeight = 360;
//  }
  public SVGPath Draw() {
    // Draw the stitch

    SVGPath path = new SVGPath();
    path.setFill(null);
    path.setStroke(javafx.scene.paint.Color.BLACK);
    path.setStrokeWidth(2);

    String content = "M 100,358 V 100,2 M 5,0 H 195,0 M 60,100 L 140, 160 M 60,130 L 140, 190 M 60, 160 L 140, 220";

    path.setContent(content);

    return path;

  }


}