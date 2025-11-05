package nl.petertillema.tibasic.inspections.controlFlow;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import nl.petertillema.tibasic.TIBasicMessageBundle;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.visitors.TIBasicCommandRecursiveVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public final class TIBasicUnusedLabelInspection extends LocalInspectionTool {

    private static final String ERROR_MESSAGE = TIBasicMessageBundle.message("inspection.unused.label.description");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        var gotoLabels = new ArrayList<String>();

        // Collect all the goto's first
        holder.getFile().accept(new TIBasicCommandRecursiveVisitor() {
            @Override
            public void visitGotoStatement(@NotNull TIBasicGotoStatement o) {
                if (o.getGotoName() != null){
                    gotoLabels.add(o.getGotoName().getText());
                }
            }
        });

        return new TIBasicVisitor() {

            @Override
            public void visitLblStatement(@NotNull TIBasicLblStatement o) {
                if (o.getLblName() != null && !gotoLabels.contains(o.getLblName().getText())) {
                    holder.registerProblem(o, ERROR_MESSAGE, LIKE_UNUSED_SYMBOL, new RemoveLabelQuickFix());
                }
            }
        };
    }

    private static class RemoveLabelQuickFix implements LocalQuickFix {

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return TIBasicMessageBundle.message("inspection.unused.label.fix.family-name");
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            var element = descriptor.getPsiElement().getParent();

            // Eventually also delete the next newline
            if (element.getNextSibling() != null && element.getNextSibling().getNode().getElementType() == TIBasicTypes.CRLF) {
                element.getNextSibling().delete();
            }
            element.delete();
        }
    }

}
