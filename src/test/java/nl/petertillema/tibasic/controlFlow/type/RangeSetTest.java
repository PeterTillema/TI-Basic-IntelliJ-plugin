package nl.petertillema.tibasic.controlFlow.type;

import junit.framework.TestCase;
import nl.petertillema.tibasic.controlFlow.type.rangeSet.RangeSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RangeSetTest extends TestCase {

    private static final RangeSet rangeSet = new RangeSet(
            Set.of(v(4), v(8), v(20)),
            List.of(
                    new RangeSet.Range(v(-6), v(-2)),
                    new RangeSet.Range(v(1), v(3)),
                    new RangeSet.Range(v(21), v(25))
            ),
            Set.of(v(-3), v(23))
    );

    public void testRangeSetEmpty() {
        assertTrue(empty().isEmpty());
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

    public void testRangeSetContains() {
        assertTrue(rangeSet.contains(v(2)));
        assertTrue(rangeSet.contains(v(4)));
        assertFalse(rangeSet.contains(v(23)));
        assertTrue(rangeSet.contains(point(2)));
        assertTrue(rangeSet.contains(point(4)));
        assertFalse(rangeSet.contains(point(23)));
        assertTrue(rangeSet.contains(pointSet(-6, 2, 24)));
        assertFalse(rangeSet.contains(pointSet(-3, 2, 23)));
        assertTrue(rangeSet.contains(range(21, 22)));
        assertFalse(rangeSet.contains(range(21, 23)));
        assertTrue(rangeSet.contains(rangeSet(-6, -4, 1, 3, 21, 22)));
        assertFalse(rangeSet.contains(rangeSet(-6, -4, 1, 3, 22, 23)));
        assertTrue(rangeSet.contains(new RangeSet(Set.of(v(4), v(22)), List.of(new RangeSet.Range(v(2), v(3))), Set.of(v(2)))));
        assertFalse(rangeSet.contains(new RangeSet(Set.of(v(4), v(23)), List.of(new RangeSet.Range(v(2), v(3))), Set.of(v(2)))));
        assertFalse(rangeSet.contains(new RangeSet(Set.of(v(4), v(22)), List.of(new RangeSet.Range(v(21), v(24))), Set.of())));
        assertTrue(rangeSet.contains(new RangeSet(Set.of(v(4), v(22)), List.of(new RangeSet.Range(v(21), v(24))), Set.of(v(23)))));
        assertFalse(rangeSet.contains(new RangeSet(Set.of(v(4), v(22)), List.of(new RangeSet.Range(v(21), v(26))), Set.of(v(23)))));
    }

    public void testRangeSetMeet() {
        assertEquals(rangeSet, rangeSet.meet(rangeSet));
        assertEquals(empty(), rangeSet.meet(empty()));
        assertEquals(point(2), rangeSet.meet(point(2)));
        assertEquals(empty(), rangeSet.meet(point(0)));
        assertEquals(pointSet(-4, 2), rangeSet.meet(pointSet(-4, 2)));
        assertEquals(point(2), rangeSet.meet(pointSet(-3, 2)));
        assertEquals(empty(), rangeSet.meet(pointSet(-3, 23)));
        assertEquals(rangeSet, rangeSet.meet(range((long) -1e100, (long) 1e100)));
        assertEquals(new RangeSet(Set.of(), List.of(new RangeSet.Range(v(-6), v(-2))), Set.of(v(-3))), rangeSet.meet(range(-6, -2)));
        assertEquals(new RangeSet(Set.of(), List.of(new RangeSet.Range(v(-6), v(-4))), Set.of()), rangeSet.meet(range(-6, -4)));
        assertEquals(new RangeSet(Set.of(v(4), v(8), v(20)), List.of(new RangeSet.Range(v(21), v(23))), Set.of(v(23))), rangeSet.meet(range(4, 23)));
        assertEquals(new RangeSet(Set.of(v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-3)), new RangeSet.Range(v(21), v(23))), Set.of(v(-3), v(23))), rangeSet.meet(rangeSet(-8, -3, 5, 23)));
        assertEquals(empty(), rangeSet.meet(new RangeSet(Set.of(v(23), v(26)), List.of(new RangeSet.Range(v(7), v(9))), Set.of(v(8)))));
    }

    public void testRangeSetJoin() {
        assertEquals(rangeSet, rangeSet.join(empty()));
        assertEquals(rangeSet, rangeSet.join(rangeSet));
        assertEquals(rangeSet, rangeSet.join(point(2)));
        assertEquals(new RangeSet(Set.of(v(0), v(4), v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.join(point(0)));
        assertEquals(new RangeSet(Set.of(v(4), v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3))), rangeSet.join(point(23)));
        assertEquals(rangeSet, rangeSet.join(pointSet(2, 21)));
        assertEquals(new RangeSet(Set.of(v(0), v(4), v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.join(pointSet(0, 2)));
        assertEquals(new RangeSet(Set.of(v(4), v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3))), rangeSet.join(pointSet(2, 23)));
        assertEquals(rangeSet, rangeSet.join(range(21, 22)));
        assertEquals(new RangeSet(Set.of(v(4), v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3))), rangeSet.join(range(21, 23)));
        assertEquals(new RangeSet(Set.of(v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(5)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.join(range(2, 5)));
        assertEquals(new RangeSet(Set.of(), List.of(new RangeSet.Range(v(-6), v(20)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.join(range(-2, 20)));
        assertEquals(new RangeSet(Set.of(), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(20)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.join(range(1, 20)));
        assertEquals(new RangeSet(Set.of(v(4)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(8), v(20)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.join(range(8, 20)));
        assertEquals(new RangeSet(Set.of(), List.of(new RangeSet.Range(v(-6), v(20)), new RangeSet.Range(v(21), v(25))), Set.of(v(23))), rangeSet.join(range(-5, 20)));
        assertEquals(new RangeSet(Set.of(), List.of(new RangeSet.Range(v(-6), v(25))), Set.of()), rangeSet.join(range(-5, 23)));
        assertEquals(new RangeSet(Set.of(v(0), v(4)), List.of(new RangeSet.Range(v(-8), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(8), v(25))), Set.of(v(-3), v(23))), rangeSet.join(new RangeSet(Set.of(v(0)), List.of(new RangeSet.Range(v(-8), v(-5)), new RangeSet.Range(v(8), v(24))), Set.of(v(23)))));
    }

    public void testRangeSetSubtract() {
        assertEquals(rangeSet, rangeSet.subtract(empty()));
        assertEquals(empty(), rangeSet.subtract(rangeSet));
        assertEquals(rangeSet, rangeSet.subtract(point(0)));
        assertEquals(new RangeSet(Set.of(v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.subtract(point(4)));
        assertEquals(new RangeSet(Set.of(v(4), v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.subtract(point(23)));
        assertEquals(new RangeSet(Set.of(v(4), v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23), v(24))), rangeSet.subtract(point(24)));
        assertEquals(rangeSet, rangeSet.subtract(pointSet(0, 9)));
        assertEquals(new RangeSet(Set.of(v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.subtract(pointSet(4, 10)));
        assertEquals(new RangeSet(Set.of(v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23))), rangeSet.subtract(pointSet(4, 8, 23)));
        assertEquals(new RangeSet(Set.of(v(4), v(8), v(20)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(v(21), v(25))), Set.of(v(-3), v(23), v(24))), rangeSet.subtract(pointSet(5, 24)));
        assertEquals(new RangeSet(Set.of(v(4)), List.of(new RangeSet.Range(v(-6), v(-2)), new RangeSet.Range(v(1), v(3)), new RangeSet.Range(new BigDecimal("24.000000000001"), v(25))), Set.of(v(-3))), rangeSet.subtract(range(5, 24)));
        assertEquals(empty(), rangeSet.subtract(range(-6, 25)));
        assertEquals(new RangeSet(
                        Set.of(v(-3), v(23)),
                        List.of(
                                new RangeSet.Range(new BigDecimal("-9999999999999000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), new BigDecimal("-6.0000000000001")),
                                new RangeSet.Range(new BigDecimal("-1.9999999999999"), new BigDecimal("0.9999999999999")),
                                new RangeSet.Range(new BigDecimal("3.0000000000001"), new BigDecimal("20.999999999999")),
                                new RangeSet.Range(new BigDecimal("25.000000000001"), new BigDecimal("9999999999999000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"))
                        ),
                        Set.of(v(4), v(8), v(20))
                ),
                RangeSet.ALL.subtract(rangeSet));
    }

    public void testRangeSetPoint() {
        RangeSet point = point(2);

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
        assertEquals(new RangeSet(Set.of(v(2)), List.of(new RangeSet.Range(v(0), v(1))), Set.of()), point.join(range(0, 1)));
        assertEquals(range(2, 3), point.join(range(2, 3)));
        assertEquals(new RangeSet(Set.of(v(2)), List.of(new RangeSet.Range(v(3), v(4))), Set.of()), point.join(range(3, 4)));
        assertEquals(rangeSet(0, 1, 2, 3), point.join(rangeSet(0, 1, 2, 3)));
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

    private static BigDecimal v(long value) {
        return new BigDecimal(value);
    }

    private static RangeSet empty() {
        return new RangeSet(Set.of(), List.of(), Set.of());
    }

    private static RangeSet point(long value) {
        return new RangeSet(Set.of(new BigDecimal(value)), List.of(), Set.of());
    }

    private static RangeSet pointSet(long... values) {
        Set<BigDecimal> out = new HashSet<>();
        for (long value : values) {
            out.add(new BigDecimal(value));
        }
        return new RangeSet(out, List.of(), Set.of());
    }

    private static RangeSet range(long from, long to) {
        return new RangeSet(Set.of(), List.of(new RangeSet.Range(new BigDecimal(from), new BigDecimal(to))), Set.of());
    }

    private static RangeSet rangeSet(long... values) {
        ArrayList<RangeSet.Range> ranges = new ArrayList<>();
        for (int i = 0; i < values.length; i += 2) {
            ranges.add(new RangeSet.Range(BigDecimal.valueOf(values[i]), BigDecimal.valueOf(values[i + 1])));
        }
        return new RangeSet(Set.of(), ranges, Set.of());
    }

}
