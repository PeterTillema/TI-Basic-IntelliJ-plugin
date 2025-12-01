package nl.petertillema.tibasic.controlFlow.type;

import junit.framework.TestCase;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.Empty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.fromRanges;

public class BigDecimalRangeSetTest extends TestCase {

    public void testNarrowing() {
        assertEquals(empty(), pointSet());
        assertEquals(point(1), pointSet(1));
        assertEquals(point(1), range(1, 1));
        assertEquals(empty(), fromRanges(new BigDecimal[0], 0));
        assertEquals(range(1, 2), rangeSet(1, 2));
    }

    public void testEmpty() {
        assertFalse(empty().contains(v(1)));
        assertTrue(empty().contains(empty()));
        assertFalse(empty().contains(point(1)));
        assertFalse(empty().contains(pointSet(1, 2)));
        assertFalse(empty().contains(range(1, 2)));
        assertFalse(empty().contains(rangeSet(1, 2, 3, 4)));
        assertEquals(empty(), empty().meet(empty()));
        assertEquals(empty(), empty().meet(point(1)));
        assertEquals(empty(), empty().meet(pointSet(1, 2)));
        assertEquals(empty(), empty().meet(range(1, 2)));
        assertEquals(empty(), empty().meet(rangeSet(1, 2, 3, 4)));
        assertEquals(empty(), empty().join(empty()));
        assertEquals(point(1), empty().join(point(1)));
        assertEquals(pointSet(1, 2), empty().join(pointSet(1, 2)));
        assertEquals(range(1, 2), empty().join(range(1, 2)));
        assertEquals(rangeSet(1, 2, 3, 4), empty().join(rangeSet(1, 2, 3, 4)));
        assertEquals(empty(), empty().subtract(empty()));
        assertEquals(empty(), empty().subtract(point(1)));
        assertEquals(empty(), empty().subtract(pointSet(1, 2)));
        assertEquals(empty(), empty().subtract(range(1, 2)));
        assertEquals(empty(), empty().subtract(rangeSet(1, 2, 3, 4)));
        assertFalse(empty().intersects(empty()));
        assertFalse(empty().intersects(point(1)));
        assertFalse(empty().intersects(pointSet(1, 2)));
        assertFalse(empty().intersects(range(1, 2)));
        assertFalse(empty().intersects(rangeSet(1, 2, 3, 4)));
    }

    public void testEmptyMath() {
        assertEquals(empty(), empty().negate());
        assertEquals(empty(), empty().plus(empty()));
        assertEquals(empty(), empty().plus(point(2)));
        assertEquals(empty(), empty().plus(pointSet(2, 3)));
        assertEquals(empty(), empty().plus(range(2, 3)));
        assertEquals(empty(), empty().plus(rangeSet(2, 3, 4, 5)));
        assertEquals(empty(), empty().mul(point(2)));
        assertEquals(empty(), empty().mul(pointSet(2, 3)));
        assertEquals(empty(), empty().mul(range(2, 3)));
        assertEquals(empty(), empty().mul(rangeSet(2, 3, 4, 5)));
        assertEquals(empty(), empty().div(empty()));
        assertEquals(empty(), empty().div(point(2)));
        assertEquals(empty(), empty().div(pointSet(2, 3)));
        assertEquals(empty(), empty().div(range(2, 3)));
        assertEquals(empty(), empty().div(rangeSet(2, 3, 5, 6)));
    }

