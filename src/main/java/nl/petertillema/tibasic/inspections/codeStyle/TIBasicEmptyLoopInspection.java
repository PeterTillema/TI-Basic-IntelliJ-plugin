package nl.petertillema.tibasic.inspections.codeStyle;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import nl.petertillema.tibasic.TIBasicMessageBundle;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicIfStatement;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicRepeatStatement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.TIBasicWhileStatement;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfType;

public final class TIBasicEmptyLoopInspection extends LocalInspectionTool {

    private static final String ERROR_MESSAGE = TIBasicMessageBundle.message("inspection.TIBasic.empty.loop.display.description");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new TIBasicVisitor() {
            @Override
            public void visitWhileStatement(@NotNull TIBasicWhileStatement o) {
                if (containsNoneExprFunctionCall(o.getExpr()) && o.getStatementList().isEmpty()) {
                    holder.registerProblem(o.getFirstChild(), ERROR_MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new RemoveMainLoopQuickFix());
                }
            }

            @Override
            public void visitRepeatStatement(@NotNull TIBasicRepeatStatement o) {
                if (containsNoneExprFunctionCall(o.getExpr()) && o.getStatementList().isEmpty()) {
                    holder.registerProblem(o.getFirstChild(), ERROR_MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new RemoveMainLoopQuickFix());
                }
            }

            @Override
            public void visitForStatement(@NotNull TIBasicForStatement o) {
                if (o.getStatementList().isEmpty()) {
                    holder.registerProblem(o.getFirstChild(), ERROR_MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new RemoveMainLoopQuickFix());
                }
            }

            @Override
            public void visitIfStatement(@NotNull TIBasicIfStatement o) {
                // "Then" without a body, and no "Else"
                if (o.getThenStatement() != null && o.getThenStatement().getStatementList().isEmpty() && o.getElseStatement() == null) {
                    holder.registerProblem(o, "Empty body", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new RemoveMainLoopQuickFix());
                }

                // "Else" without a body
                if (o.getThenStatement() != null && o.getElseStatement() != null && o.getElseStatement().getStatementList().isEmpty()) {
                    holder.registerProblem(o.getElseStatement(), "Empty body", ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new RemoveElseStatementQuickFix());
                }

                // todo: "Then" without a body but "Else" with a body. That means the "Then" can be removed, but the condition of the
                //  main If statement needs to be flipped. This is harder than expected: how to replace "If Ans"? If Ans is a string,
                //  we need something like "If not(length(Ans". If Ans is a list, we might need "If not(dim(Ans". Finally, if Ans is
                //  a number, a simple "If not(Ans" would be sufficient. And this is only for "Ans", but in reality the check might be
                //  more complex. For example, an EQ_EXPR might be replaced by a NE_EXPR etc. Leave it out for now, maybe in the future
                //  with advanced checks it might be possible to do that.
            }

            private boolean containsNoneExprFunctionCall(PsiElement element) {
                Predicate<PsiElement> isExprFunction = el -> el.getNode().getElementType() == TIBasicTypes.EXPR_FUNCTIONS_NO_ARGS;

                if (isExprFunction.test(element.getFirstChild())) return false;

                var literalExprs = findChildrenOfType(element, TIBasicLiteralExpr.class);
                return literalExprs.stream()
                        .map(PsiElement::getFirstChild)
                        .noneMatch(isExprFunction);
            }
        };
    }

    private static class RemoveMainLoopQuickFix implements LocalQuickFix {

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return "Remove loop";
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

    private static class RemoveElseStatementQuickFix implements LocalQuickFix {

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return "Remove loop";
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            var lastChild = descriptor.getPsiElement().getLastChild();
            if (lastChild == null) {
                // If there is no child at all, delete it immediately
                descriptor.getPsiElement().delete();
            } else if (lastChild.getNode().getElementType() == TIBasicTypes.END) {
                // If the last child is an "End", replace the Else statement with a single "End"
                descriptor.getPsiElement().replace(lastChild);
            } else {
                // If the last child is anything else, delete the element entirely
                descriptor.getPsiElement().delete();
            }
        }
    }

}
