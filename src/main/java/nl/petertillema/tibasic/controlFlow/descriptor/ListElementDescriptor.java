package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryState;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaValueFactory;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import com.intellij.codeInspection.dataFlow.value.VariableDescriptor;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.Range;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

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

    private static @Nullable DfaValue getListElementValue(@NotNull DfaValueFactory factory,
                                                          @NotNull DfaVariableValue array,
                                                          BigDecimal index,
                                                          int maxLength) {
        try {
            int intIndex = index.intValueExact();
            if (intIndex < 1 || intIndex > maxLength) return null;
            return factory.getVarFactory().createVariableValue(new ListElementDescriptor(intIndex), array);
        } catch (ArithmeticException ex) {
            return null;
        }
    }

    public static @NotNull DfaValue getListElementValue(@NotNull DfaValueFactory factory,
                                                        @NotNull DfaMemoryState state,
                                                        @NotNull DfaValue array,
                                                        @NotNull BigDecimalRangeSet indexSet) {
        if (!(array instanceof DfaVariableValue arrayDfaVar)) return factory.getUnknown();
        if (indexSet.isEmpty()) return factory.getUnknown();
        boolean isMatrix = state.getDfType(array) instanceof DfMatrixType;
        BigDecimal min = indexSet.min();
        BigDecimal max = indexSet.max();
        int maxLength = isMatrix ? 99 : 999;
        if (min.compareTo(max) == 0) {
            DfaValue value = getListElementValue(factory, arrayDfaVar, min, maxLength);
            return value == null ? factory.getUnknown() : value;
        }
        DfType result = indexSet.stream()
                .map(value -> getListElementValue(factory, arrayDfaVar, value, maxLength))
                .filter(Objects::nonNull)
                .map(state::getDfType)
                .reduce(DfType::join)
                .orElse(DfType.TOP);
        return factory.fromDfType(result);
    }

}
