package nl.petertillema.tibasic.controlFlow.type.rangeSet;

import com.intellij.codeInspection.dataFlow.value.RelationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MAX;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MC;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.MIN;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.nextDown;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.nextUp;
import static nl.petertillema.tibasic.controlFlow.BigDecimalUtil.round;

public class RangeSet {

    public record Range(BigDecimal from, BigDecimal to) {
        public Range {
            if (from.compareTo(to) > 0) {
                BigDecimal temp = from;
                from = to;
                to = temp;
            }
        }

        public boolean contains(@NotNull BigDecimal value) {
            return value.compareTo(from) >= 0 && value.compareTo(to) <= 0;
        }

        public boolean isFullOverlap(@NotNull Range other) {
            return other.from.compareTo(from) >= 0 && other.to.compareTo(to) <= 0;
        }

        public boolean hasOverlap(@NotNull Range other) {
            return other.to.compareTo(from) >= 0 && other.from.compareTo(to) <= 0;
        }

        public @Nullable Range meet(@NotNull Range other) {
            BigDecimal low = from.max(other.from);
            BigDecimal high = to.min(other.to);
            return low.compareTo(high) <= 0 ? new Range(low, high) : null;
        }
    }

    /**
     * A set of possible values. The values are guaranteed outside all the ranges.
     */
    private final Set<BigDecimal> values;

    /**
     * A list of ranges with possible values. The ranges are guaranteed to be sorted.
     */
    private final List<Range> ranges;

    /**
     * A set of excluded points. All the excluded points lie within one of the ranges.
     */
    private final Set<BigDecimal> exclusions;

    public static final RangeSet EMPTY = new RangeSet(Set.of(), List.of(), Set.of());

    public static final RangeSet ALL = new RangeSet(Set.of(), List.of(new Range(MIN, MAX)), Set.of());

    public RangeSet(Set<BigDecimal> values, List<Range> ranges, Set<BigDecimal> exclusions) {
        this.values = values;
        this.ranges = ranges;
        this.exclusions = exclusions;
    }

    public static RangeSet point(@NotNull BigDecimal value) {
        return new RangeSet(Set.of(value), List.of(), Set.of());
    }

    public static RangeSet pointSet(@NotNull BigDecimal... values) {
        return new RangeSet(new HashSet<>(Arrays.asList(values)), List.of(), Set.of());
    }

    public static RangeSet range(@NotNull BigDecimal from, @NotNull BigDecimal to) {
        if (from.compareTo(to) > 0) return EMPTY;
        return new RangeSet(Set.of(), List.of(new Range(from, to)), Set.of());
    }

    /**
     * @return true if the set is empty
     */
    public boolean isEmpty() {
        return values.isEmpty() && ranges.isEmpty();
    }

    /**
     * @return a constant value if this set represents a constant; null otherwise
     */
    public @Nullable BigDecimal getConstantValue() {
        return values.size() == 1 && ranges.isEmpty() ? values.iterator().next() : null;
    }

    /**
     * Checks whether the current set contains the given value
     *
     * @param value value to find
     * @return true if the current set contains the given value
     */
    public boolean contains(@NotNull BigDecimal value) {
        if (exclusions.contains(value)) return false;
        if (values.contains(value)) return true;
        for (Range range : ranges) {
            if (range.contains(value)) return true;
        }
        return false;
    }

    /**
     * Checks whether the current set contains all the values from the other set
     *
     * @param other a subset candidate
     * @return true if the current set contains all the values from other
     */
    public boolean contains(@NotNull RangeSet other) {
        if (other.isEmpty()) return true;
        for (BigDecimal value : exclusions) {
            if (other.contains(value)) return false;
        }
        for (BigDecimal value : other.values) {
            if (!contains(value)) return false;
        }
        for (Range otherRange : other.ranges) {
            boolean hasOverlap = false;
            for (Range thisRange : ranges) {
                if (thisRange.isFullOverlap(otherRange)) hasOverlap = true;
            }
            if (!hasOverlap) return false;
        }
        return true;
    }

