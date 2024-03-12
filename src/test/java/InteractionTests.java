import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.util.ArrayList;
import javafx.stage.Stage;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.CrochetApplication;
import org.batah.CrochetPatternParserLexer;
import org.batah.CrochetPatternParserParser;
import org.batah.CrochetVisitor;
import org.batah.SerializationUtil;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Stitch;
import org.batah.model.stitches.StitchLoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InteractionTests {

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

  @Test
  void testSerialization() throws IOException, ClassNotFoundException {
    //SerializationUtil.serialize(pattern, "pattern.ser");
    Pattern pattern2 = (Pattern) SerializationUtil.deserialize("pattern.ser");
    assertEquals(4, pattern2.getRowCount());
    assertEquals("DoubleCrochet", pattern2.getRow(2).getStitch(0).getStitchName());
  }

//  @Test
//  void TestSimpleSort() {
//    CharStream input = CharStreams.fromString("10 dc");
//    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
//    CrochetPatternParserParser parser = new CrochetPatternParserParser(
//        new CommonTokenStream(lexer));
//    ParseTree tree = parser.instructions();
//    visitor.visit(tree);
//    System.out.println(pattern);
//    var x = pattern.getRow(2).getStitch(0);
//    System.out.println(x);
//    assertEquals(2, x.getLoc().getRowNum());
//    assertEquals(1, x.getLoc().getStitchNum());
//    var newParentStitches = new ArrayList<StitchLoc>();
//    var newParent = pattern.getRow(1).getStitch(0).getLoc();
//    newParentStitches.add(newParent);
//    x.getParentStitches().clear();
//    x.setParentStitches(newParentStitches);
//    System.out.println(x);
//    pattern.sortRowOnParentStitchNum(pattern.getRow(2));
//    System.out.println(pattern);
//    assertEquals(1, x.getParentStitch(0).getRowNum());
//    assertEquals(1, x.getParentStitch(0).getStitchNum());
//  }

//  @Test
//  void sortTest() {
//    CharStream input = CharStreams.fromString("9 dc");
//    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
//    CrochetPatternParserParser parser = new CrochetPatternParserParser(
//        new CommonTokenStream(lexer));
//    ParseTree tree = parser.instructions();
//    visitor.visit(tree);
//    CharStream input2 = CharStreams.fromString("5 tr");
//    CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
//    CrochetPatternParserParser parser2 = new CrochetPatternParserParser(
//        new CommonTokenStream(lexer2));
//    ParseTree tree2 = parser2.instructions();
//    visitor.visit(tree2);
//    var x = pattern.getRow(3).getStitch(0);
//    System.out.println(x);
//    assertEquals(3, x.getLoc().getRowNum());
//    assertEquals(2, x.getParentStitch(0).getRowNum());
//    assertEquals(9, x.getParentStitch(0).getStitchNum());
//    var pStitches = new ArrayList<StitchLoc>();
//    var pStitch = new StitchLoc(1, 5);
//    pStitches.add(pStitch);
//    x.setParentStitches(pStitches);
//    assertEquals(1, x.getParentStitch(0).getRowNum());
//    assertEquals(5, x.getParentStitch(0).getStitchNum());
////    pattern.sortStitches();
////    System.out.println(x);
////    assertEquals(2, x.getLoc().getRowNum());
//
//  }

}