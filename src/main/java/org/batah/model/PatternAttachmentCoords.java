package org.batah.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.StreamSupport;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.StitchLoc;

public class PatternAttachmentCoords implements java.io.Serializable {

  Pattern pattern;
  ArrayList<RowAttachmentCoords> rowAttachmentCoordsList;

  public PatternAttachmentCoords(Pattern pattern) {
    this.pattern = pattern;
    this.rowAttachmentCoordsList = new ArrayList<>();
  }

  @Override
  public String toString() {
    return rowAttachmentCoordsList.toString();
  }

  public void addRowCoords(RowAttachmentCoords rowAttachmentCoords) {
    this.rowAttachmentCoordsList.add(rowAttachmentCoords);
  }

  public ArrayList<RowAttachmentCoords> getRowCoordsList() {
    return rowAttachmentCoordsList;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public int getRowCount() {
    return pattern.getRowCount();
  }

  public StitchAttachmentCoords getStitchCoords(int rowNum, int stitchNum) {
    return rowAttachmentCoordsList.get(rowNum - 1).getStitchCoords(stitchNum);
  }

  public ArrayList<Coords> getAllCoords() {
    ArrayList<Coords> allCoords = new ArrayList<>();
    for (RowAttachmentCoords rowAttachmentCoords : rowAttachmentCoordsList) {
      for (StitchAttachmentCoords stitchAttachmentCoords : rowAttachmentCoords.getStitchCoordsList()) {
        allCoords.add(stitchAttachmentCoords.getCoords());
      }
    }
    return allCoords;
  }

  public StitchLoc getStitchLocByCoords(Coords coords) {
    for (RowAttachmentCoords rowAttachmentCoords : rowAttachmentCoordsList) {
      for (StitchAttachmentCoords stitchAttachmentCoords : rowAttachmentCoords.getStitchCoordsList()) {
        if (stitchAttachmentCoords.getCoords().equals(coords)) {
          return stitchAttachmentCoords.getStitch().getLoc();
        }
      }
    }
    return null;

  }
}