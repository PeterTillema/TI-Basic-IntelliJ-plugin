package nl.petertillema.tibasic.editor;

import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TIBasicTrailingSpacesTestCase extends LightPlatformCodeInsightTestCase {

    @Override
    protected @NonNls @NotNull String getTestDataPath() {
        return "src/test/testData/editor/";
    }

    public void testTrailingSpacesAndNewLines() {
        configureByFile("TrailingSpacesAndNewLinesSampleCode.basic");
        type('\n');
        FileDocumentManager.getInstance().saveDocument(getDocument(getFile()));
        checkResultByFile("TrailingSpacesAndNewLinesSampleCode_Right.basic");
    }

}
