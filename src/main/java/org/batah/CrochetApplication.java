package org.batah;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.batah.ui.TextView;

public class CrochetApplication extends Application {

  private Stage stage;
  @Override
  public void start(Stage stage) {
    this.stage = stage;
    stage.setTitle("Crochet Application");
    openTextView();
  }

  public static void main(String[] args) {
    launch();
  }

  public void openTextView() {
    var textView = new TextView(this, 1600, 1000, "style.css");
    textView.build();
    stage.setScene(textView.getScene());
    stage.show();
    stage.centerOnScreen();
  }
}