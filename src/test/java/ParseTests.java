import static org.junit.jupiter.api.Assertions.assertEquals;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.CrochetPatternParserLexer;
import org.batah.CrochetPatternParserParser;
import org.batah.CrochetVisitor;
import org.batah.model.Pattern;
import org.batah.model.Row;
import org.batah.model.stitches.Stitch;
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
        CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));
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
//
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
        CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));
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
        CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));
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
        assertEquals(1, pattern.getRow(2).getStitch(9).getParentStitch().getRowNum());
        assertEquals(1, pattern.getRow(2).getStitch(9).getParentStitch().getStitchNum());
        assertEquals(1, pattern.getRow(2).getStitch(1).getParentStitch().getRowNum());
        assertEquals(9, pattern.getRow(2).getStitch(1).getParentStitch().getStitchNum());
    }

    @Test
    void testParse10DC3rdRow() {
        CharStream input = CharStreams.fromString("10 dc");
        CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
        CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.instructions();
        visitor.visit(tree);
        CharStream input2 = CharStreams.fromString("10 dc");
        CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
        CrochetPatternParserParser parser2 = new CrochetPatternParserParser(new CommonTokenStream(lexer2));
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
        assertEquals(2, pattern.getRow(3).getStitch(9).getParentStitch().getRowNum());
        assertEquals(1, pattern.getRow(3).getStitch(9).getParentStitch().getStitchNum());
        assertEquals(2, pattern.getRow(3).getStitch(1).getParentStitch().getRowNum());
        assertEquals(9, pattern.getRow(3).getStitch(1).getParentStitch().getStitchNum());
        StitchLoc parentStitchLoc = pattern.getRow(3).getStitch(1).getParentStitch();
        Stitch parentStitch = pattern.getRow(parentStitchLoc.getRowNum()).getStitch(parentStitchLoc.getStitchNum());
        assertEquals("DoubleCrochet", parentStitch.getStitchName());

    }

    @Test
    void testParse10trInNext10tr() {
        CharStream input = CharStreams.fromString("10 tr in next 10 tr");
        CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
        CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.instructions();
        visitor.visit(tree);
        CharStream input2 = CharStreams.fromString("10 tr in next 10 tr");
        CrochetPatternParserLexer lexer2 = new CrochetPatternParserLexer(input2);
        CrochetPatternParserParser parser2 = new CrochetPatternParserParser(new CommonTokenStream(lexer2));
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
        StitchLoc parentStitchLoc = pattern.getRow(3).getStitch(1).getParentStitch();
        Stitch parentStitch = pattern.getRow(parentStitchLoc.getRowNum()).getStitch(parentStitchLoc.getStitchNum());
        assertEquals("TrebleCrochet", parentStitch.getStitchName());
        StitchLoc parentStitchLoc2 = pattern.getRow(2).getStitch(9).getParentStitch();
        Stitch parentStitch2 = pattern.getRow(parentStitchLoc2.getRowNum()).getStitch(parentStitchLoc2.getStitchNum());
        assertEquals("Chain", parentStitch2.getStitchName());

    }

    @Test
    void testDc4thChHook() {
        CharStream input = CharStreams.fromString("dc in 4th ch from hook");
        CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
        CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.instructions();
        visitor.visit(tree);
        assertEquals(2, pattern.getRowCount());
        assertEquals(1, pattern.getRow(2).getStitchCount());
        assertEquals(1, pattern.getRow(2).getStitches().size());
        assertEquals("DoubleCrochet", pattern.getRow(2).getStitch(0).getStitchName());
        assertEquals(1, pattern.getRow(2).getStitch(0).getLoc().getStitchNum());
        StitchLoc parentStitch = pattern.getRow(2).getStitch(0).getParentStitch();
        assertEquals(1, parentStitch.getRowNum());
        assertEquals(7, parentStitch.getStitchNum());
    }

}
