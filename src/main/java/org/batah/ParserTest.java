package org.batah;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.batah.CrochetPatternParserLexer;
import org.batah.CrochetPatternParserParser;

public class ParserTest {
    public static void main(String[] args) throws Exception {
        // create a CharStream that reads from standard input
        CharStream input = CharStreams.fromStream(System.in);

        CrochetPatternParserLexer lexer = new CrochetPatternParserLexer(input);
        CrochetPatternParserParser parser = new CrochetPatternParserParser(new CommonTokenStream(lexer));

        ParseTree tree = parser.instruction(); // begin parsing at instruction rule
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}