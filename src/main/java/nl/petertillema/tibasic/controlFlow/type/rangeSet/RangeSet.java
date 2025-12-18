package nl.petertillema.tibasic.controlFlow.type.rangeSet;

import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.all;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.fromRanges;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.range;

public record RangeSet(BigDecimal[] ranges) implements BigDecimalRangeSet {

    public RangeSet {
        if (ranges.length < 4 || ranges.length % 2 != 0) {
            // 0 ranges = Empty; 1 range = Range
            throw new IllegalArgumentException("Bad length: " + ranges.length + " " + Arrays.toString(ranges));
        }
        for (int i = 0; i < ranges.length; i += 2) {
            if (ranges[i + 1].compareTo(ranges[i]) < 0) {
                throw new IllegalArgumentException("Bad sub-range #" + (i / 2) + " " + Arrays.toString(ranges));
            }
        }
    }

    @Override
    public boolean contains(@NotNull BigDecimal value) {
        for (int i = 0; i < ranges.length; i += 2) {
            if (value.compareTo(ranges[i]) >= 0 && value.compareTo(ranges[i + 1]) <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(@NotNull BigDecimalRangeSet other) {
        if (other.isEmpty() || other == this) return true;
        if (other instanceof Point(BigDecimal value)) {
            return contains(value);
        }
        BigDecimalRangeSet result = other;
        for (int i = 0; i < ranges.length; i += 2) {
            result = result.subtract(range(ranges[i], ranges[i + 1]));
            if (result.isEmpty()) return true;
        }
        return false;
    }

    @Override
    public @NotNull BigDecimalRangeSet meet(@NotNull BigDecimalRangeSet other) {
        if (other == this) return this;
        if (other.isEmpty()) return other;
        if (other instanceof Point || other instanceof PointSet || other instanceof Range) {
            return other.meet(this);
        }
        return subtract(all().subtract(other));
    }

    @Override
    public @NotNull BigDecimalRangeSet join(@NotNull BigDecimalRangeSet other) {
        if (!(other instanceof RangeSet)) {
            return other.join(this);
        }
        if (other == this) return this;
        if (other.contains(this)) return other;
        if (this.contains(other)) return this;
        BigDecimalRangeSet result = other;
        for (int i = 0; i < ranges.length; i += 2) {
            result = range(ranges[i], ranges[i + 1]).join(result);
        }
        return result;
    }

    @Override
    public @NotNull BigDecimalRangeSet tryJoinExactly(@NotNull BigDecimalRangeSet other) {
        return join(other);
    }

    @Override
    public @NotNull BigDecimal min() {
        return ranges[0];
    }

    @Override
    public @NotNull BigDecimal max() {
        return ranges[ranges.length - 1];
    }

    @Override
    public @NotNull BigDecimalRangeSet negate() {
        BigDecimal[] out = new BigDecimal[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            out[i] = ranges[ranges.length - i - 1].negate();
        }
        return new RangeSet(out);
    }

    @Override
    public @NotNull BigDecimalRangeSet plus(BigDecimalRangeSet other) {
        if (other.isEmpty()) return other;
        if (other instanceof Point(BigDecimal value)) {
            BigDecimal[] out = new BigDecimal[ranges.length];
            for (int i = 0; i < ranges.length; i++) {
                out[i] = ranges[i].add(value);
            }
            return new RangeSet(out);
        }
        BigDecimal min = min().add(other.min());
        BigDecimal max = max().add(other.max());
        return min.compareTo(max) <= 0 ? range(min, max) : range(max, min);
    }

    @Override
    public @NotNull BigDecimalRangeSet mul(BigDecimalRangeSet other) {
        if (other.isEmpty()) return other;
        BigDecimal min = min().multiply(other.min());
        BigDecimal max = max().multiply(other.max());
        return min.compareTo(max) <= 0 ? range(min, max) : range(max, min);
    }

    @Override
    public @NotNull BigDecimalRangeSet subtract(@NotNull BigDecimalRangeSet other) {
        if (other.isEmpty()) return this;
        if (other == this) return Empty.EMPTY;
        BigDecimal[] result = new BigDecimal[ranges.length + other.asRangeArray().length];
        int index = 0;
        for (int i = 0; i < ranges.length; i += 2) {
            BigDecimalRangeSet res = range(ranges[i], ranges[i + 1]).subtract(other);
            BigDecimal[] ranges = res.asRangeArray();
            System.arraycopy(ranges, 0, result, index, ranges.length);
            index += ranges.length;
        }
        return fromRanges(result, index);
    }

    @Override
    public boolean intersects(BigDecimalRangeSet other) {
        if (other.isEmpty()) return false;
        if (other instanceof Point(BigDecimal value)) {
            return contains(value);
        }
        BigDecimal[] otherRanges = other.asRangeArray();
        int a = 0, b = 0;
        while (true) {
            BigDecimal aFrom = ranges[a];
            BigDecimal aTo = ranges[a + 1];
            BigDecimal bFrom = otherRanges[b];
            BigDecimal bTo = otherRanges[b + 1];
            if (aFrom.compareTo(bTo) <= 0 && bFrom.compareTo(aTo) <= 0) return true;
            if (aFrom.compareTo(bTo) > 0) {
                b += 2;
                if (b >= otherRanges.length) return false;
            } else {
                a += 2;
                if (a >= ranges.length) return false;
            }
        }
    }

    @Override
    public BigDecimal[] asRangeArray() {
        return ranges;
    }

    @Override
    public Stream<BigDecimal> stream() {
        return Stream.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RangeSet rangeSet = (RangeSet) o;
        return Objects.deepEquals(ranges, rangeSet.ranges);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(ranges);
    }

    @Override
    public @NlsSafe @NotNull String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ranges.length; i += 2) {
            sb.append(BigDecimalRangeSet.toString(ranges[i], ranges[i + 1]));
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        return "{" + sb + "}";
    }
}