    public void testPoint() {
        BigDecimalRangeSet point = point(2);

        assertTrue(point.contains(v(2)));
        assertFalse(point.contains(v(1)));
        assertTrue(point.contains(empty()));
        assertTrue(point.contains(point(2)));
        assertFalse(point.contains(point(1)));
        assertFalse(point.contains(pointSet(1, 2)));
        assertFalse(point.contains(range(1, 2)));
        assertFalse(point.contains(rangeSet(1, 2, 3, 4)));
        assertEquals(empty(), point.meet(empty()));
        assertEquals(point(2), point.meet(point(2)));
        assertEquals(empty(), point.meet(point(1)));
        assertEquals(point(2), point.meet(pointSet(1, 2)));
        assertEquals(empty(), point.meet(pointSet(1, 3)));
        assertEquals(point(2), point.meet(range(1, 2)));
        assertEquals(empty(), point.meet(range(3, 4)));
        assertEquals(point(2), point.meet(rangeSet(1, 2, 3, 4)));
        assertEquals(empty(), point.meet(rangeSet(3, 4, 5, 6)));
        assertEquals(point(2), point.join(empty()));
        assertEquals(point(2), point.join(point(2)));
        assertEquals(pointSet(1, 2), point.join(point(1)));
        assertEquals(pointSet(1, 2, 3), point.join(pointSet(1, 3)));
        assertEquals(rangeSet(0, 1, 2, 2), point.join(range(0, 1)));
        assertEquals(range(2, 3), point.join(range(2, 3)));
        assertEquals(rangeSet(2, 2, 3, 4), point.join(range(3, 4)));
        assertEquals(rangeSet(0, 1, 2, 3), point.join(rangeSet(0, 1, 2, 3)));
        assertEquals(rangeSet(0, 1, 2, 2, 3, 4), point.join(rangeSet(0, 1, 3, 4)));
        assertEquals(point(2), point.subtract(point(1)));
        assertEquals(empty(), point.subtract(point(2)));
        assertEquals(point(2), point.subtract(pointSet(1, 3)));
        assertEquals(empty(), point.subtract(pointSet(1, 2)));
        assertEquals(point(2), point.subtract(range(0, 1)));
        assertEquals(empty(), point.subtract(range(1, 2)));
        assertEquals(point(2), point.subtract(rangeSet(0, 1, 3, 4)));
        assertEquals(empty(), point.subtract(rangeSet(0, 1, 2, 3)));
        assertFalse(point.intersects(empty()));
        assertTrue(point.intersects(point(2)));
        assertFalse(point.intersects(point(1)));
        assertTrue(point.intersects(pointSet(1, 2)));
        assertFalse(point.intersects(pointSet(1, 3)));
        assertTrue(point.intersects(range(1, 2)));
        assertFalse(point.intersects(range(0, 1)));
        assertTrue(point.intersects(rangeSet(1, 2, 3, 4)));
        assertFalse(point.intersects(rangeSet(0, 1, 3, 4)));
    }

    public void testPointMath() {
        BigDecimalRangeSet point = point(2);

        assertEquals(point(-2), point.negate());
        assertEquals(empty(), point.plus(empty()));
        assertEquals(point(6), point.plus(point(4)));
        assertEquals(pointSet(3, 5), point.plus(pointSet(1, 3)));
        assertEquals(range(5, 7), point.plus(range(3, 5)));
        assertEquals(rangeSet(5, 7, 8, 9), point.plus(rangeSet(3, 5, 6, 7)));
        assertEquals(empty(), point.minus(empty()));
        assertEquals(point(-2), point.minus(point(4)));
        assertEquals(pointSet(-1, 1), point.minus(pointSet(1, 3)));
        assertEquals(range(-3, -1), point.minus(range(3, 5)));
        assertEquals(rangeSet(-5, -4, -3, -1), point.minus(rangeSet(3, 5, 6, 7)));
        assertEquals(empty(), point.mul(empty()));
        assertEquals(point(8), point.mul(point(4)));
        assertEquals(pointSet(2, 6), point.mul(pointSet(1, 3)));
        assertEquals(range(6, 10), point.mul(range(3, 5)));
        assertEquals(range(6, 14), point.mul(rangeSet(3, 5, 6, 7)));
        assertEquals(empty(), point.div(empty()));
        assertEquals(BigDecimalRangeSet.point(new BigDecimal("-0.5")), point.div(point(-4)));
        assertEquals(BigDecimalRangeSet.range(new BigDecimal("0.25"), new BigDecimal("0.5")), point.div(pointSet(4, 8)));
        assertEquals(BigDecimalRangeSet.range(new BigDecimal("0.25"), new BigDecimal("0.5")), point.div(range(4, 8)));
        assertEquals(rangeSet("-2e13", "-0.5", "0.25", "2e13"), point.div(rangeSet(-4, 5, 7, 8)));
    }

