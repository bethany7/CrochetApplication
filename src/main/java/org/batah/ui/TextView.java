package org.batah.ui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.CrochetApplication;
import org.batah.CrochetPatternParserLexer;
import org.batah.CrochetPatternParserParser;
import org.batah.CrochetVisitor;
import org.batah.model.Pattern;

public class TextView extends BaseWindow {

  int i = 1;

  Pattern pattern;
  ArrayList<String> rowList = new ArrayList<String>();

  public TextView(CrochetApplication app, int width, int height, String style) {
    super(app, width, height, style);

  }

  @Override
  public void build() {

    this.pattern = app.getPattern();

    var main = new BorderPane();
    root.getChildren().add(main);

    // Settings Pane
    var settingsPane = new VBox();
    settingsPane.setPrefWidth(400);
    settingsPane.getStyleClass().add("settings");
    main.setLeft(settingsPane);

    // Settings
    var settingsLabel = new Text("Project Settings");
    settingsPane.getChildren().add(settingsLabel);

    // Row or Rounds
    var patternStyleLabel = new Text("Pattern Style");
    settingsPane.getChildren().add(patternStyleLabel);
    final ToggleGroup patternStyleGroup = new ToggleGroup();
    RadioButton patternStyleRow = new RadioButton("Rows");
    patternStyleRow.setToggleGroup(patternStyleGroup);
    patternStyleRow.setSelected(true);
    RadioButton patternStyleRound = new RadioButton("Rounds");
    patternStyleRound.setToggleGroup(patternStyleGroup);
    settingsPane.getChildren().add(patternStyleRow);
    settingsPane.getChildren().add(patternStyleRound);

    /// Turning Chain Settings
    var turningChainLabel = new Text("Turning Chain counts as Stitch?");
    settingsPane.getChildren().add(turningChainLabel);
    final ToggleGroup turningChainGroup = new ToggleGroup();
    RadioButton turningChainYes = new RadioButton("Yes");
    turningChainYes.setToggleGroup(turningChainGroup);
    turningChainYes.setSelected(true);
    RadioButton turningChainNo = new RadioButton("No");
    turningChainNo.setToggleGroup(turningChainGroup);
    settingsPane.getChildren().add(turningChainYes);
    settingsPane.getChildren().add(turningChainNo);

    // Turning Chain Length
    var turningChainLengthLabel = new Text("Turning Chain Length");
    settingsPane.getChildren().add(turningChainLengthLabel);
    var turningChainLengthChoice = new ChoiceBox<Integer>(
        javafx.collections.FXCollections.observableArrayList(0, 1, 2, 3, 4, 5));
    settingsPane.getChildren().add(turningChainLengthChoice);

    // Apply Settings Button
    var applySettingsButton = new Button("Apply Settings");
    settingsPane.getChildren().add(applySettingsButton);
    applySettingsButton.setOnAction(e -> {
      // apply settings
      pattern.setPatternStyle(patternStyleGroup.getSelectedToggle().toString());
      pattern.setTurningChainIsStitch(
          turningChainGroup.getSelectedToggle().toString().equals("Yes"));
      pattern.setTurningChainLength(turningChainLengthChoice.getValue());

    });

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

    // Toolbar Pane
    var toolbarPane = new HBox();
    toolbarPane.setPrefHeight(50);
    toolbarPane.getStyleClass().add("toolbar");
    main.setTop(toolbarPane);

    // Toolbar Buttons
    var fileButton = new Button("File");
    toolbarPane.getChildren().add(fileButton);
    var editButton = new Button("Edit");
    toolbarPane.getChildren().add(editButton);
    var convertButton = new Button("Convert");
    convertButton.setOnAction(e -> {
      CrochetVisitor<String> eval = new CrochetVisitor<String>(pattern);
      eval.setParameters(pattern.getPatternStyle(), pattern.isTurningChainIsStitch(),
          pattern.getTurningChainLength());
      for (Node node : entryPane.getChildren()) {
        if (node instanceof TextArea textArea) {
          CharStream input = CharStreams.fromString(textArea.getText());
          CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
          CrochetPatternParserParser parser = new CrochetPatternParserParser(
              new CommonTokenStream(lexer));
          ParseTree tree = parser.instructions();
          eval.visit(tree);
        }
      }
      System.out.println(pattern);
      app.setPattern(pattern);
      app.openGraphicalView();
    });
    toolbarPane.getChildren().add(convertButton);
    var translateButton = new Button("Translate");
    toolbarPane.getChildren().add(translateButton);

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