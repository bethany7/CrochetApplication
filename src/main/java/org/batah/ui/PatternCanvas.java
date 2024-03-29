package org.batah.ui;


import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.batah.SerializableBounds;
import org.batah.SerializationUtil;
import org.batah.decorators.SelectedStitch;
import org.batah.model.Coords;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.RowBounds;
import org.batah.model.StitchBounds;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.StitchBuilder;
import org.batah.model.stitches.StitchLoc;

public class PatternCanvas extends Pane {

  Pattern pattern;
  GraphicalView graphicalView;

  double offsetX = 0;
  double offsetY = 0;

  double scaleX = 1;
  double scaleY = 1;

  double chainScale = 1;
  int chainCounter = 0;

  int height;
  int width;
  boolean selected = false;

  private SVGPath activeStitch = null;
  private EventHandler<MouseEvent> activeStitchHandler = null;
  private ImageView activeMoveHandle = null;
  private EventHandler<MouseEvent> activeMoveHandleHandler = null;

  private ImageView activeResizeHandle = null;
  private EventHandler<MouseEvent> activeResizeHandler = null;

  private Row currentRow;

  private int totalIncreaseCounter = 0;
  private int currentIncreaseCounter = 0;

  public PatternCanvas(Pattern pattern, GraphicalView graphicalView) {
    this.pattern = pattern;
    this.graphicalView = graphicalView;
    this.currentRow = pattern.getRow(1);
  }

