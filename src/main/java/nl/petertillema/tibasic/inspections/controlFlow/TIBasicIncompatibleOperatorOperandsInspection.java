package nl.petertillema.tibasic.inspections.controlFlow;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
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
import nl.petertillema.tibasic.psi.TIBasicNcrExpr;
import nl.petertillema.tibasic.psi.TIBasicNeExpr;
import nl.petertillema.tibasic.psi.TIBasicNprExpr;
import nl.petertillema.tibasic.psi.TIBasicOrExpr;
import nl.petertillema.tibasic.psi.TIBasicPlusExpr;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.psi.TIBasicVisitor;
import nl.petertillema.tibasic.psi.TIBasicXorExpr;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR;

public final class TIBasicIncompatibleOperatorOperandsInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new TIBasicVisitor() {
            @Override
            public void visitPlusExpr(@NotNull TIBasicPlusExpr o) {
                super.visitPlusExpr(o);
                this.checkOperator(o, o.getExprList(), "+");
            }

            @Override
            public void visitMinusExpr(@NotNull TIBasicMinusExpr o) {
                super.visitMinusExpr(o);
                this.checkOperator(o, o.getExprList(), "-");
            }

            @Override
            public void visitMulExpr(@NotNull TIBasicMulExpr o) {
                super.visitMulExpr(o);
                this.checkOperator(o, o.getExprList(), "*");
            }

            @Override
            public void visitDivExpr(@NotNull TIBasicDivExpr o) {
                super.visitDivExpr(o);
                this.checkOperator(o, o.getExprList(), "-");
            }

            @Override
            public void visitEqExpr(@NotNull TIBasicEqExpr o) {
                super.visitEqExpr(o);
                this.checkOperator(o, o.getExprList(), "=");
            }

            @Override
            public void visitNeExpr(@NotNull TIBasicNeExpr o) {
                super.visitNeExpr(o);
                this.checkOperator(o, o.getExprList(), "!=");
            }

            @Override
            public void visitLtExpr(@NotNull TIBasicLtExpr o) {
                super.visitLtExpr(o);
                this.checkOperator(o, o.getExprList(), "<");
            }

            @Override
            public void visitLeExpr(@NotNull TIBasicLeExpr o) {
                super.visitLeExpr(o);
                this.checkOperator(o, o.getExprList(), "<=");
            }

            @Override
            public void visitGtExpr(@NotNull TIBasicGtExpr o) {
                super.visitGtExpr(o);
                this.checkOperator(o, o.getExprList(), ">");
            }

            @Override
            public void visitGeExpr(@NotNull TIBasicGeExpr o) {
                super.visitGeExpr(o);
                this.checkOperator(o, o.getExprList(), ">=");
            }

            @Override
            public void visitOrExpr(@NotNull TIBasicOrExpr o) {
                super.visitOrExpr(o);
                this.checkOperator(o, o.getExprList(), "or");
            }

            @Override
            public void visitXorExpr(@NotNull TIBasicXorExpr o) {
                super.visitXorExpr(o);
                this.checkOperator(o, o.getExprList(), "xor");
            }

            @Override
            public void visitAndExpr(@NotNull TIBasicAndExpr o) {
                super.visitAndExpr(o);
                this.checkOperator(o, o.getExprList(), "and");
            }

            @Override
            public void visitNprExpr(@NotNull TIBasicNprExpr o) {
                super.visitNprExpr(o);
                this.checkOperator(o, o.getExprList(), "nPr");
            }

            @Override
            public void visitNcrExpr(@NotNull TIBasicNcrExpr o) {
                super.visitNcrExpr(o);
                this.checkOperator(o, o.getExprList(), "nCr");
            }

            private void checkOperator(PsiElement element, List<TIBasicExpr> expressions, String operator) {
                if (expressions.size() != 2) return;

                if (!(expressions.getFirst() instanceof TIBasicLiteralExpr l1)) return;
                if (!(expressions.getLast() instanceof TIBasicLiteralExpr l2)) return;

                var type1 = getLiteralType(l1);
                var type2 = getLiteralType(l2);

                // Anything with "Ans" is not checked
                if (type1 == TIBasicTypes.ANS_VARIABLE || type2 == TIBasicTypes.ANS_VARIABLE) return;

                // String concatenation is allowed
                if ((type1 == TIBasicTypes.STRING || type1 == TIBasicTypes.STRING_VARIABLE) &&
                        (type2 == TIBasicTypes.STRING || type2 == TIBasicTypes.STRING_VARIABLE)) return;

                // Doing something with equations and strings is never allowed
                var invalidOperandTypes = List.of(
                        TIBasicTypes.EQUATION_VARIABLE,
                        TIBasicTypes.STRING,
                        TIBasicTypes.STRING_VARIABLE
                );
                if (!invalidOperandTypes.contains(type1) && !invalidOperandTypes.contains(type2)) return;

                var message = TIBasicMessageBundle.message("inspection.incompatible.operands.description", operator);
                holder.registerProblem(element, message, GENERIC_ERROR);
            }

            private IElementType getLiteralType(TIBasicLiteralExpr literalExpr) {
                if (literalExpr.getAnonymousList() != null) return TIBasicTypes.LIST_VARIABLE;
                if (literalExpr.getAnonymousMatrix() != null) return TIBasicTypes.MATRIX_VARIABLE;
                if (literalExpr.getListIndex() != null) return TIBasicTypes.NUMBER;
                if (literalExpr.getMatrixIndex() != null) return TIBasicTypes.NUMBER;
                return literalExpr.getFirstChild().getNode().getElementType();
            }
        };
    }

}
