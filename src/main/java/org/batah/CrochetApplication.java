package org.batah;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.model.Pattern;
import org.batah.ui.GraphicalView;
import org.batah.ui.PatternCanvas;
import org.batah.ui.TextView;

public class CrochetApplication extends Application {

  private Stage stage;
  @Override
  public void start(Stage stage) {
    this.stage = stage;
    stage.setTitle("Crochet Application");
    //openTextView();
    openGraphicalView();
  }

  public static void main(String[] args) {
    launch();

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
    var width = graphicalView.getPatternPaneWidth();
    var height = graphicalView.getPatternPaneHeight();

    Pattern pattern = new Pattern();
    CharStream input = CharStreams.fromString("ch 20");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions(); // begin parsing at instruction rule
//    System.out.println(tree.toStringTree(parser)); // print LISP-style

    CharStream input2 = CharStreams.fromString("20 dc");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions(); // begin parsing at instruction rule


    CrochetVisitor<String> eval = new CrochetVisitor<String>(pattern);
    eval.visit(tree);
    eval.visit(tree2);

    Platform.runLater(() -> {
      PatternCanvas patternCanvas = new PatternCanvas(pattern, graphicalView);
      patternCanvas.drawPattern(width, height);
    });


  }
}