package nl.petertillema.tibasic.annotator;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import nl.petertillema.tibasic.TIBasicMessageBundle;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.util.PsiTreeUtil.nextLeaf;
import static nl.petertillema.tibasic.psi.TIBasicUtil.createFromText;

public final class TIBasicUnnecessaryQuoteAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof TIBasicLiteralExpr literalExpr)) return;
        String text = element.getText();
        if (text.length() < 2 || text.charAt(text.length() - 1) != '"') return;

        ASTNode node = literalExpr.getFirstChild().getNode();
        if (node.getElementType() != TIBasicTypes.STRING) return;

        PsiElement nextLeaf = nextLeaf(element);
        while (nextLeaf != null) {
            if (nextLeaf instanceof PsiComment) return;
            IElementType elementType = nextLeaf.getNode().getElementType();
            if (elementType == TIBasicTypes.CRLF || elementType == TIBasicTypes.STO) break;
            if (!(nextLeaf instanceof PsiWhiteSpace)) return;
            nextLeaf = nextLeaf(nextLeaf);
        }

        int endOffset = element.getTextRange().getEndOffset();
        holder.newAnnotation(HighlightSeverity.INFORMATION, TIBasicMessageBundle.message("annotator.unnecessary.quote.description"))
                .range(TextRange.from(endOffset - 1, 1))
                .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                .withFix(new RemoveUnnecessaryQuoteQuickFix(endOffset - 1))
                .create();
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
            PsiElement element = file.findElementAt(this.offset);
            if (element == null) return;

            String text = element.getText();
            TIBasicFile tempBasicFile = createFromText(project, text.substring(0, text.length() - 1));
            TIBasicLiteralExpr literalExpr = PsiTreeUtil.findChildOfType(tempBasicFile, TIBasicLiteralExpr.class);
            if (literalExpr == null) return;
            PsiElement newElement = literalExpr.getFirstChild();

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
