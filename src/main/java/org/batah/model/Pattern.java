package org.batah.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.Stitch.Attachment;
import org.batah.model.stitches.StitchLoc;

public class Pattern implements Serializable {

  ArrayList<Row> pattern;

  public Pattern() {
    this.pattern = new ArrayList<>();
  }

  @Override
  public String toString() {
    return "" + pattern;
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

//  public void sortStitches() {
//    for (Row row : pattern) {
//      ArrayList<Stitch> stitches = row.getStitches();
//      for (Stitch stitch : stitches) {
//        if (stitch.getAttachment() != Attachment.NONE) {
//          stitch.redoLoc();
//          if (stitch.getLoc().getRowNum() != row.getRowNum()) {
//            getRow(row.getRowNum()).removeStitch(getRow(row.getRowNum()), stitch);
//            getRow(stitch.getLoc().getRowNum()).addStitch(getRow(stitch.getLoc().getRowNum()),
//                stitch);
//          }
//        }
//      }
//      System.out.println("Sorting stitches: " + row.getStitches());
//      stitches.sort(
//          Comparator.comparingInt((Stitch s) ->
//              s.getParentStitch(0).getStitchNum()).reversed());
//      System.out.println("Sorted stitches: " + pattern);
//      //renumber stitches
//      for (int i = 0; i < pattern.size(); i++) {
//        Stitch stitch = stitches.get(i);
//        stitch.setLoc(new StitchLoc(stitch.getLoc().getRowNum(), i + 1));
//      }
//      System.out.println("Renumbered stitches: " + pattern);
//
//    }
//  }

//  public void sortStitches() {
//    for (Row row : pattern) {
//      ArrayList<Stitch> stitches = new ArrayList<>(row.getStitches());
//      for (Stitch stitch : stitches) {
//        if (stitch.getAttachment() != Attachment.NONE) {
//          stitch.redoLoc();
//          if (stitch.getLoc().getRowNum() != row.getRowNum()) {
//            row.removeStitch(row, stitch);
//            getRow(stitch.getLoc().getRowNum()).addStitch(getRow(stitch.getLoc().getRowNum()),
//                stitch);
//          }
//        }
//      }
//      System.out.println("Sorting stitches: " + row.getStitches());
//      row.getStitches().sort(
//          Comparator.comparingInt((Stitch s) -> s.getParentStitch(0).getStitchNum()));
//      System.out.println("Sorted stitches: " + pattern);
      //renumber stitches
//      for (int i = 0; i < row.getStitches().size(); i++) {
//        Stitch stitch = row.getStitches().get(i);
//        stitch.setLoc(new StitchLoc(stitch.getLoc().getRowNum(), i + 1));
//      }
//
//
//      System.out.println("Renumbered stitches: " + pattern);
//    }
//  }

}