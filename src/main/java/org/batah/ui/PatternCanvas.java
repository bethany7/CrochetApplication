package org.batah.ui;

import java.io.IOException;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Scale;
import org.batah.SerializableBounds;
import org.batah.SerializationUtil;
import org.batah.decorators.SelectedStitch;
import org.batah.model.Coords;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.RowBounds;
import org.batah.model.StitchBounds;

public class PatternCanvas extends Pane {

  Pattern pattern;
  GraphicalView graphicalView;

  double offsetX = 0;
  double offsetY = 0;

  double scaleX = 1;
  double scaleY = 1;

  boolean selected = false;

  //PatternBounds patternBounds;

  public PatternCanvas(Pattern pattern, GraphicalView graphicalView) {
    this.pattern = pattern;
    this.graphicalView = graphicalView;
    //this.patternBounds = new PatternBounds(pattern);
  }

  public void drawPattern(int width, int height) {
    // Draw the pattern

    var rowCount = pattern.getRowCount();
    var stitchCount = pattern.getMaxStitchCount();

    var stitchMaxWidth = ((width - 60) / stitchCount);
    var stitchMaxHeight = ((height - 60) / rowCount);

    drawChain(pattern.getRow(1), width, height, stitchMaxWidth, stitchMaxHeight);

    for (int i = 2; i <= rowCount; i++) {
      var row = pattern.getRow(i);
      drawRow(row);
    }

    try {
      SerializationUtil.serialize(pattern, "pattern.ser");
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      Pattern pattern2 = (Pattern) SerializationUtil.deserialize("pattern.ser");
      System.out.println("pattern object: " + pattern);
      System.out.println("Deserialized pattern object: " + pattern2);
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }

  }

  public void drawChain(Row row, int width, int height, int stitchMaxWidth, int stitchMaxHeight) {

    RowBounds rowBounds = new RowBounds(pattern, row.getRowNum());
    var defaultStitchWidth = 200;
    var defaultStitchHeight = 60;

    scaleX = (double) stitchMaxWidth / defaultStitchWidth;
    scaleY = (double) stitchMaxHeight / defaultStitchHeight;

    if (scaleX < scaleY) {
      scaleY = scaleX;
    } else {
      scaleX = scaleY;
    }

    offsetX = ((double) defaultStitchWidth / 2) * scaleX + 10;
    offsetY = (height - 40 - ((double) defaultStitchHeight / 2 * scaleY));

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

      graphicalView.patternPane.getChildren().add(stitchPath);

      offsetX += (defaultStitchWidth * scaleX);
    }

    pattern.addRowBounds(rowBounds);

  }

  public void drawRow(Row row) {

    RowBounds rowBounds = new RowBounds(pattern, row.getRowNum());

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

      stitchPath.setOnMouseClicked(e -> {
        if (selected == true) {
          graphicalView.patternPane.getChildren()
              .remove(graphicalView.patternPane.lookup(".selected-stitch"));
          graphicalView.patternPane.getChildren()
              .remove(graphicalView.patternPane.lookup(".move-handle"));
          selected = false;
        }
        SelectedStitch selectedStitch = new SelectedStitch(stitchPath);
        var border = selectedStitch.drawBorderOnSelectedStitch();
        border.getStyleClass().add("selected-stitch");

        graphicalView.patternPane.getChildren().add(border);
        var moveHandle = selectedStitch.drawMoveHandleOnSelectedStitch();
        moveHandle.getStyleClass().add("move-handle");
        graphicalView.patternPane.getChildren().add(moveHandle);
        selected = true;
        moveHandle.setOnMouseDragged(e2 -> {
          var offsetX = e2.getX();
          var offsetY = e2.getY();
          stitchPath.relocate(offsetX - stitchPath.getBoundsInParent().getWidth(),
              offsetY - stitchPath.getBoundsInParent().getHeight());
          border.relocate(offsetX - stitchPath.getBoundsInParent().getWidth(),
              offsetY - stitchPath.getBoundsInParent().getHeight());
          moveHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
          moveHandle.setY(stitchPath.getBoundsInParent().getMaxY() - 10);

          var allCoords = pattern.getAllAttachmentCoords();
          for (Coords coords : allCoords) {
            if (stitchPath.getBoundsInParent().intersects(coords.getX(), coords.getY(), 1, 1)) {
              var offsetX2 =
                  coords.getX()
                      - (stitch.getDefaultStitchWidth() * scaleX / 2);
              var offsetY2 =
                  coords.getY()
                      - (stitch.getDefaultStitchHeight() * scaleY);

              stitchPath.relocate(offsetX2, offsetY2);
              border.relocate(offsetX2, offsetY2);
              moveHandle.setX(stitchPath.getBoundsInParent().getMaxX() - 10);
              moveHandle.setY(stitchPath.getBoundsInParent().getMaxY() - 10);

              stitch.getParentStitches().clear();
              stitch.getParentStitches().add(
                  pattern.getStitchLocByAttachmentCoords(coords));
            }
          }

        });

      });


    }
    pattern.addRowBounds(rowBounds);
  }

}