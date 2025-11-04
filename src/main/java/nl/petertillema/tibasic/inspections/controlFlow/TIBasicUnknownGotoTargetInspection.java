package nl.petertillema.tibasic.inspections.controlFlow;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.visitors.TIBasicCommandRecursiveVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR;

public final class TIBasicUnknownGotoTargetInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        var lblLabels = new ArrayList<String>();

        // Collect all the labels first
        holder.getFile().accept(new TIBasicCommandRecursiveVisitor() {
            @Override
            public void visitLblStatement(@NotNull TIBasicLblStatement o) {
                if (o.getLblName() != null) {
                    lblLabels.add(o.getLblName().getText());
                }
            }
        });

        return new TIBasicVisitor() {

            @Override
            public void visitGotoStatement(@NotNull TIBasicGotoStatement o) {
                if (o.getGotoName() != null && !lblLabels.contains(o.getGotoName().getText())) {
                    holder.registerProblem(o.getGotoName(), "Invalid Goto target", GENERIC_ERROR);
                }
            }
        };
    }

}
