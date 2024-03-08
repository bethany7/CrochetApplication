package org.batah.ui;

import java.util.ArrayList;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Scale;
import org.batah.decorators.SelectedStitch;
import org.batah.model.Coords;
import org.batah.model.Pattern;
import org.batah.model.PatternAttachmentCoords;
import org.batah.model.Row;
import org.batah.model.RowAttachmentCoords;
import org.batah.model.StitchAttachmentCoords;
import org.batah.model.stitches.StitchLoc;

public class PatternCanvas extends Pane {

  Pattern pattern;
  GraphicalView graphicalView;

  double offsetX = 0;
  double offsetY = 0;

  double scaleX = 1;
  double scaleY = 1;

  boolean selected = false;

  PatternAttachmentCoords patternAttachmentCoords;

  public PatternCanvas(Pattern pattern, GraphicalView graphicalView) {
    this.pattern = pattern;
    this.graphicalView = graphicalView;
    this.patternAttachmentCoords = new PatternAttachmentCoords(pattern);
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
  }

  public void drawChain(Row row, int width, int height, int stitchMaxWidth, int stitchMaxHeight) {

    RowAttachmentCoords rowAttachmentCoords = new RowAttachmentCoords(pattern, row.getRowNum());
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

      var stitchTop = stitchPath.getBoundsInParent().getMinY();
      var stitchCenter = stitchPath.getBoundsInParent().getCenterX();

      StitchAttachmentCoords stitchAttachmentCoords = new StitchAttachmentCoords(stitch,
          new Coords(stitchCenter, stitchTop));
      rowAttachmentCoords.addStitchCoords(stitchAttachmentCoords);

      graphicalView.patternPane.getChildren().add(stitchPath);

      offsetX += (defaultStitchWidth * scaleX);
    }

    patternAttachmentCoords.addRowCoords(rowAttachmentCoords);

  }

  public void drawRow(Row row) {

    RowAttachmentCoords rowAttachmentCoords = new RowAttachmentCoords(pattern, row.getRowNum());

    for (int j = 0; j < row.getStitchCount(); j++) {

      int parentStitchNum;
      var stitch = row.getStitch(j);
      SVGPath stitchPath;

      if (stitch.getStitchName().equals("Chain") && j != 0) {

        var prevStitchCoords = rowAttachmentCoords.getStitchCoords(j - 1);
        offsetX = prevStitchCoords.getCoords().getX() + (stitch.getDefaultStitchWidth() * scaleX);
        offsetY =
            prevStitchCoords.getCoords().getY() - ((stitch.getDefaultStitchHeight() * scaleY) / 2);

        stitchPath = stitch.Draw();

        stitchPath.setLayoutX(offsetX);
        stitchPath.setLayoutY(offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));


      } else if (stitch.getStitchName().equals("Slip")) {
        parentStitchNum = stitch.getParentStitch(0).getStitchNum() - 1;
        var prevStitchCoords = rowAttachmentCoords.getStitchCoords(j - 1);

        offsetX =
            patternAttachmentCoords.getStitchCoords(row.getRowNum() - 1, parentStitchNum)
                .getCoords().getX()
                - (stitch.getDefaultStitchWidth() * scaleX / 2);
        offsetY =
            prevStitchCoords.getCoords().getY() - ((stitch.getDefaultStitchHeight() * scaleY) / 2);

        stitchPath = stitch.Draw();

        stitchPath.relocate(offsetX, offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));

      } else {
        parentStitchNum = stitch.getParentStitch(0).getStitchNum() - 1;

        offsetX =
            patternAttachmentCoords.getStitchCoords(row.getRowNum() - 1, parentStitchNum)
                .getCoords().getX()
                - (stitch.getDefaultStitchWidth() * scaleX / 2);
        offsetY =
            (patternAttachmentCoords.getStitchCoords(row.getRowNum() - 1, parentStitchNum)
                .getCoords().getY())
                - (stitch.getDefaultStitchHeight() * scaleY) - (5 * scaleY);

        stitchPath = stitch.Draw();
        stitchPath.setStrokeWidth(3);
        stitchPath.relocate(offsetX, offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));

      }

      graphicalView.patternPane.getChildren().add(stitchPath);

      var stitchTop = stitchPath.getBoundsInParent().getMinY();
      var stitchCenter = stitchPath.getBoundsInParent().getCenterX();
      StitchAttachmentCoords stitchAttachmentCoords = new StitchAttachmentCoords(stitch,
          new Coords(stitchCenter, stitchTop));
      rowAttachmentCoords.addStitchCoords(stitchAttachmentCoords);
      stitchPath.setPickOnBounds(true);

      stitchPath.setOnMouseClicked(e -> {
        if (selected == true) {
          graphicalView.patternPane.getChildren()
              .remove(graphicalView.patternPane.lookup(".selected-stitch"));
          graphicalView.patternPane.getChildren()
              .remove(graphicalView.patternPane.lookup(".move-handle"));
          selected = false;
        }
        SelectedStitch selectedStitch = new SelectedStitch(stitchPath, stitchAttachmentCoords);
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

          var allCoords = patternAttachmentCoords.getAllCoords();
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
                  patternAttachmentCoords.getStitchLocByCoords(coords));
            }
          }

        });

      });


    }

    patternAttachmentCoords.addRowCoords(rowAttachmentCoords);

  }
}