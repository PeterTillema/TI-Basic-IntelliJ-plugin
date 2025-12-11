package nl.petertillema.tibasic.controlFlow.type.rangeSet;

import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MAX;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MIN;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.nextDown;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.nextUp;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.fromRanges;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.range;

public record Range(BigDecimal from, BigDecimal to) implements BigDecimalRangeSet {

    public static final BigDecimalRangeSet FULL_RANGE = new Range(MIN, MAX);

    @Override
    public boolean contains(@NotNull BigDecimal value) {
        return value.compareTo(from) >= 0 && value.compareTo(to) <= 0;
    }

    @Override
    public boolean contains(@NotNull BigDecimalRangeSet other) {
        return other.isEmpty() || (other.min().compareTo(from) >= 0 && other.max().compareTo(to) <= 0);
    }

    @Override
    public @NotNull BigDecimalRangeSet meet(@NotNull BigDecimalRangeSet other) {
        if (other == this) return this;
        if (other.isEmpty()) return other;
        if (other instanceof Point || other instanceof PointSet) {
            return other.meet(this);
        }
        if (other instanceof Range(BigDecimal from1, BigDecimal v)) {
            if (from1.compareTo(this.from) <= 0 && v.compareTo(this.to) >= 0) return this;
            if (from1.compareTo(this.from) >= 0 && v.compareTo(this.to) <= 0) return other;
            if (from1.compareTo(this.from) < 0) {
                from1 = this.from;
            }
            if (v.compareTo(this.to) > 0) {
                v = this.to;
            }
            return from1.compareTo(v) <= 0 ? range(from1, v) : Empty.EMPTY;
        }
        BigDecimal[] ranges = ((RangeSet) other).ranges();
        BigDecimal[] result = new BigDecimal[ranges.length];
        int index = 0;
        for (int i = 0; i < ranges.length; i += 2) {
            BigDecimal[] res = meet(range(ranges[i], ranges[i + 1])).asRangeArray();
            System.arraycopy(res, 0, result, index, res.length);
            index += res.length;
        }
        return fromRanges(result, index);
    }

    @Override
    public @NotNull BigDecimalRangeSet join(@NotNull BigDecimalRangeSet other) {
        if (other.isEmpty() || other == this) return this;
        if (other instanceof Point || other instanceof PointSet) {
            return other.join(this);
        }
        if (other instanceof Range) {
            if (other.min().compareTo(max()) <= 0 && min().compareTo(other.max()) <= 0) {
                return range(min().min(other.min()), max().max(other.max()));
            }
            if (other.max().compareTo(min()) < 0) {
                return new RangeSet(new BigDecimal[]{other.min(), other.max(), min(), max()});
            }
            return new RangeSet(new BigDecimal[]{min(), max(), other.min(), other.max()});
        }
        BigDecimal[] longs = other.asRangeArray();
        int minIndex = Arrays.binarySearch(longs, min());
        if (minIndex < 0) {
            minIndex = -minIndex - 1;
//            if (minIndex % 2 == 0 && minIndex > 0 && longs[minIndex - 1] + 1 == min()) {
//                minIndex--;
//            }
        } else if (minIndex % 2 == 0) {
            minIndex++;
        }
        int maxIndex = Arrays.binarySearch(longs, max());
        if (maxIndex < 0) {
            maxIndex = -maxIndex - 1;
//            if (maxIndex % 2 == 0 && maxIndex < longs.length && max() + 1 == longs[maxIndex]) {
//                maxIndex++;
//            }
        } else if (maxIndex % 2 == 0) {
            maxIndex++;
        }
        BigDecimal[] result = new BigDecimal[longs.length + 2];
        System.arraycopy(longs, 0, result, 0, minIndex);
        int pos = minIndex;
        if (minIndex % 2 == 0) {
            result[pos++] = min();
        }
        if (maxIndex % 2 == 0) {
            result[pos++] = max();
        }
        System.arraycopy(longs, maxIndex, result, pos, longs.length - maxIndex);
        return fromRanges(result, longs.length + pos - maxIndex);
    }

    @Override
    public @NotNull BigDecimalRangeSet tryJoinExactly(@NotNull BigDecimalRangeSet other) {
        return join(other);
    }

    @Override
    public @NotNull BigDecimal min() {
        return from;
    }

