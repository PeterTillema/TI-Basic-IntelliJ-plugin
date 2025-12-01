package nl.petertillema.tibasic.controlFlow.type;

import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import org.jetbrains.annotations.NotNull;

import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MAX;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MIN;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.range;

public enum BinaryOperator {
    PLUS, MINUS, TIMES, DIVIDE, POW;

    @NotNull
    public BigDecimalRangeSet eval(BigDecimalRangeSet left, BigDecimalRangeSet right) {
        return switch (this) {
            case PLUS -> left.plus(right);
            case MINUS -> left.minus(right);
            case TIMES -> left.mul(right);
            case DIVIDE -> left.div(right);
            case POW -> range(MIN, MAX);
        };
    }

    @NotNull
    public BigDecimalRangeSet evalWide(BigDecimalRangeSet left, BigDecimalRangeSet right) {
        return range(MIN, MAX);
    }
}
