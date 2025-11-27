package nl.petertillema.tibasic.parser;

import com.intellij.testFramework.ParsingTestCase;

public class TIBasicGrammarBadFlowTestCase extends ParsingTestCase {

    public TIBasicGrammarBadFlowTestCase() {
        super("bad/", "basic", new TIBasicParserDefinition());
    }

    public void testFlowISDS() {
        doTest(true);
    }

    public void testFlowIfSingle() {
        doTest(true);
    }

    public void testFlowIfThen() {
        doTest(true);
    }

    public void testFlowIfThenElse() {
        doTest(true);
    }

    public void testFlowWhile() {
        doTest(true);
    }

    public void testFlowRepeat() {
        doTest(true);
    }

    public void testFlowFor() {
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
