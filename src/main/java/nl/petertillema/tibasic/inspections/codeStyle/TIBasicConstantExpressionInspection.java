package nl.petertillema.tibasic.inspections.codeStyle;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import nl.petertillema.tibasic.TIBasicMessageBundle;
import nl.petertillema.tibasic.psi.TIBasicAndExpr;
import nl.petertillema.tibasic.psi.TIBasicDivExpr;
import nl.petertillema.tibasic.psi.TIBasicEqExpr;
import nl.petertillema.tibasic.psi.TIBasicExpr;
import nl.petertillema.tibasic.psi.TIBasicGeExpr;
import nl.petertillema.tibasic.psi.TIBasicGtExpr;
import nl.petertillema.tibasic.psi.TIBasicLeExpr;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicLtExpr;
import nl.petertillema.tibasic.psi.TIBasicMinusExpr;
import nl.petertillema.tibasic.psi.TIBasicMulExpr;
import nl.petertillema.tibasic.psi.TIBasicNeExpr;
import nl.petertillema.tibasic.psi.TIBasicOrExpr;
import nl.petertillema.tibasic.psi.TIBasicPlusExpr;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.TIBasicXorExpr;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BinaryOperator;

import static nl.petertillema.tibasic.psi.TIBasicUtil.createFromText;

public final class TIBasicConstantExpressionInspection extends LocalInspectionTool {

    private static final String ERROR_MESSAGE = TIBasicMessageBundle.message("inspection.constant.expression.description");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new TIBasicVisitor() {
            @Override
            public void visitPlusExpr(@NotNull TIBasicPlusExpr o) {
                super.visitPlusExpr(o);
                this.checkOperator(o, o.getExprList(), BigDecimal::add);
            }

            @Override
            public void visitMinusExpr(@NotNull TIBasicMinusExpr o) {
                super.visitMinusExpr(o);
                this.checkOperator(o, o.getExprList(), BigDecimal::subtract);
            }

            @Override
            public void visitMulExpr(@NotNull TIBasicMulExpr o) {
                super.visitMulExpr(o);
                this.checkOperator(o, o.getExprList(), BigDecimal::multiply);
            }

            @Override
            public void visitDivExpr(@NotNull TIBasicDivExpr o) {
                super.visitDivExpr(o);
                this.checkOperator(o, o.getExprList(), BigDecimal::divide);
            }

            @Override
            public void visitEqExpr(@NotNull TIBasicEqExpr o) {
                super.visitEqExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(num2) == 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            @Override
            public void visitNeExpr(@NotNull TIBasicNeExpr o) {
                super.visitNeExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(num2) != 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            @Override
            public void visitLtExpr(@NotNull TIBasicLtExpr o) {
                super.visitLtExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(num2) < 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            @Override
            public void visitLeExpr(@NotNull TIBasicLeExpr o) {
                super.visitLeExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(num2) <= 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            @Override
            public void visitGtExpr(@NotNull TIBasicGtExpr o) {
                super.visitGtExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(num2) > 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            @Override
            public void visitGeExpr(@NotNull TIBasicGeExpr o) {
                super.visitGeExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(num2) >= 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            @Override
            public void visitOrExpr(@NotNull TIBasicOrExpr o) {
                super.visitOrExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(BigDecimal.ZERO) != 0 || num2.compareTo(BigDecimal.ZERO) != 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            @Override
            public void visitXorExpr(@NotNull TIBasicXorExpr o) {
                super.visitXorExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(BigDecimal.ZERO) != 0 ^ num2.compareTo(BigDecimal.ZERO) != 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            @Override
            public void visitAndExpr(@NotNull TIBasicAndExpr o) {
                super.visitAndExpr(o);
                this.checkOperator(o, o.getExprList(), (num1, num2) ->
                        num1.compareTo(BigDecimal.ZERO) != 0 && num2.compareTo(BigDecimal.ZERO) != 0 ? BigDecimal.ONE : BigDecimal.ZERO);
            }

            private void checkOperator(PsiElement element, List<TIBasicExpr> expressions, BinaryOperator<BigDecimal> evaluator) {
                if (expressions.size() != 2) return;

                if (!(expressions.getFirst() instanceof TIBasicLiteralExpr l1)) return;
                if (!(expressions.getLast() instanceof TIBasicLiteralExpr l2)) return;

                if (l1.getFirstChild().getNode().getElementType() == TIBasicTypes.NUMBER && l2.getFirstChild().getNode().getElementType() == TIBasicTypes.NUMBER) {
                    holder.registerProblem(element, ERROR_MESSAGE, new ReplaceByConstantQuickFix(expressions.getFirst(), expressions.getLast(), evaluator));
                }
            }
        };
    }

    private static class ReplaceByConstantQuickFix implements LocalQuickFix {

        private final PsiElement first;
        private final PsiElement second;
        private final BinaryOperator<BigDecimal> evaluator;

        private ReplaceByConstantQuickFix(PsiElement first, PsiElement second, BinaryOperator<BigDecimal> evaluator) {
            this.first = first;
            this.second = second;
            this.evaluator = evaluator;
        }

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return TIBasicMessageBundle.message("inspection.constant.expression.fix.family.name");
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            var result = evaluator.apply(new BigDecimal(first.getText()), new BigDecimal(second.getText()));
            var tempBasicFile = createFromText(project, result.toString());
            var literalExpr = PsiTreeUtil.findChildOfType(tempBasicFile, TIBasicLiteralExpr.class);
            if (literalExpr == null) return;

            descriptor.getPsiElement().replace(literalExpr);
        }
    }

}