    /**
     * Intersects the current set with other
     *
     * @param other the other set to intersect with
     * @return a new set
     */
    public @NotNull RangeSet meet(@NotNull RangeSet other) {
        if (this == other) return this;
        if (other.isEmpty()) return other;
        // Meet all the ranges
        List<Range> meetRanges = new ArrayList<>();
        for (Range range : ranges) {
            for (Range otherRange : other.ranges) {
                Range meet = range.meet(otherRange);
                if (meet != null) meetRanges.add(meet);
            }
        }
        // Meet all the values and remove them if the other set doesn't contain them
        Set<BigDecimal> meetValues = new HashSet<>(values);
        meetValues.removeIf(value -> !other.contains(value));
        for (BigDecimal value : other.values) {
            if (contains(value)) meetValues.add(value);
        }
        // Meet all the exclusions and remove unnecessary
        Set<BigDecimal> meetExclusions = new HashSet<>(exclusions);
        meetExclusions.addAll(other.exclusions);
        meetExclusions.removeIf(value -> {
            for (Range range : meetRanges) {
                if (range.contains(value)) return false;
            }
            return true;
        });
        return new RangeSet(meetValues, meetRanges, meetExclusions);
    }

    /**
     * Merge the current set with the other
     *
     * @param other the other set to merge with
     * @return a new set
     */
    public @NotNull RangeSet join(@NotNull RangeSet other) {
        if (this == other || other.isEmpty()) return this;
        Set<BigDecimal> joinValues = new HashSet<>(values);
        joinValues.addAll(other.values);
        List<Range> joinRanges = new ArrayList<>(ranges);
        joinRanges.addAll(other.ranges);
        // Join all the exclusions and remove them if the other set contains the value
        Set<BigDecimal> joinExclusions = new HashSet<>(exclusions);
        joinExclusions.removeIf(other::contains);
        Set<BigDecimal> otherExclusions = new HashSet<>(other.exclusions);
        otherExclusions.removeIf(this::contains);
        joinExclusions.addAll(otherExclusions);
        return normalizeRangeSet(joinValues, joinRanges, joinExclusions);
    }

    /**
     * Try to merge the current set with other so that the resulting set contains only values
     * from the input sets. Return null if it's impossible.
     *
     * @param other the other set to join with
     * @return a new set; null if exact join is not possible
     */
    public @Nullable RangeSet tryJoinExactly(@NotNull RangeSet other) {
        return join(other);
    }

    /**
     * Subtracts the given set from the current
     *
     * @param other set to subtract
     * @return a new set
     */
    public @NotNull RangeSet subtract(@NotNull RangeSet other) {
        if (other.isEmpty()) return this;
        // The new set of exclusions consist of my exclusions and all the other points (since the points get
        // subtracted from this set)
        Set<BigDecimal> newExclusions = new HashSet<>(exclusions);
        newExclusions.addAll(other.values);
        // The new set of values consist of my values minus the ones that are part of the other set
        Set<BigDecimal> newValues = new HashSet<>(values);
        newValues.removeIf(other::contains);
        // The other exclusions now become points if the current set contains the exclusion
        for (BigDecimal exclusion : other.exclusions) {
            if (contains(exclusion)) newValues.add(exclusion);
        }
        List<Range> subtractedRanges = new ArrayList<>();
        mainLoop:
        for (int i = 0; i < ranges.size(); i++) {
            Range thisRange = ranges.get(i);
            for (int j = 0; j < other.ranges.size(); j++) {
                Range otherRange = other.ranges.get(j);
                // No overlap at all -> continue with checking the next one
                if (otherRange.to.compareTo(thisRange.from) < 0) continue;
                // No overlap with any other range, so add it and continue to the next one
                if (otherRange.from.compareTo(thisRange.to) > 0) {
                    subtractedRanges.add(thisRange);
                    continue mainLoop;
                }
                // Overlap, so split into the left and right part
                if (otherRange.from.compareTo(thisRange.from) > 0) {
                    BigDecimal nextDown = nextDown(otherRange.from);
                    int cmp = nextDown.compareTo(thisRange.from);
                    if (cmp == 0) {
                        newValues.add(thisRange.from);
                    } else if (cmp > 0) {
                        subtractedRanges.add(new Range(thisRange.from, nextDown));
                    }
                }
                // Right part
                if (otherRange.to.compareTo(thisRange.to) < 0) {
                    BigDecimal nextUp = nextUp(otherRange.to);
                    int cmp = nextUp.compareTo(thisRange.to);
                    if (cmp == 0) {
                        newValues.add(thisRange.to);
                        // The full range is already subtracted from, so just continue with the other ranges from this set
                        continue mainLoop;
                    } else if (cmp < 0) {
                        thisRange = new Range(nextUp, thisRange.to);
                    }
                } else {
                    // The left part is already handled, and the right part is empty, so the range has been fully
                    // subtracted from. Just continue with the next range.
                    continue mainLoop;
                }
            }
            subtractedRanges.add(thisRange);
        }
        // Remove exclusions which are not part of the new ranges
        newExclusions.removeIf(value -> {
            for (Range range : subtractedRanges) {
                if (range.contains(value)) return false;
            }
            return true;
        });
        return new RangeSet(newValues, subtractedRanges, newExclusions);
    }

