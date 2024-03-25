package org.batah.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import org.batah.CrochetApplication;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Chain;
import org.batah.model.stitches.DoubleCrochet;
import org.batah.model.stitches.DoubleTreble;
import org.batah.model.stitches.HalfTreble;
import org.batah.model.stitches.Slip;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.TrebleCrochet;
import org.batah.model.stitches.TripleTreble;

public class GraphicalView extends BaseWindow {

  Pane patternPane;
  PatternCanvas patternCanvas;

  public GraphicalView(CrochetApplication app, int width, int height, String style) {
    super(app, width, height, style);
  }

  @Override
  public void build() {

    var main = new BorderPane();
    root.getChildren().add(main);

    // Toolbar Pane
    var toolbarPane = new HBox();
    toolbarPane.setPrefHeight(50);
    toolbarPane.getStyleClass().add("toolbar");
    main.setTop(toolbarPane);
    var testText = new Text("Toolbar Goes Here!");
    toolbarPane.getChildren().add(testText);

    // Stitch Panel
//    var stitchPanel = new VBox();
//    stitchPanel.setPrefWidth(300);
//    stitchPanel.getStyleClass().add("stitchPanel");
//    main.setRight(stitchPanel);
//    var testText2 = new Text("Stitches Go Here!");
//    stitchPanel.getChildren().add(testText2);

    var stitchPalette = buildStitchPalette();
    stitchPalette.setPrefWidth(300);
    stitchPalette.setPrefColumns(2);
    stitchPalette.setPrefTileHeight(100);
    stitchPalette.setPrefTileWidth(100);
    stitchPalette.getStyleClass().add("stitchPalette");

    main.setRight(stitchPalette);

    // Settings Pane
    var settingsPane = new VBox();
    settingsPane.setPrefWidth(300);
    settingsPane.getStyleClass().add("settings");
    main.setLeft(settingsPane);
    var testText4 = new Text("Settings Go Here!");
    settingsPane.getChildren().add(testText4);

    // Pattern Pane
    this.patternPane = new Pane();
    patternPane.setMaxWidth((width - 600));
    patternPane.setMaxHeight((height - 250));
    patternPane.getStyleClass().add("patternPane");
    main.setCenter(patternPane);

    var doneButton = new Text("Done");
    toolbarPane.getChildren().add(doneButton);
    doneButton.setOnMouseClicked(e -> {
      patternCanvas.updatePattern();
    });

    // Add Row Pane?
    var addRowPane = new HBox();
    addRowPane.setPrefHeight(200);
    addRowPane.setPrefWidth(width);
    addRowPane.getStyleClass().add("addRowPane");
    main.setBottom(addRowPane);
    var addRowButton = new Button("Add Row");
    addRowPane.getChildren().add(addRowButton);
    addRowButton.setOnMouseClicked(e -> {
      patternCanvas.addRow();
    });

    var deleteStitchButton = new Button("Delete Stitch");
    addRowPane.getChildren().add(deleteStitchButton);
    deleteStitchButton.setOnMouseClicked(e -> {
      patternCanvas.deleteStitch();
    });

    var savePatternButton = new Button("Save Pattern");
    addRowPane.getChildren().add(savePatternButton);
    savePatternButton.setOnMouseClicked(e -> {
      patternCanvas.savePattern();
    });

  }

  public TilePane buildStitchPalette() {
    TilePane stitchPalette = new TilePane();
    Pattern pattern = new Pattern();
    Row row = new Row(pattern);
    row.addStitch(new Chain(null, null, null, row));
    row.addStitch(new DoubleCrochet(null, null, null, row));
    row.addStitch(new DoubleTreble(null, null, null, row));
    row.addStitch(new HalfTreble(null, null, null, row));
    row.addStitch(new Slip(null, null, null, row));
    row.addStitch(new TrebleCrochet(null, null, null, row));
    row.addStitch(new TripleTreble(null, null, null, row));
    pattern.addRow(row);

    for (Stitch stitch : row.getStitches()) {
      String stitchName = stitch.getStitchName();
      var scale = 0.2;
      SVGPath path = stitch.Draw();
      path.setScaleX(scale);
      path.setScaleY(scale);
      path.setPickOnBounds(true);
      path.setOnMouseClicked(e -> {
        patternCanvas.addStitch(stitchName);
      });
      stitchPalette.getChildren().add(path);
    }
    return stitchPalette;
  }

  public int getPatternPaneWidth() {
    return (int) patternPane.getWidth();
  }

  public int getPatternPaneHeight() {
    return (int) patternPane.getHeight();
  }

}