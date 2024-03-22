package org.batah.ui;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Scale;
import org.batah.SerializableBounds;

import org.batah.decorators.SelectedStitch;
import org.batah.model.Coords;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.RowBounds;
import org.batah.model.StitchBounds;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.StitchLoc;

public class PatternCanvas extends Pane {

  Pattern pattern;
  GraphicalView graphicalView;

  double offsetX = 0;
  double offsetY = 0;

  double scaleX = 1;
  double scaleY = 1;

  int height;
  int width;
  boolean selected = false;

  private SVGPath activeStitch = null;
  private EventHandler<MouseEvent> activeStitchHandler = null;
  private ImageView activeMoveHandle = null;
  private EventHandler<MouseEvent> activeMoveHandleHandler = null;

  private ImageView activeResizeHandle = null;
  private EventHandler<MouseEvent> activeResizeHandler = null;


  public PatternCanvas(Pattern pattern, GraphicalView graphicalView) {
    this.pattern = pattern;
    this.graphicalView = graphicalView;
    graphicalView.patternCanvas = this;
  }

  public void drawPattern(int width, int height) {
    // Draw the pattern
    this.width = width;
    this.height = height;

    var rowCount = pattern.getRowCount();
    var stitchCount = pattern.getMaxStitchCount();

    var stitchMaxWidth = ((width - 60) / stitchCount);
    var stitchMaxHeight = ((height - 60) / rowCount);

    var firstStitch = pattern.getRow(1).getStitch(0);
    scaleX = (double) stitchMaxWidth / firstStitch.getDefaultStitchWidth();
    scaleY = (double) stitchMaxHeight / firstStitch.getDefaultStitchHeight();

    if (scaleX < scaleY) {
      scaleY = scaleX;
    } else {
      scaleX = scaleY;
    }

    if (pattern.getRowBoundsList().isEmpty()) {
      drawChain(pattern.getRow(1));
      for (int i = 2; i <= rowCount; i++) {
        var row = pattern.getRow(i);
        var rowBounds = drawRow(row);
        pattern.addRowBounds(rowBounds);
      }
    } else {
      redrawFromSaved(pattern);
    }
//
//    try {
//      SerializationUtil.serialize(pattern, "pattern.ser");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

//    try {
//      Pattern pattern2 = (Pattern) SerializationUtil.deserialize("pattern.ser");
//      System.out.println("pattern object: " + pattern);
//      System.out.println("Deserialized pattern object: " + pattern2);
//    } catch (ClassNotFoundException | IOException e) {
//      e.printStackTrace();
//    }

  }