    /**
     * Checks if the current set and the other set have at least one common element
     *
     * @param other the other set to check whether intersection exists
     * @return true if this set intersects the other set
     */
    public boolean intersects(RangeSet other) {
        for (BigDecimal value : values) {
            if (other.contains(value)) return true;
        }
        for (Range range : ranges) {
            for (BigDecimal value : other.values) {
                if (range.contains(value) && !exclusions.contains(value)) return true;
            }
            for (Range otherRange : other.ranges) {
                if (range.hasOverlap(otherRange)) return true;
            }
        }
        return false;
    }

    /**
     * Creates a new set which contains all possible values satisfying given predicate regarding the current set.
     *
     * @param relation relation to be applied to the current set (RelationType.EQ/NE/GT/GE/LT/LE/IS/IS_NOT)
     * @return new set or null if relation is unsupported
     */
    public RangeSet fromRelation(@Nullable RelationType relation) {
        if (isEmpty() || relation == null) return null;
        return switch (relation) {
            case EQ -> this;
            case NE -> {
                BigDecimal min = min();
                if (min.compareTo(max()) == 0) yield ALL.subtract(point(min));
                yield ALL;
            }
            case GT -> range(nextUp(min()), MAX);
            case GE -> range(min(), MAX);
            case LT -> range(MIN, nextDown(max()));
            case LE -> range(MIN, max());
            case IS, IS_NOT -> null;
        };
    }

    /**
     * @return a minimal value contained in the set
     * @throws NoSuchElementException if the set is empty
     */
    public @NotNull BigDecimal min() {
        Set<BigDecimal> certainValues = new HashSet<>(values);
        Optional<BigDecimal> valueMin = certainValues.stream().min(Comparator.naturalOrder());
        for (Range range : ranges) {
            BigDecimal value = range.from;
            while (value.compareTo(range.to) <= 0) {
                if (!exclusions.contains(value)) return valueMin.isPresent() ? valueMin.get().min(value) : value;
                value = nextUp(value);
            }
        }
        return valueMin.orElseThrow();
    }

    /**
     * @return a maximal value contained in the set
     * @throws NoSuchElementException if the set is empty
     */
    public @NotNull BigDecimal max() {
        Set<BigDecimal> certainValues = new HashSet<>(values);
        Optional<BigDecimal> valueMax = certainValues.stream().max(Comparator.naturalOrder());
        for (int i = ranges.size() - 1; i >= 0; i--) {
            Range range = ranges.get(i);
            BigDecimal value = range.to;
            while (value.compareTo(range.from) >= 0) {
                if (!exclusions.contains(value)) return valueMax.isPresent() ? valueMax.get().max(value) : value;
                value = nextDown(value);
            }
        }
        return valueMax.orElseThrow();
    }

    /**
     * Returns a range which represents all the possible values after applying unary minus
     * to the values from this set
     *
     * @return a new set
     */
    public @NotNull RangeSet negate() {
        Set<BigDecimal> negatedValues = new HashSet<>();
        List<Range> negatedRanges = new ArrayList<>();
        Set<BigDecimal> negatedExclusions = new HashSet<>();
        for (BigDecimal value : values) {
            negatedValues.add(value.negate());
        }
        for (Range range : ranges) {
            negatedRanges.add(new Range(range.to, range.from));
        }
        for (BigDecimal value : exclusions) {
            negatedExclusions.add(value.negate());
        }
        return new RangeSet(negatedValues, negatedRanges, negatedExclusions);
    }

