package org.batah.model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import org.batah.SerializableBounds;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.StitchLoc;

public class RowBounds implements Serializable {

  ArrayList<StitchBounds> stitchBounds;
  int rowNum;
  Pattern pattern;

  Row row;


  public RowBounds(Pattern pattern, int rowNum, Row row) {
    this.stitchBounds = new ArrayList<>();
    this.rowNum = rowNum;
    this.pattern = pattern;
    this.row = row;
  }

  public String toString() {
    return "" + stitchBounds;
  }

  public void addStitchAndBounds(Stitch stitch, SerializableBounds bounds) {
    stitchBounds.add(new StitchBounds(stitch, bounds));
  }

  public void addStitchBounds(StitchBounds stitchBounds) {
    this.stitchBounds.add(stitchBounds);
  }

  public void removeStitchBounds(StitchBounds stitchBounds) {
    this.stitchBounds.remove(stitchBounds);
  }


  public ArrayList<StitchBounds> getStitchBoundsList() {
    return stitchBounds;
  }

  public StitchBounds getStitchAndBounds(int i) {
    return stitchBounds.get(i);
  }

  public Stitch getStitch(int i) {
    return stitchBounds.get(i).getStitch();
  }

  public SerializableBounds getBounds(int i) {
    return stitchBounds.get(i).getBounds();
  }

  public int getRowNum() {
    return rowNum;
  }

  public Row getRow() {
    return row;
  }

  public void updateRow(Row row) {
    row.clearRow();
    for (StitchBounds stitchBound : stitchBounds) {
      row.addStitch(stitchBound.getStitch());
    }
  }

  public Coords getStitchAttachmentCoords(int i) {
    return getStitchAndBounds(i).getAttachmentCoords();
  }

}