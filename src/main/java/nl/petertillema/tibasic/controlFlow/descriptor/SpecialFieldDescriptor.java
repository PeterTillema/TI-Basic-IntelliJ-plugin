package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DerivedVariableDescriptor;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import nl.petertillema.tibasic.controlFlow.type.DfMatrixType;
import nl.petertillema.tibasic.controlFlow.type.DfStringType;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.Range;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MAX;
import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;

public enum SpecialFieldDescriptor implements DerivedVariableDescriptor {
    LIST_LENGTH {
        @Override
        public @NotNull DfType asDfType(@NotNull DfType fieldValue) {
            return new DfListType(this, fieldValue);
        }
    },

    MATRIX_LENGTH {
        @Override
        public @NotNull DfType asDfType(@NotNull DfType fieldValue) {
            return new DfMatrixType(this, fieldValue);
        }
    },

    STRING_LENGTH {
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
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return fromRange(new Range(BigDecimal.ZERO, MAX));
    }

}
