package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import nl.petertillema.tibasic.controlFlow.operator.BinaryOperator;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.RangeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;

public interface DfBigDecimalType extends DfType {

    @NotNull
    RangeSet range();

    @NotNull
    RangeSet wideRange();

    default DfType eval(@NotNull DfType other, BinaryOperator binOp) {
        if (!(other instanceof DfBigDecimalType bigDecimalType)) return DfType.TOP;
        RangeSet range = binOp.eval(range(), bigDecimalType.range());
        RangeSet wideRange = binOp.evalWide(wideRange(), bigDecimalType.wideRange());
        return fromRange(range, wideRange);
    }

    @Override
    default boolean isSuperType(@NotNull DfType other) {
        if (other == DfType.BOTTOM) return true;
        if (!(other instanceof DfBigDecimalType bigDecimalType)) return false;
        return range().contains(bigDecimalType.range()) &&
                wideRange().contains(bigDecimalType.wideRange());
    }

    @Override
    default @NotNull DfType join(@NotNull DfType other) {
        if (other == DfType.BOTTOM) return this;
        if (!(other instanceof DfBigDecimalType bigDecimalType)) return DfType.TOP;
        RangeSet range = bigDecimalType.range().join(range());
        RangeSet wideRange = bigDecimalType.wideRange().join(wideRange());
        return fromRange(range, wideRange);
    }

    @Override
    default @Nullable DfType tryJoinExactly(@NotNull DfType other) {
        if (other == DfType.BOTTOM) return this;
        if (other == DfType.TOP) return other;
        if (!(other instanceof DfBigDecimalType bigDecimalType)) return null;
        RangeSet range = bigDecimalType.range().tryJoinExactly(range());
        RangeSet wideRange = bigDecimalType.wideRange().tryJoinExactly(wideRange());
        if (range == null || wideRange == null) return null;
        return fromRange(range, wideRange);
    }

    @Override
    default @NotNull DfType meet(@NotNull DfType other) {
        if (other == DfType.TOP) return this;
        if (!(other instanceof DfBigDecimalType bigDecimalType)) return DfType.BOTTOM;
        RangeSet range = bigDecimalType.range().meet(range());
        RangeSet wideRange = bigDecimalType.wideRange().meet(wideRange());
        return fromRange(range, wideRange);
    }

    @Override
    @NotNull
    default DfType fromRelation(@NotNull RelationType relationType) {
        return fromRange(range().fromRelation(relationType).meet(RangeSet.ALL));
    }

    @Override
    default DfType widen() {
        RangeSet wideRange = wideRange();
        return wideRange.equals(range()) ? this : fromRange(wideRange, null);
    }

    @Override
    @Nullable
    default DfType tryNegate() {
        RangeSet range = range();
        RangeSet res = RangeSet.ALL.subtract(range);
        return res.intersects(range) ? null : fromRange(res, null);
    }

    static RangeSet extractRange(@NotNull DfType type) {
        return type instanceof DfBigDecimalType bigDecimalType ? bigDecimalType.range() : RangeSet.ALL;
    }
}
