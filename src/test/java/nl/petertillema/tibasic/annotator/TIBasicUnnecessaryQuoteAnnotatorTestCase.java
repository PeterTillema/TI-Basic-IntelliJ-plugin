package nl.petertillema.tibasic.annotator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TIBasicUnnecessaryQuoteAnnotatorTestCase extends BasePlatformTestCase {

    @Override
    protected @NonNls @NotNull String getTestDataPath() {
        return "src/test/testData/annotator/";
    }

    public void testAnnotator() {
        myFixture.configureByFile("UnnecessaryQuoteSampleCode.basic");
        myFixture.checkHighlighting(true, true, true);

        var quickFixes = myFixture.getAllQuickFixes();
        assertEquals(1, quickFixes.size());

        // Run all quick fixes
        WriteCommandAction.writeCommandAction(getProject()).run(() -> {
            quickFixes.forEach(quickFix -> {
                quickFix.invoke(getProject(), myFixture.getEditor(), myFixture.getFile());
            });
        });
        myFixture.checkResultByFile("UnnecessaryQuoteSampleCode_Right.basic");
    }

}
