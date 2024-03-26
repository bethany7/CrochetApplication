import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.CrochetPatternParserLexer;
import org.batah.CrochetPatternParserParser;
import org.batah.CrochetVisitor;
import org.batah.SerializationUtil;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.Stitch.Attachment;
import org.batah.model.stitches.StitchLoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParseTests {

  Pattern pattern = new Pattern();
  CrochetVisitor<String> visitor = new CrochetVisitor<>(pattern);

  /**
   * Set up the pattern and visitor for each test
   */
  @BeforeEach
  void setUp() {
    CharStream input = CharStreams.fromString("chain 10");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
  }

  /**
   * Test that the pattern has two rows after adding a row
   */

  @Test
  void testAddRow() {
    assertEquals(1, pattern.getRowCount());
    pattern.addRow(new Row(pattern));
    assertEquals(2, pattern.getRowCount());
  }

//    @Test
//    void testParseCh10() {
//        Pattern pattern = new Pattern();
//        CrochetVisitor<String> visitor = new CrochetVisitor<>(pattern);
//        CharStream input = CharStreams.fromString("chain 10");
//        CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
//        CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));
//        ParseTree tree = parser.instructions();
//        visitor.visit(tree);
//        assertEquals(1, pattern.getRowCount());
//        assertEquals(1, pattern.getRow(1).getRowNum());
//        assertEquals(10, pattern.getRow(1).getStitchCount());
//        assertEquals(10, pattern.getRow(1).getStitches().size());
//        assertEquals("Chain", pattern.getRow(1).getStitch(0).getStitchName());
//        assertEquals(1, pattern.getRow(1).getStitch(0).getLoc().getRowNum());
//        assertEquals(1, pattern.getRow(1).getStitch(0).getLoc().getStitchNum());
//        assertEquals("Chain", pattern.getRow(1).getStitch(9).getStitchName());
//        assertEquals(1, pattern.getRow(1).getStitch(9).getLoc().getRowNum());
//        assertEquals(10, pattern.getRow(1).getStitch(9).getLoc().getStitchNum());
//    }

  /**
   * Test adding a sequence of stitches to a row
   */
  @Test
  void testSequence() {
    CharStream input = CharStreams.fromString("1 dc, 1 tr, 2 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    assertEquals(2, pattern.getRowCount());
    assertEquals(2, pattern.getRow(2).getRowNum());
    assertEquals(4, pattern.getRow(2).getStitchCount());
    assertEquals(4, pattern.getRow(2).getStitches().size());
    assertEquals("DoubleCrochet", pattern.getRow(2).getStitch(0).getStitchName());
    assertEquals(2, pattern.getRow(2).getStitch(0).getLoc().getRowNum());
    assertEquals(1, pattern.getRow(2).getStitch(0).getLoc().getStitchNum());
    assertEquals("TrebleCrochet", pattern.getRow(2).getStitch(1).getStitchName());
    assertEquals(2, pattern.getRow(2).getStitch(1).getLoc().getRowNum());
    assertEquals(2, pattern.getRow(2).getStitch(1).getLoc().getStitchNum());
    assertEquals("DoubleCrochet", pattern.getRow(2).getStitch(2).getStitchName());
    assertEquals(2, pattern.getRow(2).getStitch(2).getLoc().getRowNum());
    assertEquals(3, pattern.getRow(2).getStitch(2).getLoc().getStitchNum());
    assertEquals("DoubleCrochet", pattern.getRow(2).getStitch(3).getStitchName());
    assertEquals(2, pattern.getRow(2).getStitch(3).getLoc().getRowNum());
    assertEquals(4, pattern.getRow(2).getStitch(3).getLoc().getStitchNum());

  }

  @Test
  void testParse10DC() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    assertEquals(2, pattern.getRowCount());
    assertEquals(2, pattern.getRow(2).getRowNum());
    assertEquals(10, pattern.getRow(2).getStitchCount());
    assertEquals(10, pattern.getRow(2).getStitches().size());
    assertEquals("DoubleCrochet", pattern.getRow(2).getStitch(0).getStitchName());
    assertEquals(2, pattern.getRow(2).getStitch(0).getLoc().getRowNum());
    assertEquals(1, pattern.getRow(2).getStitch(0).getLoc().getStitchNum());
    assertEquals("DoubleCrochet", pattern.getRow(2).getStitch(9).getStitchName());
    assertEquals(2, pattern.getRow(2).getStitch(9).getLoc().getRowNum());
    assertEquals(10, pattern.getRow(2).getStitch(9).getLoc().getStitchNum());
    assertEquals(1, pattern.getRow(2).getStitch(9).getParentStitch(0).getRowNum());
    assertEquals(1, pattern.getRow(2).getStitch(9).getParentStitch(0).getStitchNum());
    assertEquals(1, pattern.getRow(2).getStitch(1).getParentStitch(0).getRowNum());
    assertEquals(9, pattern.getRow(2).getStitch(1).getParentStitch(0).getStitchNum());
  }

  @Test
  void testParse10DC3rdRow() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals(3, pattern.getRow(3).getRowNum());
    assertEquals(10, pattern.getRow(3).getStitchCount());
    assertEquals(10, pattern.getRow(3).getStitches().size());
    assertEquals("DoubleCrochet", pattern.getRow(3).getStitch(0).getStitchName());
    assertEquals(3, pattern.getRow(3).getStitch(0).getLoc().getRowNum());
    assertEquals(1, pattern.getRow(3).getStitch(0).getLoc().getStitchNum());
    assertEquals("DoubleCrochet", pattern.getRow(3).getStitch(9).getStitchName());
    assertEquals(3, pattern.getRow(3).getStitch(9).getLoc().getRowNum());
    assertEquals(10, pattern.getRow(2).getStitch(9).getLoc().getStitchNum());
    assertEquals(2, pattern.getRow(3).getStitch(9).getParentStitch(0).getRowNum());
    assertEquals(1, pattern.getRow(3).getStitch(9).getParentStitch(0).getStitchNum());
    assertEquals(2, pattern.getRow(3).getStitch(1).getParentStitch(0).getRowNum());
    assertEquals(9, pattern.getRow(3).getStitch(1).getParentStitch(0).getStitchNum());
    StitchLoc parentStitchLoc = pattern.getRow(3).getStitch(1).getParentStitch(0);
    Stitch parentStitch = pattern.getRow(parentStitchLoc.getRowNum())
        .getStitch(parentStitchLoc.getStitchNum());
    assertEquals("DoubleCrochet", parentStitch.getStitchName());
  }

  @Test
  void testParse10trInNext10tr() {
    CharStream input = CharStreams.fromString("tr in next 10 tr");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("tr in next 10 tr");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals(3, pattern.getRow(3).getRowNum());
    assertEquals(10, pattern.getRow(3).getStitchCount());
    assertEquals(10, pattern.getRow(3).getStitches().size());
    assertEquals("TrebleCrochet", pattern.getRow(3).getStitch(0).getStitchName());
    assertEquals(3, pattern.getRow(3).getStitch(0).getLoc().getRowNum());
    assertEquals(1, pattern.getRow(3).getStitch(0).getLoc().getStitchNum());
    assertEquals("TrebleCrochet", pattern.getRow(3).getStitch(9).getStitchName());
    assertEquals(3, pattern.getRow(3).getStitch(9).getLoc().getRowNum());
    assertEquals(10, pattern.getRow(3).getStitch(9).getLoc().getStitchNum());
    StitchLoc parentStitchLoc = pattern.getRow(3).getStitch(1).getParentStitch(0);
    Stitch parentStitch = pattern.getRow(parentStitchLoc.getRowNum())
        .getStitch(parentStitchLoc.getStitchNum());
    assertEquals("TrebleCrochet", parentStitch.getStitchName());
    StitchLoc parentStitchLoc2 = pattern.getRow(2).getStitch(9).getParentStitch(0);
    Stitch parentStitch2 = pattern.getRow(parentStitchLoc2.getRowNum())
        .getStitch(parentStitchLoc2.getStitchNum());
    assertEquals("Chain", parentStitch2.getStitchName());

  }

  @Test
  void testDc4thChHook() {
    CharStream input = CharStreams.fromString("dc in 4th ch from hook");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    assertEquals(2, pattern.getRowCount());
    assertEquals(1, pattern.getRow(2).getStitchCount());
    assertEquals(1, pattern.getRow(2).getStitches().size());
    assertEquals("DoubleCrochet", pattern.getRow(2).getStitch(0).getStitchName());
    assertEquals(1, pattern.getRow(2).getStitch(0).getLoc().getStitchNum());
    StitchLoc parentStitch = pattern.getRow(2).getStitch(0).getParentStitch(0);
    assertEquals(1, parentStitch.getRowNum());
    assertEquals(7, parentStitch.getStitchNum());
  }

  @Test
  void dc2tog() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("dc2tog");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals(2, pattern.getRow(3).getStitch(0).getParentStitches().size());
  }

  @Test
  void dc2tog5() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("5 dc2tog");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals("DoubleCrochet", pattern.getRow(3).getStitch(0).getStitchName());
    assertEquals(2, pattern.getRow(3).getStitch(0).getParentStitches().size());
    assertEquals(2, pattern.getRow(3).getStitch(1).getParentStitches().size());
    assertEquals(2, pattern.getRow(3).getStitch(2).getParentStitches().size());
    assertEquals(2, pattern.getRow(3).getStitch(3).getParentStitches().size());
    assertEquals(2, pattern.getRow(3).getStitch(4).getParentStitches().size());
    assertEquals(5, pattern.getRow(3).getStitchCount());
    assertEquals(10, pattern.getRow(3).getStitch(0).getParentStitch(0).getStitchNum());
    assertEquals(9, pattern.getRow(3).getStitch(0).getParentStitch(1).getStitchNum());
    assertEquals(8, pattern.getRow(3).getStitch(1).getParentStitch(0).getStitchNum());
    assertEquals(7, pattern.getRow(3).getStitch(1).getParentStitch(1).getStitchNum());
    assertEquals(2, pattern.getRow(3).getStitch(4).getParentStitch(0).getStitchNum());
    assertEquals(1, pattern.getRow(3).getStitch(4).getParentStitch(1).getStitchNum());
  }

  @Test
  void tr2tog5() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("5 tr2tog");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals("TrebleCrochet", pattern.getRow(3).getStitch(0).getStitchName());
    assertEquals(2, pattern.getRow(3).getStitch(0).getParentStitches().size());
    assertEquals(2, pattern.getRow(3).getStitch(1).getParentStitches().size());
    assertEquals(2, pattern.getRow(3).getStitch(2).getParentStitches().size());
    assertEquals(2, pattern.getRow(3).getStitch(3).getParentStitches().size());
    assertEquals(2, pattern.getRow(3).getStitch(4).getParentStitches().size());
    assertEquals(5, pattern.getRow(3).getStitchCount());
    assertEquals(10, pattern.getRow(3).getStitch(0).getParentStitch(0).getStitchNum());
    assertEquals(9, pattern.getRow(3).getStitch(0).getParentStitch(1).getStitchNum());
    assertEquals(8, pattern.getRow(3).getStitch(1).getParentStitch(0).getStitchNum());
    assertEquals(7, pattern.getRow(3).getStitch(1).getParentStitch(1).getStitchNum());
    assertEquals(2, pattern.getRow(3).getStitch(4).getParentStitch(0).getStitchNum());
    assertEquals(1, pattern.getRow(3).getStitch(4).getParentStitch(1).getStitchNum());
  }

  @Test
  void incOnDecTest() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("2 tr3tog, 2tr, dc2tog");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    CharStream input3 = CharStreams.fromString("2 dc in next 5 tr");
    CrochetPatternParserLexer lexer3 = new CrochetPatternParserLexer(input3);
    CrochetPatternParserParser parser3 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer3));
    ParseTree tree3 = parser3.instructions();
    visitor.visit(tree3);
    CharStream input4 = CharStreams.fromString("10 tr");
    CrochetPatternParserLexer lexer4 = new CrochetPatternParserLexer(input4);
    CrochetPatternParserParser parser4 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer4));
    ParseTree tree4 = parser4.instructions();
    visitor.visit(tree4);
    assertEquals(5, pattern.getRowCount());
    assertEquals(10, pattern.getRow(1).getStitchCount());
    assertEquals(10, pattern.getRow(2).getStitchCount());
    assertEquals(5, pattern.getRow(3).getStitchCount());
    assertEquals(10, pattern.getRow(4).getStitchCount());
    assertEquals(10, pattern.getRow(5).getStitchCount());
    assertEquals(10, pattern.getRow(3).getStitch(0).getParentStitch(0).getStitchNum());
    assertEquals(9, pattern.getRow(3).getStitch(0).getParentStitch(1).getStitchNum());
    assertEquals(8, pattern.getRow(3).getStitch(0).getParentStitch(2).getStitchNum());
    assertEquals(7, pattern.getRow(3).getStitch(1).getParentStitch(0).getStitchNum());
    assertEquals(6, pattern.getRow(3).getStitch(1).getParentStitch(1).getStitchNum());
    assertEquals(5, pattern.getRow(3).getStitch(1).getParentStitch(2).getStitchNum());
    assertEquals(4, pattern.getRow(4).getStitch(2).getParentStitch(0).getStitchNum());
    assertEquals(4, pattern.getRow(4).getStitch(3).getParentStitch(0).getStitchNum());
    assertEquals(3, pattern.getRow(4).getStitch(4).getParentStitch(0).getStitchNum());
    assertEquals(3, pattern.getRow(4).getStitch(5).getParentStitch(0).getStitchNum());
  }

  @Test
  void inc2dc() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("2 dc in same st");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals("DoubleCrochet", pattern.getRow(3).getStitch(0).getStitchName());
    assertEquals(pattern.getRow(3).getStitch(0).getParentStitch(0),
        pattern.getRow(3).getStitch(1).getParentStitch(0));
  }

  @Test
  void inc2dc2() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("2 dc in next 2 st");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals("DoubleCrochet", pattern.getRow(3).getStitch(0).getStitchName());
    assertEquals(pattern.getRow(3).getStitch(0).getParentStitch(0),
        pattern.getRow(3).getStitch(1).getParentStitch(0));
    assertEquals(4, pattern.getRow(3).getStitchCount());
    assertNotEquals(pattern.getRow(3).getStitch(1).getLoc().getStitchNum(),
        pattern.getRow(3).getStitch(2).getLoc().getStitchNum());
  }

  @Test
  void inc2tr2() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("2 tr in next 2 dc");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals("TrebleCrochet", pattern.getRow(3).getStitch(0).getStitchName());
    assertEquals(pattern.getRow(3).getStitch(0).getParentStitch(0),
        pattern.getRow(3).getStitch(1).getParentStitch(0));
    assertEquals(4, pattern.getRow(3).getStitchCount());
    assertNotEquals(pattern.getRow(3).getStitch(1).getLoc().getStitchNum(),
        pattern.getRow(3).getStitch(2).getLoc().getStitchNum());
  }

  @Test
  void skipTest() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString("2dc, skip 2, 2dc, skip 3, dc");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);

    assertEquals(3, pattern.getRowCount());
    assertEquals(5, pattern.getRow(3).getStitchCount());
    assertEquals(10, pattern.getRow(3).getStitch(0).getParentStitch(0).getStitchNum());
    assertEquals(9, pattern.getRow(3).getStitch(1).getParentStitch(0).getStitchNum());
    assertEquals(6, pattern.getRow(3).getStitch(2).getParentStitch(0).getStitchNum());
    assertEquals(5, pattern.getRow(3).getStitch(3).getParentStitch(0).getStitchNum());
    assertEquals(1, pattern.getRow(3).getStitch(4).getParentStitch(0).getStitchNum());

  }

  @Test
  void skipIncTest() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString(
        "2dc in next dc, skip 1, 2dc, skip 3, 2 dc in next 3 dc");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals(10, pattern.getRow(3).getStitchCount());
    assertEquals(10, pattern.getRow(3).getStitch(0).getParentStitch(0).getStitchNum());
    assertEquals(10, pattern.getRow(3).getStitch(1).getParentStitch(0).getStitchNum());
    assertEquals(8, pattern.getRow(3).getStitch(2).getParentStitch(0).getStitchNum());
    assertEquals(7, pattern.getRow(3).getStitch(3).getParentStitch(0).getStitchNum());
    assertEquals(3, pattern.getRow(3).getStitch(4).getParentStitch(0).getStitchNum());
    assertEquals(3, pattern.getRow(3).getStitch(5).getParentStitch(0).getStitchNum());
    assertEquals(2, pattern.getRow(3).getStitch(6).getParentStitch(0).getStitchNum());
    assertEquals(2, pattern.getRow(3).getStitch(7).getParentStitch(0).getStitchNum());
    assertEquals(1, pattern.getRow(3).getStitch(8).getParentStitch(0).getStitchNum());
    assertEquals(1, pattern.getRow(3).getStitch(9).getParentStitch(0).getStitchNum());

  }

  @Test
  void repeatTest() {
    CharStream input = CharStreams.fromString("10 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString(
        "*1 dc, 1 tr* repeat from * to * 3 times");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    assertEquals(3, pattern.getRowCount());
    assertEquals(8, pattern.getRow(3).getStitchCount());
    assertEquals("DoubleCrochet", pattern.getRow(3).getStitch(0).getStitchName());
    assertEquals("TrebleCrochet", pattern.getRow(3).getStitch(1).getStitchName());
    assertEquals("DoubleCrochet", pattern.getRow(3).getStitch(2).getStitchName());
    assertEquals("TrebleCrochet", pattern.getRow(3).getStitch(3).getStitchName());
  }

  @Test
  void skipAndChainTest() {
    CharStream input = CharStreams.fromString("10 tr");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString(
        "2 tr, ch 3, skip 2, 2 dc in next 2 tr, 4 tr");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
    CharStream input3 = CharStreams.fromString(
        "12 dc");
    CrochetPatternParserLexer lexer3 = new CrochetPatternParserLexer(input3);
    CrochetPatternParserParser parser3 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer3));
    ParseTree tree3 = parser3.instructions();
    visitor.visit(tree3);
    assertEquals(4, pattern.getRowCount());
    assertEquals(10, pattern.getRow(2).getStitchCount());
    assertEquals(13, pattern.getRow(3).getStitchCount());
    assertEquals(12, pattern.getRow(4).getStitchCount());
    assertEquals(0, pattern.getRow(3).getStitch(3).getParentStitch(0).getStitchNum());
    assertEquals(Attachment.NONE, pattern.getRow(3).getStitch(3).getAttachment());
    assertEquals(6, pattern.getRow(3).getStitch(5).getParentStitch(0).getStitchNum());
    assertEquals(6, pattern.getRow(3).getStitch(6).getParentStitch(0).getStitchNum());
    assertEquals(5, pattern.getRow(3).getStitch(7).getParentStitch(0).getStitchNum());
    assertEquals(5, pattern.getRow(3).getStitch(8).getParentStitch(0).getStitchNum());
    assertEquals(Attachment.BEYOND, pattern.getRow(4).getStitch(9).getAttachment());
  }

  @Test
  void chainSpace() {
    CharStream input = CharStreams.fromString("4 tr, ch 4, skip 2, 4 tr");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    CharStream input2 = CharStreams.fromString(
        "4 tr, 5 tr in ch 4 space, 2 tr2tog");
    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
        new CommonTokenStream(lexer2));
    ParseTree tree2 = parser2.instructions();
    visitor.visit(tree2);
  }

}