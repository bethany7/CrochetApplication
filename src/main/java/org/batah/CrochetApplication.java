package org.batah;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.model.Pattern;
import org.batah.ui.GraphicalView;
import org.batah.ui.PatternCanvas;
import org.batah.ui.StartView;
import org.batah.ui.TextView;

public class CrochetApplication extends Application {

  private Stage stage;

  Pattern pattern;

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    stage.setTitle("Crochet Application");
    createPattern();
//    try {
//      pattern = (Pattern) SerializationUtil.deserialize("pattern.ser");
//      System.out.println("pattern object: " + pattern);
//      System.out.println("Deserialized pattern object: " + pattern);
//    } catch (ClassNotFoundException | IOException e) {
//      e.printStackTrace();
//    }
    //openTextView();
    //openGraphicalView();
    openStartView();
  }

  public static void main(String[] args) {
    launch();

  }

  public void createPattern() {
    this.pattern = new Pattern();
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public void openStartView() {
    var startView = new StartView(this, 1920, 1080, "style.css");
    startView.build();

    stage.setScene(startView.getScene());
    stage.show();
    stage.centerOnScreen();
  }

  public void openTextView() {
    var textView = new TextView(this, 1920, 1080, "style.css");
    textView.build();
    stage.setScene(textView.getScene());
    stage.show();
    stage.centerOnScreen();
  }

  public void openGraphicalView() {
    var graphicalView = new GraphicalView(this, 1920, 1080, "style.css");
    graphicalView.build();
    stage.setScene(graphicalView.getScene());
    stage.show();
    stage.centerOnScreen();

    CharStream input = CharStreams.fromString("ch 10");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions(); // begin parsing at instruction rule

    CharStream input2 = CharStreams.fromString("10 tr");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions(); // begin parsing at instruction rule

    CharStream input3 = CharStreams.fromString(
        "2 tr, skip 2, 2 tr in next tr, skip 2, 2 tr in next tr");
    CrochetPatternParserLexer lexer3 = new CrochetPatternParserLexer(input3);
    CrochetPatternParserParser parser3 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer3));
    ParseTree tree3 = parser3.instructions(); // begin parsing at instruction rule

    CharStream input4 = CharStreams.fromString("6 tr");
    CrochetPatternParserLexer lexer4 = new CrochetPatternParserLexer(input4);
    CrochetPatternParserParser parser4 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer4));
    ParseTree tree4 = parser4.instructions(); // begin parsing at instruction rule

//    CharStream input5 = CharStreams.fromString("*tr, sl, tr, dc, dc* repeat 9 times");
//    CrochetPatternParserLexer lexer5 = new CrochetPatternParserLexer(input5);
//    CrochetPatternParserParser parser5 = new CrochetPatternParserParser(
//        new CommonTokenStream(lexer5));
//    ParseTree tree5 = parser5.instructions(); // begin parsing at instruction rule
//
//    CrochetVisitor<String> eval = new CrochetVisitor<String>(pattern);
//    eval.visit(tree);
//    eval.visit(tree2);
//    eval.visit(tree3);
//    eval.visit(tree4);
//    eval.visit(tree5);

    Platform.runLater(() -> {
      PatternCanvas patternCanvas = new PatternCanvas(pattern, graphicalView);
      graphicalView.setPatternCanvas(patternCanvas);
      var patternPaneWidth = graphicalView.getPatternPaneWidth();
      var patternPaneHeight = graphicalView.getPatternPaneHeight();
      if (pattern != null) {
        patternCanvas.drawPattern(patternPaneWidth, patternPaneHeight);
      }
    });


  }

  public Window getStage() {
    return stage;
  }
}