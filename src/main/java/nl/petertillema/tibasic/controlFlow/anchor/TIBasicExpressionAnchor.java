package nl.petertillema.tibasic.controlFlow.anchor;

import nl.petertillema.tibasic.psi.TIBasicExpr;

public class TIBasicExpressionAnchor extends TIBasicDfaAnchor {

    private final TIBasicExpr expression;

    public TIBasicExpressionAnchor(TIBasicExpr expression) {
        this.expression = expression;
    }
}
