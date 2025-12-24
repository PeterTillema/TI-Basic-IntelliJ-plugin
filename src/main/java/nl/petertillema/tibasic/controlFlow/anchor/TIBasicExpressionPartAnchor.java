package nl.petertillema.tibasic.controlFlow.anchor;

import nl.petertillema.tibasic.psi.TIBasicExpr;

public class TIBasicExpressionPartAnchor extends TIBasicDfaAnchor {

    private final TIBasicExpr expression;

    public TIBasicExpressionPartAnchor(TIBasicExpr expression) {
        this.expression = expression;
    }
}
