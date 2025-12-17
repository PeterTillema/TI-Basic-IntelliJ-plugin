package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import nl.petertillema.tibasic.controlFlow.type.DfBigDecimalType;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
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
 * Descriptor for a single element within a list. The actual list is provided
 * as a qualifier to the {@link DfaVariableValue}. This allows the DFA engine
 * to distinguish the same index belonging to different lists.
 */
public final class ListElementDescriptor implements VariableDescriptor {

    private final int index;

    public ListElementDescriptor(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        if (qualifier != null && qualifier.getDfType() instanceof DfMatrixType) {
            return new DfListType(SpecialFieldDescriptor.LIST_LENGTH, fromRange(new Range(BigDecimal.ZERO, BigDecimal.valueOf(999))));
        }
        return fromRange(FULL_RANGE);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ListElementDescriptor that)) return false;
        return that.index == this.index;
    }

    @Override
    public int hashCode() {
        return index + 12345678;
    }

    @Override
    public String toString() {
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
