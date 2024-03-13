import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.CrochetPatternParserLexer;
import org.batah.CrochetPatternParserParser;
import org.batah.CrochetVisitor;
import org.batah.model.Pattern;
import org.batah.Unparser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UnparseTests {

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
  void testUnparse() {
    CharStream input = CharStreams.fromString("1 dc, 1 tr, 2 dc");
    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));
    ParseTree tree = parser.instructions();
    visitor.visit(tree);
    Unparser unparser = new Unparser(pattern);
    System.out.println(unparser.unparseRow(pattern.getRow(2)));

  }

}