    /**
     * Returns a range which represents all the possible values after performing an addition between any value from this range
     * and any value from the other range.
     *
     * @return a new set
     */
    public @NotNull RangeSet plus(RangeSet other) {
        if (other.isEmpty()) return other;
        Set<BigDecimal> plusValues = new HashSet<>();
        List<Range> plusRanges = new ArrayList<>();
        for (BigDecimal thisValue : values) {
            for (BigDecimal otherValue : other.values) {
                plusValues.add(round(thisValue.add(otherValue)));
            }
            for (Range range : other.ranges) {
                plusRanges.add(new Range(round(thisValue.add(range.from)), round(thisValue.add(range.to))));
            }
        }
        for (Range thisRange : ranges) {
            for (BigDecimal otherValue : other.values) {
                plusRanges.add(new Range(round(otherValue.add(thisRange.from)), round(otherValue.add(thisRange.to))));
            }
            for (Range otherRange : other.ranges) {
                plusRanges.add(new Range(round(thisRange.from.add(otherRange.from)), round(thisRange.to.add(otherRange.to))));
            }
        }
        return normalizeRangeSet(plusValues, plusRanges, Set.of());
    }

    /**
     * Returns a range which represents all the possible values after performing a subtraction between any value from this range
     * and any value from the other set
     *
     * @return a new set
     */
    public RangeSet minus(RangeSet other) {
        return plus(other.negate());
    }

    /**
     * Returns a set which represents all the possible values after performing a multiplication between any value from this set
     * and any value from the other set
     *
     * @return a new set
     */
    public @NotNull RangeSet mul(RangeSet other) {
        if (other.isEmpty()) return other;
        Set<BigDecimal> mulValues = new HashSet<>();
        List<Range> mulRanges = new ArrayList<>();
        for (BigDecimal thisValue : values) {
            for (BigDecimal otherValue : other.values) {
                mulValues.add(round(thisValue.multiply(otherValue)));
            }
            for (Range range : other.ranges) {
                mulRanges.add(new Range(round(thisValue.multiply(range.from)), round(thisValue.multiply(range.to))));
            }
        }
        for (Range thisRange : ranges) {
            for (BigDecimal otherValue : other.values) {
                mulRanges.add(new Range(round(otherValue.multiply(thisRange.from)), round(otherValue.multiply(thisRange.to))));
            }
            for (Range otherRange : other.ranges) {
                mulRanges.add(new Range(round(thisRange.from.multiply(otherRange.from)), round(thisRange.to.multiply(otherRange.to))));
            }
        }
        return normalizeRangeSet(mulValues, mulRanges, Set.of());
    }

    /**
     * Returns a set which represents all the possible values after performing a division between any value from this set
     * and any value from the other set
     *
     * @return a new set
     */
    public @NotNull RangeSet div(RangeSet other) {
        Set<BigDecimal> divValues = new HashSet<>();
        List<Range> divRanges = new ArrayList<>();
        Set<BigDecimal> divExclusions = new HashSet<>();
        for (BigDecimal otherValue : other.values) {
            if (otherValue.compareTo(BigDecimal.ZERO) == 0) continue;
            // Point / point -> new point
            for (BigDecimal thisValue : values) {
                divValues.add(thisValue.divide(otherValue, MC));
            }
            // Range / point -> new range
            for (Range thisRange : ranges) {
                divRanges.add(new Range(thisRange.from.divide(otherValue, MC), thisRange.to.divide(otherValue, MC)));
            }
            // Exclusion / point -> new exclusion
            for (BigDecimal thisExclusion : exclusions) {
                divExclusions.add(thisExclusion.divide(otherValue, MC));
            }
        }
        List<Range> otherRangesWithoutZero = other.ranges.stream().flatMap(range -> splitAtZero(range).stream()).toList();
        for (Range otherRange : otherRangesWithoutZero) {
            // Point / range -> new Range(point/from, point/to)
            for (BigDecimal thisValue : values) {
                if (thisValue.compareTo(BigDecimal.ZERO) == 0) {
                    divValues.add(BigDecimal.ZERO);
                } else {
                    divRanges.add(new Range(thisValue.divide(otherRange.from, MC), thisValue.divide(otherRange.to, MC)));
                }
            }
            // Range / range -> new range
            for (Range thisRange : ranges) {
                BigDecimal v1 = thisRange.from.divide(otherRange.from, MC);
                BigDecimal v2 = thisRange.from.divide(otherRange.to, MC);
                BigDecimal v3 = thisRange.to.divide(otherRange.from, MC);
                BigDecimal v4 = thisRange.to.divide(otherRange.to, MC);
                divRanges.add(new Range(v1.min(v2).min(v3).min(v4), v1.max(v2).max(v3).max(v4)));
            }
        }
        // Point / exclusion -> new exclusion
        for (BigDecimal exclusion : other.exclusions) {
            for (BigDecimal value : values) {
                divExclusions.add(value.divide(exclusion, MC));
            }
        }
        return normalizeRangeSet(divValues, divRanges, divExclusions);
    }

