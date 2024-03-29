package org.batah.model.stitches;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.SVGPath;
import org.batah.model.Row;

public class Chain extends Stitch {

  public Chain(Attachment attachment, ArrayList<StitchLoc> parentStitches, StitchLoc loc,
      Row row) {
    super(attachment, parentStitches, loc, row);
    this.defaultStitchHeight = 60;
    this.defaultStitchWidth = 200;
  }

//  public Chain(Attachment attachment, Row row) {
//    super(attachment, row);
//    this.defaultStitchHeight = 60;
//    this.defaultStitchWidth = 200;
//  }

  public SVGPath Draw() {

    SVGPath path = new SVGPath();
    path.setFill(null);
    path.setStroke(javafx.scene.paint.Color.BLACK);
    path.setStrokeWidth(2);
    path.setContent("M 0,0 a 100,30 0 1,0 1,0 z");

    return path;

    }

}