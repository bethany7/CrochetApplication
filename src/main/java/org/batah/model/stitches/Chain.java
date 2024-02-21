package org.batah.model.stitches;

import javafx.scene.canvas.GraphicsContext;
import org.batah.model.Row;

public class Chain extends Stitch {

  public Chain(Attachment attachment, Row row) {
    super(attachment, row);
    this.defaultStitchHeight = 30;
    this.defaultStitchWidth = 100;
  }

  public Chain(Row row) {
    super(Attachment.NONE, row);
    this.defaultStitchHeight = 30;
    this.defaultStitchWidth = 100;
  }

  public void Draw(Stitch stitch, GraphicsContext gc, double offsetX, double offsetY, double scaleX,
      double scaleY) {
    // Draw the stitch
      gc.scale(scaleX, scaleY);

      double startY = (int) (offsetY / scaleY);
      gc.setStroke(javafx.scene.paint.Color.BLACK);
      gc.setLineWidth(4);
      gc.beginPath();
      gc.moveTo(offsetX, startY);
      gc.strokeOval(offsetX, startY, 100, 30);

      gc.closePath();
      gc.stroke();
    }

}