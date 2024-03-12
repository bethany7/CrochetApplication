package org.batah.model;

import java.io.Serializable;
import org.batah.SerializableBounds;
import org.batah.model.stitches.Stitch;

public class StitchBounds implements Serializable {

  Stitch stitch;
  SerializableBounds bounds;

  public StitchBounds(Stitch stitch, SerializableBounds bounds) {
    this.stitch = stitch;
    this.bounds = bounds;
  }

  @Override
  public String toString() {
    return stitch + ", " + bounds;
  }


  public SerializableBounds getBounds() {
    return bounds;
  }

  public void setBounds(SerializableBounds bounds) {
    this.bounds = bounds;
  }

  public Stitch getStitch() {
    return stitch;
  }

  public Coords getAttachmentCoords() {
    return new Coords(bounds.getCenterX(), bounds.getMinY());
  }

}