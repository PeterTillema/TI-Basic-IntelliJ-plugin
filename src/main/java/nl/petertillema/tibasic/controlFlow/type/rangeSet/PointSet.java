package nl.petertillema.tibasic.controlFlow.type.rangeSet;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.fromRanges;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.range;

public record PointSet(Set<BigDecimal> values) implements BigDecimalRangeSet {

    public PointSet {
        if (values.size() < 2) {
            throw new IllegalArgumentException("Not enough values for set");
        }
    }

    @Override
    public boolean contains(@NotNull BigDecimal value) {
        return values.contains(value);
    }

    @Override
    public boolean contains(@NotNull BigDecimalRangeSet other) {
        if (other.isEmpty() || other == this) return true;
        if (other instanceof Point(BigDecimal value)) {
            return contains(value);
        }
        if (other instanceof PointSet(Set<BigDecimal> values1)) {
            return values.containsAll(values1);
        }
        return false;
    }

    @Override
    public @NotNull BigDecimalRangeSet meet(@NotNull BigDecimalRangeSet other) {
        if (other == this) return this;
        if (other.isEmpty()) return other;
        if (other instanceof Point) {
            return other.meet(this);
        }
        Set<BigDecimal> out = new HashSet<>();
        for (BigDecimal value : values) {
            if (other.contains(value)) out.add(value);
        }
        return out.isEmpty() ? Empty.EMPTY : (out.size() == 1 ? new Point(out.iterator().next()) : new PointSet(out));
    }

    @Override
    public @NotNull BigDecimalRangeSet join(@NotNull BigDecimalRangeSet other) {
        if (other.isEmpty() || other == this) return this;
        if (other instanceof Point(BigDecimal value)) {
            Set<BigDecimal> out = new HashSet<>(values);
            out.add(value);
            return new PointSet(out);
        }
        if (other instanceof PointSet(Set<BigDecimal> values1)) {
            Set<BigDecimal> out = new HashSet<>(values);
            out.addAll(values1);
            return new PointSet(out);
        }
        BigDecimal[] result = other.asRangeArray();
        for (BigDecimal value : values) {
            int pos = -Arrays.binarySearch(result, value) - 1;
            if (pos < 0 || pos % 2 == 1) continue;
            BigDecimal[] newResult = new BigDecimal[result.length + 2];
            System.arraycopy(result, 0, newResult, 0, pos);
            newResult[pos] = newResult[pos + 1] = value;
            System.arraycopy(result, pos, newResult, pos + 2, result.length - pos);
            result = newResult;
        }
        return fromRanges(result, result.length);
    }

    @Override
    public @NotNull BigDecimalRangeSet tryJoinExactly(@NotNull BigDecimalRangeSet other) {
        return join(other);
    }

    @Override
    public @NotNull BigDecimal min() {
        return values.stream().min(Comparator.naturalOrder()).get();
    }

    @Override
    public @NotNull BigDecimal max() {
        return values.stream().max(Comparator.naturalOrder()).get();
    }

    @Override
    public @NotNull BigDecimalRangeSet negate() {
        Set<BigDecimal> out = new HashSet<>();
        values.forEach(value -> out.add(value.negate()));
        return new PointSet(out);
    }

    @Override
    public @NotNull BigDecimalRangeSet plus(BigDecimalRangeSet other) {
        if (other.isEmpty()) return other;
        if (other instanceof Point(BigDecimal value)) {
            if (value.compareTo(BigDecimal.ZERO) == 0) return this;
            Set<BigDecimal> out = new HashSet<>();
            values.forEach(val -> out.add(val.add(value)));
            return new PointSet(out);
        }
        if (other instanceof PointSet(Set<BigDecimal> values1)) {
            Set<BigDecimal> out = new HashSet<>();
            values.forEach(thisValue ->
                    values1.forEach(otherValue -> out.add(thisValue.add(otherValue))));
            return new PointSet(out);
        }
        BigDecimal min = min().add(other.min());
        BigDecimal max = max().add(other.max());
        return min.compareTo(max) <= 0 ? range(min, max) : range(max, min);
    }

    @Override
    public @NotNull BigDecimalRangeSet mul(BigDecimalRangeSet other) {
        if (other.isEmpty()) return other;
        if (other instanceof Point(BigDecimal value)) {
            if (value.compareTo(BigDecimal.ZERO) == 0) return empty();
            if (value.compareTo(BigDecimal.ONE) == 0) return this;
            Set<BigDecimal> out = new HashSet<>();
            values.forEach(val -> out.add(val.multiply(value)));
            return new PointSet(out);
        }
        if (other instanceof PointSet(Set<BigDecimal> values1)) {
            Set<BigDecimal> out = new HashSet<>();
            values.forEach(thisValue ->
                    values1.forEach(otherValue -> out.add(thisValue.multiply(otherValue))));
            return new PointSet(out);
        }
        BigDecimal min = min().multiply(other.min());
        BigDecimal max = max().multiply(other.max());
        return min.compareTo(max) <= 0 ? range(min, max) : range(max, min);
    }

    @Override
    public @NotNull BigDecimalRangeSet subtract(@NotNull BigDecimalRangeSet other) {
        if (other.isEmpty()) return this;
        if (other == this) return Empty.EMPTY;
        Set<BigDecimal> out = new HashSet<>();
        for (BigDecimal value : values) {
            if (!other.contains(value)) out.add(value);
        }
        return out.isEmpty() ? Empty.EMPTY : (out.size() == 1 ? new Point(out.iterator().next()) : new PointSet(out));
    }

    @Override
    public boolean intersects(BigDecimalRangeSet other) {
        if (other.isEmpty()) return false;
        for (BigDecimal value : values) {
            if (other.contains(value)) return true;
        }
        return false;
    }

    @Override
    public BigDecimal[] asRangeArray() {
        return values.stream()
                .sorted()
                .flatMap(value -> Stream.of(value, value))
                .toArray(BigDecimal[]::new);
    }

    @Override
    public Stream<BigDecimal> stream() {
        return values.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PointSet pointSet = (PointSet) o;
        return Objects.equals(values, pointSet.values);
    }

    @Override
    public @NotNull String toString() {
        String out = values.stream()
                .limit(5)
                .map(BigDecimal::toString)
                .collect(Collectors.joining(", "));
        if (values.size() > 5) out = out + "...";
        return "[" + out + "]";
    }
}
