package org.batah;

import java.io.Serializable;

public class SerializableBounds implements Serializable {

  private double minX;
  private double minY;
  private double width;
  private double height;

  public SerializableBounds(double minX, double minY, double width, double height) {
    this.minX = minX;
    this.minY = minY;
    this.width = width;
    this.height = height;
  }

  @Override
  public String toString() {
    return "minX=" + minX +
        ", minY=" + minY +
        ", width=" + width +
        ", height=" + height;
  }

  public double getMinX() {
    return minX;
  }

  public double getMinY() {
    return minY;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public double getMaxX() {
    return minX + width;
  }

  public double getMaxY() {
    return minY + height;
  }

  public double getCenterX() {
    return minX + width / 2;
  }

  public double getCenterY() {
    return minY + height / 2;
  }
}