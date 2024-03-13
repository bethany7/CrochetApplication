package org.batah.model;

public class Coords implements java.io.Serializable {

  private double x;
  private double y;

  public Coords(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "" + x + "," + y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    Coords coords = (Coords) obj;
    return x == coords.x && y == coords.y;
  }
}