    public void testPointSet() {
        BigDecimalRangeSet pointSet = pointSet(1, 2);

        assertTrue(pointSet.contains(v(2)));
        assertFalse(pointSet.contains(v(0)));
        assertTrue(pointSet.contains(empty()));
        assertTrue(pointSet.contains(point(2)));
        assertFalse(pointSet.contains(point(0)));
        assertTrue(pointSet.contains(pointSet(1, 2)));
        assertFalse(pointSet.contains(pointSet(0, 2)));
        assertFalse(pointSet.contains(range(1, 2)));
        assertFalse(pointSet.contains(rangeSet(1, 2, 3, 4)));
        assertEquals(empty(), pointSet.meet(empty()));
        assertEquals(point(2), pointSet.meet(point(2)));
        assertEquals(empty(), pointSet.meet(point(0)));
        assertEquals(pointSet(1, 2), pointSet.meet(pointSet(1, 2)));
        assertEquals(point(1), pointSet.meet(pointSet(0, 1)));
        assertEquals(empty(), pointSet.meet(pointSet(0, 3)));
        assertEquals(pointSet(1, 2), pointSet.meet(range(1, 2)));
        assertEquals(pointSet(1), pointSet.meet(range(0, 1)));
        assertEquals(empty(), pointSet.meet(range(3, 4)));
        assertEquals(pointSet(1, 2), pointSet.meet(rangeSet(1, 2, 3, 4)));
        assertEquals(point(1), pointSet.meet(rangeSet(0, 1, 4, 5)));
        assertEquals(empty(), pointSet.meet(rangeSet(-1, 0, 3, 4)));
        assertEquals(pointSet(1, 2), pointSet.join(empty()));
        assertEquals(pointSet(1, 2), pointSet.join(point(2)));
        assertEquals(pointSet(1, 2, 3), pointSet.join(point(3)));
        assertEquals(pointSet(1, 2, 3), pointSet.join(pointSet(1, 3)));
        assertEquals(rangeSet(0, 1, 2, 2), pointSet.join(range(0, 1)));
        assertEquals(rangeSet(1, 1, 2, 3), pointSet.join(range(2, 3)));
        assertEquals(rangeSet(1, 1, 2, 2, 3, 4), pointSet.join(range(3, 4)));
        assertEquals(rangeSet(0, 1, 2, 3), pointSet.join(rangeSet(0, 1, 2, 3)));
        assertEquals(rangeSet(0, 1, 2, 2, 3, 4), pointSet.join(rangeSet(0, 1, 3, 4)));
        assertEquals(point(2), pointSet.subtract(point(1)));
        assertEquals(pointSet(1, 2), pointSet.subtract(point(3)));
        assertEquals(point(2), pointSet.subtract(pointSet(1, 3)));
        assertEquals(empty(), pointSet.subtract(pointSet(1, 2)));
        assertEquals(point(2), pointSet.subtract(range(0, 1)));
        assertEquals(empty(), pointSet.subtract(range(1, 2)));
        assertEquals(point(2), pointSet.subtract(rangeSet(0, 1, 3, 4)));
        assertEquals(empty(), pointSet.subtract(rangeSet(0, 1, 2, 3)));
        assertFalse(pointSet.intersects(empty()));
        assertTrue(pointSet.intersects(point(2)));
        assertFalse(pointSet.intersects(point(3)));
        assertTrue(pointSet.intersects(pointSet(1, 2)));
        assertFalse(pointSet.intersects(pointSet(3, 4)));
        assertTrue(pointSet.intersects(range(1, 2)));
        assertFalse(pointSet.intersects(range(-1, 0)));
        assertTrue(pointSet.intersects(rangeSet(1, 2, 3, 4)));
        assertFalse(pointSet.intersects(rangeSet(-1, 0, 3, 4)));
    }

