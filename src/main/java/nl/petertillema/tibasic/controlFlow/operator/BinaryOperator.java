package nl.petertillema.tibasic.controlFlow.operator;

import nl.petertillema.tibasic.controlFlow.type.rangeSet.RangeSet;
import org.jetbrains.annotations.NotNull;

public enum BinaryOperator {
    PLUS, MINUS, TIMES, DIVIDE, POW;

    @NotNull
    public RangeSet eval(RangeSet left, RangeSet right) {
        return switch (this) {
            case PLUS -> left.plus(right);
            case MINUS -> left.minus(right);
            case TIMES -> left.mul(right);
            case DIVIDE -> left.div(right);
            case POW -> RangeSet.ALL;
        };
    }

    @NotNull
    public RangeSet evalWide(RangeSet left, RangeSet right) {
        return RangeSet.ALL;
    }
}
