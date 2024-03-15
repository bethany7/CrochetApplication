package org.batah;

import java.io.IOException;
import java.util.ArrayList;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Chain;
import org.batah.model.stitches.DoubleCrochet;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.Stitch.Attachment;
import org.batah.model.stitches.StitchLoc;

public class SerializationTest {

  public static void main(String[] args) {

    Pattern pattern = new Pattern();
    Row row = new Row(pattern);

    ArrayList<StitchLoc> parentStitches1 = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      StitchLoc loc = new StitchLoc(row.getRowNum(), i + 1);
      Stitch stitch = new Chain(Attachment.NONE, parentStitches1,
          loc,
          row);
      row.addStitch(stitch);
    }

    pattern.addRow(row);
    Row row2 = new Row(pattern);

    ArrayList<StitchLoc> parentStitches2 = new ArrayList<>();
    parentStitches2.add(new StitchLoc(row.getRowNum(), 1));
    parentStitches2.add(new StitchLoc(row.getRowNum(), 2));
    for (int i = 0; i < 10; i++) {
      Stitch stitch = new DoubleCrochet(Attachment.INTO, parentStitches2,
          new StitchLoc(row2.getRowNum(), i + 1), row2);
      row2.addStitch(stitch);
    }
    pattern.addRow(row2);

    try {
      SerializationUtil.serialize(pattern, "pattern.ser");
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    try {
      Pattern pattern2 = (Pattern) SerializationUtil.deserialize("pattern.ser");
      System.out.println("pattern object: " + pattern);
      System.out.println("Deserialized pattern object: " + pattern2);
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
  }


}