    public void testPointSetMath() {
        BigDecimalRangeSet pointSet = pointSet(1, 2);

        assertEquals(pointSet(-2, -1), pointSet.negate());
        assertEquals(empty(), pointSet.plus(empty()));
        assertEquals(pointSet(4, 5), pointSet.plus(point(3)));
        assertEquals(pointSet(4, 5, 8, 9), pointSet.plus(pointSet(3, 7)));
        assertEquals(range(4, 7), pointSet.plus(range(3, 5)));
        assertEquals(range(4, 10), pointSet.plus(rangeSet(3, 5, 7, 8)));
        assertEquals(empty(), pointSet.minus(empty()));
        assertEquals(pointSet(-1, -2), pointSet.minus(point(3)));
        assertEquals(pointSet(-6, -5, -2, -1), pointSet.minus(pointSet(3, 7)));
        assertEquals(range(-4, -1), pointSet.minus(range(3, 5)));
        assertEquals(range(-7, -1), pointSet.minus(rangeSet(3, 5, 7, 8)));
        assertEquals(empty(), pointSet.mul(empty()));
        assertEquals(pointSet(4, 8), pointSet.mul(point(4)));
        assertEquals(pointSet(1, 2, 3, 6), pointSet.mul(pointSet(1, 3)));
        assertEquals(range(3, 10), pointSet.mul(range(3, 5)));
        assertEquals(range(3, 14), pointSet.mul(rangeSet(3, 5, 6, 7)));
        assertEquals(empty(), pointSet.div(empty()));
        assertEquals(BigDecimalRangeSet.pointSet(Set.of(new BigDecimal("-0.25"), new BigDecimal("-0.5"))), pointSet.div(point(-4)));
        assertEquals(BigDecimalRangeSet.range(new BigDecimal("0.125"), new BigDecimal("0.5")), pointSet.div(pointSet(4, 8)));
        assertEquals(BigDecimalRangeSet.range(new BigDecimal("0.125"), new BigDecimal("0.5")), pointSet.div(range(4, 8)));
        assertEquals(rangeSet("-2e13", "-0.25", "0.125", "2e13"), pointSet.div(rangeSet(-4, 5, 7, 8)));
    }

