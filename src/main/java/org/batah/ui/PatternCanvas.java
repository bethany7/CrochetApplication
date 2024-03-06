package org.batah.ui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Scale;
import org.batah.model.Coords;
import org.batah.model.Pattern;
import org.batah.model.PatternCoords;
import org.batah.model.Row;
import org.batah.model.RowCoords;
import org.batah.model.StitchCoords;

public class PatternCanvas extends Pane {

  Pattern pattern;
  GraphicalView graphicalView;

  double offsetX = 0;
  double offsetY = 0;

  double scaleX = 1;
  double scaleY = 1;

  PatternCoords patternCoords;

  public PatternCanvas(Pattern pattern, GraphicalView graphicalView) {
    this.pattern = pattern;
    this.graphicalView = graphicalView;
    this.patternCoords = new PatternCoords(pattern);
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

  public void drawChain (Row row, int width, int height, int stitchMaxWidth, int stitchMaxHeight) {

    RowCoords rowCoords = new RowCoords(pattern, row.getRowNum());
    var defaultStitchWidth = 200;
    var defaultStitchHeight = 60;

    scaleX = (double) stitchMaxWidth / defaultStitchWidth;
    scaleY = (double) stitchMaxHeight / defaultStitchHeight;

    if (scaleX < scaleY) {
      scaleY = scaleX;
    } else {
      scaleX = scaleY;
    }

    offsetX = ((double) defaultStitchWidth /2) * scaleX + 10;
    offsetY = (height - 40 - ((double) defaultStitchHeight /2 * scaleY));

    for (int j = 0; j < row.getStitchCount(); j++) {

      var stitch = row.getStitch(j);

      SVGPath stitchPath = stitch.Draw();

      stitchPath.setLayoutX(offsetX);
      stitchPath.setLayoutY(offsetY);
      stitchPath.setScaleX(scaleX);
      stitchPath.setScaleY(scaleY);

      var stitchTop = stitchPath.getBoundsInParent().getMinY();
      var stitchCenter = stitchPath.getBoundsInParent().getCenterX();

      StitchCoords stitchCoords = new StitchCoords(stitch, new Coords(stitchCenter, stitchTop));
      rowCoords.addStitchCoords(stitchCoords);

      graphicalView.patternPane.getChildren().add(stitchPath);

      offsetX += (defaultStitchWidth * scaleX);
    }

    patternCoords.addRowCoords(rowCoords);

  }
  public void drawRow(Row row) {

    RowCoords rowCoords = new RowCoords(pattern, row.getRowNum());

    for (int j = 0; j < row.getStitchCount(); j++) {

      int parentStitchNum;
      var stitch = row.getStitch(j);
      SVGPath stitchPath;

      if (stitch.getStitchName().equals("Chain") && j != 0) {

        var prevStitchCoords = rowCoords.getStitchCoords(j - 1);
        offsetX = prevStitchCoords.getCoords().getX() + (stitch.getDefaultStitchWidth() * scaleX);
        offsetY =
            prevStitchCoords.getCoords().getY() - ((stitch.getDefaultStitchHeight() * scaleY) / 2);

        stitchPath = stitch.Draw();

        stitchPath.setLayoutX(offsetX);
        stitchPath.setLayoutY(offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));


      }
      else if (stitch.getStitchName().equals("Slip")){
        parentStitchNum = stitch.getParentStitch(0).getStitchNum() - 1;
        var prevStitchCoords = rowCoords.getStitchCoords(j - 1);

        offsetX = patternCoords.getStitchCoords(row.getRowNum() - 1, parentStitchNum).getCoords().getX()
                - (stitch.getDefaultStitchWidth() * scaleX / 2);
        offsetY = prevStitchCoords.getCoords().getY() - ((stitch.getDefaultStitchHeight() * scaleY) / 2);

        stitchPath = stitch.Draw();

        stitchPath.relocate(offsetX, offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));

      }

      else {
        parentStitchNum = stitch.getParentStitch(0).getStitchNum() - 1;

        offsetX =
            patternCoords.getStitchCoords(row.getRowNum() - 1, parentStitchNum).getCoords().getX()
                - (stitch.getDefaultStitchWidth() * scaleX / 2);
        offsetY =
            (patternCoords.getStitchCoords(row.getRowNum() - 1, parentStitchNum).getCoords().getY())
                - (stitch.getDefaultStitchHeight() * scaleY) - (5 * scaleY);

        stitchPath = stitch.Draw();

        stitchPath.relocate(offsetX, offsetY);
        stitchPath.getTransforms().add(new Scale(scaleX, scaleY));

      }

      graphicalView.patternPane.getChildren().add(stitchPath);

      var stitchTop = stitchPath.getBoundsInParent().getMinY();
      var stitchCenter = stitchPath.getBoundsInParent().getCenterX();
      StitchCoords stitchCoords = new StitchCoords(stitch, new Coords(stitchCenter, stitchTop));
      rowCoords.addStitchCoords(stitchCoords);
      stitchPath.setOnMousePressed(e -> stitchPath.setStroke(javafx.scene.paint.Color.RED));

    }

    patternCoords.addRowCoords(rowCoords);

  }
}