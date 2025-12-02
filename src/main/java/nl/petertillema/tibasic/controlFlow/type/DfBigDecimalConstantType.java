package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfConstantType;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

public class DfBigDecimalConstantType extends DfConstantType<BigDecimal> implements DfBigDecimalType {

    private final @Nullable BigDecimalRangeSet wideRange;

    public DfBigDecimalConstantType(BigDecimal value, @Nullable BigDecimalRangeSet wideRange) {
        super(value);
        this.wideRange = wideRange;
    }

    public static DfType fromValue(BigDecimal value) {
        return new DfBigDecimalConstantType(value, null);
    }

    @Override
    public boolean isSuperType(@NotNull DfType other) {
        return DfBigDecimalType.super.isSuperType(other);
    }

    @Override
    public @NotNull DfType meet(@NotNull DfType other) {
        return DfBigDecimalType.super.meet(other);
    }

    @Override
    public @NotNull DfType fromRelation(@NotNull RelationType relationType) {
        return DfBigDecimalType.super.fromRelation(relationType);
    }

    @Override
    public @NotNull BigDecimalRangeSet range() {
        return BigDecimalRangeSet.point(getValue());
    }

    @Override
    public @NotNull BigDecimalRangeSet wideRange() {
        return wideRange == null ? range() : this.wideRange;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || super.equals(obj) && Objects.equals(((DfBigDecimalConstantType) obj).wideRange, wideRange);
    }

    @Override
    public @NotNull String toString() {
        return getValue().toString();
    }
}
