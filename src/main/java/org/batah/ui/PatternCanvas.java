package org.batah.ui;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import org.batah.model.Coords;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Stitch;
import org.javatuples.Pair;

public class PatternCanvas extends javafx.scene.canvas.Canvas{

  Pattern pattern;
  GraphicalView graphicalView;

  double offsetX;
  double offsetY = 0;

  double scaleX = 1;
  double scaleY = 1;

  ArrayList<Coords> startCoords = new ArrayList<>();

  public PatternCanvas(Pattern pattern, GraphicalView graphicalView) {
    this.pattern = pattern;
    this.graphicalView = graphicalView;
  }

  public void drawPattern(int width, int height) {
    // Draw the pattern

    var rowCount = pattern.getRowCount();
    var stitchCount = pattern.getMaxStitchCount();

    var stitchMaxWidth = ((width - 60) / stitchCount);
    var stitchMaxHeight = (height / rowCount);

    drawChain(pattern.getRow(1), width, height, stitchMaxWidth, stitchMaxHeight);



    for (int i = 2; i <= rowCount; i++) {
      var row = pattern.getRow(i);
      drawRow(row, width, height, stitchMaxWidth, stitchMaxHeight);

    }
  }

  public void drawChain (Row row, int width, int height, int stitchMaxWidth, int stitchMaxHeight) {
    offsetX = 20;
    for (int j = 0; j < row.getStitchCount(); j++) {

      var canv = new Canvas(width, height);
      var gc = canv.getGraphicsContext2D();
      var stitch = row.getStitch(j);

      var defaultStitchWidth = 100;
      var defaultStitchHeight = 30;

      scaleX = (double) stitchMaxWidth / defaultStitchWidth;
      scaleY = (double) stitchMaxHeight / defaultStitchHeight;
      if (scaleX < scaleY) {
        scaleY = scaleX;
      } else {
        scaleX = scaleY;
      }

      offsetY = (height - 20 - (defaultStitchHeight * scaleY));

      stitch.Draw(stitch, gc, offsetX, offsetY, scaleX, scaleY);

      startCoords.add(new Coords((offsetX+(defaultStitchWidth * scaleX)/2)+20, offsetY - 2));

      graphicalView.patternPane.getChildren().add(canv);
      offsetX += (defaultStitchWidth);
    }

  }
  public void drawRow(Row row, int width, int height, int stitchMaxWidth, int stitchMaxHeight) {

    for (int j = 0; j < row.getStitchCount(); j++) {

      int prevStitchIndex = row.getStitchCount() - 1 - j;
      offsetX = startCoords.get(prevStitchIndex).getX();
      offsetY = startCoords.get(prevStitchIndex).getY();



      var canv = new Canvas(width, height);
      var gc = canv.getGraphicsContext2D();
      var stitch = row.getStitch(j);

      stitch.Draw(stitch, gc, offsetX, offsetY, scaleX, scaleY);
      graphicalView.patternPane.getChildren().add(canv);


    }

  }
}