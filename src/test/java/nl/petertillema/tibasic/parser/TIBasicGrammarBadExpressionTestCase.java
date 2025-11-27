package nl.petertillema.tibasic.parser;

import com.intellij.testFramework.ParsingTestCase;

public class TIBasicGrammarBadExpressionTestCase extends ParsingTestCase {

    public TIBasicGrammarBadExpressionTestCase() {
        super("bad/", "basic", new TIBasicParserDefinition());
    }

    public void testExpression() {
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
