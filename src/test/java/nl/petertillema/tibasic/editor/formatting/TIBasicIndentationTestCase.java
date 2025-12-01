package nl.petertillema.tibasic.editor.formatting;

import com.intellij.application.options.CodeStyle;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TIBasicIndentationTestCase extends LightPlatformCodeInsightTestCase {

    @Override
    protected @NonNls @NotNull String getTestDataPath() {
        return "src/test/testData/editor/formatting/";
    }

    public void testIndentEmptyLoopWithEndWithoutStatement() {
        testDoIndent("IndentationEmptyLoopWithEndWithoutStatement");
    }

    public void testIndentEmptyLoopWithEndWithStatement1() {
        testDoIndent("IndentationEmptyLoopWithEndWithStatement1");
    }

    public void testIndentEmptyLoopWithEndWithStatement2() {
        testDoIndent("IndentationEmptyLoopWithEndWithStatement2");
    }

    public void testIndentEmptyLoopWithoutEndWithoutStatement() {
        testDoIndent("IndentationEmptyLoopWithoutEndWithoutStatement");
    }

    public void testIndentEmptyLoopWithoutEndWithStatement1() {
        testDoIndent("IndentationEmptyLoopWithoutEndWithStatement1");
    }

    public void testIndentEmptyLoopWithoutEndWithStatement2() {
        testDoIndent("IndentationEmptyLoopWithoutEndWithStatement2");
    }

    public void testDedentEndAfterEnter() {
        testDoIndent("IndentationDedentEnd");
    }

    public void testDedentElseAfterEnter() {
        testDoIndent("IndentationDedentElse");
    }

    private void testDoIndent(String filename) {
        configureByFile(filename + ".basic");
        setTabIndent();
        type('\n');
        checkResultByFile(filename + "_Right.basic");
    }

    private void setTabIndent() {
        CommonCodeStyleSettings.IndentOptions indentOptions = CodeStyle.getLanguageSettings(getFile()).getIndentOptions();
        if (indentOptions != null) {
            indentOptions.USE_TAB_CHARACTER = true;
        }
    }
}