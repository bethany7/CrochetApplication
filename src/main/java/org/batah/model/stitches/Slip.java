package org.batah.model.stitches;

import java.util.ArrayList;
import javafx.scene.shape.SVGPath;
import org.batah.model.Pattern;
import org.batah.model.Row;

public class Slip extends Stitch {


  public Slip(Attachment attachment, ArrayList<StitchLoc> parentStitches, StitchLoc loc,
      Row row) {
    super(attachment, parentStitches, loc, row);
    this.defaultStitchWidth = 100;
    this.defaultStitchHeight = 100;
  }

//  public Slip(Attachment attachment, Row row) {
//    super(attachment, row);
//    this.defaultStitchWidth = 100;
//    this.defaultStitchHeight = 100;
//  }

  public SVGPath Draw() {
    // Draw the stitch

    SVGPath path = new SVGPath();

    path.setFill(javafx.scene.paint.Color.BLACK);
    path.setStroke(javafx.scene.paint.Color.BLACK);
    path.setStrokeWidth(2);

    String content = "M 50,50 m 50,0 a 50,50 0 1, 0 -100,0 a 50,50 0 1,0 100, 0 z";
    path.setContent(content);

    return path;


  }

}