    public void testRange() {
        BigDecimalRangeSet range = range(1, 2);

        assertTrue(range.contains(v(2)));
        assertFalse(range.contains(v(0)));
        assertTrue(range.contains(empty()));
        assertTrue(range.contains(point(2)));
        assertFalse(range.contains(point(0)));
        assertTrue(range.contains(pointSet(1, 2)));
        assertFalse(range.contains(pointSet(0, 2)));
        assertTrue(range.contains(range(1, 2)));
        assertFalse(range.contains(range(0, 1)));
        assertFalse(range.contains(rangeSet(1, 2, 3, 4)));
        assertEquals(empty(), range.meet(empty()));
        assertEquals(point(2), range.meet(point(2)));
        assertEquals(empty(), range.meet(point(0)));
        assertEquals(pointSet(1, 2), range.meet(pointSet(1, 2)));
        assertEquals(point(1), range.meet(pointSet(0, 1)));
        assertEquals(empty(), range.meet(pointSet(0, 3)));
        assertEquals(range(1, 2), range.meet(range(0, 3)));
        assertEquals(point(1), range.meet(range(0, 1)));
        assertEquals(empty(), range.meet(range(3, 4)));
        assertEquals(range(1, 2), range.meet(rangeSet(1, 2, 3, 4)));
        assertEquals(point(1), range.meet(rangeSet(0, 1, 4, 5)));
        assertEquals(range(1, 2), range.meet(rangeSet(0, 2, 4, 5)));
        assertEquals(empty(), range.meet(rangeSet(-1, 0, 3, 4)));
        assertEquals(range(1, 2), range.join(empty()));
        assertEquals(range(1, 2), range.join(point(2)));
        assertEquals(rangeSet(1, 2, 3, 3), range.join(point(3)));
        assertEquals(rangeSet(1, 2, 3, 3), range.join(pointSet(1, 3)));
        assertEquals(rangeSet(0, 2), range.join(range(0, 1)));
        assertEquals(rangeSet(1, 2, 3, 4), range.join(range(3, 4)));
        assertEquals(rangeSet(0, 3), range.join(rangeSet(0, 1, 2, 3)));
        assertEquals(rangeSet(0, 2, 3, 4), range.join(rangeSet(0, 1, 3, 4)));
        assertEquals(rangeSet("1.0000000000001", "2"), range.subtract(point(1)));
        assertEquals(range(1, 2), range.subtract(point(3)));
        assertEquals(rangeSet("1.0000000000001", "2"), range.subtract(pointSet(1, 3)));
        assertEquals(range(1, 2), range.subtract(pointSet(3, 4)));
        assertEquals(rangeSet("1.0000000000001", "2"), range.subtract(range(0, 1)));
        assertEquals(empty(), range.subtract(range(1, 3)));
        assertEquals(rangeSet("1.0000000000001", "2"), range.subtract(rangeSet(0, 1, 3, 4)));
        assertEquals(rangeSet("1.0000000000001", "1.9999999999999"), range.subtract(rangeSet(0, 1, 2, 3)));
        assertEquals(empty(), range.subtract(rangeSet(0, 2, 3, 4)));
        assertFalse(range.intersects(empty()));
        assertTrue(range.intersects(point(2)));
        assertFalse(range.intersects(point(3)));
        assertTrue(range.intersects(pointSet(1, 2)));
        assertFalse(range.intersects(pointSet(3, 4)));
        assertTrue(range.intersects(range(1, 2)));
        assertFalse(range.intersects(range(-1, 0)));
        assertTrue(range.intersects(rangeSet(1, 2, 3, 4)));
        assertFalse(range.intersects(rangeSet(-1, 0, 3, 4)));
    }

    public void testRangeMath() {
        BigDecimalRangeSet range = range(1, 2);

        assertEquals(range(-2, -1), range.negate());
        assertEquals(empty(), range.plus(empty()));
        assertEquals(range(4, 5), range.plus(point(3)));
        assertEquals(range(4, 7), range.plus(pointSet(3, 5)));
        assertEquals(range(5, 8), range.plus(range(4, 6)));
        assertEquals(range(5, 10), range.plus(rangeSet(4, 6, 7, 8)));
        assertEquals(empty(), range.minus(empty()));
        assertEquals(range(-2, -1), range.minus(point(3)));
        assertEquals(range(-4, -1), range.minus(pointSet(3, 5)));
        assertEquals(range(-5, -2), range.minus(range(4, 6)));
        assertEquals(range(-7, -2), range.minus(rangeSet(4, 6, 7, 8)));
        assertEquals(empty(), range.mul(empty()));
        assertEquals(range(3, 6), range.mul(point(3)));
        assertEquals(range(3, 10), range.mul(pointSet(3, 5)));
        assertEquals(range(4, 12), range.mul(range(4, 6)));
        assertEquals(range(4, 16), range.mul(rangeSet(4, 6, 7, 8)));
        assertEquals(empty(), range.div(empty()));
        assertEquals(BigDecimalRangeSet.range(new BigDecimal("-0.5"), new BigDecimal("-0.25")), range.div(point(-4)));
        assertEquals(BigDecimalRangeSet.range(new BigDecimal("0.125"), new BigDecimal("0.5")), range.div(pointSet(4, 8)));
        assertEquals(BigDecimalRangeSet.range(new BigDecimal("0.125"), new BigDecimal("0.5")), range.div(range(4, 8)));
        assertEquals(rangeSet("-2e13", "-0.25", "0.125", "2e13"), range.div(rangeSet(-4, 5, 7, 8)));
    }

