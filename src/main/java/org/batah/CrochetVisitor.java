package org.batah;

public class CrochetVisitor<String> extends CrochetPatternParserBaseVisitor<String> {

    @Override
    public String visitInstructions(CrochetPatternParserParser.InstructionsContext ctx) {
        return super.visitInstructions(ctx);
    }

    @Override
    public String visitInstruction(CrochetPatternParserParser.InstructionContext ctx) {
        System.out.println(ctx.getText());
        return super.visitInstruction(ctx);
    }

}
