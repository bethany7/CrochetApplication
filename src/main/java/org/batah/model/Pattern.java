package org.batah.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.scene.shape.SVGPath;
import org.batah.SerializableBounds;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.StitchLoc;

public class Pattern implements Serializable {

  ArrayList<Row> rowList;
  ArrayList<RowBounds> rowBoundsList;

  String patternStyle = "Rounds";
  boolean turningChainIsStitch;
  int turningChainLength;

  private static final long serialVersionUID = 1L;


  public Pattern() {
    this.rowList = new ArrayList<>();
    this.rowBoundsList = new ArrayList<>();
  }


  @Override
  public String toString() {
    if (rowBoundsList.isEmpty()) {
      return "" + rowList;
    } else {
      return "" + rowBoundsList;
    }
  }

  public String getPatternStyle() {
    return patternStyle;
  }

  public void setPatternStyle(String patternStyle) {
    this.patternStyle = patternStyle;
  }

  public boolean isTurningChainIsStitch() {
    return turningChainIsStitch;
  }

  public void setTurningChainIsStitch(boolean turningChainIsStitch) {
    this.turningChainIsStitch = turningChainIsStitch;
  }

  public int getTurningChainLength() {
    return turningChainLength;
  }

  public void setTurningChainLength(int turningChainLength) {
    this.turningChainLength = turningChainLength;
  }

  public ArrayList<Row> getRowList() {
    return rowList;
  }

  public int getRowCount() {
    return rowList.size();
  }

  public Row getRow(int rowNum) {
    return rowList.get(rowNum - 1);
  }

  public void addRow(Row row) {
    rowList.add(row);
  }

