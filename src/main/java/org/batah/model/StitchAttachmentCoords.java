package org.batah.model;

import java.io.Serializable;
import org.batah.model.stitches.Stitch;

public class StitchAttachmentCoords implements Serializable {

  Stitch stitch;
  Coords coords;

  public StitchAttachmentCoords(Stitch stitch, Coords coords) {
    this.stitch = stitch;
    this.coords = coords;
  }

  @Override
  public String toString() {
    return "" + stitch + " " + coords;
  }

  public Stitch getStitch() {
    return stitch;
  }

  public Coords getCoords() {
    return coords;
  }

  public void setCoords(Coords coords) {
    this.coords = coords;
  }


}