  public void drawPattern(int width, int height) {

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
        currentRow = row;
        var rowBounds = drawRow(row);
        pattern.addRowBounds(rowBounds);
      }
    } else {
      redrawFromSaved(pattern);
    }
  }


  public void redrawFromSaved(Pattern pattern) {

    for (RowBounds rowBounds : pattern.getRowBoundsList()) {
      currentRow = rowBounds.getRow();
      offsetX =
          (pattern.getRowBoundsList().getFirst().getStitch(0).getDefaultStitchWidth() / 2)
              * scaleX
              + 10;
      for (StitchBounds stitchBounds : rowBounds.getStitchBoundsList()) {
        var stitch = stitchBounds.getStitch();
        SVGPath stitchPath = stitch.Draw();
        stitchPath.setStrokeWidth(3);
        int rotation = stitchBounds.getBounds().getRotation();
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
          Rotate rotate = new Rotate(rotation, stitchPath.getBoundsInLocal().getCenterX(),
              stitchPath
                  .getBoundsInLocal().getMaxY());
          stitchPath.getTransforms().add(rotate);
          if (stitchBounds.getBounds().getRotation() < 180 && stitchBounds.getBounds().getRotation()
              > 0) {
            var newOffsetX = offsetX - stitchPath.getBoundsInParent().getWidth() / 2 - 2;
            stitchPath.relocate(newOffsetX, offsetY);
          } else if (stitchBounds.getBounds().getRotation() > 180) {

            var newOffsetX = offsetX + stitchPath.getBoundsInParent().getWidth() / 2 - 4;
            stitchPath.relocate(newOffsetX, offsetY);
          }
          graphicalView.patternPane.getChildren().add(stitchPath);
        }
        stitchPath.setPickOnBounds(true);
        stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
          stitchClickedHandler(e, stitchPath, stitchBounds, rowBounds);
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
      int rotation = 0;
      double pivotX = stitchPath.getBoundsInLocal().getCenterX();
      double pivotY = stitchPath.getBoundsInLocal().getMaxY();

      SerializableBounds serialBounds = new SerializableBounds(
          stitchPath.getBoundsInParent().getMinX(), stitchPath.getBoundsInParent().getMinY(),
          stitchPath.getBoundsInParent().getWidth(), stitchPath.getBoundsInParent().getHeight(),
          rotation, pivotX, pivotY);
      StitchBounds stitchBounds = new StitchBounds(stitch, serialBounds);
      rowBounds.addStitchBounds(stitchBounds);
      stitchPath.setPickOnBounds(true);
      stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
        stitchClickedHandler(e, stitchPath, stitchBounds, rowBounds);
      });

      graphicalView.patternPane.getChildren().add(stitchPath);

      offsetX += (defaultStitchWidth * scaleX);
    }

    pattern.addRowBounds(rowBounds);

  }

  public RowBounds drawRow(Row row) {

    RowBounds rowBounds = new RowBounds(pattern, row.getRowNum(), row);
    int rotation = 0;

    for (int j = 0; j < row.getStitchCount(); j++) {

      int parentStitchNum;
      var stitch = row.getStitch(j);
      SVGPath stitchPath;

      if (stitch.getStitchName().equals("Chain") && j != 0) {

        if (chainCounter == 0) {

          //check how many of the next stitches are also chain
          int chainCount = 0;
          for (int k = j; k < row.getStitchCount(); k++) {
            if (row.getStitch(k).getStitchName().equals("Chain")) {
              chainCount++;
            } else {
              break;
            }
          }

          chainScale = scaleX / chainCount;
          chainCounter = chainCount;
        }

        var prevStitchBounds = rowBounds.getStitchAndBounds(j - 1);
        var prevStitch = prevStitchBounds.getStitch();
        var prevStitchCoords = rowBounds.getStitchAttachmentCoords(j - 1);
        if (prevStitch.getStitchName().equals("Chain")) {
          offsetX = prevStitchCoords.getX() + (stitch.getDefaultStitchWidth()
              * chainScale);
          offsetY = prevStitchCoords.getY();
        } else {
          offsetX =
              prevStitchCoords.getX() + (prevStitchBounds.getBounds().getWidth() / 2) +
                  (stitch.getDefaultStitchWidth() * chainScale / 2);
          offsetY =
              prevStitchCoords.getY() - (
                  (stitch.getDefaultStitchHeight() * chainScale) / 2);
        }

        stitchPath = stitch.Draw();

        stitchPath.setLayoutX(offsetX);
        stitchPath.setLayoutY(offsetY);
        stitchPath.getTransforms().add(new Scale(chainScale, chainScale));
        chainCounter--;


      } else if (stitch.getStitchName().equals("Slip")) {
        parentStitchNum = stitch.getParentStitch(0).getStitchNum() - 1;
        var prevStitchAttachmentCoords = rowBounds.getStitchAttachmentCoords(j - 1);

        offsetX =
            pattern.getRowBounds(row.getRowNum() - 1).getStitchAndBounds(parentStitchNum)
                .getBounds().getCenterX() - (stitch.getDefaultStitchWidth() * scaleX / 2);

        offsetY =
            prevStitchAttachmentCoords.getY() - ((stitch.getDefaultStitchHeight() * scaleY) / 2);

        stitchPath = stitch.Draw();

        stitchPath.setLayoutX(offsetX);
        stitchPath.setLayoutY(offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));

      } else {
        if (totalIncreaseCounter == 0 && j < row.getStitchCount() - 1
            && stitch.getParentStitches().getFirst() == row.getStitch(j + 1)
            .getParentStitches().getFirst()) {
          totalIncreaseCounter++;
          currentIncreaseCounter++;
          for (int k = j; k < row.getStitchCount() - 1; k++) {
            if (row.getStitch(k + 1).getParentStitches().getFirst().getStitchNum()
                == stitch.getParentStitch(0).getStitchNum()) {
              totalIncreaseCounter++;
              currentIncreaseCounter++;
            } else {
              break;
            }
          }
        }

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
        var bounds = stitchPath.getBoundsInParent();
        var pivotX = stitchPath.getBoundsInLocal().getCenterX();
        var pivotY = stitchPath.getBoundsInLocal().getMaxY();
        if (currentIncreaseCounter > 0) {

          if (totalIncreaseCounter == 2 && currentIncreaseCounter == 2) {
            rotation = 20;
          } else if (totalIncreaseCounter == 2 && currentIncreaseCounter == 1) {
            rotation = 340;
          }
          currentIncreaseCounter--;

          Rotate rotate = new Rotate(rotation, pivotX,
              pivotY);
          stitchPath.getTransforms().add(rotate);

          graphicalView.patternPane.getChildren().add(stitchPath);

          stitchPath.setPickOnBounds(true);

          SerializableBounds serialBounds = new SerializableBounds(
              stitchPath.getBoundsInParent().getMinX(), stitchPath.getBoundsInParent().getMinY(),
              stitchPath.getBoundsInParent().getWidth(), stitchPath.getBoundsInParent().getHeight(),
              rotation, pivotX, pivotY);
          StitchBounds stitchBounds = new StitchBounds(stitch, serialBounds);
          rowBounds.addStitchBounds(stitchBounds);

          stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            stitchClickedHandler(e, stitchPath, stitchBounds, rowBounds);
          });

          if (currentIncreaseCounter == 0) {
            totalIncreaseCounter = 0;
            rotation = 0;
          }

        } else {
          graphicalView.patternPane.getChildren().add(stitchPath);

          stitchPath.setPickOnBounds(true);

          SerializableBounds serialBounds = new SerializableBounds(
              stitchPath.getBoundsInParent().getMinX(), stitchPath.getBoundsInParent().getMinY(),
              stitchPath.getBoundsInParent().getWidth(), stitchPath.getBoundsInParent().getHeight(),
              rotation, pivotX, pivotY);
          StitchBounds stitchBounds = new StitchBounds(stitch, serialBounds);
          rowBounds.addStitchBounds(stitchBounds);

          stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            stitchClickedHandler(e, stitchPath, stitchBounds, rowBounds);
          });
        }


      }
    }
    return rowBounds;
  }

  public void stitchClickedHandler(MouseEvent e, SVGPath stitchPath, StitchBounds stitchBounds,
      RowBounds rowBounds) {

    if (activeStitch != null) {
      activeStitch.removeEventHandler(MouseEvent.MOUSE_CLICKED, activeStitchHandler);
      activeMoveHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeMoveHandleHandler);
      activeResizeHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeResizeHandler);
    }

    EventHandler<MouseEvent> stitchHandler = e1 -> {
      stitchClickedHandler(e1, stitchPath, stitchBounds, rowBounds);
    };
    stitchPath.addEventHandler(MouseEvent.MOUSE_CLICKED, stitchHandler);

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

    EventHandler<MouseEvent> moveHandleHandler = e2 -> moveHandleOnDraggedHandler(e2, stitchPath,
        border, stitchBounds, moveHandle, resizeHandle);
    moveHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveHandleHandler);

    EventHandler<MouseEvent> resizeHandleHandler = e3 -> resizeHandleOnDraggedHandler(e3,
        border, stitchBounds, stitchPath, resizeHandle, moveHandle);
    resizeHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeHandleHandler);

    activeMoveHandle = moveHandle;
    activeMoveHandleHandler = moveHandleHandler;
    activeResizeHandle = resizeHandle;
    activeResizeHandler = resizeHandleHandler;

  }

  public void moveHandleOnDraggedHandler(MouseEvent e, SVGPath stitchPath,
      SVGPath border, StitchBounds stitchBounds, ImageView moveHandle,
      ImageView resizeHandle) {

    e.consume();
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
                - (stitchPath.getBoundsInParent().getHeight()) - (2 * scaleY);

        stitchPath.relocate(offsetX2, offsetY2);
        border.relocate(offsetX2, offsetY2);
        moveHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
        moveHandle.setY(stitchPath.getBoundsInParent().getMaxY() - 10);
        resizeHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
        resizeHandle.setY(stitchPath.getBoundsInParent().getMinY() - 10);
        StitchLoc newParentLoc = pattern.getStitchLocByAttachmentCoords(coords);

        moveHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeMoveHandleHandler);

        EventHandler<MouseEvent> moveHandleHandler = e2 -> moveHandleOnDragReleasedHandler(e2,
            stitchPath, border, stitchBounds, moveHandle, resizeHandle, newParentLoc);
        moveHandle.addEventHandler(MouseEvent.MOUSE_RELEASED, moveHandleHandler);

        // Update the active move handle and its handler
        activeMoveHandle = moveHandle;
        activeMoveHandleHandler = moveHandleHandler;

      }
    }
  }

  public void moveHandleOnDragReleasedHandler(MouseEvent e, SVGPath stitchPath,
      SVGPath border, StitchBounds stitchBounds, ImageView moveHandle,
      ImageView resizeHandle, StitchLoc newParentLoc) {
    e.consume();

    Stitch stitch = stitchBounds.getStitch();
    RowBounds rowBounds = pattern.getRowBounds(stitch.getLoc().getRowNum());
    int rotation = stitchBounds.getBounds().getRotation();
    var pivotX = stitchPath.getBoundsInLocal().getCenterX();
    var pivotY = stitchPath.getBoundsInLocal().getMaxY();
    stitch.getParentStitches().clear();
    stitch.getParentStitches().add(newParentLoc);
    rowBounds.removeStitchBounds(stitchBounds);
    StitchBounds newStitchBounds = new StitchBounds(stitch, new SerializableBounds(
        stitchPath.getBoundsInParent().getMinX(),
        stitchPath.getBoundsInParent().getMinY(),
        stitchPath.getBoundsInParent().getWidth(),
        stitchPath.getBoundsInParent().getHeight(), rotation, pivotX, pivotY));
    rowBounds.addStitchBounds(newStitchBounds);
    System.out.println(newStitchBounds);

    moveHandle.removeEventHandler(MouseEvent.MOUSE_RELEASED, activeMoveHandleHandler);
    EventHandler<MouseEvent> moveHandleHandler = e2 -> moveHandleOnDraggedHandler(e2,
        stitchPath, border, newStitchBounds, moveHandle, resizeHandle);
    moveHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveHandleHandler);
    activeMoveHandle = moveHandle;
    activeMoveHandleHandler = moveHandleHandler;

    resizeHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeResizeHandler);
    EventHandler<MouseEvent> resizeHandleHandler = e2 -> resizeHandleOnDraggedHandler(e2,
        border, newStitchBounds, stitchPath, resizeHandle, moveHandle);
    resizeHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeHandleHandler);
    activeResizeHandle = resizeHandle;
    activeResizeHandler = resizeHandleHandler;

    updatePattern();
  }

  public void resizeHandleOnDraggedHandler(MouseEvent e, SVGPath border, StitchBounds stitchBounds,
      SVGPath stitchPath, ImageView resizeHandle, ImageView moveHandle) {

    Scale newScale = new Scale();

    var x = stitchBounds.getBounds().getCenterX();
    var y = stitchBounds.getBounds().getMaxY();
    int rotation = stitchBounds.getBounds().getRotation();

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
        stitchBounds, stitchPath, resizeHandle, moveHandle);
    resizeHandle.addEventHandler(MouseEvent.MOUSE_RELEASED, resizeHandleHandler);

    activeResizeHandle = resizeHandle;
    activeResizeHandler = resizeHandleHandler;


  }

  public void resizeHandleOnDragReleasedHandler(MouseEvent e, SVGPath border,
      StitchBounds stitchBounds,
      SVGPath stitchPath, ImageView resizeHandle, ImageView moveHandle) {

    int rotation = stitchBounds.getBounds().getRotation();
    var pivotX = stitchPath.getBoundsInLocal().getCenterX();
    var pivotY = stitchPath.getBoundsInLocal().getMaxY();
    RowBounds rowBounds = pattern.getRowBounds(stitchBounds.getStitch().getLoc().getRowNum());
    pattern.removeRowBounds(rowBounds);
    rowBounds.removeStitchBounds(stitchBounds);
    StitchBounds newStitchBounds = new StitchBounds(stitchBounds.getStitch(),
        new SerializableBounds(
            stitchPath.getBoundsInParent().getMinX(),
            stitchPath.getBoundsInParent().getMinY(),
            stitchPath.getBoundsInParent().getWidth(),
            stitchPath.getBoundsInParent().getHeight(), rotation, pivotX, pivotY));
    rowBounds.addStitchBounds(newStitchBounds);
    pattern.addRowBounds(rowBounds);

    moveHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeMoveHandleHandler);

    EventHandler<MouseEvent> moveHandleHandler = e2 -> moveHandleOnDraggedHandler(e2,
        stitchPath, border, newStitchBounds, moveHandle, resizeHandle);
    moveHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveHandleHandler);

    activeMoveHandle = moveHandle;
    activeMoveHandleHandler = moveHandleHandler;

    resizeHandle.removeEventHandler(MouseEvent.MOUSE_DRAGGED, activeResizeHandler);
    EventHandler<MouseEvent> resizeHandleHandler = e2 -> resizeHandleOnDraggedHandler(e2,
        border, newStitchBounds, stitchPath, resizeHandle, moveHandle);
    resizeHandle.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeHandleHandler);
    activeResizeHandle = resizeHandle;
    activeResizeHandler = resizeHandleHandler;

  }

  public void addStitch(String stitchName) {
    Stitch stitch = StitchBuilder.buildStitch(stitchName, null, new ArrayList<>(),
        new StitchLoc(0, 0), currentRow);
    SVGPath stitchPath = stitch.Draw();
    stitchPath.setStrokeWidth(3);
    stitchPath.relocate(10, 10);
    stitchPath.getTransforms().add(new Scale(scaleX, scaleY));
    stitchPath.setPickOnBounds(true);

    Stitch prevStitch;
    if (currentRow.getStitchCount() != 0) {
      prevStitch = currentRow.getStitches().getLast();
    } else {
      prevStitch = pattern.getRow(currentRow.getRowNum() - 1).getStitches().getLast();
    }

    var prevStitchLoc = prevStitch.getLoc();
    int thisStitchNum;
    if (prevStitchLoc.getRowNum() == 1) {
      addRow();
      thisStitchNum = 1;
    } else {
      thisStitchNum = prevStitchLoc.getStitchNum() + 1;
    }
    var thisStitchLoc = new StitchLoc(currentRow.getRowNum(), thisStitchNum);
    stitch.setLoc(thisStitchLoc);
    ArrayList<StitchLoc> parentStitches = getParentStitchLocs(prevStitch,
        stitch, prevStitchLoc);
    stitch.setParentStitches(parentStitches);
    int rotation = 0;
    var pivotX = stitchPath.getBoundsInLocal().getCenterX();
    var pivotY = stitchPath.getBoundsInLocal().getMaxY();

    StitchBounds stitchBounds = new StitchBounds(stitch, new SerializableBounds(
        stitchPath.getBoundsInParent().getMinX(),
        stitchPath.getBoundsInParent().getMinY(),
        stitchPath.getBoundsInParent().getWidth(),
        stitchPath.getBoundsInParent().getHeight(), rotation, pivotX, pivotY));
    RowBounds rowBounds = pattern.getRowBounds(currentRow.getRowNum());
    rowBounds.addStitchBounds(stitchBounds);

    graphicalView.patternPane.getChildren().add(stitchPath);

    stitchPath.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
      stitchClickedHandler(e, stitchPath, stitchBounds, rowBounds);
    });
  }

  private ArrayList<StitchLoc> getParentStitchLocs(Stitch prevStitch, Stitch stitch,
      StitchLoc prevStitchLoc) {
    StitchLoc thisStitchParentLoc;
    if (prevStitch.getParentStitches() != null) {
      var prevStitchParent = prevStitch.getParentStitches().getLast();
      var prevStitchParentRow = prevStitchParent.getRowNum();
      var prevStitchParentNum = prevStitchParent.getStitchNum();
      if (stitch.getLoc().getRowNum() % 2 == 0) {
        thisStitchParentLoc = new StitchLoc(prevStitchParentRow, prevStitchParentNum - 1);
      } else {
        thisStitchParentLoc = new StitchLoc(prevStitchParentRow, prevStitchParentNum + 1);
      }
    } else {
      thisStitchParentLoc = new StitchLoc(1, prevStitchLoc.getStitchNum() - 1);
    }
    ArrayList<StitchLoc> parentStitches = new ArrayList<StitchLoc>();
    parentStitches.add(thisStitchParentLoc);
    return parentStitches;
  }

  public void addRow() {
    var row = new Row(pattern);
    pattern.addRow(row);
    pattern.addRowBounds(new RowBounds(pattern, row.getRowNum(), row));
    currentRow = row;
    System.out.println("Row added");
  }

  public void updatePattern() {
    pattern.updateAll();
    pattern.prettyPrintWithBounds();
  }

  public void deleteStitch() {
    SVGPath stitchPath = activeStitch;
    StitchBounds stitchBounds = pattern.getStitchByPath(stitchPath);
    RowBounds rowBounds = pattern.getRowBounds(stitchBounds.getStitch().getLoc().getRowNum());
    rowBounds.removeStitchBounds(stitchBounds);
    graphicalView.patternPane.getChildren().remove(stitchPath);
    //deselect stitch
    graphicalView.patternPane.getChildren()
        .remove(graphicalView.patternPane.lookup(".selected-stitch"));
    graphicalView.patternPane.getChildren()
        .remove(graphicalView.patternPane.lookup(".move-handle"));
    graphicalView.patternPane.getChildren()
        .remove(graphicalView.patternPane.lookup(".resize-handle"));
    selected = false;
    updatePattern();
  }

  public void savePattern() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Pattern File");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Pattern Files", "*.ser"));
    File selectedFile = fileChooser.showSaveDialog(graphicalView.getScene().getWindow());
    if (selectedFile != null) {
      //serialize to file
      try {
        SerializationUtil.serialize(pattern, selectedFile.getAbsolutePath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

}