package org.batah.model.stitches;

import java.util.ArrayList;
import javafx.scene.shape.SVGPath;
import org.batah.model.*;

public class TrebleCrochet extends Stitch {

  public TrebleCrochet(Attachment attachment, ArrayList<StitchLoc> parentStitches, StitchLoc loc,
      Row row) {
    super(attachment, parentStitches, loc, row);
    this.defaultStitchWidth = 200;
    this.defaultStitchHeight = 280;
  }

//  public TrebleCrochet(Attachment attachment, Row row) {
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

    String content = "M 100,278 V 100,2 M 5,0 H 195,0 M 60,100 L 140, 160";
    path.setContent(content);

    return path;


  }

}