    @Override
    public @NotNull BigDecimal max() {
        return to;
    }

    @Override
    public @NotNull BigDecimalRangeSet negate() {
        return new Range(to.negate(), from.negate());
    }

    @Override
    public @NotNull BigDecimalRangeSet plus(BigDecimalRangeSet other) {
        if (other.isEmpty()) return other;
        BigDecimal min = from.add(other.min());
        BigDecimal max = to.add(other.max());
        return min.compareTo(max) <= 0 ? range(min, max) : range(max, min);
    }

    @Override
    public @NotNull BigDecimalRangeSet mul(BigDecimalRangeSet other) {
        if (other.isEmpty()) return other;
        BigDecimal min = from.multiply(other.min());
        BigDecimal max = to.multiply(other.max());
        return min.compareTo(max) <= 0 ? range(min, max) : range(max, min);
    }

    @Override
    public @NotNull BigDecimalRangeSet subtract(@NotNull BigDecimalRangeSet other) {
        if (other.isEmpty()) return this;
        if (other == this) return Empty.EMPTY;
        if (other instanceof Point(BigDecimal value)) {
            if (value.compareTo(from) < 0 || value.compareTo(to) > 0) return this;
            if (value.compareTo(from) == 0) return range(nextUp(from), to);
            if (value.compareTo(to) == 0) return range(from, nextDown(to));
            return new RangeSet(new BigDecimal[]{from, nextDown(value), nextUp(value), to});
        }
        if (other instanceof PointSet(Set<BigDecimal> values)) {
            List<BigDecimal> otherValues = values.stream().sorted().toList();
            int i = 0;
            BigDecimal[] result = new BigDecimal[otherValues.size() + 2];
            BigDecimal lastValue = from;
            for (BigDecimal value : otherValues) {
                if (value.compareTo(from) < 0) continue;
                if (value.compareTo(to) > 0) break;
                if (value.compareTo(lastValue) == 0) {
                    lastValue = nextUp(value);
                    continue;
                }
                result[i++] = lastValue;
                result[i++] = nextDown(value);
                lastValue = nextUp(value);
            }
            if (lastValue.compareTo(to) < 0) {
                result[i++] = lastValue;
                result[i++] = to;
            }
            return fromRanges(result, i);
        }
        if (other instanceof Range(BigDecimal from1, BigDecimal v)) {
            BigDecimalRangeSet toJoin = Empty.EMPTY;
            if (v.compareTo(this.from) < 0 || from1.compareTo(this.to) > 0) return this;
            if (from1.compareTo(this.from) <= 0 && v.compareTo(this.to) >= 0) return toJoin;
            if (from1.compareTo(this.from) > 0 && v.compareTo(this.to) < 0) {
                return new RangeSet(new BigDecimal[]{this.from, nextDown(from1), nextUp(v), this.to}).join(toJoin);
            }
            if (from1.compareTo(this.from) <= 0) {
                return range(nextUp(v), this.to).join(toJoin);
            }
            return range(this.from, nextDown(from1)).join(toJoin);
        }
        BigDecimal[] ranges = ((RangeSet) other).ranges();
        BigDecimalRangeSet result = this;
        for (int i = 0; i < ranges.length; i += 2) {
            result = result.subtract(range(ranges[i], ranges[i + 1]));
            if (result.isEmpty()) return result;
        }
        return result;
    }

    @Override
    public boolean intersects(BigDecimalRangeSet other) {
        if (other.isEmpty()) return false;
        if (other instanceof RangeSet(BigDecimal[] ranges)) {
            for (int i = 0; i < ranges.length && ranges[i].compareTo(to) <= 0; i += 2) {
                if (to.compareTo(ranges[i]) >= 0 && from.compareTo(ranges[i + 1]) <= 0) return true;
            }
            return false;
        }
        return to.compareTo(other.min()) >= 0 && from.compareTo(other.max()) <= 0;
    }

    @Override
    public BigDecimal[] asRangeArray() {
        return new BigDecimal[]{from, to};
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        return o != null && o.getClass() == getClass() && from.compareTo(((Range) o).from) == 0 && to.compareTo(((Range) o).to) == 0;
    }

    @Override
    public @NlsSafe @NotNull String toString() {
        return "{" + BigDecimalRangeSet.toString(from, to) + "}";
    }

}
