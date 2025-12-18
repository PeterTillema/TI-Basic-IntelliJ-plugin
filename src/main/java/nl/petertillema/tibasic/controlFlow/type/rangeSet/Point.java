package nl.petertillema.tibasic.controlFlow.type.rangeSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.round;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.fromRanges;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.point;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.pointSet;

public record Point(BigDecimal value) implements BigDecimalRangeSet {

    public static final Point ZERO = new Point(BigDecimal.ZERO);
    public static final Point ONE = new Point(BigDecimal.ONE);

    public Point(BigDecimal value) {
        this.value = round(value);
    }

    @Override
    public boolean contains(@NotNull BigDecimal value) {
        return this.value.compareTo(value) == 0;
    }

    @Override
    public boolean contains(@NotNull BigDecimalRangeSet other) {
        return other.isEmpty() || equals(other);
    }

    @Override
    public @NotNull BigDecimalRangeSet meet(@NotNull BigDecimalRangeSet other) {
        return other.contains(value) ? this : Empty.EMPTY;
    }

    @Override
    public @NotNull BigDecimalRangeSet join(@NotNull BigDecimalRangeSet other) {
        if (other.isEmpty() || other == this) return this;
        if (other.contains(value)) return other;
        if (other instanceof Point(BigDecimal value1)) {
            return pointSet(value, value1);
        }
        if (other instanceof PointSet) {
            return other.join(this);
        }
        if (other instanceof Range range) {
            if (value.compareTo(range.min()) < 0) {
                return new RangeSet(new BigDecimal[]{value, value, other.min(), other.max()});
            } else {
                return new RangeSet(new BigDecimal[]{other.min(), other.max(), value, value});
            }
        }
        BigDecimal[] arr = other.asRangeArray();
        int pos = -Arrays.binarySearch(arr, value) - 1;
        BigDecimal[] result = new BigDecimal[arr.length + 2];
        System.arraycopy(arr, 0, result, 0, pos);
        result[pos] = value;
        result[pos + 1] = value;
        System.arraycopy(arr, pos, result, pos + 2, arr.length - pos);
        return fromRanges(result, result.length);
    }

    @Override
    public @NotNull BigDecimalRangeSet tryJoinExactly(@NotNull BigDecimalRangeSet other) {
        return join(other);
    }

    @Override
    public @NotNull BigDecimal min() {
        return value;
    }

    @Override
    public @NotNull BigDecimal max() {
        return value;
    }

    @Override
    public @NotNull BigDecimalRangeSet negate() {
        return point(value.negate());
    }

    @Override
    public @NotNull BigDecimalRangeSet plus(BigDecimalRangeSet other) {
        if (other.isEmpty()) return other;
        if (other instanceof Point(BigDecimal value1)) {
            return point(value.add(value1));
        }
        return other.plus(this);
    }

    @Override
    public @NotNull BigDecimalRangeSet mul(BigDecimalRangeSet multiplier) {
        if (multiplier.isEmpty()) return multiplier;
        if (value.compareTo(BigDecimal.ZERO) == 0) return this;
        if (value.compareTo(BigDecimal.ONE) == 0) return multiplier;
        if (value.compareTo(BigDecimal.valueOf(-1)) == 0) return multiplier.negate();
        if (multiplier instanceof Point(BigDecimal value1)) {
            return point(value.multiply(value1));
        }
        return multiplier.mul(this);
    }

    @Override
    public @NotNull BigDecimalRangeSet subtract(@NotNull BigDecimalRangeSet other) {
        return other.contains(value) ? Empty.EMPTY : this;
    }

    @Override
    public boolean intersects(BigDecimalRangeSet other) {
        return other.contains(value);
    }

    @Override
    public BigDecimal[] asRangeArray() {
        return new BigDecimal[]{value, value};
    }

    @Override
    public Stream<BigDecimal> stream() {
        return Stream.of(value);
    }

    @Override
    public @Nullable BigDecimal getConstantValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        return o instanceof Point(BigDecimal value1) && value.compareTo(value1) == 0;
    }

}
