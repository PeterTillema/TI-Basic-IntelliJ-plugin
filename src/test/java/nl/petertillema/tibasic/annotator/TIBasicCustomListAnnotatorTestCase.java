package nl.petertillema.tibasic.annotator;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TIBasicCustomListAnnotatorTestCase extends BasePlatformTestCase {

    @Override
    protected @NonNls @NotNull String getTestDataPath() {
        return "src/test/testData/annotator/";
    }

    public void testAnnotator() {
        myFixture.configureByFile("CustomListSampleCode.basic");
        myFixture.checkHighlighting(true, true, true);
    }

}
