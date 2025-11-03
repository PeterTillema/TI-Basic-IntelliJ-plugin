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
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import nl.petertillema.tibasic.psi.TIBasicAssignmentStatement;
import nl.petertillema.tibasic.psi.TIBasicAssignmentTarget;
import nl.petertillema.tibasic.psi.TIBasicCommandStatement;
import nl.petertillema.tibasic.psi.TIBasicExprStatement;
import nl.petertillema.tibasic.psi.TIBasicForInitializer;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.psi.util.PsiTreeUtil.getDeepestLast;
import static com.intellij.psi.util.PsiTreeUtil.nextLeaf;
import static com.intellij.psi.util.PsiTreeUtil.prevLeaf;

public final class TIBasicUnnecessaryParenthesisAnnotator implements Annotator {

    private static final List<IElementType> CLOSING_PARENTHESIS_TYPES = List.of(
            TIBasicTypes.RPAREN,
            TIBasicTypes.RBRACKET,
            TIBasicTypes.RCURLY
    );

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        var elementToCheck = getMainElementToCheck(element);
        if (elementToCheck == null) return;

        var lastChild = getDeepestLast(elementToCheck);
        var originalLastChild = lastChild;
        var firstToDelete = lastChild;
        while (lastChild != null && CLOSING_PARENTHESIS_TYPES.contains(lastChild.getNode().getElementType())) {
            firstToDelete = lastChild;
            lastChild = prevLeaf(lastChild);
        }

        if (firstToDelete != originalLastChild) {
            var startOffset = firstToDelete.getTextRange().getStartOffset();
            holder.newAnnotation(HighlightSeverity.INFORMATION, "Unnecessary closing parenthesis")
                    .range(TextRange.from(startOffset, originalLastChild.getTextRange().getEndOffset() - startOffset))
                    .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                    .withFix(new RemoveUnnecessaryParenthesisQuickFix(firstToDelete, originalLastChild))
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

        private final @NotNull PsiElement firstToDelete;
        private final @NotNull PsiElement lastToDelete;

        private RemoveUnnecessaryParenthesisQuickFix(@NotNull PsiElement firstToDelete, @NotNull PsiElement lastToDelete) {
            this.firstToDelete = firstToDelete;
            this.lastToDelete = lastToDelete;
        }

        @Override
        public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
            return true;
        }

        @Override
        public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
            var current = firstToDelete;
            while (current != null && current.getTextRange().getStartOffset() <= lastToDelete.getTextRange().getStartOffset()) {
                var next = nextLeaf(current);
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
