package nl.petertillema.tibasic.inspections.controlFlow;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import nl.petertillema.tibasic.TIBasicMessageBundle;
import nl.petertillema.tibasic.psi.TIBasicGotoStatement;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicMenuOption;
import nl.petertillema.tibasic.psi.TIBasicMenuStatement;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.visitors.TIBasicCommandRecursiveVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR;

public final class TIBasicUnknownGotoTargetInspection extends LocalInspectionTool {

    private static final String ERROR_MESSAGE = TIBasicMessageBundle.message("inspection.unknown.goto.target.description");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        ArrayList<String> lblLabels = new ArrayList<>();

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
            public void visitMenuStatement(@NotNull TIBasicMenuStatement o) {
                List<TIBasicMenuOption> options = o.getMenuOptionList();
                for (TIBasicMenuOption option : options) {
                    if (option.getGotoName() != null && !lblLabels.contains(option.getGotoName().getText())) {
                        holder.registerProblem(option.getGotoName(), ERROR_MESSAGE, GENERIC_ERROR);
                    }
                }
            }

            @Override
            public void visitGotoStatement(@NotNull TIBasicGotoStatement o) {
                if (o.getGotoName() != null && !lblLabels.contains(o.getGotoName().getText())) {
                    holder.registerProblem(o.getGotoName(), ERROR_MESSAGE, GENERIC_ERROR);
                }
            }
        };
    }

}
