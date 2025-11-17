package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfConstantType;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static nl.petertillema.tibasic.controlFlow.type.DfDoubleRangeType.FULL_RANGE;

/**
 * This is a copy from the DfDoubleConstantType used in the Java implementation. However, that class
 * is not accessible from within the plugin, so copy and use it here.
 */
public class DfDoubleConstantType extends DfConstantType<Double> {

    public static final DfDoubleConstantType NAN = new DfDoubleConstantType(Double.NaN);

    private final boolean shouldWiden;

    public DfDoubleConstantType(double value) {
        this(value, false);
    }

    private DfDoubleConstantType(double value, boolean widen) {
        super(value);
        shouldWiden = widen;
    }

    public static DfDoubleConstantType fromBoolean(boolean bool) {
        return new DfDoubleConstantType(bool ? 1.0 : 0.0);
    }

    public DfDoubleConstantType makeWide() {
        return shouldWiden ? this : new DfDoubleConstantType(getValue(), true);
    }

    @Override
    public DfType widen() {
        return shouldWiden ? FULL_RANGE : super.widen();
    }

    @Override
    public @NotNull DfType join(@NotNull DfType other) {
        return Objects.requireNonNull(join(other, false));
    }

    @Override
    public @Nullable DfType tryJoinExactly(@NotNull DfType other) {
        return join(other, true);
    }

    private DfType join(@NotNull DfType other, boolean exact) {
        if (other.isSuperType(this)) return other;
        if (other instanceof DfDoubleRangeType) {
            return ((DfDoubleRangeType) other).join(this, exact);
        }
        if (other instanceof DfDoubleConstantType) {
            double val1 = getValue();
            double val2 = ((DfDoubleConstantType) other).getValue();
            if (Double.isNaN(val1)) {
                return DfDoubleRangeType.create(val2, val2, false, true);
            }
            if (Double.isNaN(val2)) {
                return DfDoubleRangeType.create(val1, val1, false, true);
            }
            double from = Math.min(val1, val2);
            double to = Math.max(val1, val2);
            // We don't support disjoint sets, so the best we can do is to return a range from min to max
            return exact ? null : DfDoubleRangeType.create(from, to, false, false);
        }
        return exact ? null : DfType.TOP;
    }

    @Override
    public @NotNull DfType fromRelation(@NotNull RelationType relationType) {
        double value = getValue();
        if (Double.isNaN(value)) {
            return relationType == RelationType.NE ? FULL_RANGE : BOTTOM;
        }
        return DfDoubleRangeType.fromRelation(relationType, value, value);
    }

    @Override
    public @NotNull DfType tryNegate() {
        double value = getValue();
        if (Double.isNaN(value)) {
            return DfDoubleRangeType.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, false);
        }
        return DfDoubleRangeType.create(value, value, true, true);
    }

    @Override
    public @NotNull DfType correctForRelationResult(@NotNull RelationType relation, boolean result) {
        if (result == (relation != RelationType.NE)) {
            return meet(new DfDoubleRangeType(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, false));
        }
        return this;
    }

    @Override
    public @NotNull DfType meetRelation(@NotNull RelationType relationType,
                                        @NotNull DfType other) {
        DfType result = meet(other.fromRelation(relationType));
        if (result == DfType.BOTTOM && relationType != RelationType.NE) {
            if (!isSuperType(NAN) && other.isSuperType(NAN)) {
                return this;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof DfDoubleConstantType that && super.equals(o) && shouldWiden == that.shouldWiden;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + (shouldWiden ? 1 : 0);
    }

}
