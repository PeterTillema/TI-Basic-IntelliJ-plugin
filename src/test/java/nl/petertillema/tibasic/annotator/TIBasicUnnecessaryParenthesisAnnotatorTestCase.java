package nl.petertillema.tibasic.annotator;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TIBasicUnnecessaryParenthesisAnnotatorTestCase extends BasePlatformTestCase {

    @Override
    protected @NonNls @NotNull String getTestDataPath() {
        return "src/test/testData/annotator/";
    }

    public void testAnnotator() {
        myFixture.configureByFile("UnnecessaryParenthesisSampleCode.basic");
        myFixture.checkHighlighting(true, true, true);

        List<IntentionAction> quickFixes = myFixture.getAllQuickFixes();
        assertEquals(4, quickFixes.size());

        // Run all quick fixes
        WriteCommandAction.writeCommandAction(getProject()).run(() -> {
            quickFixes.forEach(quickFix -> {
                quickFix.invoke(getProject(), myFixture.getEditor(), myFixture.getFile());
            });
        });
        myFixture.checkResultByFile("UnnecessaryParenthesisSampleCode_Right.basic");
    }

}
