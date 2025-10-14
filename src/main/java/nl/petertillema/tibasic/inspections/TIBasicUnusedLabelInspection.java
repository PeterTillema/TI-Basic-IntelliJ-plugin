package nl.petertillema.tibasic.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.visitors.TIBasicCommandRecursiveVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class TIBasicUnusedLabelInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        var gotoLabels = new ArrayList<String>();

        // Collect all the goto's first
        holder.getFile().accept(new TIBasicCommandRecursiveVisitor() {
            @Override
            public void visitGotoStatement(@NotNull TIBasicGotoStatement o) {
                gotoLabels.add(o.getGotoName().getText());
            }
        });

        return new TIBasicVisitor() {

            @Override
            public void visitLblStatement(@NotNull TIBasicLblStatement o) {
                if (!gotoLabels.contains(o.getLblName().getText())) {
                    holder.registerProblem(o, "Unused label", LIKE_UNUSED_SYMBOL, new RemoveLabelQuickFix());
                }
            }
        };
    }

    private static class RemoveLabelQuickFix implements LocalQuickFix {

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return "Remove label";
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            descriptor.getPsiElement().delete();
        }
    }

}
