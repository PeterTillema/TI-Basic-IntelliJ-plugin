package nl.petertillema.tibasic.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.psi.TIBasicAssignmentTarget;
import nl.petertillema.tibasic.psi.TIBasicImpliedMulExpr;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.petertillema.tibasic.syntax.TIBasicSyntaxHighlighter.LIST_IDENTIFIER;

public final class TIBasicCustomListAnnotator implements Annotator {

    private static final String FIRST_DIGIT_PATTERN = "([A-Zθ]|theta)";
    private static final String OTHER_DIGIT_PATTERN = "([0-9A-Zθ]|theta)";
    private static final Pattern CUSTOM_LIST_WITH_L_PATTERN = Pattern.compile("\\|L" + FIRST_DIGIT_PATTERN + OTHER_DIGIT_PATTERN + "{0,4}");
    private static final Pattern CUSTOM_LIST_WITHOUT_L_PATTERN = Pattern.compile("^(?!L[1-6])" + FIRST_DIGIT_PATTERN + OTHER_DIGIT_PATTERN + "{1,4}");

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Quick filter before the pattern matching
        if ((element instanceof TIBasicImpliedMulExpr || element instanceof TIBasicLiteralExpr || element instanceof TIBasicAssignmentTarget) &&
                element.getText().contains("|L")) {
            var matcher = CUSTOM_LIST_WITH_L_PATTERN.matcher(element.getText());
            addFromMatcher(element, holder, matcher);
        }

        // Also check the target of an assignment, which doesn't necessarily need the |L prefix
        if (element instanceof TIBasicAssignmentTarget) {
            var matcher = CUSTOM_LIST_WITHOUT_L_PATTERN.matcher(element.getText());
            addFromMatcher(element, holder, matcher);
        }
    }

    private void addFromMatcher(PsiElement element, AnnotationHolder holder, Matcher matcher) {
        while (matcher.find()) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(TextRange.from(element.getTextRange().getStartOffset() + matcher.start(), matcher.end() - matcher.start()))
                    .textAttributes(LIST_IDENTIFIER)
                    .create();
        }
    }

}
