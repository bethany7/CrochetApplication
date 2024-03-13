package org.batah.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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

  public void removeRowBounds(RowBounds rowBounds) {
    this.rowBoundsList.remove(rowBounds);
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
        if (stitchBounds.getAttachmentCoords().toString().equals(coords.toString())) {
          return stitchBounds.getStitch().getLoc();
        }
      }
    }
    return null;
  }

  public void correctRows() {
    for (Row row : pattern) {
      for (Stitch stitch : row.getStitches()) {
        if (stitch.getLoc().getRowNum() != row.getRowNum()) {
          Row rightRow = pattern.get(stitch.getLoc().getRowNum());
          rightRow.addStitch(rightRow, stitch);
          row.removeStitch(row, stitch);
        }
      }
    }
  }

  public void sortByBounds() {
    for (RowBounds rowBounds : rowBoundsList) {
      rowBounds.getStitchBoundsList().sort(new Comparator<StitchBounds>() {
        @Override
        public int compare(StitchBounds o1, StitchBounds o2) {
          return (int) (o1.getBounds().getMinX() - o2.getBounds().getMinX());
        }
      });
    }
  }
}