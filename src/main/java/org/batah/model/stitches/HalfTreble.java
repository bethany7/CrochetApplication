package org.batah.model.stitches;

import java.util.ArrayList;
import javafx.scene.shape.SVGPath;
import org.batah.model.Pattern;
import org.batah.model.Row;

public class HalfTreble extends Stitch {


  public HalfTreble(Attachment attachment, ArrayList<StitchLoc> parentStitches, StitchLoc loc,
      Row row ) {
    super(attachment, parentStitches, loc, row);
    this.defaultStitchWidth = 200;
    this.defaultStitchHeight = 280;
  }

//  public HalfTreble(Attachment attachment, Row row) {
//    super(attachment, row);
//    this.defaultStitchWidth = 200;
//    this.defaultStitchHeight = 280;
//  }
  public SVGPath Draw() {
    // Draw the stitch

    SVGPath path = new SVGPath();

    path.setFill(null);
    path.setStroke(javafx.scene.paint.Color.BLACK);
    path.setStrokeWidth(2);

    String content = "M 100,280 V 100,0 M 5,0 H 195,0";
    path.setContent(content);

    return path;


  }

}