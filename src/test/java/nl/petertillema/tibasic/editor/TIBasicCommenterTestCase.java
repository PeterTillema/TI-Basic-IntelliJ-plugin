package nl.petertillema.tibasic.editor;

import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TIBasicCommenterTestCase extends LightPlatformCodeInsightTestCase {

    @Override
    protected @NonNls @NotNull String getTestDataPath() {
        return "src/test/testData/editor/";
    }

    public void testCommenter() {
        configureByFile("CommenterSampleCode.basic");
        lineComment();
        lineComment();
        lineComment();
        checkResultByFile("CommenterSampleCode_Right.basic");
    }

}
