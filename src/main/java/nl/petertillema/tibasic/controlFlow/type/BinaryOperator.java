package nl.petertillema.tibasic.controlFlow.type;

import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import org.jetbrains.annotations.NotNull;

import static nl.petertillema.tibasic.controlFlow.type.rangeSet.Range.FULL_RANGE;

public enum BinaryOperator {
    PLUS, MINUS, TIMES, DIVIDE, POW;

    @NotNull
    public BigDecimalRangeSet eval(BigDecimalRangeSet left, BigDecimalRangeSet right) {
        return switch (this) {
            case PLUS -> left.plus(right);
            case MINUS -> left.minus(right);
            case TIMES -> left.mul(right);
            case DIVIDE -> left.div(right);
            case POW -> FULL_RANGE;
        };
    }

    @NotNull
    public BigDecimalRangeSet evalWide(BigDecimalRangeSet left, BigDecimalRangeSet right) {
        return FULL_RANGE;
    }
}