    /**
     * Returns a stream with all fixed values
     *
     * @return a stream with all fixed values
     */
    public Stream<BigDecimal> stream() {
        return values.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RangeSet rangeSet)) return false;
        return Objects.equals(values, rangeSet.values) && Objects.equals(ranges, rangeSet.ranges) && Objects.equals(exclusions, rangeSet.exclusions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, ranges, exclusions);
    }

    @Override
    public String toString() {
        String valuesString = values.stream().limit(5).map(BigDecimal::toString).collect(Collectors.joining(", "));
        String rangesString = ranges.stream().limit(5).map(range -> range.from + ".." + range.to).collect(Collectors.joining(", "));
        String exclusionsString = exclusions.stream().limit(5).map(v -> "o" + v.toString()).collect(Collectors.joining(", "));
        String combined = Stream.of(valuesString, rangesString, exclusionsString)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));
        return "[" + combined + "]";
    }

    private static List<Range> splitAtZero(@NotNull Range range) {
        int cmpFrom = range.from.compareTo(BigDecimal.ZERO);
        int cmpTo = range.to.compareTo(BigDecimal.ZERO);
        if (cmpFrom > 0 || cmpTo < 0) return List.of(range);
        if (cmpFrom == 0) return List.of(new Range(nextUp(BigDecimal.ZERO), range.to));
        if (cmpTo == 0) return List.of(new Range(range.from, nextDown(BigDecimal.ZERO)));
        return List.of(new Range(range.from, nextDown(BigDecimal.ZERO)), new Range(nextUp(BigDecimal.ZERO), range.to));
    }

    private static List<Range> normalizeRanges(List<Range> ranges) {
        ranges = ranges.stream().sorted(Comparator.comparing(Range::from).thenComparing(Range::to)).toList();
        List<Range> normalizedRanges = new ArrayList<>();
        Range prevRange = null;
        for (Range range : ranges) {
            if (prevRange == null) {
                prevRange = range;
            } else if (range.from.compareTo(prevRange.to) <= 0) {
                prevRange = new Range(prevRange.from, range.to.max(prevRange.to));
            } else {
                normalizedRanges.add(prevRange);
                prevRange = range;
            }
        }
        if (prevRange != null) normalizedRanges.add(prevRange);
        return normalizedRanges;
    }

    private static RangeSet normalizeRangeSet(Set<BigDecimal> values, List<Range> ranges, Set<BigDecimal> exclusions) {
        List<Range> normalizedRanges = normalizeRanges(ranges);
        Set<BigDecimal> normalizedValues = new HashSet<>(values);
        normalizedValues.removeIf(value -> {
            for (Range range : normalizedRanges) {
                if (range.contains(value)) return true;
            }
            return false;
        });
        Set<BigDecimal> normalizedExclusions = new HashSet<>(exclusions);
        normalizedExclusions.removeIf(value -> {
            for (Range range : normalizedRanges) {
                if (range.contains(value)) return false;
            }
            return true;
        });
        return new RangeSet(normalizedValues, normalizedRanges, normalizedExclusions);
    }
}
