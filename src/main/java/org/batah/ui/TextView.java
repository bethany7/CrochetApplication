package org.batah.ui;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.batah.CrochetApplication;

public class TextView extends BaseWindow {

  int i = 1;

  public TextView(CrochetApplication app, int width, int height, String style) {
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
    var testText3 = new Text("Hello, JavaFX");
    toolbarPane.getChildren().add(testText3);

    // Settings Pane
    var settingsPane = new VBox();
    settingsPane.setPrefWidth(400);
    settingsPane.getStyleClass().add("settings");
    main.setLeft(settingsPane);
    var testText = new Text("Hello, JavaFX");
    settingsPane.getChildren().add(testText);

    // Pattern Entry Pane
    var scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    main.setCenter(scrollPane);

    var entryPane = new GridPane();
    entryPane.setHgap(20);
    entryPane.setVgap(20);
    entryPane.getStyleClass().add("entry");
    scrollPane.setContent(entryPane);

    var addRowButton = new Button("Add Row");
    addRowButton.setOnAction(e -> {
      addRow(entryPane);
    });
    entryPane.add(addRowButton, 2, 0);

    // add text entry form
    var rowLabel = new Text("Row 0");
    entryPane.add(rowLabel, 0, 0);
    var rowEntryBox = new TextArea();
    rowEntryBox.setPrefRowCount(2);
    rowEntryBox.setPrefColumnCount(60);
    entryPane.add(rowEntryBox, 1, 0);

  }

  private void addRow(GridPane entryPane) {
    Text[] rowLabel = new Text[100];
    rowLabel[i] = new Text("Row " + i);
    entryPane.add(rowLabel[i], 0, i);

    TextArea[] textArea = new TextArea[100];
    textArea[i] = new TextArea();
    textArea[i].setPrefRowCount(2);
    textArea[i].setPrefColumnCount(60);
    entryPane.add(textArea[i], 1, i);

    i++;
  }

}