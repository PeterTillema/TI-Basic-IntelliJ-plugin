package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.RelationType;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static nl.petertillema.tibasic.controlFlow.type.DfDoubleConstantType.NAN;

/**
 * This is a copy from the DfDoubleRangeType used in the Java implementation. However, that class
 * is not accessible from within the plugin, so copy and use it here.
 */
public record DfDoubleRangeType(double from, double to, boolean invert, boolean nan) implements DfType {

    public static final DfDoubleRangeType FULL_RANGE = new DfDoubleRangeType(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, true);

    public static DfType create(double from, double to, boolean invert, boolean nan) {
        assert !Double.isNaN(from);
        assert !Double.isNaN(to);
        if (Double.compare(from, to) > 0) {
            return nan ? new DfDoubleConstantType(Double.NaN) : DfType.BOTTOM;
        }
        if (to == Double.POSITIVE_INFINITY && from == Double.NEGATIVE_INFINITY) {
            if (invert) {
                return nan ? new DfDoubleConstantType(Double.NaN) : DfType.BOTTOM;
            }
            return new DfDoubleRangeType(from, to, false, nan);
        }
        if (to == Double.POSITIVE_INFINITY) {
            to = nextDown(from);
            from = Double.NEGATIVE_INFINITY;
            invert = !invert;
        }
        if (!nan && !invert && Double.compare(from, to) == 0) {
            return new DfDoubleConstantType(from);
        }
        if (!nan && invert && from == Double.NEGATIVE_INFINITY && to == nextDown(Double.POSITIVE_INFINITY)) {
            return new DfDoubleConstantType(Double.POSITIVE_INFINITY);
        }
        return new DfDoubleRangeType(from, to, invert, nan);
    }

    @Override
    public boolean isSuperType(@NotNull DfType other) {
        if (other == DfType.BOTTOM || other.equals(this)) return true;
        if (other instanceof DfDoubleConstantType) {
            double val = ((DfDoubleConstantType) other).getValue();
            if (Double.isNaN(val)) return nan;
            int from = Double.compare(this.from, val);
            int to = Double.compare(val, this.to);
            return (from <= 0 && to <= 0) != invert;
        }
        if (other instanceof DfDoubleRangeType range) {
            if (range.nan && !nan) return false;
            if (!invert && from == Double.NEGATIVE_INFINITY && to == Double.POSITIVE_INFINITY) return true;
            int from = Double.compare(this.from, range.from);
            int to = Double.compare(range.to, this.to);
            if (invert) {
                if (range.invert) {
                    return from >= 0 && to >= 0;
                } else {
                    return Double.compare(range.to, this.from) < 0 || Double.compare(range.from, this.to) > 0;
                }
            } else {
                return !range.invert && from <= 0 && to <= 0;
            }
        }
        return false;
    }

    @Override
    public @NotNull DfType join(@NotNull DfType other) {
        return Objects.requireNonNull(join(other, false));
    }

    @Override
    public @Nullable DfType tryJoinExactly(@NotNull DfType other) {
        return join(other, true);
    }

    DfType join(@NotNull DfType other, boolean exact) {
        if (other.isSuperType(this)) return other;
        if (this.isSuperType(other)) return this;
        if (other instanceof DfDoubleConstantType) {
            double value = ((DfDoubleConstantType) other).getValue();
            if (Double.isNaN(value)) {
                return create(from, to, invert, true);
            }
            return joinRange(value, value, exact);
        }
        if (!(other instanceof DfDoubleRangeType range)) {
            return exact ? null : TOP;
        }
        DfDoubleRangeType res = range.nan && !nan ? new DfDoubleRangeType(from, to, invert, true) : this;
        if (range.invert) {
            if (range.from > Double.NEGATIVE_INFINITY) {
                res = res.joinRange(Double.NEGATIVE_INFINITY, nextDown(range.from), exact);
                if (res == null) return null;
            }
            if (range.to < Double.POSITIVE_INFINITY) {
                res = res.joinRange(nextUp(range.to), Double.POSITIVE_INFINITY, exact);
            }
        } else {
            res = res.joinRange(range.from, range.to, exact);
        }
        return res;
    }

