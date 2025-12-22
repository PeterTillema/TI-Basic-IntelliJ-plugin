package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DerivedVariableDescriptor;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import nl.petertillema.tibasic.controlFlow.type.DfStringType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.RangeSet.range;

/**
 * These values are special descriptors, in the sense that those are not actual variable types which you will see in
 * TI-BASIC. However, they are derived from another type and could be used as such, for example, the length of a list
 * or a string.
 */
public enum SpecialFieldDescriptor implements DerivedVariableDescriptor {

    LIST_LENGTH {
        @Override
        public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
            int length = qualifier != null && qualifier.getDfType() instanceof DfMatrixType ? 99 : 999;
            return fromRange(range(BigDecimal.ZERO, BigDecimal.valueOf(length)));
        }

        @Override
        public @NotNull DfType asDfType(@NotNull DfType fieldValue) {
            return new DfListType(this, fieldValue);
        }
    },

    MATRIX_LENGTH {
        @Override
        public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
            return fromRange(range(BigDecimal.ZERO, BigDecimal.valueOf(99)));
        }

        @Override
        public @NotNull DfType asDfType(@NotNull DfType fieldValue) {
            return new DfMatrixType(this, fieldValue);
        }
    },

    STRING_LENGTH {
        @Override
        public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
            return fromRange(range(BigDecimal.ZERO, BigDecimal.valueOf(999)));
        }

        @Override
        public @NotNull DfType asDfType(@NotNull DfType fieldValue) {
            return new DfStringType(this, fieldValue);
        }
    };

    @Override
    public @NotNull DfType asDfType(@NotNull DfType qualifierType, @NotNull DfType fieldValue) {
        return asDfType(fieldValue).meet(qualifierType);
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public boolean isImplicitReadPossible() {
        return true;
    }
}
