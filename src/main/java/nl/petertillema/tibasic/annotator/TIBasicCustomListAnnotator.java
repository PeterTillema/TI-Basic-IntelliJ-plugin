package nl.petertillema.tibasic.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.psi.TIBasicAssignmentTarget;
import nl.petertillema.tibasic.psi.TIBasicCustomList;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.petertillema.tibasic.syntax.TIBasicSyntaxHighlighter.LIST_IDENTIFIER;

public final class TIBasicCustomListAnnotator implements Annotator {

    private static final String FIRST_DIGIT_PATTERN = "([A-Zθ]|theta)";
    private static final String OTHER_DIGIT_PATTERN = "([0-9A-Zθ]|theta)";
    private static final Pattern CUSTOM_LIST_WITHOUT_L_PATTERN = Pattern.compile("^(?!L[1-6])" + FIRST_DIGIT_PATTERN + OTHER_DIGIT_PATTERN + "{1,4}");

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TIBasicCustomList) {
            addAnnotation(holder, element.getTextRange());
        }

        if (element instanceof TIBasicAssignmentTarget) {
            Matcher matcher = CUSTOM_LIST_WITHOUT_L_PATTERN.matcher(element.getText());
            if (matcher.matches()) {
                addAnnotation(holder, element.getTextRange());
            }
        }
    }

    private void addAnnotation(AnnotationHolder holder, TextRange range) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(range)
                .textAttributes(LIST_IDENTIFIER)
                .create();
    }

}
