package org.batah.model;

import static java.util.Collections.reverseOrder;

import java.io.Serializable;
import java.util.ArrayList;
import org.batah.model.stitches.Stitch;

public class Row implements Serializable {

  int rowNum;
  ArrayList<Stitch> stitches;
  Pattern pattern;

  //Triplet<Integer, Integer, ArrayList<Stitch>> row;

  public Row(Pattern pattern) {
    this.stitches = new ArrayList<>();
    this.rowNum = pattern.rowList.isEmpty() ? 1 : pattern.rowList.size() + 1;
    //this.row = new Triplet<>(rowNum, 0, stitches);
    this.pattern = pattern;
  }

  @Override
  public String toString() {
    return stitches.toString();
  }

  public void addStitch(Stitch stitch) {
    this.stitches.add(stitch);
  }

  public int getRowNum() {
    return rowNum;
  }

  public int getStitchCount() {
    return stitches.size();
  }

  public Stitch getStitch(int i) {
    return stitches.get(i);
  }

  public ArrayList<Stitch> getStitches() {
    return stitches;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void removeStitch(Stitch stitch) {
    this.stitches.remove(stitch);
  }

  public void clearRow() {
    stitches.clear();
  }

//  public void sortStitchesOnParentStitchNum() {
//    System.out.println("Sorting stitches: " + stitches);
//    stitches.sort(
//        Comparator.comparingInt((Stitch s) -> s.getParentStitch(0).getStitchNum()).reversed());
//    System.out.println("Sorted stitches: " + stitches);
//    //renumber stitches
//    for (int i = 0; i < stitches.size(); i++) {
//      Stitch stitch = stitches.get(i);
//      stitch.setLoc(new StitchLoc(rowNum, i + 1));
//    }
//    System.out.println("Renumbered stitches: " + stitches);
//
//  }
}