  public void redrawFromSaved(Pattern pattern) {

    for (RowBounds rowBounds : pattern.getRowBoundsList()) {
      offsetX =
          (pattern.getRowBoundsList().getFirst().getStitch(0).getDefaultStitchWidth() / 2)
              * scaleX
              + 10;
      for (StitchBounds stitchBounds : rowBounds.getStitchBoundsList()) {
        var stitch = stitchBounds.getStitch();
        SVGPath stitchPath = stitch.Draw();
        stitchPath.setStrokeWidth(3);
        if (stitch.getStitchName().equals("Chain")) {
          offsetY = (height - 40 - (stitch.getDefaultStitchHeight() / 2 * scaleY));
          stitchPath.setLayoutX(offsetX);
          stitchPath.setLayoutY(offsetY);
          stitchPath.setScaleX(scaleX);
          stitchPath.setScaleY(scaleY);
          graphicalView.patternPane.getChildren().add(stitchPath);
          offsetX += (stitch.getDefaultStitchWidth() * scaleX);

        } else {
          offsetX = stitchBounds.getBounds().getMinX();
          offsetY = stitchBounds.getBounds().getMinY();
          stitchPath.relocate(offsetX, offsetY);
          stitchPath.getTransforms().add(new Scale(scaleX, scaleY));
          graphicalView.patternPane.getChildren().add(stitchPath);
        }
        stitchPath.setPickOnBounds(true);
        stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
          stitchClickedHandler(stitchPath, stitchBounds, rowBounds);
        });

      }
    }
  }

  public void drawChain(Row row) {

    var defaultStitchWidth = row.getStitch(0).getDefaultStitchWidth();
    var defaultStitchHeight = row.getStitch(0).getDefaultStitchHeight();

    RowBounds rowBounds = new RowBounds(pattern, row.getRowNum(), row);

    offsetX = (defaultStitchWidth / 2) * scaleX + 10;
    offsetY = (height - 40 - (defaultStitchHeight / 2 * scaleY));

    for (int j = 0; j < row.getStitchCount(); j++) {

      var stitch = row.getStitch(j);

      SVGPath stitchPath = stitch.Draw();

      stitchPath.setLayoutX(offsetX);
      stitchPath.setLayoutY(offsetY);
      stitchPath.setScaleX(scaleX);
      stitchPath.setScaleY(scaleY);

      SerializableBounds serialBounds = new SerializableBounds(
          stitchPath.getBoundsInParent().getMinX(), stitchPath.getBoundsInParent().getMinY(),
          stitchPath.getBoundsInParent().getWidth(), stitchPath.getBoundsInParent().getHeight());
      StitchBounds stitchBounds = new StitchBounds(stitch, serialBounds);
      rowBounds.addStitchBounds(stitchBounds);
      stitchPath.setPickOnBounds(true);
      stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
        stitchClickedHandler(stitchPath, stitchBounds, rowBounds);
      });

      graphicalView.patternPane.getChildren().add(stitchPath);

      offsetX += (defaultStitchWidth * scaleX);
    }

    pattern.addRowBounds(rowBounds);

  }

  public RowBounds drawRow(Row row) {

    RowBounds rowBounds = new RowBounds(pattern, row.getRowNum(), row);

    for (int j = 0; j < row.getStitchCount(); j++) {

      int parentStitchNum;
      var stitch = row.getStitch(j);
      SVGPath stitchPath;

      if (stitch.getStitchName().equals("Chain") && j != 0) {

        var prevStitchCoords = rowBounds.getStitchAttachmentCoords(j - 1);
        offsetX = prevStitchCoords.getX() + (stitch.getDefaultStitchWidth()
            * scaleX);
        offsetY =
            prevStitchCoords.getY() - (
                (stitch.getDefaultStitchHeight() * scaleY) / 2);

        stitchPath = stitch.Draw();

        stitchPath.setLayoutX(offsetX);
        stitchPath.setLayoutY(offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));


      } else if (stitch.getStitchName().equals("Slip")) {
        parentStitchNum = stitch.getParentStitch(0).getStitchNum() - 1;
        var prevStitchAttachmentCoords = rowBounds.getStitchAttachmentCoords(j - 1);

        offsetX =
            pattern.getRowBounds(row.getRowNum() - 1).getStitchAndBounds(parentStitchNum)
                .getBounds().getCenterX() - (stitch.getDefaultStitchWidth() * scaleX / 2);

        offsetY =
            prevStitchAttachmentCoords.getY() - ((stitch.getDefaultStitchHeight() * scaleY) / 2);

        stitchPath = stitch.Draw();

        stitchPath.relocate(offsetX, offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));

      } else {
        parentStitchNum = stitch.getParentStitch(0).getStitchNum() - 1;

        offsetX =
            pattern.getRowBounds(row.getRowNum() - 1).getStitchAndBounds(parentStitchNum)
                .getAttachmentCoords().getX() - (stitch.getDefaultStitchWidth() * scaleX / 2);

        offsetY =
            pattern.getRowBounds(row.getRowNum() - 1).getStitchAndBounds(parentStitchNum)
                .getAttachmentCoords().getY() - (stitch.getDefaultStitchHeight() * scaleY) - (5
                * scaleY);

        stitchPath = stitch.Draw();
        stitchPath.setStrokeWidth(3);
        stitchPath.relocate(offsetX, offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));

      }

      graphicalView.patternPane.getChildren().add(stitchPath);

      stitchPath.setPickOnBounds(true);

      SerializableBounds serialBounds = new SerializableBounds(
          stitchPath.getBoundsInParent().getMinX(), stitchPath.getBoundsInParent().getMinY(),
          stitchPath.getBoundsInParent().getWidth(), stitchPath.getBoundsInParent().getHeight());
      StitchBounds stitchBounds = new StitchBounds(stitch, serialBounds);
      rowBounds.addStitchBounds(stitchBounds);

      stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
        stitchClickedHandler(stitchPath, stitchBounds, rowBounds);
      });

    }
    return rowBounds;
  }

  public void stitchClickedHandler(SVGPath stitchPath, StitchBounds stitchBounds,
      RowBounds rowBounds) {

    if (activeStitch != null) {
      activeStitch.removeEventHandler(MouseEvent.MOUSE_CLICKED, activeStitchHandler);
      activeMoveHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeMoveHandleHandler);
      activeResizeHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeResizeHandler);
    }

    EventHandler<MouseEvent> stitchHandler = e -> {
      stitchClickedHandler(stitchPath, stitchBounds, rowBounds);
    };
    stitchPath.addEventHandler(MouseEvent.MOUSE_CLICKED, stitchHandler);

    // Update the active stitch and its handler
    activeStitch = stitchPath;
    activeStitchHandler = stitchHandler;

    if (selected) {
      graphicalView.patternPane.getChildren()
          .remove(graphicalView.patternPane.lookup(".selected-stitch"));
      graphicalView.patternPane.getChildren()
          .remove(graphicalView.patternPane.lookup(".move-handle"));
      graphicalView.patternPane.getChildren()
          .remove(graphicalView.patternPane.lookup(".resize-handle"));
      selected = false;
    }
    SelectedStitch selectedStitch = new SelectedStitch(stitchPath);
    var border = selectedStitch.drawBorderOnSelectedStitch();
    border.getStyleClass().add("selected-stitch");
    graphicalView.patternPane.getChildren().add(border);

    var moveHandle = selectedStitch.drawMoveHandleOnSelectedStitch();
    moveHandle.getStyleClass().add("move-handle");
    graphicalView.patternPane.getChildren().add(moveHandle);

    var resizeHandle = selectedStitch.drawResizeHandleOnSelectedStitch();
    resizeHandle.getStyleClass().add("resize-handle");
    graphicalView.patternPane.getChildren().add(resizeHandle);

    selected = true;

    EventHandler<MouseEvent> moveHandleHandler = e -> moveHandleOnDraggedHandler(e, stitchPath,
        border, stitchBounds, rowBounds, moveHandle, resizeHandle);
    moveHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveHandleHandler);

    EventHandler<MouseEvent> resizeHandleHandler = e2 -> resizeHandleOnDraggedHandler(e2,
        border, stitchBounds, rowBounds, stitchPath, resizeHandle, moveHandle);
    resizeHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeHandleHandler);

    // Update the active move handle and its handler
    activeMoveHandle = moveHandle;
    activeMoveHandleHandler = moveHandleHandler;
    activeResizeHandle = resizeHandle;
    activeResizeHandler = resizeHandleHandler;

  }

  public void moveHandleOnDraggedHandler(MouseEvent e, SVGPath stitchPath,
      SVGPath border, StitchBounds stitchBounds, RowBounds rowBounds, ImageView moveHandle,
      ImageView resizeHandle) {

    var offsetX = e.getX();
    var offsetY = e.getY();
    var allCoords = pattern.getAllAttachmentCoords();
    allCoords.remove(stitchBounds.getAttachmentCoords());
    stitchPath.relocate(offsetX - stitchPath.getBoundsInParent().getWidth(),
        offsetY - stitchPath.getBoundsInParent().getHeight());
    border.relocate(offsetX - stitchPath.getBoundsInParent().getWidth(),
        offsetY - stitchPath.getBoundsInParent().getHeight());
    moveHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
    moveHandle.setY(stitchPath.getBoundsInParent().getMaxY() - 10);
    resizeHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
    resizeHandle.setY(stitchPath.getBoundsInParent().getMinY() - 10);

    for (Coords coords : allCoords) {
      if (stitchPath.getBoundsInParent().intersects(coords.getX(), coords.getY(), 1, 1)
          && pattern.getStitchLocByAttachmentCoords(coords).getRowNum()
          < pattern.getRowCount()) {
        var offsetX2 =
            coords.getX()
                - (stitchPath.getBoundsInParent().getWidth() / 2);
        var offsetY2 =
            coords.getY()
                - (stitchPath.getBoundsInParent().getHeight()) - (5 * scaleY);

        stitchPath.relocate(offsetX2, offsetY2);
        border.relocate(offsetX2, offsetY2);
        moveHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
        moveHandle.setY(stitchPath.getBoundsInParent().getMaxY() - 10);
        resizeHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
        resizeHandle.setY(stitchPath.getBoundsInParent().getMinY() - 10);
        StitchLoc newParentLoc = pattern.getStitchLocByAttachmentCoords(coords);

        moveHandle.removeEventHandler(MouseEvent.MOUSE_RELEASED, activeMoveHandleHandler);

        EventHandler<MouseEvent> moveHandleHandler = e2 -> moveHandleOnDragReleasedHandler(e2,
            stitchPath, border, stitchBounds, rowBounds, moveHandle, resizeHandle, newParentLoc);
        moveHandle.addEventHandler(MouseEvent.MOUSE_RELEASED, moveHandleHandler);

        // Update the active move handle and its handler
        activeMoveHandle = moveHandle;
        activeMoveHandleHandler = moveHandleHandler;

      }
    }
  }

  public void moveHandleOnDragReleasedHandler(MouseEvent e, SVGPath stitchPath,
      SVGPath border, StitchBounds stitchBounds, RowBounds rowBounds, ImageView moveHandle,
      ImageView resizeHandle, StitchLoc newParentLoc) {

    Stitch stitch = stitchBounds.getStitch();

    stitch.getParentStitches().clear();
    stitch.getParentStitches().add(newParentLoc);
    pattern.removeRowBounds(rowBounds);
    rowBounds.removeStitchBounds(stitchBounds);
    StitchBounds newStitchBounds = new StitchBounds(stitch, new SerializableBounds(
        stitchPath.getBoundsInParent().getMinX(),
        stitchPath.getBoundsInParent().getMinY(),
        stitchPath.getBoundsInParent().getWidth(),
        stitchPath.getBoundsInParent().getHeight()));
    rowBounds.addStitchBounds(newStitchBounds);
    System.out.println(newStitchBounds);
    pattern.addRowBounds(rowBounds);

    moveHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeMoveHandleHandler);
    EventHandler<MouseEvent> moveHandleHandler = e2 -> moveHandleOnDraggedHandler(e2,
        stitchPath, border, stitchBounds, rowBounds, moveHandle, resizeHandle);
    moveHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveHandleHandler);
    activeMoveHandle = moveHandle;
    activeMoveHandleHandler = moveHandleHandler;

    resizeHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeResizeHandler);
    EventHandler<MouseEvent> resizeHandleHandler = e2 -> resizeHandleOnDraggedHandler(e2,
        border, newStitchBounds, rowBounds, stitchPath, resizeHandle, moveHandle);
    resizeHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeHandleHandler);
    activeResizeHandle = resizeHandle;
    activeResizeHandler = resizeHandleHandler;

