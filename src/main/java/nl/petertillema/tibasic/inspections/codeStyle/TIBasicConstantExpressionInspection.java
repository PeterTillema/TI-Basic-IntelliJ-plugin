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
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicAndExpr;
import nl.petertillema.tibasic.psi.TIBasicDivExpr;
import nl.petertillema.tibasic.psi.TIBasicEqExpr;
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
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static nl.petertillema.tibasic.psi.TIBasicUtil.createFromText;

public final class TIBasicConstantExpressionInspection extends LocalInspectionTool {

    private static final String ERROR_MESSAGE = TIBasicMessageBundle.message("inspection.constant.expression.description");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new TIBasicVisitor() {
            @Override
            public void visitPlusExpr(@NotNull TIBasicPlusExpr o) {
                super.visitPlusExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitMinusExpr(@NotNull TIBasicMinusExpr o) {
                super.visitMinusExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitMulExpr(@NotNull TIBasicMulExpr o) {
                super.visitMulExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitDivExpr(@NotNull TIBasicDivExpr o) {
                super.visitDivExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitEqExpr(@NotNull TIBasicEqExpr o) {
                super.visitEqExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitNeExpr(@NotNull TIBasicNeExpr o) {
                super.visitNeExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitLtExpr(@NotNull TIBasicLtExpr o) {
                super.visitLtExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitLeExpr(@NotNull TIBasicLeExpr o) {
                super.visitLeExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitGtExpr(@NotNull TIBasicGtExpr o) {
                super.visitGtExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitGeExpr(@NotNull TIBasicGeExpr o) {
                super.visitGeExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitOrExpr(@NotNull TIBasicOrExpr o) {
                super.visitOrExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitXorExpr(@NotNull TIBasicXorExpr o) {
                super.visitXorExpr(o);
                this.checkOperator(o);
            }

            @Override
            public void visitAndExpr(@NotNull TIBasicAndExpr o) {
                super.visitAndExpr(o);
                this.checkOperator(o);
            }

            private void checkOperator(PsiElement element) {
                if (element.getChildren().length != 2) return;
                if (!(element.getFirstChild() instanceof TIBasicLiteralExpr l1)) return;
                if (!(element.getLastChild() instanceof TIBasicLiteralExpr l2)) return;

                if (l1.getFirstChild().getNode().getElementType() == TIBasicTypes.NUMBER && l2.getFirstChild().getNode().getElementType() == TIBasicTypes.NUMBER) {
                    holder.registerProblem(element, ERROR_MESSAGE, new ReplaceByConstantQuickFix());
                }
            }
        };
    }

    private static class ReplaceByConstantQuickFix implements LocalQuickFix {

        @Override
        public @IntentionFamilyName @NotNull String getFamilyName() {
            return TIBasicMessageBundle.message("inspection.constant.expression.fix.family.name");
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement element = descriptor.getPsiElement();
            if (element == null) return;
            BiFunction<BigDecimal, BigDecimal, BigDecimal> evaluator = getEvaluator(element);
            if (evaluator == null) return;

            BigDecimal result = evaluator.apply(new BigDecimal(element.getFirstChild().getText()), new BigDecimal(element.getLastChild().getText()));
            TIBasicFile tempBasicFile = createFromText(project, result.toString());
            TIBasicLiteralExpr literalExpr = PsiTreeUtil.findChildOfType(tempBasicFile, TIBasicLiteralExpr.class);
            if (literalExpr == null) return;

            element.replace(literalExpr);
        }

        private BiFunction<BigDecimal, BigDecimal, BigDecimal> getEvaluator(PsiElement element) {
            return switch (element) {
                case TIBasicPlusExpr ignored -> BigDecimal::add;
                case TIBasicMinusExpr ignored -> BigDecimal::subtract;
                case TIBasicMulExpr ignored -> BigDecimal::multiply;
                case TIBasicDivExpr ignored -> BigDecimal::divide;
                case TIBasicEqExpr ignored -> equalityTest(result -> result == 0);
                case TIBasicNeExpr ignored -> equalityTest(result -> result != 0);
                case TIBasicLtExpr ignored -> equalityTest(result -> result < 0);
                case TIBasicLeExpr ignored -> equalityTest(result -> result <= 0);
                case TIBasicGtExpr ignored -> equalityTest(result -> result > 0);
                case TIBasicGeExpr ignored -> equalityTest(result -> result >= 0);
                case TIBasicOrExpr ignored -> logicalTest(Boolean::logicalOr);
                case TIBasicXorExpr ignored -> logicalTest(Boolean::logicalXor);
                case TIBasicAndExpr ignored -> logicalTest(Boolean::logicalAnd);
                default -> null;
            };
        }

        /**
         * Performs an equality test between the two given operands. Based on the result, the output is always a
         * BigDecimal of 1 or 0.
         *
         * @param comparisonFunction The function to test the comparison result against.
         * @return A BigDecimal of 1 or 0, based on whether the comparison function was true.
         */
        private BiFunction<BigDecimal, BigDecimal, BigDecimal> equalityTest(Predicate<Integer> comparisonFunction) {
            BiFunction<BigDecimal, BigDecimal, Integer> func = BigDecimal::compareTo;
            return func.andThen(x -> comparisonFunction.test(x) ? BigDecimal.ONE : BigDecimal.ZERO);
        }

        /**
         * Performs a logical test between the two given operands. Based on the result, the output is always a
         * BigDecimal of 1 or 0.
         *
         * @param logicalTest The function to test the comparison result against.
         * @return A BigDecimal of 1 or 0, based on whether the comparison function was true.
         */
        private BiFunction<BigDecimal, BigDecimal, BigDecimal> logicalTest(BiPredicate<Boolean, Boolean> logicalTest) {
            Predicate<BigDecimal> nonZeroPredicate = num1 -> num1.compareTo(BigDecimal.ZERO) != 0;

            return (num1, num2) -> {
                boolean result1 = nonZeroPredicate.test(num1);
                boolean result2 = nonZeroPredicate.test(num2);
                return logicalTest.test(result1, result2) ? BigDecimal.ONE : BigDecimal.ZERO;
            };
        }
    }

}
