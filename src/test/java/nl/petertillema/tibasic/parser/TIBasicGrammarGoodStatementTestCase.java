package nl.petertillema.tibasic.parser;

import com.intellij.testFramework.ParsingTestCase;

public class TIBasicGrammarGoodStatementTestCase extends ParsingTestCase {

    public TIBasicGrammarGoodStatementTestCase() {
        super("good/", "basic", new TIBasicParserDefinition());
    }

    public void testStatementDelvar() {
        doTest(true);
    }

    public void testStatementGoto() {
        doTest(true);
    }

    public void testStatementLbl() {
        doTest(true);
    }

    public void testStatementMenu() {
        doTest(true);
    }

    public void testStatementPlot() {
        doTest(true);
    }

    public void testStatementDisp() {
        doTest(true);
    }
    
    public void testStatementCommand() {
        doTest(true);
    }
    
    public void testStatementAsm() {
        doTest(true);
    }
    
    public void testStatementPrgm() {
        doTest(true);
    }
    
    public void testStatementExpr() {
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
