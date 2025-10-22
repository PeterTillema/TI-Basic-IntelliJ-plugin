package nl.petertillema.tibasic.inspections;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import nl.petertillema.tibasic.psi.TIBasicAssignmentStatement;
import nl.petertillema.tibasic.psi.TIBasicAssignmentTarget;
import nl.petertillema.tibasic.psi.TIBasicCommandStatement;
import nl.petertillema.tibasic.psi.TIBasicExprStatement;
import nl.petertillema.tibasic.psi.TIBasicForInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TIBasicUnnecessaryParenthesisAnnotator implements Annotator {

    private static final List<Character> CLOSING_PARENTHESIS = List.of(')', ']', '}');
    private static final List<Character> WHITE_SPACE = List.of(' ', '\t', '\f');

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        var elementToCheck = getMainElementToCheck(element);
        if (elementToCheck == null) return;

        var text = elementToCheck.getText();
        var endIndex = text.length() - 1;
        var startEndIndex = endIndex;

        while (endIndex >= 0 && (CLOSING_PARENTHESIS.contains(text.charAt(endIndex)) || WHITE_SPACE.contains(text.charAt(endIndex)))) {
            endIndex--;
        }

        if (endIndex != startEndIndex) {
            var startOffset = elementToCheck.getTextRange().getStartOffset();

            holder.newAnnotation(HighlightSeverity.INFORMATION, "Unnecessary closing parenthesis")
                    .range(TextRange.from(startOffset + endIndex + 1, startEndIndex - endIndex))
                    .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                    .withFix(new RemoveUnnecessaryParenthesisQuickFix(startOffset + endIndex + 1, startOffset + startEndIndex))
                    .create();
        }
    }

    private @Nullable PsiElement getMainElementToCheck(PsiElement element) {
        if (element instanceof TIBasicAssignmentStatement assignmentStatement) return assignmentStatement.getExpr();
        if (element instanceof TIBasicExprStatement exprStatement) return exprStatement.getExpr();
        if (element instanceof TIBasicCommandStatement ||
                element instanceof TIBasicAssignmentTarget ||
                element instanceof TIBasicForInitializer) return element;
        return null;
    }

    private static class RemoveUnnecessaryParenthesisQuickFix extends BaseIntentionAction {

        private final int startOffset;
        private final int endOffset;

        private RemoveUnnecessaryParenthesisQuickFix(int startOffset, int endOffset) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        @Override
        public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
            return true;
        }

        @Override
        public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
            var start = file.findElementAt(this.startOffset);
            var end = file.findElementAt(this.endOffset);

            if (start == null || end == null) return;

            var current = start;
            while (current != null && current.getTextRange().getStartOffset() <= end.getTextRange().getStartOffset()) {
                var next = PsiTreeUtil.nextLeaf(current);
                current.delete();
                current = next;
            }
        }

        @Override
        public @NotNull @IntentionName String getText() {
            return "Remove unnecessary parenthesis";
        }

        @Override
        public @NotNull @IntentionFamilyName String getFamilyName() {
            return "Remove unnecessary parenthesis";
        }
    }

}