    public void testRangeSet() {
        BigDecimalRangeSet rangeSet = rangeSet(1, 2, 5, 7);

        assertTrue(rangeSet.contains(v(2)));
        assertFalse(rangeSet.contains(v(0)));
        assertTrue(rangeSet.contains(empty()));
        assertTrue(rangeSet.contains(point(2)));
        assertFalse(rangeSet.contains(point(0)));
        assertTrue(rangeSet.contains(pointSet(1, 2)));
        assertFalse(rangeSet.contains(pointSet(0, 3)));
        assertTrue(rangeSet.contains(range(1, 2)));
        assertFalse(rangeSet.contains(range(2, 5)));
        assertTrue(rangeSet.contains(rangeSet(1, 2, 5, 6)));
        assertFalse(rangeSet.contains(rangeSet(1, 2, 5, 8)));
        assertEquals(empty(), rangeSet.meet(empty()));
        assertEquals(point(2), rangeSet.meet(point(2)));
        assertEquals(empty(), rangeSet.meet(point(0)));
        assertEquals(pointSet(1, 2), rangeSet.meet(pointSet(1, 2)));
        assertEquals(point(1), rangeSet.meet(pointSet(0, 1)));
        assertEquals(empty(), rangeSet.meet(pointSet(0, 3)));
        assertEquals(range(1, 2), rangeSet.meet(range(1, 4)));
        assertEquals(range(5, 6), rangeSet.meet(range(5, 6)));
        assertEquals(point(5), rangeSet.meet(range(4, 5)));
        assertEquals(empty(), rangeSet.meet(range(3, 4)));
        assertEquals(range(1, 2), rangeSet.meet(rangeSet(0, 2, 3, 4)));
        assertEquals(point(1), rangeSet.meet(rangeSet(0, 1, 3, 4)));
        assertEquals(empty(), rangeSet.meet(rangeSet(-1, 0, 3, 4)));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.join(empty()));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.join(point(1)));
        assertEquals(rangeSet(1, 2, 3, 3, 5, 7), rangeSet.join(point(3)));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.join(pointSet(1, 2)));
        assertEquals(rangeSet(1, 2, 3, 3, 5, 7), rangeSet.join(pointSet(1, 3)));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.join(range(1, 2)));
        assertEquals(rangeSet(1, 2, 3, 4, 5, 7), rangeSet.join(range(3, 4)));
        assertEquals(rangeSet(0, 3, 5, 7), rangeSet.join(range(0, 3)));
        assertEquals(rangeSet(1, 2, 5, 8), rangeSet.join(range(6, 8)));
        assertEquals(rangeSet(1, 4, 5, 7), rangeSet.join(range(2, 4)));
        assertEquals(range(1, 7), rangeSet.join(range(2, 5)));
        assertEquals(rangeSet(0, 2, 3, 4, 5, 8), rangeSet.join(rangeSet(0, 1, 3, 4, 6, 8)));
        assertEquals(rangeSet(1, 3, 4, 8), rangeSet.join(rangeSet(1, 3, 4, 8)));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.subtract(empty()));
        assertEquals(rangeSet("1.0000000000001", "2", "5", "7"), rangeSet.subtract(point(1)));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.subtract(point(3)));
        assertEquals(rangeSet("1.0000000000001", "2", "5", "7"), rangeSet.subtract(pointSet(1, 4)));
        assertEquals(rangeSet("1", "2", "5.0000000000001", "5.9999999999999", "6.0000000000001", "7"), rangeSet.subtract(pointSet(5, 6)));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.subtract(pointSet(3, 4)));
        assertEquals(range(5, 7), rangeSet.subtract(range(1, 2)));
        assertEquals(rangeSet("1", "2", "6.0000000000001", "7"), rangeSet.subtract(range(4, 6)));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.subtract(range(3, 4)));
        assertEquals(rangeSet(1, 2, 5, 7), rangeSet.subtract(rangeSet(-1, 0, 3, 4)));
        assertEquals(rangeSet("1.0000000000001", "2", "5", "5.9999999999999"), rangeSet.subtract(rangeSet(-1, 1, 3, 4, 6, 8)));
        assertFalse(rangeSet.intersects(empty()));
        assertTrue(rangeSet.intersects(point(2)));
        assertFalse(rangeSet.intersects(point(3)));
        assertTrue(rangeSet.intersects(pointSet(1, 2)));
        assertFalse(rangeSet.intersects(pointSet(3, 4)));
        assertTrue(rangeSet.intersects(range(1, 2)));
        assertFalse(rangeSet.intersects(range(-1, 0)));
        assertTrue(rangeSet.intersects(rangeSet(1, 2, 3, 4)));
        assertTrue(rangeSet.intersects(rangeSet(3, 4, 7, 9)));
        assertFalse(rangeSet.intersects(rangeSet(-1, 0, 3, 4)));
    }

    public void testRangeSetMath() {
        BigDecimalRangeSet rangeSet = rangeSet(1, 2, 5, 7);

        assertEquals(rangeSet(-7, -5, -2, -1), rangeSet.negate());
        assertEquals(empty(), rangeSet.plus(empty()));
        assertEquals(rangeSet(5, 6, 9, 11), rangeSet.plus(point(4)));
        assertEquals(range(5, 12), rangeSet.plus(pointSet(4, 5)));
        assertEquals(range(5, 12), rangeSet.plus(range(4, 5)));
        assertEquals(range(5, 17), rangeSet.plus(rangeSet(4, 5, 8, 10)));
        assertEquals(empty(), rangeSet.minus(empty()));
        assertEquals(rangeSet(-3, -2, 1, 3), rangeSet.minus(point(4)));
        assertEquals(range(-4, 3), rangeSet.minus(pointSet(4, 5)));
        assertEquals(range(-4, 3), rangeSet.minus(range(4, 5)));
        assertEquals(range(-9, 3), rangeSet.minus(rangeSet(4, 5, 8, 10)));
        assertEquals(empty(), rangeSet.mul(empty()));
        assertEquals(range(4, 28), rangeSet.mul(point(4)));
        assertEquals(range(4, 35), rangeSet.mul(pointSet(4, 5)));
        assertEquals(range(4, 35), rangeSet.mul(range(4, 5)));
        assertEquals(range(4, 70), rangeSet.mul(rangeSet(4, 5, 8, 10)));
        assertEquals(empty(), rangeSet.div(empty()));
        assertEquals(rangeSet("-1.75", "-1.25", "-0.5", "-0.25"), rangeSet.div(point(-4)));
        assertEquals(rangeSet("0.125", "0.5", "0.625", "1.75"), rangeSet.div(pointSet(4, 8)));
        assertEquals(rangeSet("0.125", "0.5", "0.625", "1.75"), rangeSet.div(range(4, 8)));
        assertEquals(rangeSet("-7e13", "-0.25", "0.125", "7e13"), rangeSet.div(rangeSet(-4, 5, 7, 8)));
    }

    private static BigDecimal v(long value) {
        return new BigDecimal(value);
    }

    private static BigDecimalRangeSet empty() {
        return Empty.EMPTY;
    }

    private static BigDecimalRangeSet point(long value) {
        return BigDecimalRangeSet.point(new BigDecimal(value));
    }

    private static BigDecimalRangeSet pointSet(long... values) {
        HashSet<BigDecimal> set = new HashSet<>();
        for (long l : values) {
            set.add(new BigDecimal(l));
        }
        return BigDecimalRangeSet.pointSet(set);
    }

    private static BigDecimalRangeSet range(long from, long to) {
        return BigDecimalRangeSet.range(new BigDecimal(from), new BigDecimal(to));
    }

    private static BigDecimalRangeSet rangeSet(long... values) {
        ArrayList<BigDecimal> vals = new ArrayList<>();
        for (long l : values) {
            vals.add(new BigDecimal(l));
        }
        return fromRanges(vals.toArray(new BigDecimal[0]), vals.size());
    }

    private static BigDecimalRangeSet rangeSet(String... values) {
        ArrayList<BigDecimal> vals = new ArrayList<>();
        for (String l : values) {
            vals.add(new BigDecimal(l));
        }
        return fromRanges(vals.toArray(new BigDecimal[0]), vals.size());
    }

}
