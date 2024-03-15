package org.batah.decorators;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;

public class SelectedStitch {

  SVGPath stitchPath;


  public SelectedStitch(SVGPath stitchPath) {
    this.stitchPath = stitchPath;
  }

  public SVGPath drawBorderOnSelectedStitch() {
    // Draw a border around the selected stitch
    Bounds bounds = stitchPath.getBoundsInParent();
    SVGPath border = new SVGPath();
    border.setContent(
        "M" + (bounds.getMinX() - 2) + " " + (bounds.getMinY() - 2) + " L" + (bounds.getMaxX() + 2)
            + " " + (bounds.getMinY() - 2) + " L" + (bounds.getMaxX() + 2) + " " + (bounds.getMaxY()
            + 2) + " L" + (bounds.getMinX() - 2) + " " + (bounds.getMaxY() + 2) + " Z");
    border.setStyle("-fx-stroke: red; -fx-stroke-width: 1; -fx-fill: none;");
    return border;
  }

  public ImageView drawResizeHandleOnSelectedStitch() {
    ImageView resizeHandle = new ImageView();
    Image resizeHandleImg = new Image("file:src/main/resources/images/resize-expand.png", 20, 20,
        false,
        false);
    resizeHandle.setImage(resizeHandleImg);
    Bounds bounds = stitchPath.getBoundsInParent();
    resizeHandle.setPickOnBounds(true);
    resizeHandle.setX(bounds.getMaxX() - 10);
    resizeHandle.setY(bounds.getMaxY() - 10);

    return resizeHandle;
  }

  public ImageView drawMoveHandleOnSelectedStitch() {
    ImageView moveHandle = new ImageView();
    Image moveHandleImg = new Image("file:src/main/resources/images/move.png", 20, 20,
        false,
        false);
    moveHandle.setImage(moveHandleImg);
    Bounds bounds = stitchPath.getBoundsInParent();
    moveHandle.setPickOnBounds(true);
    moveHandle.setX(bounds.getMaxX() - 10);
    moveHandle.setY(bounds.getMaxY() - 10);

    return moveHandle;

  }


}