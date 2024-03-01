package org.batah.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.batah.CrochetApplication;
import org.batah.model.Pattern;

public class GraphicalView extends BaseWindow {

  Pane patternPane;

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
    var stitchPanel = new VBox();
    stitchPanel.setPrefWidth(300);
    stitchPanel.getStyleClass().add("stitchPanel");
    main.setRight(stitchPanel);
    var testText2 = new Text("Stitches Go Here!");
    stitchPanel.getChildren().add(testText2);

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

    // Add Row Pane?
    var addRowPane = new HBox();
    addRowPane.setPrefHeight(200);
    addRowPane.setPrefWidth(width);
    addRowPane.getStyleClass().add("addRowPane");
    main.setBottom(addRowPane);
    var testText5 = new Text("Add Row Goes Here!");
    addRowPane.getChildren().add(testText5);

  }

  public void openGraphicalView(Stage stage) {
    stage.setScene(scene);
    stage.show();
    stage.centerOnScreen();
  }

  public int getPatternPaneWidth() {
    return (int) patternPane.getWidth();
  }

  public int getPatternPaneHeight() {
    return (int) patternPane.getHeight();
  }

}