package nl.petertillema.tibasic.parser;

import com.intellij.testFramework.ParsingTestCase;

public class TIBasicGrammarGoodExpressionTestCase extends ParsingTestCase {

    public TIBasicGrammarGoodExpressionTestCase() {
        super("good/", "basic", new TIBasicParserDefinition());
    }

    public void testExpressionPrecedence() {
        doTest(true);
    }

    public void testExpressionImpliedMultiplication() {
        doTest(true);
    }

    public void testExpressionOptionalRParen() {
        doTest(true);
    }

    public void testExpressionAnonymousListMatrix() {
        doTest(true);
    }

    public void testExpressionPowRightAssociative() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData/parser/";
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }
}