//    try {
//      SerializationUtil.serialize(pattern, "pattern2.ser");
//    } catch (IOException e3) {
//      e3.printStackTrace();
//    }

  }

  public void resizeHandleOnDraggedHandler(MouseEvent e, SVGPath border, StitchBounds stitchBounds,
      RowBounds rowBounds, SVGPath stitchPath, ImageView resizeHandle, ImageView moveHandle) {

    Scale newScale = new Scale();

    var x = stitchBounds.getBounds().getCenterX();
    var y = stitchBounds.getBounds().getMaxY();

    var newScaleVal = sqrt(pow(e.getX() - stitchBounds.getBounds().getMaxX(), 2) + pow(
        e.getY() - stitchBounds.getBounds().getMaxY(), 2)) / 300;
    newScale.setX(newScaleVal);
    newScale.setY(newScaleVal);
    stitchPath.getTransforms().clear();
    stitchPath.getTransforms().add(newScale);

    stitchPath.relocate(x - stitchPath.getBoundsInParent().getWidth() / 2,
        y - stitchPath.getBoundsInParent().getHeight());

    Bounds bounds = stitchPath.getBoundsInParent();
    border.setContent(
        "M" + (bounds.getMinX() - 2) + " " + (bounds.getMinY() - 2) + " L" + (bounds.getMaxX() + 2)
            + " " + (bounds.getMinY() - 2) + " L" + (bounds.getMaxX() + 2) + " " + (bounds.getMaxY()
            + 2) + " L" + (bounds.getMinX() - 2) + " " + (bounds.getMaxY() + 2) + " Z");
    border.relocate(bounds.getMinX() - 2, bounds.getMinY() - 2);

    moveHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
    moveHandle.setY(stitchPath.getBoundsInParent().getMaxY() - 10);
    resizeHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
    resizeHandle.setY(stitchPath.getBoundsInParent().getMinY() - 10);

    resizeHandle.removeEventHandler(MouseEvent.MOUSE_RELEASED, activeResizeHandler);
    EventHandler<MouseEvent> resizeHandleHandler = e2 -> resizeHandleOnDragReleasedHandler(e2,
        border,
        stitchBounds, rowBounds, stitchPath, resizeHandle, moveHandle);
    resizeHandle.addEventHandler(MouseEvent.MOUSE_RELEASED, resizeHandleHandler);

    activeResizeHandle = resizeHandle;
    activeResizeHandler = resizeHandleHandler;


  }

  public void resizeHandleOnDragReleasedHandler(MouseEvent e, SVGPath border,
      StitchBounds stitchBounds,
      RowBounds rowBounds, SVGPath stitchPath, ImageView resizeHandle, ImageView moveHandle) {

    pattern.removeRowBounds(rowBounds);
    rowBounds.removeStitchBounds(stitchBounds);
    StitchBounds newStitchBounds = new StitchBounds(stitchBounds.getStitch(),
        new SerializableBounds(
            stitchPath.getBoundsInParent().getMinX(),
            stitchPath.getBoundsInParent().getMinY(),
            stitchPath.getBoundsInParent().getWidth(),
            stitchPath.getBoundsInParent().getHeight()));
    rowBounds.addStitchBounds(newStitchBounds);
    pattern.addRowBounds(rowBounds);

    moveHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeMoveHandleHandler);

    EventHandler<MouseEvent> moveHandleHandler = e2 -> moveHandleOnDraggedHandler(e2,
        stitchPath, border, newStitchBounds, rowBounds, moveHandle, resizeHandle);
    moveHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveHandleHandler);

    // Update the active move handle and its handler
    activeMoveHandle = moveHandle;
    activeMoveHandleHandler = moveHandleHandler;

    resizeHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeResizeHandler);
    EventHandler<MouseEvent> resizeHandleHandler = e2 -> resizeHandleOnDraggedHandler(e2,
        border, newStitchBounds, rowBounds, stitchPath, resizeHandle, moveHandle);
    resizeHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeHandleHandler);
    activeResizeHandle = resizeHandle;
    activeResizeHandler = resizeHandleHandler;

  }

  public void addStitch(Stitch stitch) {
    SVGPath stitchPath = stitch.Draw();
    stitchPath.setScaleX(scaleX);
    stitchPath.setScaleY(scaleY);
    stitchPath.setLayoutX(10);
    stitchPath.setLayoutY(10);
    stitchPath.setPickOnBounds(true);
    stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
      stitchClickedHandler(stitchPath, null, null);
    });
    graphicalView.patternPane.getChildren().add(stitchPath);
  }

  public void done() {
    pattern.updateStitchRow();
    //pattern.sortByBounds();
    System.out.println(pattern);
  }


}