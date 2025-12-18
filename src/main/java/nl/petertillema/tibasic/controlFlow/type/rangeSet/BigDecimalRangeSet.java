package nl.petertillema.tibasic.controlFlow.type.rangeSet;

import com.intellij.codeInspection.dataFlow.value.RelationType;
import nl.petertillema.tibasic.controlFlow.BigDecimalUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MAX;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MC;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MIN;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.nextDown;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.nextUp;

public interface BigDecimalRangeSet {

    /**
     * Creates a set containing a single given value
     *
     * @param value a value to be included in the set
     * @return a new set
     */
    static BigDecimalRangeSet point(@NotNull BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) return Point.ZERO;
        if (value.compareTo(BigDecimal.ONE) == 0) return Point.ONE;
        return new Point(BigDecimalUtil.round(value));
    }

    static BigDecimalRangeSet pointSet(@NotNull BigDecimal... values) {
        return values.length == 0 ? Empty.EMPTY : (values.length == 1 ? new Point(values[0]) : new PointSet(new HashSet<>(Arrays.asList(values))));
    }

    static BigDecimalRangeSet modRange(@NotNull BigDecimal from, @NotNull BigDecimal to, @NotNull BigDecimal mod) {
        if (from.compareTo(to) > 0) return Empty.EMPTY;
        if (from.compareTo(to) == 0) return new Point(from);
        if (mod.compareTo(BigDecimal.ZERO) <= 0) return Empty.EMPTY;
        return new ModRange(from, to, mod);
    }

    static BigDecimalRangeSet range(@NotNull BigDecimal from, @NotNull BigDecimal to) {
        return from.compareTo(to) == 0 ? point(from) : new Range(from, to);
    }

    static BigDecimalRangeSet fromRanges(@NotNull BigDecimal[] ranges, int bound) {
        if (bound == 0) {
            return Empty.EMPTY;
        } else if (bound == 2) {
            return range(ranges[0], ranges[1]);
        } else {
            return new RangeSet(Arrays.copyOf(ranges, bound));
        }
    }

    static String toString(@NotNull BigDecimal from, @NotNull BigDecimal to) {
        return from + (from.compareTo(to) == 0 ? "" : ".." + to);
    }

    /**
     * Checks whether the current set contains the given value
     *
     * @param value value to find
     * @return true if the current set contains the given value
     */
    boolean contains(@NotNull BigDecimal value);

    /**
     * Checks whether the current set contains all the values from the other set
     *
     * @param other a subset candidate
     * @return true if the current set contains all the values from other
     */
    boolean contains(@NotNull BigDecimalRangeSet other);

    /**
     * Intersects the current set with other
     *
     * @param other the other set to intersect with
     * @return a new set
     */
    @NotNull BigDecimalRangeSet meet(@NotNull BigDecimalRangeSet other);

    /**
     * Merge the current set with the other. May return a bigger set if exact representation is impossible.
     *
     * @param other the other set to merge with
     * @return a new set
     */
    @NotNull BigDecimalRangeSet join(@NotNull BigDecimalRangeSet other);

    /**
     * Try to merge the current set with other so that the resulting set contains only values
     * from the input sets. Return null if it's impossible.
     *
     * @param other the other set to join with
     * @return a new set; null if exact join is not possible
     */
    @Nullable BigDecimalRangeSet tryJoinExactly(@NotNull BigDecimalRangeSet other);

    /**
     * Subtracts the given set from the current. May return a bigger set (containing some additional elements) if exact subtraction is impossible.
     *
     * @param other set to subtract
     * @return a new set
     */
    @NotNull BigDecimalRangeSet subtract(@NotNull BigDecimalRangeSet other);

    /**
     * Checks if the current set and the other set have at least one common element
     *
     * @param other the other set to check whether intersection exists
     * @return true if this set intersects the other set
     */
    boolean intersects(BigDecimalRangeSet other);

    default BigDecimalRangeSet without(BigDecimal value) {
        return subtract(point(value));
    }

    /**
     * @return a minimal value contained in the set
     * @throws NoSuchElementException if the set is empty
     */
    @NotNull BigDecimal min();

    /**
     * @return a maximal value contained in the set
     * @throws NoSuchElementException if the set is empty
     */
    @NotNull BigDecimal max();

    /**
     * Returns a range which represents all the possible values after applying unary minus
     * to the values from this set
     *
     * @return a new range
     */
    @NotNull BigDecimalRangeSet negate();

    /**
     * Returns a range which represents all the possible values after performing an addition between any value from this range
     * and any value from other range. The resulting range may contain some more values which cannot be produced by addition.
     * Guaranteed to be commutative.
     *
     * @return a new range
     */
    @NotNull BigDecimalRangeSet plus(BigDecimalRangeSet other);

    /**
     * Returns a range which represents all the possible values after performing an addition between any value from this range
     * and any value from other range. The resulting range may contain some more values which cannot be produced by addition.
     *
     * @return a new range
     */
    default BigDecimalRangeSet minus(BigDecimalRangeSet other) {
        return plus(other.negate());
    }

    /**
     * Returns a range which represents all the possible values after performing a multiplication between any value from this range
     * and any value from other range. The resulting range may contain some more values which cannot be produced by multiplication.
     * Guaranteed to be commutative.
     *
     * @return a new range
     */
    @NotNull BigDecimalRangeSet mul(BigDecimalRangeSet other);

    /**
     * Returns a range which represents all the possible values after applying {@code x / y} operation for
     * all {@code x} from this set and for all {@code y} from the divisor set. The resulting set may contain
     * some more values. Division by zero yields an empty set of possible results.
     *
     * @param divisor divisor set to divide by
     * @return a new range
     */
    default @NotNull BigDecimalRangeSet div(BigDecimalRangeSet divisor) {
        if (divisor.isEmpty() || divisor.equals(Point.ZERO)) return empty();
        BigDecimal[] left = splitAtZero(this.asRangeArray());
        BigDecimal[] right = splitAtZero(new BigDecimal[]{divisor.min(), divisor.max()});
        BigDecimalRangeSet result = empty();
        for (int i = 0; i < left.length; i += 2) {
            for (int j = 0; j < right.length; j += 2) {
                result = result.join(divide(left[i], left[i + 1], right[j], right[j + 1]));
            }
        }
        return result;
    }

    private static BigDecimal[] splitAtZero(BigDecimal[] ranges) {
        for (int i = 0; i < ranges.length; i += 2) {
            if (ranges[i].compareTo(BigDecimal.ZERO) < 0 && ranges[i + 1].compareTo(BigDecimal.ZERO) >= 0) {
                BigDecimal[] result = new BigDecimal[ranges.length + 2];
                System.arraycopy(ranges, 0, result, 0, i + 1);
                result[i + 1] = nextDown(BigDecimal.ZERO);
                result[i + 2] = nextUp(BigDecimal.ZERO);
                System.arraycopy(ranges, i + 1, result, i + 3, ranges.length - i - 1);
                return result;
            }
        }
        return ranges;
    }

    private @NotNull BigDecimalRangeSet divide(BigDecimal dividendMin, BigDecimal dividendMax, BigDecimal divisorMin, BigDecimal divisorMax) {
        if (divisorMin.compareTo(BigDecimal.ZERO) == 0) {
            if (divisorMax.compareTo(BigDecimal.ZERO) == 0) return empty();
            divisorMin = nextUp(BigDecimal.ZERO);
        }
        if (dividendMin.compareTo(BigDecimal.ZERO) >= 0) {
            return divisorMin.compareTo(BigDecimal.ZERO) > 0
                    ? range(dividendMin.divide(divisorMax, MC), dividendMax.divide(divisorMin, MC))
                    : range(dividendMax.divide(divisorMax, MC), dividendMin.divide(divisorMin, MC));
        }
        if (divisorMin.compareTo(BigDecimal.ZERO) > 0) {
            return range(dividendMin.divide(divisorMin, MC), dividendMax.divide(divisorMax, MC));
        }
        return range(dividendMax.divide(divisorMin, MC), dividendMin.divide(divisorMax, MC));
    }

    @NotNull BigDecimal[] asRangeArray();

    Stream<BigDecimal> stream();

    /**
     * @return true if the set is empty
     */
    default boolean isEmpty() {
        return this == Empty.EMPTY;
    }

    /**
     * @return an empty set
     */
    default BigDecimalRangeSet empty() {
        return Empty.EMPTY;
    }

    /**
     * @return a constant value if this set represents a constant; null otherwise
     */
    default @Nullable BigDecimal getConstantValue() {
        return null;
    }

    /**
     * Creates a new set which contains all possible values satisfying given predicate regarding the current set.
     * <p>
     * E.g., if the current set is {0..10} and the relation is "GT", then the result will be {1..Long.MAX_VALUE} (values which can be greater than
     * some value from the current set)
     *
     * @param relation relation to be applied to the current set (JavaTokenType.EQEQ/NE/GT/GE/LT/LE)
     * @return new set or null if relation is unsupported
     */
    default BigDecimalRangeSet fromRelation(@Nullable RelationType relation) {
        if (isEmpty() || relation == null) return null;
        return switch (relation) {
            case EQ -> this;
            case NE -> {
                BigDecimal min = min();
                if (min.compareTo(max()) == 0) yield all().without(min);
                yield all();
            }
            case GT -> {
                BigDecimal min = min();
                yield range(nextUp(min), MAX);
            }
            case GE -> range(min(), MAX);
            case LE -> range(MIN, max());
            case LT -> {
                BigDecimal max = max();
                yield range(MIN, nextDown(max));
            }
            default -> null;
        };
    }

    static @NotNull BigDecimalRangeSet all() {
        return Range.FULL_RANGE;
    }

}