    private DfDoubleRangeType joinRange(double from, double to, boolean exact) {
        if (Double.compare(from, to) > 0) return this;
        if (invert) {
            if (Double.compare(to, this.from) < 0 || Double.compare(from, this.to) > 0) return this;
            int fromCmp = Double.compare(this.from, from);
            int toCmp = Double.compare(to, this.to);
            if (fromCmp >= 0 && toCmp >= 0 || fromCmp < 0 && toCmp < 0) {
                return exact ? null : (DfDoubleRangeType) create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, nan);
            }
            if (fromCmp >= 0) {
                return (DfDoubleRangeType) create(nextUp(to), this.to, true, nan);
            }
            return (DfDoubleRangeType) create(this.from, nextDown(from), true, nan);
        } else {
            if (Double.compare(this.to, nextDown(from)) < 0 || Double.compare(to, nextDown(this.from)) < 0) {
                if (this.from == Double.NEGATIVE_INFINITY && to == Double.POSITIVE_INFINITY) {
                    return (DfDoubleRangeType) create(nextUp(this.to), nextDown(from), true, nan);
                }
                if (exact) return null;
            }
            return (DfDoubleRangeType) create(Math.min(this.from, from), Math.max(this.to, to), false, nan);
        }
    }

    @Override
    public @NotNull DfType meet(@NotNull DfType other) {
        if (other.isSuperType(this)) return this;
        if (this.isSuperType(other)) return other;
        if (!(other instanceof DfDoubleRangeType range)) return DfType.BOTTOM;
        boolean nan = range.nan && this.nan;
        if (!invert) {
            if (!range.invert) {
                double from = Math.max(this.from, range.from);
                double to = Math.min(this.to, range.to);
                return create(from, to, false, nan);
            } else {
                int fromCmp = Double.compare(from, range.from);
                int toCmp = Double.compare(range.to, to);
                if (fromCmp >= 0) {
                    return create(Math.max(from, nextUp(range.to)), to, false, nan);
                }
                if (toCmp >= 0) {
                    return create(from, Math.min(to, nextDown(range.from)), false, nan);
                }
                if (from == Double.NEGATIVE_INFINITY && to == Double.POSITIVE_INFINITY) {
                    return create(range.from, range.to, true, nan);
                }
                // disjoint [myFrom, nextDown(range.myFrom)] U [nextUp(range.myTo), myTo] -- not supported
                return create(from, to, false, nan);
            }
        } else {
            if (!range.invert) {
                return range.meet(this);
            } else {
                // both inverted
                if (to >= Math.nextDown(range.from) && range.to >= Math.nextDown(from)) {
                    // excluded ranges intersect or touch each other: we can exclude their union
                    double from = Math.min(this.from, range.from);
                    double to = Math.max(this.to, range.to);
                    return create(from, to, true, nan);
                }
                // excluded ranges don't intersect: we cannot encode this case
                // just keep one of the ranges (with lesser from, for stability)
                if (from < range.from) {
                    return create(from, to, true, nan);
                }
                return create(range.from, range.to, true, nan);
            }
        }
    }

    @Override
    public @NotNull DfType fromRelation(@NotNull RelationType relationType) {
        if (relationType == RelationType.EQ) {
            DfType result = nan ? this : create(from, to, invert, true);
            DfType zero = create(-0.0, 0.0, false, false);
            return meet(zero) != BOTTOM ? result.join(zero) : result;
        }
        if (invert) {
            double max = to == Double.POSITIVE_INFINITY ? nextDown(from) : Double.POSITIVE_INFINITY;
            double min = from == Double.NEGATIVE_INFINITY ? nextUp(to) : Double.NEGATIVE_INFINITY;
            return fromRelation(relationType, min, max);
        }
        return fromRelation(relationType, from, to);
    }

    static @NotNull DfType fromRelation(@NotNull RelationType relationType, double min, double max) {
        assert !Double.isNaN(min);
        assert !Double.isNaN(max);
        return switch (relationType) {
            case LE -> create(Double.NEGATIVE_INFINITY, max == 0.0 ? 0.0 : max, false, true);
            case LT -> max == Double.NEGATIVE_INFINITY ? NAN :
                    create(Double.NEGATIVE_INFINITY, Math.nextDown(max), false, true);
            case GE -> create(min == 0.0 ? -0.0 : min, Double.POSITIVE_INFINITY, false, true);
            case GT -> min == Double.POSITIVE_INFINITY ? NAN :
                    create(Math.nextUp(min), Double.POSITIVE_INFINITY, false, true);
            case EQ -> {
                if (min == 0.0) min = -0.0;
                if (max == 0.0) max = 0.0;
                yield create(min, max, false, true);
            }
            case NE -> {
                if (min == max) {
                    if (min == 0.0) {
                        yield create(-0.0, 0.0, true, true);
                    }
                    yield create(min, min, true, true);
                }
                yield FULL_RANGE;
            }
            default -> FULL_RANGE;
        };
    }

    @Override
    public @NotNull DfType tryNegate() {
        return create(from, to, !invert, !nan);
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
    public @NotNull String toString() {
        String range;
        if (invert) {
            if (from == to) {
                range = "!= " + (Double.compare(from, -0.0) == 0 && Double.compare(to, 0.0) == 0 ? "±0.0" : from);
            } else {
                String first = from == Double.NEGATIVE_INFINITY ? "" : formatRange(Double.NEGATIVE_INFINITY, nextDown(from));
                String second = to == Double.POSITIVE_INFINITY ? "" : formatRange(nextUp(to), Double.POSITIVE_INFINITY);
                range = StreamEx.of(first, second).without("").joining(" || ");
            }
        } else {
            range = formatRange(from, to);
        }
        String result = "double";
        if (!range.isEmpty()) {
            result += " " + range;
        }
        if (!nan) {
            result += " not NaN";
        } else if (!range.isEmpty()) {
            result += " (or NaN)";
        }
        return result;
    }

    private static double nextDown(double val) {
        // Math.nextDown returns -MIN_VALUE for 0.0. This is suitable for relations
        // (see fromRelation) but not suitable for inverted range boundary
        if (Double.compare(val, 0.0) == 0) {
            return -0.0;
        }
        return Math.nextDown(val);
    }

    private static double nextUp(double val) {
        // Math.nextUp returns MIN_VALUE for -0.0. This is suitable for relations
        // (see fromRelation) but not suitable for inverted range boundary
        if (Double.compare(val, -0.0) == 0) {
            return 0.0;
        }
        return Math.nextUp(val);
    }

    private static String formatRange(double from, double to) {
        int cmp = Double.compare(from, to);
        if (cmp == 0) return Double.toString(from);
        if (Double.compare(from, -0.0) == 0 && Double.compare(to, 0.0) == 0) {
            return "±0.0"; // ± = +/-
        }
        if (from == Double.NEGATIVE_INFINITY) {
            if (to == Double.POSITIVE_INFINITY) return "";
            return formatTo(to);
        }
        if (to == Double.POSITIVE_INFINITY) {
            return formatFrom(from);
        }
        return formatFrom(from) + " && " + formatTo(to);
    }

    private static @NotNull String formatFrom(double from) {
        double prev = nextDown(from);
        if (Double.toString(prev).length() < Double.toString(from).length()) {
            return "> " + prev;
        }
        return ">= " + from;
    }

    private static @NotNull String formatTo(double to) {
        double next = nextUp(to);
        if (Double.toString(next).length() < Double.toString(to).length()) {
            return "< " + next;
        }
        return "<= " + to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DfDoubleRangeType type = (DfDoubleRangeType) o;
        return Double.compare(type.from, from) == 0 &&
                Double.compare(type.to, to) == 0 &&
                invert == type.invert &&
                nan == type.nan;
    }

}
