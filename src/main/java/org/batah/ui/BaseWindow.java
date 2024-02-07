package org.batah.ui;

import java.util.Objects;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import org.batah.CrochetApplication;

/** Base window used to create and build all the different windows in the application */
public abstract class BaseWindow {

  protected final Scene scene;

  protected final CrochetApplication app;

  protected final int height;

  protected final int width;

  protected StackPane root;

  public BaseWindow(CrochetApplication app, int width, int height, String style) {
    this.app = app;
    this.height = height;
    this.width = width;
    root = new StackPane();
    this.scene = new Scene(root, width, height);
    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/" + style)).toExternalForm());
  }

  public Scene getScene() {
    return this.scene;
  }

  /** Builds the scene to be displayed by this window */
  abstract void build();
}