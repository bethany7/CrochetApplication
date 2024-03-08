package org.batah.decorators;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.SVGPath;
import org.batah.model.StitchAttachmentCoords;

public class SelectedStitch {

  SVGPath stitchPath;
  StitchAttachmentCoords stitchAttachmentCoords;


  public SelectedStitch(SVGPath stitchPath, StitchAttachmentCoords stitchAttachmentCoords) {
    this.stitchPath = stitchPath;
    this.stitchAttachmentCoords = stitchAttachmentCoords;
  }

  public SVGPath drawBorderOnSelectedStitch() {
    // Draw a border around the selected stitch
    Bounds bounds = stitchPath.getBoundsInParent();
    System.out.println("Bounds: " + bounds);
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