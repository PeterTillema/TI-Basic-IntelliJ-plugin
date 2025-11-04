package nl.petertillema.tibasic.inspections.controlFlow;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import nl.petertillema.tibasic.TIBasicMessageBundle;
import nl.petertillema.tibasic.psi.TIBasicAnonymousList;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import org.jetbrains.annotations.NotNull;

public final class TIBasicTooMuchListArgumentsInspection extends LocalInspectionTool {

    private static final String ERROR_MESSAGE = TIBasicMessageBundle.message("inspection.TIBasic.too.much.arguments.display.description");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new TIBasicVisitor() {
            @Override
            public void visitAnonymousList(@NotNull TIBasicAnonymousList o) {
                super.visitAnonymousList(o);
                if (o.getExprList().size() > 999) {
                    holder.registerProblem(o, ERROR_MESSAGE, ProblemHighlightType.WARNING);
                }
            }
        };
    }

}
