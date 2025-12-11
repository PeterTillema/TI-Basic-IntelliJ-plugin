package nl.petertillema.tibasic.controlFlow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalUtil {

    public static final MathContext MC = new MathContext(14, RoundingMode.HALF_EVEN);
    public static final BigDecimal MIN = nextUp(new BigDecimal("-1e100"));
    public static final BigDecimal MAX = nextDown(new BigDecimal("1e100"));

    public static BigDecimal numToString(String num) {
        num = num.replace("~", "-")
                .replace("|E", "e")
                .replace("ᴇ", "e");
        return new BigDecimal(num);
    }

    public static BigDecimal round(BigDecimal value) {
        return value.round(MC);
    }

    /**
     * Returns the next BigDecimal in the TI-BASIC domain, which is not equal to the given value with a precision
     * of 14 digits.
     * @param value The input value
     * @return The next BigDecimal
     */
    public static BigDecimal nextUp(BigDecimal value) {
        value = round(value);
        int scale = MC.getPrecision() - value.precision() + value.scale();
        BigDecimal step = BigDecimal.ONE.movePointLeft(scale);
        return value.add(step);
    }

    /**
     * Returns the previous BigDecimal in the TI-BASIC domain, which is not equal to the given value with a precision
     * of 14 digits.
     * @param value The input value
     * @return The previous BigDecimal
     */
    public static BigDecimal nextDown(BigDecimal value) {
        value = round(value);
        int scale = MC.getPrecision() - value.precision() + value.scale();
        BigDecimal step = BigDecimal.ONE.movePointLeft(scale);
        return value.subtract(step);
    }

}
