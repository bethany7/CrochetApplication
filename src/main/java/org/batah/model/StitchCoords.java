package org.batah.model;

import org.batah.model.stitches.Stitch;

public class StitchCoords {

  Stitch stitch;
  Coords coords;

  public StitchCoords(Stitch stitch, Coords coords) {
    this.stitch = stitch;
    this.coords = coords;
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