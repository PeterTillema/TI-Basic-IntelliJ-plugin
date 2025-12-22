package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.RangeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalConstantType.fromValue;

public record DfBigDecimalRangeType(@NotNull RangeSet range,
                                    @Nullable RangeSet wideRange) implements DfBigDecimalType {

    public static DfType fromRange(@NotNull RangeSet range) {
        if (range.isEmpty()) return DfType.BOTTOM;
        BigDecimal value = range.getConstantValue();
        if (value != null) {
            return fromValue(value);
        }
        return new DfBigDecimalRangeType(range, null);
    }

    public static DfType fromRange(@NotNull RangeSet range, @Nullable RangeSet wideRange) {
        if (wideRange == null || wideRange.equals(range) || wideRange.isEmpty()) return fromRange(range);
        if (range.isEmpty()) {
            return DfType.BOTTOM;
        }
        BigDecimal value = range.getConstantValue();
        if (value != null) {
            return new DfBigDecimalConstantType(value, wideRange);
        }
        return new DfBigDecimalRangeType(range, wideRange);
    }

    @Override
    public @NotNull RangeSet wideRange() {
        return wideRange == null ? range : wideRange;
    }

    @Override
    public int hashCode() {
        return range.hashCode();
    }

    @Override
    public @NotNull String toString() {
        return range.toString();
    }
}
