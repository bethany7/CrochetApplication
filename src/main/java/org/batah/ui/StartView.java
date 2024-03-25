package org.batah.ui;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Popup;
import org.batah.CrochetApplication;
import org.batah.SerializationUtil;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Chain;
import org.batah.model.stitches.Stitch.Attachment;
import org.batah.model.stitches.StitchBuilder;
import org.batah.model.stitches.StitchLoc;

public class StartView extends BaseWindow {

  private int length;

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

    var newProjectChartButton = newChartProject();
    optionsPane.getChildren().add(newProjectChartButton);

    var openSavedProjectButton = new Button("Open Saved Project");
    openSavedProjectButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Pattern File");
      fileChooser.getExtensionFilters().addAll(
          new ExtensionFilter("Pattern Files", "*.ser"));
      File selectedFile = fileChooser.showOpenDialog(app.getStage());
      if (selectedFile != null) {
        try {
          Pattern pattern = (Pattern) SerializationUtil.deserialize(selectedFile.getAbsolutePath());
          app.setPattern(pattern);
          app.openGraphicalView();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    optionsPane.getChildren().add(openSavedProjectButton);

    main.setCenter(optionsPane);


  }

  private Button newChartProject() {
    var newProjectChartButton = new Button("New Project from Chart Pattern");
    newProjectChartButton.setOnAction(e -> {
      Pattern pattern = app.getPattern();
      var popup = new Popup() {
        @Override
        public void show() {
          super.show();
        }
      };
      var startingChainText = new Text("Starting Chain Length: ");
      var startingChainLength = new TextField();
      var startButton = new Button("Start");

      var window = new HBox();

      window.getStyleClass().add("popup");
      window.setPrefWidth(400);
      window.setPrefHeight(100);
      window.setAlignment(javafx.geometry.Pos.CENTER);
      window.getChildren().add(startingChainText);
      window.getChildren().add(startingChainLength);
      window.getChildren().add(startButton);
      popup.getContent().add(window);

      startButton.setOnAction(e2 -> {
        length = Integer.parseInt(startingChainLength.getText());
        pattern.addRow(new Row(pattern));
        for (int i = 0; i < length; i++) {
          ArrayList<StitchLoc> parentStitches = new ArrayList<>();
          parentStitches.add(new StitchLoc(0, 0));
          Chain chain = new Chain(Attachment.NONE, parentStitches,
              new StitchLoc(1, i), pattern.getRow(1));
          pattern.getRow(1).addStitch(chain);
//      }
        }
        app.setPattern(pattern);
        app.openGraphicalView();
        popup.hide();
      });

      Platform.runLater(() -> {
        popup.show(app.getStage());
        popup.requestFocus();
      });

    });
    return newProjectChartButton;
  }


}