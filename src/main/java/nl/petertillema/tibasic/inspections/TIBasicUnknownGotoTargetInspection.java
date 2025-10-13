package nl.petertillema.tibasic.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.visitors.TIBasicCommandRecursiveVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.intellij.codeInspection.ProblemHighlightType.ERROR;

public class TIBasicUnknownGotoTargetInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        var gotos = new ArrayList<TIBasicGotoStatement>();
        var lblLabels = new ArrayList<String>();

        return new TIBasicCommandRecursiveVisitor() {

            @Override
            public void visitFile(@NotNull PsiFile file) {
                super.visitFile(file);

                for (var goto1 : gotos) {
                    if (!lblLabels.contains(goto1.getLblName().getText())) {
                        holder.registerProblem(goto1.getLblName(), "Invalid Goto target", ERROR);
                    }
                }
            }

            @Override
            public void visitGotoStatement(@NotNull TIBasicGotoStatement o) {
                gotos.add(o);
            }

            @Override
            public void visitLblStatement(@NotNull TIBasicLblStatement o) {
                lblLabels.add(o.getLblName().getText());
            }
        };
    }

}