  public int getMaxStitchCount() {
    int maxStitchCount = 0;
    for (Row row : rowList) {
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

  public void removeRow(Row row) {
    this.rowList.remove(row);
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

  public StitchBounds getStitchByPath(SVGPath stitchPath) {
    for (RowBounds rowBounds : rowBoundsList) {
      for (StitchBounds stitchBounds : rowBounds.getStitchBoundsList()) {
        if (stitchBounds.getBounds().getCenterX() == stitchPath.getBoundsInParent().getCenterX()
            && stitchBounds.getBounds().getCenterY() == stitchPath.getBoundsInParent().getCenterY())
        {
          return stitchBounds;
        }
      }
    }
    return null;
  }

  public void updateStitchRow() {
    List<StitchBounds> toBeModified = new ArrayList<>();
    var tempRowBoundsList = new ArrayList<>(rowBoundsList);
    //tempRowBoundsList.removeFirst();
    for (RowBounds rowBounds : tempRowBoundsList) {
      for (StitchBounds stitchBounds : new ArrayList<>(rowBounds.getStitchBoundsList())) {
        if (stitchBounds.getStitch().getLoc().getRowNum()
            != stitchBounds.getStitch().getParentStitch(0).getRowNum() + 1) {
          removeRowBounds(rowBounds);
          rowBounds.removeStitchBounds(stitchBounds);
          rowBounds.getRow().removeStitch(stitchBounds.getStitch());
          rowBoundsList.add(rowBounds);
          rowBounds.updateRow(rowBounds.getRow());
          sortByRow();
          stitchBounds.getStitch().setLoc(
              new StitchLoc(stitchBounds.getStitch().getParentStitch(0).getRowNum() + 1,
                  stitchBounds.getStitch().getLoc().getStitchNum()));
          if (stitchBounds.getStitch().getLoc().getRowNum() != rowBounds.getRowNum()) {
            toBeModified.add(stitchBounds);
          }

        }
      }
    }
    for (StitchBounds stitchBounds : toBeModified) {
      int correctRowNum = stitchBounds.getStitch().getLoc().getRowNum();
      System.out.println("Correct Row Num: " + correctRowNum);
      System.out.println("Row Bounds List: " + rowBoundsList.size());
      if (rowBoundsList.size() < correctRowNum) {
        RowBounds rowBounds = new RowBounds(this, correctRowNum, new Row(this));
        rowBoundsList.add(rowBounds);
      }
      RowBounds correctRowBounds = rowBoundsList.get(correctRowNum - 1);
      rowBoundsList.remove(correctRowBounds);
      correctRowBounds.addStitchBounds(stitchBounds);
      correctRowBounds.getRow().addStitch(stitchBounds.getStitch());
      correctRowBounds.updateRow(correctRowBounds.getRow());
      rowBoundsList.add(correctRowBounds);

    }

    sortByRow();
    updateRowListFromRowBoundsList();
  }


  public void sortByRow() {
    rowBoundsList.sort(new Comparator<RowBounds>() {
      @Override
      public int compare(RowBounds o1, RowBounds o2) {
        return o1.getRow().getRowNum() - o2.getRow().getRowNum();
      }
    });
    updateRowListFromRowBoundsList();
  }

  // sort row so that stitches are in order of their x-coordinates
  public void sortByBounds() {
    var tempRowBoundsList = new ArrayList<>(rowBoundsList);
    for (RowBounds rowBounds : tempRowBoundsList) {
      rowBoundsList.remove(rowBounds);
      rowBounds.getStitchBoundsList().sort(new Comparator<StitchBounds>() {
        @Override
        public int compare(StitchBounds o1, StitchBounds o2) {
          return (int) (o1.getBounds().getMinX() - o2.getBounds().getMinX());
        }
      });
      rowBoundsList.add(rowBounds);
      rowBounds.updateRow(rowBounds.getRow());
    }
    updateRowListFromRowBoundsList();
  }

//  public void sortByBounds() {
//    for (RowBounds rowBounds : rowBoundsList) {
//      rowBounds.getStitchBoundsList().sort(new Comparator<StitchBounds>() {
//        @Override
//        public int compare(StitchBounds o1, StitchBounds o2) {
//          return (int) (o1.getBounds().getMinX() - o2.getBounds().getMinX());
//        }
//      });
//      rowBounds.updateRow(rowBounds.getRow());
//    }
//    updateRowListFromRowBoundsList();
//  }

  public void updateStitchLocation() {
    for (RowBounds rowBounds : rowBoundsList) {
      //Row row = rowBounds.getRow();
      //odd rows
      if (rowBounds.getRowNum() % 2 != 0) {
        for (int i = 0; i < rowBounds.getStitchCount(); i++) {
          rowBounds.getStitch(i).setLoc(new StitchLoc(rowBounds.getRowNum(), i + 1));
        }
      } else {
        for (int i = 0; i < rowBounds.getStitchCount(); i++) {
          rowBounds.getStitch(i)
              .setLoc(new StitchLoc(rowBounds.getRowNum(), rowBounds.getStitchCount() - i));
        }
      }
      rowBounds.updateRow(rowBounds.getRow());
    }
    updateRowListFromRowBoundsList();
  }

  public void updateRowListFromRowBoundsList() {
    rowList.clear();
    for (RowBounds rowBounds : rowBoundsList) {
      rowList.add(rowBounds.getRow());
    }
  }

  public void updateAll() {
    updateStitchRow();
    sortByBounds();
    updateStitchLocation();
    var tempRowBoundsList = new ArrayList<>(rowBoundsList);
    for (RowBounds rowBounds : tempRowBoundsList) {
      if (rowBounds.getStitchCount() == 0) {
        removeRowBounds(rowBounds);
      }
    }
  }

  public void prettyPrint() {
    System.out.print("Pattern: " + "\n");
    for (RowBounds rowBounds : rowBoundsList) {
      System.out.print("Row: " + rowBounds.getRowNum() + "\n");
      for (StitchBounds stitchBounds : rowBounds.getStitchBoundsList()) {
        Stitch stitch = stitchBounds.getStitch();
        System.out.print(
            "Stitch: " + stitch.getStitchName() + ", Loc: " + stitch.getLoc()
                + ", Parent0: " + stitch.getParentStitch(0).getLoc() + ", MinX: "
                + stitchBounds.getBounds().getMinX() + "\n");
      }
    }
  }

}