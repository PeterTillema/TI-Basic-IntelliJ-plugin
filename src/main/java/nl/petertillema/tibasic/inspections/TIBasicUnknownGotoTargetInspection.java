package nl.petertillema.tibasic.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.visitors.TIBasicCommandRecursiveVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.intellij.codeInspection.ProblemHighlightType.ERROR;

public class TIBasicUnknownGotoTargetInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        var lblLabels = new ArrayList<String>();

        // Collect all the labels first
        holder.getFile().accept(new TIBasicCommandRecursiveVisitor() {
            @Override
            public void visitLblStatement(@NotNull TIBasicLblStatement o) {
                lblLabels.add(o.getLblName().getText());
            }
        });

        return new TIBasicVisitor() {

            @Override
            public void visitGotoStatement(@NotNull TIBasicGotoStatement o) {
                if (!lblLabels.contains(o.getLblName().getText())) {
                    holder.registerProblem(o.getLblName(), "Invalid Goto target", ERROR);
                }
            }
        };
    }

}
