package nl.petertillema.tibasic.controlFlow.type;

import nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet;

import java.math.BigDecimal;
import java.util.Set;

import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.pointSet;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.BigDecimalRangeSet.range;

public class FunctionRangeSetDomain {

    public static final BigDecimalRangeSet RAND_DOMAIN = range(BigDecimal.ZERO, BigDecimal.ONE);
    public static final BigDecimalRangeSet GETKEY_DOMAIN = pointSet(Set.of(
            BigDecimal.ZERO,
            new BigDecimal("11"),
            new BigDecimal("12"),
            new BigDecimal("13"),
            new BigDecimal("14"),
            new BigDecimal("15"),
            new BigDecimal("21"),
            new BigDecimal("22"),
            new BigDecimal("23"),
            new BigDecimal("24"),
            new BigDecimal("25"),
            new BigDecimal("26"),
            new BigDecimal("31"),
            new BigDecimal("32"),
            new BigDecimal("33"),
            new BigDecimal("34"),
            new BigDecimal("41"),
            new BigDecimal("42"),
            new BigDecimal("43"),
            new BigDecimal("44"),
            new BigDecimal("45"),
            new BigDecimal("51"),
            new BigDecimal("52"),
            new BigDecimal("53"),
            new BigDecimal("54"),
            new BigDecimal("55"),
            new BigDecimal("61"),
            new BigDecimal("62"),
            new BigDecimal("63"),
            new BigDecimal("64"),
            new BigDecimal("65"),
            new BigDecimal("71"),
            new BigDecimal("72"),
            new BigDecimal("73"),
            new BigDecimal("74"),
            new BigDecimal("75"),
            new BigDecimal("81"),
            new BigDecimal("82"),
            new BigDecimal("83"),
            new BigDecimal("84"),
            new BigDecimal("85"),
            new BigDecimal("91"),
            new BigDecimal("92"),
            new BigDecimal("93"),
            new BigDecimal("94"),
            new BigDecimal("95"),
            new BigDecimal("102"),
            new BigDecimal("103"),
            new BigDecimal("104"),
            new BigDecimal("105")
    ));

}
