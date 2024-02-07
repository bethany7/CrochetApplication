import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.batah.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StitchTests {

  @Test
  @DisplayName("Test Stitch")
  public void testStitch() {
    Model.Pattern pattern = new Model().new Pattern();
    Model.Stitch stitch = new Model().new Stitch(Model.StitchType.CHAIN, Model.Attachment.NONE,
        new Model.StitchLoc(1, 1), new Model.StitchLoc(2, 1), pattern);
    assertEquals(Model.StitchType.CHAIN, stitch.getStitchType());
    assertEquals(Model.Attachment.NONE, stitch.getAttachment());
    assertEquals(1, stitch.getParentStitch().getRowNum());
    assertEquals(1, stitch.getParentStitch().getStitchNum());
    assertEquals(2, stitch.getLoc().getLoc().getValue0());
    assertEquals(1, stitch.getLoc().getLoc().getValue1());
  }

  @Test
  @DisplayName("Test Pattern and Row Creation")
  public void testRow() {
    Model.Pattern pattern = new Model().new Pattern();
    Model.Row row1 = new Model().new Row(pattern);
    pattern.addRow(row1);
    Model.Stitch stitch1 = new Model().new Stitch(Model.StitchType.CHAIN, Model.Attachment.BETWEEN,
        new Model.StitchLoc(1, 1), new Model.StitchLoc(2, 1), pattern);
    Model.Stitch stitch2 = new Model().new Stitch(Model.StitchType.DOUBLE, Model.Attachment.INTO,
        new Model.StitchLoc(1, 2), new Model.StitchLoc(2, 2), pattern);
    row1.addStitch(row1, stitch1);
    row1.addStitch(row1, stitch2);
    Model.Row row2 = new Model().new Row(pattern);
    pattern.addRow(row2);
    Model.Stitch stitch3 = new Model().new Stitch(Model.StitchType.HALF_DOUBLE,
        Model.Attachment.BEYOND, new Model.StitchLoc(2, 1), new Model.StitchLoc(3, 1), pattern);
    Model.Stitch stitch4 = new Model().new Stitch(Model.StitchType.TREBLE, Model.Attachment.NONE,
        new Model.StitchLoc(2, 2), new Model.StitchLoc(3, 2), pattern);
    row2.addStitch(row2, stitch3);
    row2.addStitch(row2, stitch4);
    assertEquals(2, pattern.getPattern().size());
    assertEquals(2, pattern.getPattern().size());
    assertEquals(2, row1.getStitchCount());
    assertEquals(2, row2.getStitchCount());
    assertEquals(1, row1.getRowNum());
    assertEquals(2, row2.getRowNum());
  }

  @Test
  @DisplayName("Test Parent Stitch")
  public void testParent() {
    Model.Pattern pattern = new Model().new Pattern();
    Model.Row row1 = new Model().new Row(pattern);
    pattern.addRow(row1);
    Model.Stitch stitch1 = new Model().new Stitch(Model.StitchType.CHAIN, Model.Attachment.BETWEEN,
        row1);
    row1.addStitch(row1, stitch1);
    Model.Stitch stitch2 = new Model().new Stitch(Model.StitchType.DOUBLE, Model.Attachment.INTO,
        row1);
    row1.addStitch(row1, stitch2);
    Model.Stitch stitch3 = new Model().new Stitch(Model.StitchType.HALF_DOUBLE,
        Model.Attachment.BEYOND, row1);
    Model.Row row2 = new Model().new Row(pattern);
    row1.addStitch(row1, stitch3);
    pattern.addRow(row2);
    Model.Stitch stitch4 = new Model().new Stitch(Model.StitchType.HALF_DOUBLE,
        Model.Attachment.BEYOND, row2);
    row2.addStitch(row2, stitch4);
    Model.Stitch stitch5 = new Model().new Stitch(Model.StitchType.TREBLE, Model.Attachment.NONE,
        row2);
    row2.addStitch(row2, stitch5);
    Model.Stitch stitch6 = new Model().new Stitch(Model.StitchType.TREBLE, Model.Attachment.NONE,
        row2);
    row2.addStitch(row2, stitch6);
    assertNull(stitch1.getParentStitch());
    assertNull(stitch2.getParentStitch());
    assertNull(stitch3.getParentStitch());
    assertEquals(3, stitch4.getParentStitch().getStitchNum());
    assertEquals(2, stitch5.getParentStitch().getStitchNum());
    assertEquals(1, stitch6.getParentStitch().getStitchNum());
    assertEquals(1, stitch1.getLoc().getRowNum());
    assertEquals(2, stitch4.getLoc().getRowNum());


  }


}