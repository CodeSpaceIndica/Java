package codespace.piseries;

import java.math.BigDecimal;

/**
 * The formula for this is taken from here.
 * https://en.wikipedia.org/wiki/Leibniz_formula_for_%CF%80
 */
public class PI_MadhavaLeibniz extends PISeries {
    private BigDecimal numerator = new BigDecimal(4.0d);
    private BigDecimal n = new BigDecimal(1.0d);
    private BigDecimal nIncr = new BigDecimal(2.0d);

    public PI_MadhavaLeibniz() {
    }

    public void calculatePI() {
        PI = PI.add( numerator.divide(n, PRECISION)
                    , PRECISION);
        n = n.add(nIncr);
        numerator = numerator.negate();
    }

    public String getName() {
        return "Madhava-Leibniz";
    }
}
