package org.batah.model.stitches;

import javafx.scene.canvas.GraphicsContext;
import org.batah.model.*;

public class DoubleCrochet extends Stitch {

  public DoubleCrochet(Attachment attachment, StitchLoc parentStitch, StitchLoc loc,
      Pattern pattern) {
    super(attachment, parentStitch, loc, pattern);
    this.defaultStitchWidth = 100;
    this.defaultStitchHeight = 130;
  }

  public DoubleCrochet(Attachment attachment, Row row) {
    super(attachment, row);
    this.defaultStitchWidth = 100;
    this.defaultStitchHeight = 130;
  }

//  public void Draw(Stitch stitch, GraphicsContext gc, double offsetX, double offsetY, double scaleX,
//      double scaleY) {
//    // Draw the stitch
//    if (stitch.getLoc().getRowNum() % 2 != 0) {
//      gc.scale(scaleX, scaleY);
//      double startY = (int) (offsetY / scaleY);
//      gc.setStroke(javafx.scene.paint.Color.BLACK);
//      gc.setLineWidth(4);
//      gc.beginPath();
//      gc.moveTo(offsetX, startY);
//      gc.lineTo(offsetX + 90, startY);
//      gc.moveTo(offsetX + 45, startY);
//      gc.lineTo(offsetX + 45, startY + 130);
//      gc.moveTo((offsetX + 25), (startY + 40));
//      gc.lineTo(offsetX + 65, startY + 65);
//      gc.closePath();
//      gc.stroke();
//    }
//  }

  public void Draw(Stitch stitch, GraphicsContext gc, double startX, double offsetY, double scaleX,
      double scaleY) {
    // Draw the stitch

    var startY = (int) (offsetY / scaleY);
    gc.scale(scaleX, scaleY);

    gc.setStroke(javafx.scene.paint.Color.BLACK);
    gc.setLineWidth(4);
    gc.beginPath();

    gc.moveTo(startX, startY);
    gc.lineTo(startX, startY - 130);
    gc.moveTo((startX - defaultStitchWidth / 2)+5, startY - 130);
    gc.lineTo((startX + defaultStitchWidth / 2)-5, startY - 130);
    gc.moveTo(startX - 15, startY - 90);
    gc.lineTo(startX + 15, startY - 70);
    gc.closePath();
    gc.stroke();
  }

}