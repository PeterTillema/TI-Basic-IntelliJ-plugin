package nl.petertillema.tibasic.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.psi.TIBasicGoto;
import nl.petertillema.tibasic.psi.TIBasicLbl;
import nl.petertillema.tibasic.psi.visitors.TIBasicCommandRecursiveVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.intellij.codeInspection.ProblemHighlightType.ERROR;

public class TIBasicUnknownGotoTargetInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        var gotos = new ArrayList<TIBasicGoto>();
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
            public void visitGoto(@NotNull TIBasicGoto o) {
                gotos.add(o);
            }

            @Override
            public void visitLbl(@NotNull TIBasicLbl o) {
                lblLabels.add(o.getLblName().getText());
            }
        };
    }

}
