package nl.petertillema.tibasic.controlFlow.anchor;

import nl.petertillema.tibasic.psi.TIBasicExpr;

import java.util.Objects;

public class TIBasicExpressionPartAnchor extends TIBasicDfaAnchor {

    private final TIBasicExpr expression;

    public TIBasicExpressionPartAnchor(TIBasicExpr expression) {
        this.expression = expression;
    }

    public TIBasicExpr getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TIBasicExpressionPartAnchor that)) return false;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }

}
