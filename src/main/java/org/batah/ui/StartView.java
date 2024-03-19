package org.batah.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.batah.CrochetApplication;

public class StartView extends BaseWindow {

  public StartView(CrochetApplication app, int width, int height, String style) {
    super(app, width, height, style);
  }

  @Override
  public void build() {

    var main = new BorderPane();
    root.getChildren().add(main);

    var optionsPane = new VBox();
    optionsPane.setPrefHeight(100);
    optionsPane.setPrefWidth(400);
    optionsPane.setAlignment(javafx.geometry.Pos.CENTER);

    var newProjectTextButton = new Button("New Project from Text Pattern");
    newProjectTextButton.setOnAction(e -> {
      app.createPattern();
      app.openTextView();
    });
    optionsPane.getChildren().add(newProjectTextButton);

    var newProjectChartButton = new Button("New Project from Chart Pattern");
    newProjectChartButton.setOnAction(e -> {
      app.createPattern();
      app.openGraphicalView();
    });
    optionsPane.getChildren().add(newProjectChartButton);

    var openSavedProjectButton = new Button("Open Saved Project");
    openSavedProjectButton.setOnAction(e -> {
      //app.openSavedProject();
    });
    optionsPane.getChildren().add(openSavedProjectButton);

    main.setCenter(optionsPane);


  }
}