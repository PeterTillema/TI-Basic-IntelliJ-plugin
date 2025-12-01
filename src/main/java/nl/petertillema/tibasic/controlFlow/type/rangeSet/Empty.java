package nl.petertillema.tibasic.controlFlow.type.rangeSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

public class Empty implements BigDecimalRangeSet {

    public static final Empty EMPTY = new Empty();

    @Override
    public boolean contains(@NotNull BigDecimal value) {
        return false;
    }

    @Override
    public boolean contains(@NotNull BigDecimalRangeSet other) {
        return other.isEmpty();
    }

    @Override
    public @NotNull BigDecimalRangeSet meet(@NotNull BigDecimalRangeSet other) {
        return this;
    }

    @Override
    public @NotNull BigDecimalRangeSet join(@NotNull BigDecimalRangeSet other) {
        return other;
    }

    @Override
    public @Nullable BigDecimalRangeSet tryJoinExactly(@NotNull BigDecimalRangeSet other) {
        return other;
    }

    @Override
    public @NotNull BigDecimal min() {
        throw new NoSuchElementException();
    }

    @Override
    public @NotNull BigDecimal max() {
        throw new NoSuchElementException();
    }

    @Override
    public @NotNull BigDecimalRangeSet negate() {
        return this;
    }

    @Override
    public @NotNull BigDecimalRangeSet plus(BigDecimalRangeSet other) {
        return this;
    }

    @Override
    public @NotNull BigDecimalRangeSet mul(BigDecimalRangeSet other) {
        return this;
    }

    @Override
    public @NotNull BigDecimalRangeSet subtract(@NotNull BigDecimalRangeSet other) {
        return this;
    }

    @Override
    public boolean intersects(BigDecimalRangeSet other) {
        return false;
    }

    @Override
    public BigDecimal[] asRangeArray() {
        return new BigDecimal[0];
    }

    @Override
    public String toString() {
        return "EMPTY";
    }
}
