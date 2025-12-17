package nl.petertillema.tibasic.controlFlow.type.rangeSet;

import java.math.BigDecimal;

import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.pointSet;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.range;

public interface FunctionRangeSetDomain {

    BigDecimalRangeSet RAND_DOMAIN = range(BigDecimal.ZERO, BigDecimal.ONE);
    BigDecimalRangeSet GETKEY_DOMAIN = pointSet(
            BigDecimal.ZERO,
            BigDecimal.valueOf(11),
            BigDecimal.valueOf(12),
            BigDecimal.valueOf(13),
            BigDecimal.valueOf(14),
            BigDecimal.valueOf(15),
            BigDecimal.valueOf(21),
            BigDecimal.valueOf(22),
            BigDecimal.valueOf(23),
            BigDecimal.valueOf(24),
            BigDecimal.valueOf(25),
            BigDecimal.valueOf(26),
            BigDecimal.valueOf(31),
            BigDecimal.valueOf(32),
            BigDecimal.valueOf(33),
            BigDecimal.valueOf(34),
            BigDecimal.valueOf(41),
            BigDecimal.valueOf(42),
            BigDecimal.valueOf(43),
            BigDecimal.valueOf(44),
            BigDecimal.valueOf(45),
            BigDecimal.valueOf(51),
            BigDecimal.valueOf(52),
            BigDecimal.valueOf(53),
            BigDecimal.valueOf(54),
            BigDecimal.valueOf(55),
            BigDecimal.valueOf(61),
            BigDecimal.valueOf(62),
            BigDecimal.valueOf(63),
            BigDecimal.valueOf(64),
            BigDecimal.valueOf(65),
            BigDecimal.valueOf(71),
            BigDecimal.valueOf(72),
            BigDecimal.valueOf(73),
            BigDecimal.valueOf(74),
            BigDecimal.valueOf(75),
            BigDecimal.valueOf(81),
            BigDecimal.valueOf(82),
            BigDecimal.valueOf(83),
            BigDecimal.valueOf(84),
            BigDecimal.valueOf(85),
            BigDecimal.valueOf(91),
            BigDecimal.valueOf(92),
            BigDecimal.valueOf(93),
            BigDecimal.valueOf(94),
            BigDecimal.valueOf(95),
            BigDecimal.valueOf(102),
            BigDecimal.valueOf(103),
            BigDecimal.valueOf(104),
            BigDecimal.valueOf(105)
    );

}
