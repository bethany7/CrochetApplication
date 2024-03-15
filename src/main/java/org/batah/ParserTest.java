package org.batah;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.model.Pattern;
import org.batah.model.Row;


public class ParserTest {

  public static void main(String[] args) throws Exception {
    Pattern pattern = new Pattern();
    // create a CharStream that reads from standard input
    //CharStream input = CharStreams.fromStream(System.in);
    // create an input that reads from file
    //CharStream input = CharStreams.fromFileName("src/main/resources/testStrings.txt");
    // Give string as input
    CharStream input = CharStreams.fromString("dc in 4th ch from hook");

    CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
    CrochetPatternParserParser parser = new CrochetPatternParserParser(
        new CommonTokenStream(lexer));

    ParseTree tree = parser.instructions(); // begin parsing at instruction rule
    System.out.println(tree.toStringTree(parser)); // print LISP-style

    CrochetVisitor<String> eval = new CrochetVisitor<String>(pattern);
    eval.visit(tree);
    Row stitches = pattern.getRowList().getFirst();
    for (int i = 0; i < stitches.getStitchCount(); i++) {
      var stitch = stitches.getStitch(i);
      System.out.println(
          stitch.getStitchName() + " " + stitch.getLoc().getRowNum() + ":" + stitch.getLoc()
              .getStitchNum());
    }

  }
}