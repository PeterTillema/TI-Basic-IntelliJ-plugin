package nl.petertillema.tibasic.annotator;

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
import nl.petertillema.tibasic.TIBasicMessageBundle;
import nl.petertillema.tibasic.psi.TIBasicAssignmentStatement;
import nl.petertillema.tibasic.psi.TIBasicAssignmentTarget;
import nl.petertillema.tibasic.psi.TIBasicCommandStatement;
import nl.petertillema.tibasic.psi.TIBasicExprStatement;
import nl.petertillema.tibasic.psi.TIBasicForInitializer;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicStatement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static nl.petertillema.tibasic.psi.TIBasicUtil.createFromText;

public final class TIBasicUnnecessaryQuoteAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        var elementToCheck = getMainElementToCheck(element);
        if (elementToCheck == null) return;

        var text = elementToCheck.getText();
        if (text.charAt(text.length() - 1) == '"') {
            // Check if the statement has whitespace and a comment attached
            var statement = getParentOfType(elementToCheck, TIBasicStatement.class);
            if (hasCommentSiblingAtSameLine(statement)) return;

            var endOffset = elementToCheck.getTextRange().getEndOffset();

            holder.newAnnotation(HighlightSeverity.INFORMATION, TIBasicMessageBundle.message("annotator.unnecessary.quote.description"))
                    .range(TextRange.from(endOffset - 1, 1))
                    .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                    .withFix(new RemoveUnnecessaryQuoteQuickFix(endOffset - 1))
                    .create();
        }
    }

    private boolean hasCommentSiblingAtSameLine(PsiElement element) {
        while (element != null) {
            if (element.getNode().getElementType() == TIBasicTypes.CRLF) return false;
            if (element.getNode().getElementType() == TIBasicTypes.COMMENT) return true;
            element = element.getNextSibling();
        }
        return false;
    }

    private @Nullable PsiElement getMainElementToCheck(PsiElement element) {
        if (element instanceof TIBasicAssignmentStatement assignmentStatement) return assignmentStatement.getExpr();
        if (element instanceof TIBasicExprStatement exprStatement) return exprStatement.getExpr();
        if (element instanceof TIBasicCommandStatement ||
                element instanceof TIBasicAssignmentTarget ||
                element instanceof TIBasicForInitializer) return element;
        return null;
    }

    private static class RemoveUnnecessaryQuoteQuickFix extends BaseIntentionAction {

        private final int offset;

        private RemoveUnnecessaryQuoteQuickFix(int startOffset) {
            this.offset = startOffset;
        }

        @Override
        public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
            return true;
        }

        @Override
        public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
            var element = file.findElementAt(this.offset);
            if (element == null) return;

            String text = element.getText();
            var tempBasicFile = createFromText(project, text.substring(0, text.length() - 1));
            var literalExpr = PsiTreeUtil.findChildOfType(tempBasicFile, TIBasicLiteralExpr.class);
            if (literalExpr == null) return;
            var newElement = literalExpr.getFirstChild();

            element.replace(newElement);
        }

        @Override
        public @NotNull @IntentionName String getText() {
            return TIBasicMessageBundle.message("annotator.unnecessary.quote.fix.text");
        }

        @Override
        public @NotNull @IntentionFamilyName String getFamilyName() {
            return TIBasicMessageBundle.message("annotator.unnecessary.quote.fix.family.name");
        }
    }

}
