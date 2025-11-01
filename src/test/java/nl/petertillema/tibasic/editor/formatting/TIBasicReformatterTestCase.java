package nl.petertillema.tibasic.editor.formatting;

import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TIBasicReformatterTestCase extends LightPlatformCodeInsightTestCase {

    @Override
    protected @NonNls @NotNull String getTestDataPath() {
        return "src/test/testData/editor/formatting/";
    }

    public void testReformat() {
        configureByFile("IndentationWeirdSpacingSample.basic");
        setTabIndent();
        executeAction(IdeActions.ACTION_EDITOR_REFORMAT);
        checkResultByFile("IndentationWeirdSpacingSample_Right.basic");
    }

    private void setTabIndent() {
        var indentOptions = CodeStyle.getLanguageSettings(getFile()).getIndentOptions();
        if (indentOptions != null) {
            indentOptions.USE_TAB_CHARACTER = true;
        }
    }

}
