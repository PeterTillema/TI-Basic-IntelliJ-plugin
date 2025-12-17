package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.Range;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.math.RoundingMode.CEILING;
import static java.math.RoundingMode.FLOOR;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.FULL_RANGE;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;

/**
 * A descriptor for a list element, which could be a single element within a normal list, or a row in a matrix.
 * This row in itself contains other ListElementDescriptor's for the actual elements. The returned DfType is determined
 * whether this descriptor is part of a matrix or a regular list.
 */
public record ListElementDescriptor(int index) implements VariableDescriptor {

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        // Is it a row in a matrix? If so, it should be a list. If not, it's a regular numeric value within the
        // TI-BASIC range.
        if (qualifier != null && qualifier.getDfType() instanceof DfMatrixType) {
            return SpecialFieldDescriptor.LIST_LENGTH.asDfType(fromRange(new Range(BigDecimal.ZERO, BigDecimal.valueOf(99))));
        }
        return fromRange(FULL_RANGE);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ListElementDescriptor(int index1))) return false;
        return index1 == this.index;
    }

    @Override
    public @NotNull String toString() {
        return "[" + index + "]";
    }

    public static Set<Integer> extractIntegerIndexCandidates(DfType indexType, Integer maxLen, int threshold) {
        Set<Integer> out = new LinkedHashSet<>();
        if (!(indexType instanceof DfBigDecimalType idxNum) || idxNum.range().isEmpty()) return out;
        BigDecimalRangeSet rs = idxNum.range();

        BigDecimal[] ranges = rs.asRangeArray();
        int added = 0;
        for (int i = 0; i < ranges.length; i += 2) {
            BigDecimal from = ranges[i];
            BigDecimal to = ranges[i + 1];
            int lo = ceilToInt(from);
            int hi = floorToInt(to);
            if (maxLen != null && hi > maxLen) hi = maxLen;
            if (lo < 1) lo = 1;
            if (hi < lo) continue;
            long count = (long) hi - lo + 1L;
            if (count > threshold - added) {
                out.clear();
                return out;
            }
            for (int v = lo; v <= hi; v++) {
                out.add(v);
                added++;
            }
        }
        return out;
    }

    private static int ceilToInt(BigDecimal val) {
        return val.setScale(0, CEILING).intValue();
    }

    private static int floorToInt(BigDecimal val) {
        return val.setScale(0, FLOOR).intValue();
    }

}
