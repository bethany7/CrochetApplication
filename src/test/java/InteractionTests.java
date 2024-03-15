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

//  @Test
//  void testSerialization() throws IOException, ClassNotFoundException {
//    //SerializationUtil.serialize(pattern, "pattern.ser");
//    Pattern pattern2 = (Pattern) SerializationUtil.deserialize("pattern.ser");
//    assertEquals(4, pattern2.getRowCount());
//    assertEquals("DoubleCrochet", pattern2.getRow(2).getStitch(0).getStitchName());
//  }

  @Test
  void TestSimpleSort() throws IOException, ClassNotFoundException {
    Pattern pattern2 = (Pattern) SerializationUtil.deserialize("pattern.ser");
    System.out.println(pattern2.getRowList());
//    assertEquals(3, pattern2.getRowCount());
//    assertEquals("TrebleCrochet", pattern2.getRow(2).getStitch(5).getStitchName());
//    assertEquals(1, pattern2.getRow(2).getStitch(5).getParentStitch(0).getRowNum());
//    assertEquals(5, pattern2.getRow(2).getStitch(5).getParentStitch(0).getStitchNum());
    Pattern pattern3 = (Pattern) SerializationUtil.deserialize("pattern2.ser");
    System.out.println(pattern3.getRowList());
    //assertEquals(2, pattern3.getRow(2).getStitch(5).getParentStitch(0).getRowNum());
    //assertEquals(7, pattern3.getRow(2).getStitch(5).getParentStitch(0).getStitchNum());
    pattern3.updateStitchRow();
    System.out.println(pattern3.getRowList());
    pattern3.sortByBounds();
    System.out.println(pattern3.getRowList());
    pattern3.updateStitchLocation();
    System.out.println(pattern3.getRowList());

    try {
      SerializationUtil.serialize(pattern3, "pattern3.ser");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}