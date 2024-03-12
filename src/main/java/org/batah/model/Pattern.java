package org.batah.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.Stitch.Attachment;
import org.batah.model.stitches.StitchLoc;

public class Pattern implements Serializable {

  ArrayList<Row> pattern;
  ArrayList<RowBounds> rowBoundsList;

  private static final long serialVersionUID = 1L;


  public Pattern() {
    this.pattern = new ArrayList<>();
    this.rowBoundsList = new ArrayList<>();
  }

  public Pattern(Pattern pattern, ArrayList<RowBounds> rowBoundsList) {
    this.pattern = new ArrayList<>();
    this.rowBoundsList = rowBoundsList;
  }


  @Override
  public String toString() {
    if (rowBoundsList.isEmpty()) {
      return "" + pattern;
    } else {
      return "" + rowBoundsList;
    }
  }

  public ArrayList<Row> getPattern() {
    return pattern;
  }

  public int getRowCount() {
    return pattern.size();
  }

  public Row getRow(int rowNum) {
    return pattern.get(rowNum - 1);
  }

  public void addRow(Row row) {
    pattern.add(row);
  }

  public int getMaxStitchCount() {
    int maxStitchCount = 0;
    for (Row row : pattern) {
      if (row.getStitchCount() > maxStitchCount) {
        maxStitchCount = row.getStitchCount();
      }
    }
    return maxStitchCount;
  }

  public void addRowBounds(RowBounds rowBounds) {
    this.rowBoundsList.add(rowBounds);
  }

  public ArrayList<RowBounds> getRowBoundsList() {
    return rowBoundsList;
  }

  public RowBounds getRowBounds(int rowNum) {
    return rowBoundsList.get(rowNum - 1);
  }

  public StitchBounds getStitchBounds(int rowNum, int stitchNum) {
    return rowBoundsList.get(rowNum - 1).getStitchAndBounds(stitchNum);
  }

  public ArrayList<StitchBounds> getStitchBoundsList(int rowNum) {
    return rowBoundsList.get(rowNum - 1).getStitchBoundsList();
  }

  public StitchBounds getStitchAndBounds(int rowNum, int stitchNum) {
    return rowBoundsList.get(rowNum - 1).getStitchAndBounds(stitchNum);
  }

  public ArrayList<Coords> getAllAttachmentCoords() {
    ArrayList<Coords> allCoords = new ArrayList<>();
    for (RowBounds rowBounds : rowBoundsList) {
      for (StitchBounds stitchBounds : rowBounds.getStitchBoundsList()) {
        allCoords.add(stitchBounds.getAttachmentCoords());
      }
    }
    return allCoords;
  }

  public StitchLoc getStitchLocByAttachmentCoords(Coords coords) {
    for (RowBounds rowBounds : rowBoundsList) {
      for (StitchBounds stitchBounds : rowBounds.getStitchBoundsList()) {
        if (stitchBounds.getAttachmentCoords().equals(coords)) {
          return stitchBounds.getStitch().getLoc();
        }
      }
    }
    return null;

  }


}