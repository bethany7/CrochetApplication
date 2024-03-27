package org.batah;

import java.io.Serializable;

public class SerializableBounds implements Serializable {

  private final double minX;
  private final double minY;
  private final double width;
  private final double height;

  private final int rotation;
  private final double pivotX;
  private final double pivotY;

  public SerializableBounds(double minX, double minY, double width, double height, int rotation, double pivotX, double pivotY) {
    this.minX = minX;
    this.minY = minY;
    this.width = width;
    this.height = height;
    this.rotation = rotation;
    this.pivotX = pivotX;
    this.pivotY = pivotY;
  }

  @Override
  public String toString() {
    return "minX=" + minX +
        ", minY=" + minY +
        ", width=" + width +
        ", height=" + height +
        ", rotation=" + rotation +
        ", pivotX=" + pivotX +
        ", pivotY=" + pivotY;
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

  public int getRotation() {
    return rotation;
  }

  public double getPivotX() {
    return pivotX;
  }

  public double getPivotY() {
    return pivotY;
  }
}